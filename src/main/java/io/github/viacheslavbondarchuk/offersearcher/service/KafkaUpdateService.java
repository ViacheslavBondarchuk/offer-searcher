package io.github.viacheslavbondarchuk.offersearcher.service;

import io.github.viacheslavbondarchuk.offersearcher.domain.EntityType;
import io.github.viacheslavbondarchuk.offersearcher.domain.KafkaProduceUpdateRequest;
import io.github.viacheslavbondarchuk.offersearcher.properties.KafkaProperties;
import io.github.viacheslavbondarchuk.offersearcher.util.KafkaUtil;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.header.Headers;
import org.apache.kafka.common.header.internals.RecordHeader;
import org.bson.Document;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static io.github.viacheslavbondarchuk.offersearcher.constants.KafkaHeaders.KEY;
import static io.github.viacheslavbondarchuk.offersearcher.constants.KafkaHeaders.SOURCE_ID;
import static io.github.viacheslavbondarchuk.offersearcher.constants.KafkaHeaders.TYPE;

/**
 * author: vbondarchuk
 * date: 1/8/2024
 * time: 1:49 PM
 **/
@Service
@RequiredArgsConstructor
public class KafkaUpdateService {
    public static final String OFFER_SEARCHER_SOURCE_ID = "OFFER-SEARCHER-TOOL";


    private final KafkaTemplate<String, String> template;
    private final KafkaProperties properties;


    private String getValue(KafkaProduceUpdateRequest request) {
        return Optional.ofNullable(request.getValue())
                .map(Document::toJson)
                .orElse(null);
    }

    private Headers getHeaders(KafkaProduceUpdateRequest request) {
        return KafkaUtil.mapToHeaders(
                request.getHeaders(),
                new RecordHeader(SOURCE_ID, OFFER_SEARCHER_SOURCE_ID.getBytes()),
                new RecordHeader(TYPE, Optional.ofNullable(request.getType())
                        .map(Enum::name)
                        .map(String::getBytes)
                        .orElse(null)),
                new RecordHeader(KEY, Optional.ofNullable(request.getKey())
                        .map(String::getBytes)
                        .orElse(null))
        );
    }

    private String resolveTopic(EntityType type) {
        return switch (type) {
            case EVENT -> properties.map(KafkaProperties::getTopic).getEvent();
            case MARKET -> properties.map(KafkaProperties::getTopic).getMarket();
            case SELECTION -> properties.map(KafkaProperties::getTopic).getSelection();
        };
    }

    public ProducerRecord<String, String> produce(KafkaProduceUpdateRequest request) {
        ProducerRecord<String, String> record = new ProducerRecord<>(resolveTopic(request.getType()),
                null, request.getKey(), getValue(request), getHeaders(request));
        template.send(record);
        return record;
    }

}
