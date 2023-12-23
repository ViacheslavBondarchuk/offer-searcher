package io.github.viacheslavbondarchuk.offersearcher.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Builder
@Getter
@RequiredArgsConstructor
public class SearchResult<T> {
    private final Long hits;
    private final T result;

    public static <T> SearchResult<T> of(long hits, T result) {
        return new SearchResult<>(hits, result);
    }
}
