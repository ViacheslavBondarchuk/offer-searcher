package io.github.viacheslavbondarchuk.offersearcher.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@Getter
@Builder
@JsonInclude(value = NON_NULL)
public final class SearchResponse<T> {
    private final T data;
    private final Long hits;
    private final Integer skip;
    private final Integer limit;
    private final String brandId;

    public SearchResponse(T data, Long hits, Integer skip, Integer limit, String brandId) {
        this.data = data;
        this.hits = hits;
        this.skip = skip;
        this.limit = limit;
        this.brandId = brandId;
    }
}
