package ccc.keeweapi.controller.api.search;

import ccc.keeweapi.service.search.query.SearchQueryApiService;
import ccc.keewecore.consts.KeeweConsts;
import ccc.keewecore.consts.SearchType;
import ccc.keewedomain.dto.SearchDto;
import ccc.keewedomain.persistence.repository.utils.CursorPageable;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/api/v1/search")
@RestController
public class SearchController {

    private final SearchQueryApiService searchQueryApiService;

    @GetMapping
    public List<SearchDto> search(
            @RequestParam SearchType searchType,
            @RequestParam String keyword,
            @RequestParam(required = false, defaultValue = KeeweConsts.LONG_MAX_STRING) Long cursor,
            @RequestParam Long limit) {
        return searchQueryApiService.search(searchType, keyword, CursorPageable.of(cursor, limit));
    }
}
