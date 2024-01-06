package io.github.viacheslavbondarchuk.offersearcher.service;

import io.github.viacheslavbondarchuk.offersearcher.domain.StorageStatus;
import io.github.viacheslavbondarchuk.offersearcher.util.KeyValuePair;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.Collection;
import java.util.List;

public abstract class AbstractDocumentStorage implements StatusAwareService<StorageStatus> {
    protected static final Logger log = LoggerFactory.getLogger(AbstractDocumentStorage.class);

    public static final String MONGO_ID_KEY = "_id";
    public static final String ENTITY_ID_KEY = "id";

    protected final MongoTemplate mongoTemplate;
    protected final String collectionName;

    protected AbstractDocumentStorage(MongoTemplate mongoTemplate, String collectionName) {
        this.mongoTemplate = mongoTemplate;
        this.collectionName = collectionName;
        mongoTemplate.dropCollection(collectionName);
    }

    protected String getCollectionName() {
        return collectionName;
    }

    @Override
    public KeyValuePair<String, StorageStatus> getStatus() {
        return KeyValuePair.of(getServiceName(), StorageStatus.of(getCollectionName(), mongoTemplate.estimatedCount(getCollectionName())));
    }

    public void saveAll(Collection<Document> documents) {
        try {
            mongoTemplate.insert(documents, getCollectionName());
        } catch (Exception ex) {
            log.error("Can not save all into collection: {}, document: {}. Exception: ", getCollectionName(), documents, ex);
        }
    }

    public void save(Document document) {
        try {
            mongoTemplate.save(document, getCollectionName());
        } catch (Exception ex) {
            log.error("Can not save into collection: {}, document: {}. Exception: ", getCollectionName(), document, ex);
        }
    }

    public void remove(String id) {
        try {
            mongoTemplate.remove(Query.query(Criteria.where(MONGO_ID_KEY).is(id)), getCollectionName());
        } catch (Exception ex) {
            log.error("Can not remove from collection: {}, entity by id: {}. Exception: ", getCollectionName(), id, ex);
        }
    }

    public List<Document> find(Query query) {
        return mongoTemplate.find(query, Document.class, getCollectionName());
    }

    public long count(Query query) {
        return mongoTemplate.count(query, Document.class, getCollectionName());
    }

}
