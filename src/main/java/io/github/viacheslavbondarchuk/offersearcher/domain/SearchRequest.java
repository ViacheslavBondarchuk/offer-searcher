package io.github.viacheslavbondarchuk.offersearcher.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import org.bson.Document;

import static com.fasterxml.jackson.annotation.JsonCreator.Mode.PROPERTIES;

@Getter
public class SearchRequest {
    private final Document query;
    private final Document sort;
    private final Document fields;
    private final EntityType type;
    private final int skip;
    private final int limit;

    @JsonCreator(mode = PROPERTIES)
    public SearchRequest(@JsonProperty Document query,
                         @JsonProperty Document sort,
                         @JsonProperty Document fields,
                         @JsonProperty EntityType type,
                         @JsonProperty Integer skip,
                         @JsonProperty Integer limit) {
        this.query = query;
        this.sort = sort == null ? new Document() : sort;
        this.fields = fields;
        this.type = type;
        this.skip = skip == null ? 0 : skip;
        this.limit = limit == null ? 10 : limit;
    }
}
