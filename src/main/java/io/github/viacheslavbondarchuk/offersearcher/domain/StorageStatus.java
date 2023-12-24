package io.github.viacheslavbondarchuk.offersearcher.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public final class StorageStatus implements ServiceStatus {
    private final String collection;
    private final Long count;

    public static StorageStatus of(String collection, long count) {
        return new StorageStatus(collection, count);
    }
}
