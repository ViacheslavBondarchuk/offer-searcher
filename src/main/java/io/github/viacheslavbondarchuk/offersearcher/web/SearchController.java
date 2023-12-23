package io.github.viacheslavbondarchuk.offersearcher.web;

import io.github.viacheslavbondarchuk.offersearcher.domain.SearchRequest;
import io.github.viacheslavbondarchuk.offersearcher.domain.SearchResponse;
import io.github.viacheslavbondarchuk.offersearcher.service.SearchService;
import lombok.RequiredArgsConstructor;
import org.bson.Document;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("search")
public class SearchController {
    private final SearchService searchService;

    @PostMapping(consumes = MimeTypeUtils.APPLICATION_JSON_VALUE, produces = MimeTypeUtils.APPLICATION_JSON_VALUE)
    public SearchResponse<List<Document>> search(@RequestBody SearchRequest request) {
        return searchService.search(request);
    }

    @ExceptionHandler(Exception.class)
    public SearchResponse<?> exceptionHandler(Exception ex) {
        return SearchResponse.builder()
                .exception(ex.getMessage())
                .build();
    }
}
