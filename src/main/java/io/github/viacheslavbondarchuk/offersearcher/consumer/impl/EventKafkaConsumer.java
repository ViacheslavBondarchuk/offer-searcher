package io.github.viacheslavbondarchuk.offersearcher.consumer.impl;

import io.github.viacheslavbondarchuk.offersearcher.consumer.AbstractRecordKeepingConsumer;
import io.github.viacheslavbondarchuk.offersearcher.service.EventStorage;
import io.github.viacheslavbondarchuk.offersearcher.service.EventUpdateStorage;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public final class EventKafkaConsumer extends AbstractRecordKeepingConsumer {

    public EventKafkaConsumer(@Value("${com.gamesys.sportsbook.common.transport.kafka.topic.event}") String topic,
                              Consumer<String, String> consumer, EventStorage eventStorage, EventUpdateStorage eventUpdatesStorage) {
        super(consumer, topic, eventUpdatesStorage, eventStorage);
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

}
