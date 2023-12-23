package io.github.viacheslavbondarchuk.offersearcher.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * author: vbondarchuk
 * date: 12/12/2023
 * time: 10:54 PM
 **/

@Getter
@RequiredArgsConstructor
public enum EntityType {
    EVENT("event"), MARKET("market"), SELECTION("selection");

    private final String value;
}
