package io.github.viacheslavbondarchuk.offersearcher.controller;

import io.github.viacheslavbondarchuk.offersearcher.domain.SearchRequest;
import io.github.viacheslavbondarchuk.offersearcher.domain.SearchResponse;
import io.github.viacheslavbondarchuk.offersearcher.exception.BoundaryLimitException;
import io.github.viacheslavbondarchuk.offersearcher.service.AuthorizationService;
import io.github.viacheslavbondarchuk.offersearcher.service.SearchService;
import io.github.viacheslavbondarchuk.offersearcher.util.Checking;
import lombok.RequiredArgsConstructor;
import org.bson.Document;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.function.Predicate;
import java.util.function.Supplier;

import static io.github.viacheslavbondarchuk.offersearcher.constants.Headers.SECRET_KEY;
import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON_VALUE;

@RestController
@RequiredArgsConstructor
@RequestMapping("/search")
public class SearchController implements WebController {
    public static final Supplier<RuntimeException> BOUNDARY_EXCEPTION_FACTORY =
            () -> new BoundaryLimitException("Boundary limit exceeded. Boundary allowable limit is 100");
    public static final Predicate<Integer> BOUNDARY_LIMIT_PREDICATE = limit -> limit > 100;

    private final SearchService searchService;
    private final AuthorizationService authorizationService;

    @PostMapping(path = "/actual", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public SearchResponse<List<Document>> search(@Validated @RequestBody SearchRequest searchRequest,
                                                 @RequestHeader(SECRET_KEY) String secretKey) {
        authorizationService.check(secretKey);
        Checking.check(searchRequest.getLimit(), BOUNDARY_LIMIT_PREDICATE, BOUNDARY_EXCEPTION_FACTORY);
        return searchService.search(searchRequest);
    }

    @PostMapping(path = "/updates", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public SearchResponse<List<Document>> searchInUpdateHistory(@Validated @RequestBody SearchRequest searchRequest,
                                                                @RequestHeader(SECRET_KEY) String secretKey) {
        authorizationService.check(secretKey);
        Checking.check(searchRequest.getLimit(), BOUNDARY_LIMIT_PREDICATE, BOUNDARY_EXCEPTION_FACTORY);
        return searchService.searchInUpdateHistory(searchRequest);
    }

}
