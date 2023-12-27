package io.github.viacheslavbondarchuk.offersearcher.service;

import io.github.viacheslavbondarchuk.offersearcher.properties.MongoProperties;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

@Service
public final class SelectionUpdateStorage extends AbstractDocumentStorage {
    public SelectionUpdateStorage(MongoTemplate mongoTemplate, MongoProperties properties) {
        super(mongoTemplate, properties.map(MongoProperties::getCollection).getSelectionUpdates());
    }

    @Override
    public String getServiceName() {
        return "Selection updates storage";
    }
}
