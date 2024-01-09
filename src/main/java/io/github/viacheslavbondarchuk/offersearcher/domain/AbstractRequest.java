package io.github.viacheslavbondarchuk.offersearcher.domain;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import static io.github.viacheslavbondarchuk.offersearcher.constants.ValidationErrors.ENTITY_TYPE_VALIDATION_ERROR;

/**
 * author: vbondarchuk
 * date: 1/8/2024
 * time: 1:21 PM
 **/

@Getter
@RequiredArgsConstructor
public sealed abstract class AbstractRequest permits SearchRequest, KafkaProduceUpdateRequest {

    @NotNull(message = ENTITY_TYPE_VALIDATION_ERROR)
    protected final EntityType type;
}
