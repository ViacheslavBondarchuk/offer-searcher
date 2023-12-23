package io.github.viacheslavbondarchuk.offersearcher.service;

import io.github.viacheslavbondarchuk.offersearcher.properties.MongoProperties;
import org.bson.Document;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

@Service
public final class MarketDocumentStorage extends AbstractDocumentStorage<String, String, Document> {
    private final MongoProperties properties;

    public MarketDocumentStorage(MongoTemplate mongoTemplate, MongoProperties properties) {
        super(mongoTemplate);
        this.properties = properties;
    }

    @Override
    protected String getCollectionName() {
        return properties.map(MongoProperties::getCollection).getMarket();
    }

    @Override
    protected Document convert(String entity) {
        return Document.parse(entity);
    }
}
