package io.github.viacheslavbondarchuk.offersearcher.consumer.impl;

import io.github.viacheslavbondarchuk.offersearcher.consumer.AbstractKeepingRecordConsumer;
import io.github.viacheslavbondarchuk.offersearcher.storage.impl.EventStorage;
import io.github.viacheslavbondarchuk.offersearcher.storage.impl.EventUpdatesStorage;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public final class EventKafkaConsumer extends AbstractKeepingRecordConsumer {
    public EventKafkaConsumer(@Value("${com.gamesys.sportsbook.common.transport.kafka.topic.event}") String topic,
                              Consumer<String, String> consumer, EventStorage storage, EventUpdatesStorage updateHistoryStorage) {
        super(storage, updateHistoryStorage, consumer, topic);
    }

    @Override
    public String getServiceName() {
        return "Event consumer";
    }

    @Override
    @KafkaListener(topics = "${com.gamesys.sportsbook.common.transport.kafka.topic.event}",
            idIsGroup = false, containerFactory = "kafkaListenerContainerFactory", errorHandler = "kafkaListenerErrorHandler")
    public void onRecords(List<ConsumerRecord<String, String>> records) {
        super.onRecords(records);
    }

}
