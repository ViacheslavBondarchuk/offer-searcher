package io.github.viacheslavbondarchuk.offersearcher.service;

import io.github.viacheslavbondarchuk.offersearcher.properties.MongoProperties;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

@Service
public final class MarketStorage extends AbstractDocumentStorage {
    public MarketStorage(MongoTemplate mongoTemplate, MongoProperties properties) {
        super(mongoTemplate, properties.map(MongoProperties::getCollection).getMarket());
    }

    @Override
    public String getServiceName() {
        return "Market storage";
    }
}
