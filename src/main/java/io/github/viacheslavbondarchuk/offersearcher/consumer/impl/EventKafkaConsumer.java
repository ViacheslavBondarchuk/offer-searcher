package io.github.viacheslavbondarchuk.offersearcher.consumer.impl;

import io.github.viacheslavbondarchuk.offersearcher.consumer.AbstractKafkaTopicStatusAwareConsumer;
import io.github.viacheslavbondarchuk.offersearcher.service.EventResettableDocumentStorage;
import io.github.viacheslavbondarchuk.offersearcher.util.CommonUtil;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public final class EventKafkaConsumer extends AbstractKafkaTopicStatusAwareConsumer<String, String> {
    private final EventResettableDocumentStorage storage;

    public EventKafkaConsumer(@Value("${com.gamesys.sportsbook.common.transport.kafka.topic.event}") String topic,
                              Consumer<String, String> consumer,
                              EventResettableDocumentStorage storage) {
        super(consumer, topic);
        this.storage = storage;
    }

    @Override
    public String getServiceName() {
        return "Event consumer";
    }

    @Override
    @KafkaListener(topics = "${com.gamesys.sportsbook.common.transport.kafka.topic.event}",
            idIsGroup = false, containerFactory = "kafkaListenerContainerFactory", errorHandler = "kafkaListenerErrorHandler")
    public void onRecords(List<ConsumerRecord<String, String>> consumerRecords) {
        super.onRecords(consumerRecords);
    }

    @Override
    public void onRecord(ConsumerRecord<String, String> record) {
        Optional.ofNullable(record.value())
                .ifPresentOrElse(value -> storage.save(record.key(), value, Map.of("headers", CommonUtil.convertHeadersToMap(record.headers()))),
                        () -> storage.remove(record.key()));
    }
}
