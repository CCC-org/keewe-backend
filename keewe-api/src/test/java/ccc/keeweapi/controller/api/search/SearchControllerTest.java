package ccc.keeweapi.controller.api.search;

import ccc.keeweapi.document.utils.ApiDocumentationTest;
import ccc.keeweapi.service.search.query.SearchQueryApiService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.restdocs.RestDocumentationContextProvider;

class SearchControllerTest extends ApiDocumentationTest {
    @InjectMocks
    private SearchController searchController;

    @Mock
    private SearchQueryApiService searchQueryApiService;

    @BeforeEach
    void setup(RestDocumentationContextProvider provider) {
        super.setup(searchController, provider);
    }


    @Test
    @DisplayName("검색 API")
    void search() throws Exception {
        // TODO
    }
}
