package io.github.viacheslavbondarchuk.offersearcher.util;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.bson.Document;

import java.util.Map;
import java.util.Optional;

import static io.github.viacheslavbondarchuk.offersearcher.domain.OperationType.REMOVE;
import static io.github.viacheslavbondarchuk.offersearcher.domain.OperationType.UPDATE;
import static io.github.viacheslavbondarchuk.offersearcher.service.AbstractDocumentStorage.ENTITY_ID_KEY;
import static io.github.viacheslavbondarchuk.offersearcher.service.AbstractDocumentStorage.MONGO_ID_KEY;

public final class DocumentUtil {
    public static final String HEADERS_KEY = "headers";
    public static final String TIMESTAMP_KEY = "timestamp";
    public static final String TIMESTAMP_TYPE_KEY = "timestampType";
    public static final String PARTITION_KEY = "partition";
    public static final String SYSTEM_DATA_KEY = "systemData";
    public static final String OFFSET_KEY = "offset";
    public static final String OPERATION_TYPE_KEY = "operationType";

    private DocumentUtil() {}

    public static Document fromRecordWithoutEntityId(String id, ConsumerRecord<String, String> record) {
        Document document = fromRecord(id, record);
        document.remove(ENTITY_ID_KEY);
        return document;
    }

    public static Document fromRecord(String id, ConsumerRecord<String, String> record) {
        Document document = record.value() == null ? new Document() : Document.parse(record.value());
        document.append(HEADERS_KEY, CommonUtil.convertHeadersToMap(record.headers()));
        document.append(SYSTEM_DATA_KEY, Map.of(
                TIMESTAMP_KEY, record.timestamp(),
                TIMESTAMP_TYPE_KEY, record.timestampType(),
                OFFSET_KEY, record.offset(),
                PARTITION_KEY, record.partition(),
                OPERATION_TYPE_KEY, record.value() == null ? REMOVE : UPDATE
        ));
        Optional.ofNullable(id).ifPresent(i -> document.append(MONGO_ID_KEY, id));
        return document;
    }
}
