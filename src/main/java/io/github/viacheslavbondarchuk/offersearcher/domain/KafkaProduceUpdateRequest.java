package io.github.viacheslavbondarchuk.offersearcher.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import org.bson.Document;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static com.fasterxml.jackson.annotation.JsonCreator.Mode.PROPERTIES;

/**
 * author: vbondarchuk
 * date: 1/8/2024
 * time: 1:32 PM
 **/
@Getter
public final class KafkaProduceUpdateRequest extends AbstractRequest {
    @NotEmpty
    private final String key;
    private final Document value;
    @NotEmpty
    private final Map<String, String> headers;

    @JsonCreator(mode = PROPERTIES)
    public KafkaProduceUpdateRequest(@JsonProperty EntityType type,
                                     @JsonProperty String key,
                                     @JsonProperty Document value,
                                     @JsonProperty Map<String, String> headers) {
        super(type);
        this.key = key;
        this.value = value;
        this.headers = Optional.ofNullable(headers).orElse(new HashMap<>());

    }
}
