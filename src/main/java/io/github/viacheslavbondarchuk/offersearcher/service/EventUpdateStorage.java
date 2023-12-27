package io.github.viacheslavbondarchuk.offersearcher.service;

import io.github.viacheslavbondarchuk.offersearcher.properties.MongoProperties;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

@Service
public final class EventUpdateStorage extends AbstractDocumentStorage {
    public EventUpdateStorage(MongoTemplate mongoTemplate, MongoProperties properties) {
        super(mongoTemplate, properties.map(MongoProperties::getCollection).getEventUpdates());
    }

    @Override
    public String getServiceName() {
        return "Event updates storage";
    }

}
