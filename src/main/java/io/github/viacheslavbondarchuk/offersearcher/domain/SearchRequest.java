package io.github.viacheslavbondarchuk.offersearcher.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import org.bson.Document;

import java.util.Optional;

import static com.fasterxml.jackson.annotation.JsonCreator.Mode.PROPERTIES;

@Getter
public final class SearchRequest extends AbstractRequest {
    @NotNull
    private final Document query;
    private final Document sort;
    private final Document fields;
    private final String distinct;
    private final int skip;
    private final int limit;


    @JsonCreator(mode = PROPERTIES)
    public SearchRequest(@JsonProperty Document query,
                         @JsonProperty Document sort,
                         @JsonProperty Document fields,
                         @JsonProperty EntityType type,
                         @JsonProperty String distinct,
                         @JsonProperty Integer skip,
                         @JsonProperty Integer limit) {
        super(type);
        this.query = query;
        this.sort = Optional.ofNullable(sort).orElse(new Document());
        this.fields = Optional.ofNullable(fields).orElse(new Document());
        this.distinct = distinct;
        this.skip = Optional.ofNullable(skip).orElse(0);
        this.limit = Optional.ofNullable(limit).orElse(10);
    }
}
