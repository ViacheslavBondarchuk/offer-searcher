package io.github.viacheslavbondarchuk.offersearcher.service;

import io.github.viacheslavbondarchuk.offersearcher.domain.SearchRequest;
import io.github.viacheslavbondarchuk.offersearcher.domain.SearchResult;
import io.github.viacheslavbondarchuk.offersearcher.domain.StorageStatus;
import io.github.viacheslavbondarchuk.offersearcher.util.KeyValuePair;
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
public abstract class AbstractResettableDocumentStorage<ID, T, D extends Document> implements StatusAwareService<StorageStatus> {
    private static final String MONGO_ID_KEY = "_id";
    private static final Query COUNT_QUERY = Query.query(Criteria.where(MONGO_ID_KEY).exists(true));

    protected final MongoTemplate mongoTemplate;
    protected final String collectionName;

    protected AbstractResettableDocumentStorage(MongoTemplate mongoTemplate, String collectionName) {
        this.mongoTemplate = mongoTemplate;
        this.collectionName = collectionName;
        mongoTemplate.dropCollection(collectionName);
    }

    protected abstract D convert(T entity);

    protected String getCollectionName() {
        return collectionName;
    }

    @Override
    public KeyValuePair<String, StorageStatus> getStatus() {
        return KeyValuePair.of(getServiceName(), StorageStatus.of(getCollectionName(), mongoTemplate.count(COUNT_QUERY, getCollectionName())));
    }

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
        return SearchResult.of(
                mongoTemplate.count(new BasicQuery(request.getQuery()), Document.class, getCollectionName()),
                mongoTemplate.find(getSearchQuery(request), Document.class, getCollectionName())
        );
    }

    private static BasicQuery getSearchQuery(SearchRequest request) {
        BasicQuery query = new BasicQuery(request.getQuery());
        query.skip(request.getSkip());
        query.limit(request.getLimit());
        query.setSortObject(request.getSort());
        return query;
    }
}
