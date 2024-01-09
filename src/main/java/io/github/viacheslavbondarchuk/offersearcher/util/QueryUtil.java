package io.github.viacheslavbondarchuk.offersearcher.util;

import io.github.viacheslavbondarchuk.offersearcher.domain.SearchRequest;
import org.bson.Document;
import org.springframework.data.mongodb.core.query.BasicQuery;
import org.springframework.data.mongodb.core.query.Query;

public final class QueryUtil {
    private QueryUtil() {}

    public static Query searchQuery(SearchRequest request) {
        BasicQuery query = new BasicQuery(new Document(request.getQuery()));
        query.skip(request.getSkip());
        query.limit(request.getLimit());
        query.setSortObject(new Document(request.getSort()));
        query.setFieldsObject(new Document(request.getFields()));
        return query;
    }

    public static Query countQuery(SearchRequest request) {
        return new BasicQuery(new Document(request.getQuery()));
    }

}
