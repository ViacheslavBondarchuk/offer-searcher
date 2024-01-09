package io.github.viacheslavbondarchuk.offersearcher.storage.impl;

import io.github.viacheslavbondarchuk.offersearcher.properties.MongoProperties;
import io.github.viacheslavbondarchuk.offersearcher.storage.AbstractDocumentStorage;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

@Service
public final class SelectionStorage extends AbstractDocumentStorage {
    public static final String SERVICE_NAME = "Selection storage";

    public SelectionStorage(MongoTemplate mongoTemplate, MongoProperties properties) {
        super(mongoTemplate, properties.map(MongoProperties::getCollection).getSelection());
    }

    @Override
    public String getServiceName() {
        return SERVICE_NAME;
    }
}
