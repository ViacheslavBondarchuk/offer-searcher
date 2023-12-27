package io.github.viacheslavbondarchuk.offersearcher.service;

import io.github.viacheslavbondarchuk.offersearcher.properties.MongoProperties;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

@Service
public final class MarketUpdateStorage extends AbstractDocumentStorage {
    public MarketUpdateStorage(MongoTemplate mongoTemplate, MongoProperties properties) {
        super(mongoTemplate, properties.map(MongoProperties::getCollection).getMarketUpdates());
    }

    @Override
    public String getServiceName() {
        return "Market updates storage";
    }
}
