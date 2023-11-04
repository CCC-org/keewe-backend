package ccc.keeweapi.service.search.query;

import ccc.keeweapi.utils.SecurityUtil;
import ccc.keewecore.consts.SearchType;
import ccc.keewedomain.dto.SearchDto;
import ccc.keewedomain.persistence.repository.utils.CursorPageable;
import ccc.keewedomain.service.search.query.SearchQueryDomainService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SearchQueryApiService {

    private final SearchQueryDomainService searchQueryDomainService;

    @Transactional(readOnly = true)
    public List<SearchDto> search(SearchType searchType, String keyword, CursorPageable<Long> cPage) {
        return searchQueryDomainService.search(searchType, keyword, SecurityUtil.getUser(), cPage);
    }
}
