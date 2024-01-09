package io.github.viacheslavbondarchuk.offersearcher.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import org.bson.Document;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;

import static com.fasterxml.jackson.annotation.JsonCreator.Mode.PROPERTIES;

@Getter
public final class SearchRequest extends AbstractRequest {
    @NotNull
    private final Map<String, Object> query;
    private final Map<String, Integer> sort;
    private final Map<String, Integer> fields;
    private final int skip;
    private final int limit;


    @JsonCreator(mode = PROPERTIES)
    public SearchRequest(@JsonProperty Map<String, Object> query,
                         @JsonProperty Map<String, Integer> sort,
                         @JsonProperty Map<String, Integer> fields,
                         @JsonProperty EntityType type,
                         @JsonProperty String distinct,
                         @JsonProperty Integer skip,
                         @JsonProperty Integer limit) {
        super(type);
        this.query = query;
        this.sort = Optional.ofNullable(sort).orElse(Collections.emptyMap());
        this.fields = Optional.ofNullable(fields).orElse(Collections.emptyMap());
        this.skip = Optional.ofNullable(skip).orElse(0);
        this.limit = Optional.ofNullable(limit).orElse(10);
    }
}
