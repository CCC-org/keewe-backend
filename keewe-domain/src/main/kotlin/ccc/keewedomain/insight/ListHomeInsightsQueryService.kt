package ccc.keewedomain.insight

import ccc.keewedomain.cache.repository.insight.CReactionCountRepository
import ccc.keewedomain.dto.insight.InsightGetForHomeDto
import ccc.keewedomain.dto.insight.InsightWriterDto
import ccc.keewedomain.dto.insight.ReactionAggregationGetDto
import ccc.keewedomain.persistence.domain.insight.Insight
import ccc.keewedomain.persistence.domain.insight.Reaction
import ccc.keewedomain.persistence.domain.user.User
import ccc.keewedomain.persistence.repository.insight.InsightQueryRepository
import ccc.keewedomain.persistence.repository.insight.ReactionAggregationRepository
import ccc.keewedomain.persistence.repository.insight.ReactionRepository
import ccc.keewedomain.persistence.repository.utils.CursorPageable
import ccc.keewedomain.service.insight.query.BookmarkQueryDomainService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flatMapMerge
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.toList
import org.springframework.stereotype.Service

@Service
class ListHomeInsightsQueryService(
    private val insightQueryRepository: InsightQueryRepository,
    private val bookmarkQueryDomainService: BookmarkQueryDomainService,
    private val cReactionCountRepository: CReactionCountRepository,
    private val reactionAggregationRepository: ReactionAggregationRepository,
    private val reactionRepository: ReactionRepository,
    private val coroutineScope: CoroutineScope,
) {
    data class Query(
        val user: User,
        val paginateStandard: CursorPageable<Long>,
        val activateFollowFilter: Boolean,
    )

    suspend fun invoke(query: Query): List<InsightGetForHomeDto> {
        val (user, paginateStandard, activateFollowFilter) = query
        val insights = insightQueryRepository.findAllForHome(user, paginateStandard, activateFollowFilter)
        val deferredBookmark = coroutineScope.async { bookmarkQueryDomainService.getBookmarkPresenceMap(user, insights) }
        val insightIds = insights.map { it.id }
        val deferredReactionAggregations = this.getReactionAggregations(insightIds)
        val deferredReactions = this.getReactions(insights, user)
        val bookmark = deferredBookmark.await()
        val reactionAggregations = deferredReactionAggregations.await()
        val reactions = deferredReactions.await()
        return insights.map { insight ->
            val isBookmark = bookmark[insight.id] ?: false
            val reactionAggregation = reactionAggregations[insight.id] ?: ReactionAggregationGetDto.EMPTY(insight.id)
            val reactionsOfInsight = reactions[insight.id] ?: emptyList()
            reactionAggregation.updateClicked(reactionsOfInsight)
            InsightGetForHomeDto.of(
                insight.id,
                insight.contents,
                isBookmark,
                insight.link,
                reactionAggregation,
                insight.createdAt,
                InsightWriterDto.of(
                    insight.writer.id,
                    insight.writer.nickname,
                    insight.writer.repTitleName,
                    insight.writer.profilePhotoURL,
                )
            )
        }
    }

    @OptIn(FlowPreview::class)
    private fun getReactionAggregations(insightIds: List<Long>): Deferred<Map<Long, ReactionAggregationGetDto>> {
        val reactionAggregationFlow = insightIds.asFlow()
            .flatMapMerge {
                flow {
                    emit(getCurrentReactionAggregation(it))
                }
            }
            .flowOn(Dispatchers.IO)
        return coroutineScope.async {
            reactionAggregationFlow.toList()
                .associateBy { it.insightId }
        }
    }

    @OptIn(FlowPreview::class)
    private fun getReactions(insights: List<Insight>, user: User): Deferred<Map<Long, List<Reaction>>> {
        val reactionFlow = insights.asFlow()
            .flatMapMerge { insight ->
                flow {
                    emit(reactionRepository.findByInsightAndReactor(insight, user))
                }
            }
            .flatMapConcat { it.asFlow() }
            .flowOn(Dispatchers.IO)
        return coroutineScope.async {
            reactionFlow.toList()
                .mapNotNull { it }
                .groupBy { it.insight.id }
        }
    }

    private fun getCurrentReactionAggregation(insightId: Long): ReactionAggregationGetDto {
        return ReactionAggregationGetDto.createByCnt(
            insightId,
            cReactionCountRepository.findByIdWithMissHandle(insightId) {
                reactionAggregationRepository.findDtoByInsightId(insightId)
            }
        )
    }
}