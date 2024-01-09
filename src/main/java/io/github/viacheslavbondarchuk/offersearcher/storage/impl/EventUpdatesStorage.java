package io.github.viacheslavbondarchuk.offersearcher.storage.impl;

import io.github.viacheslavbondarchuk.offersearcher.properties.MongoProperties;
import io.github.viacheslavbondarchuk.offersearcher.storage.AbstractDocumentStorage;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

@Service
public final class EventUpdatesStorage extends AbstractDocumentStorage {
    public static final String SERVICE_NAME = "Event updates storage";

    public EventUpdatesStorage(MongoTemplate mongoTemplate, MongoProperties properties) {
        super(mongoTemplate, properties.map(MongoProperties::getCollection).getEventUpdates());
    }

    @Override
    public String getServiceName() {
        return SERVICE_NAME;
    }
}
