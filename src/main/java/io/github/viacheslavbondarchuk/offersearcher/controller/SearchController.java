package io.github.viacheslavbondarchuk.offersearcher.controller;

import io.github.viacheslavbondarchuk.offersearcher.domain.SearchRequest;
import io.github.viacheslavbondarchuk.offersearcher.domain.SearchResponse;
import io.github.viacheslavbondarchuk.offersearcher.exception.BoundaryLimitException;
import io.github.viacheslavbondarchuk.offersearcher.service.AuthorizationService;
import io.github.viacheslavbondarchuk.offersearcher.service.SearchService;
import io.github.viacheslavbondarchuk.offersearcher.util.Checking;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.bson.Document;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("search")
public class SearchController implements WebController {
    private final SearchService searchService;
    private final AuthorizationService authorizationService;

    @PostMapping(consumes = MimeTypeUtils.APPLICATION_JSON_VALUE, produces = MimeTypeUtils.APPLICATION_JSON_VALUE)
    public SearchResponse<List<Document>> search(@RequestBody SearchRequest searchRequest, HttpServletRequest request) {
        authorizationService.check(request);
        Checking.check(searchRequest.getLimit(), limit -> limit > 100,
                () -> new BoundaryLimitException("Boundary limit exception. Boundary allowable limit is 100"));
        return searchService.search(searchRequest);
    }

    @PostMapping(path = "/updates", consumes = MimeTypeUtils.APPLICATION_JSON_VALUE, produces = MimeTypeUtils.APPLICATION_JSON_VALUE)
    public SearchResponse<List<Document>> searchUpdates(@RequestBody SearchRequest searchRequest, HttpServletRequest request) {
        authorizationService.check(request);
        Checking.check(searchRequest.getLimit(), limit -> limit > 100,
                () -> new BoundaryLimitException("Boundary limit exception. Boundary allowable limit is 100"));
        return searchService.searchUpdates(searchRequest);
    }

}
