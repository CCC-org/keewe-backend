package ccc.keeweapi.service.insight.query

import ccc.keeweapi.component.InsightAssembler
import ccc.keeweapi.dto.insight.response.InsightGetForHomeResponse
import ccc.keeweapi.utils.BlockedResourceManager
import ccc.keeweapi.utils.SecurityUtil
import ccc.keewedomain.insight.ListHomeInsightsQueryService
import ccc.keewedomain.persistence.repository.utils.CursorPageable
import kotlinx.coroutines.runBlocking
import org.springframework.stereotype.Service

@Service
class InsightQueryApiServiceKt(
    private val listHomeInsightsQueryService: ListHomeInsightsQueryService,
    private val blockedResourceManager: BlockedResourceManager,
    private val insightAssembler: InsightAssembler,
) {
    fun paginateHomeInsights(paginateStandard: CursorPageable<Long>, activateFollowFilter: Boolean): List<InsightGetForHomeResponse> {
        val query = ListHomeInsightsQueryService.Query(
            user = SecurityUtil.getUser(),
            paginateStandard = paginateStandard,
            activateFollowFilter = activateFollowFilter,
        )
        val responses = runBlocking {
            listHomeInsightsQueryService.invoke(query)
                .map(insightAssembler::toInsightGetForHomeResponse)
        }
        return blockedResourceManager.filterBlockedUsers(responses);
    }
}