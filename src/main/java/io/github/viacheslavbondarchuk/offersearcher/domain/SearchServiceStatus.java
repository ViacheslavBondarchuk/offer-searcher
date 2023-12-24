package io.github.viacheslavbondarchuk.offersearcher.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class SearchServiceStatus implements ServiceStatus {
    private final boolean ready;
}
