package io.github.viacheslavbondarchuk.offersearcher.storage.impl;

import io.github.viacheslavbondarchuk.offersearcher.properties.MongoProperties;
import io.github.viacheslavbondarchuk.offersearcher.storage.AbstractDocumentStorage;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

@Service
public final class MarketUpdateStorage extends AbstractDocumentStorage {
    private static final String SERVICE_NAME = "Market updates storage";

    public MarketUpdateStorage(MongoTemplate mongoTemplate, MongoProperties properties) {
        super(mongoTemplate, properties.map(MongoProperties::getCollection).getMarketUpdates());
    }

    @Override
    public String getServiceName() {
        return SERVICE_NAME;
    }
}
