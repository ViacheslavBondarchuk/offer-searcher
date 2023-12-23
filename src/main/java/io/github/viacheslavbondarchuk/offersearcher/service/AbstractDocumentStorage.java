package io.github.viacheslavbondarchuk.offersearcher.service;

import io.github.viacheslavbondarchuk.offersearcher.domain.SearchRequest;
import io.github.viacheslavbondarchuk.offersearcher.domain.SearchResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.BasicQuery;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
public abstract class AbstractDocumentStorage<ID, T, D extends Document> {
    private static final SearchResult<List<Document>> EMPTY_RESULT = SearchResult.of(0L, Collections.emptyList());
    private static final String MONGO_ID_KEY = "_id";

    protected final MongoTemplate mongoTemplate;

    protected abstract String getCollectionName();

    protected abstract D convert(T entity);

    public void save(ID id, T entity) {
        try {
            D document = convert(entity);
            document.append(MONGO_ID_KEY, id);
            mongoTemplate.save(document, getCollectionName());
        } catch (Exception ex) {
            log.error("Can not save into collection: {}, entity: {}. Exception: ", getCollectionName(), entity, ex);
        }
    }

    public void save(ID id, T entity, Map<String, Object> additionalData) {
        try {
            D document = convert(entity);
            document.append(MONGO_ID_KEY, id);
            additionalData.forEach(document::append);
            mongoTemplate.save(document, getCollectionName());
        } catch (Exception ex) {
            log.error("Can not save into collection: {}, entity: {}. Exception: ", getCollectionName(), entity, ex);
        }
    }

    public void remove(ID id) {
        try {
            mongoTemplate.remove(Query.query(Criteria.where(MONGO_ID_KEY).is(id)), getCollectionName());
        } catch (Exception ex) {
            log.error("Can not remove from collection: {}, entity by id: {}. Exception: ", getCollectionName(), id, ex);
        }
    }

    public SearchResult<List<Document>> find(SearchRequest request) {
        SearchResult<List<Document>> result = null;
        try {
            result = SearchResult.of(
                    mongoTemplate.count(new BasicQuery(request.getQuery()), Document.class, getCollectionName()),
                    mongoTemplate.find(getSearchQuery(request), Document.class, getCollectionName())
            );
        } catch (Exception ex) {
            log.error("Can not find documents into collection: {}. Exception: ", getCollectionName(), ex);
        }
        return result == null ? EMPTY_RESULT : result;
    }

    private static BasicQuery getSearchQuery(SearchRequest request) {
        BasicQuery query = new BasicQuery(request.getQuery());
        query.skip(request.getSkip());
        query.limit(request.getLimit());
        query.setSortObject(request.getSort());
        return query;
    }
}
