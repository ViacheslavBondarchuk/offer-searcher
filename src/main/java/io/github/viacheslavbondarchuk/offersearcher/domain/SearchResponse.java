package io.github.viacheslavbondarchuk.offersearcher.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@Getter
@Builder
@RequiredArgsConstructor
@JsonInclude(value = NON_NULL)
public class SearchResponse<T> {
    private final T data;
    private final Long hits;
    private final Integer skip;
    private final Integer limit;
    private final String exception;
}
