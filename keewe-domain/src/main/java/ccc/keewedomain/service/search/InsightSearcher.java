package ccc.keewedomain.service.search;

import ccc.keewedomain.persistence.domain.insight.Insight;
import ccc.keewedomain.persistence.repository.insight.InsightQueryRepository;
import ccc.keewedomain.persistence.repository.utils.CursorPageable;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class InsightSearcher implements Searcher<Insight> {

    private final InsightQueryRepository insightQueryRepository;

    @Override
    public List<Insight> search(String keyword, CursorPageable<Long> cPage) {
        return insightQueryRepository.findAllByKeyword(keyword, cPage);
    }

}
