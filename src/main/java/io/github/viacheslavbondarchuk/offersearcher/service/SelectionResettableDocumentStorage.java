package io.github.viacheslavbondarchuk.offersearcher.service;

import io.github.viacheslavbondarchuk.offersearcher.properties.MongoProperties;
import org.bson.Document;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

@Service
public final class SelectionResettableDocumentStorage extends AbstractResettableDocumentStorage<String, String, Document> {
    public SelectionResettableDocumentStorage(MongoTemplate mongoTemplate, MongoProperties properties) {
        super(mongoTemplate, properties.map(MongoProperties::getCollection).getSelection());
    }

    @Override
    protected Document convert(String entity) {
        return Document.parse(entity);
    }

    @Override
    public String getServiceName() {
        return "Selection storage";
    }
}
