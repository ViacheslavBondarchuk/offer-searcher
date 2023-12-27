package io.github.viacheslavbondarchuk.offersearcher.consumer.impl;

import io.github.viacheslavbondarchuk.offersearcher.consumer.AbstractRecordKeepingConsumer;
import io.github.viacheslavbondarchuk.offersearcher.service.SelectionStorage;
import io.github.viacheslavbondarchuk.offersearcher.service.SelectionUpdateStorage;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public final class SelectionKafkaConsumer extends AbstractRecordKeepingConsumer {

    public SelectionKafkaConsumer(@Value("${com.gamesys.sportsbook.common.transport.kafka.topic.selection}") String topic,
                                  Consumer<String, String> consumer, SelectionStorage selectionStorage, SelectionUpdateStorage updateStorage) {
        super(consumer, topic, updateStorage, selectionStorage);
    }

    @Override
    public String getServiceName() {
        return "Selection consumer";
    }

    @Override
    @KafkaListener(topics = "${com.gamesys.sportsbook.common.transport.kafka.topic.selection}",
            idIsGroup = false, containerFactory = "kafkaListenerContainerFactory", errorHandler = "kafkaListenerErrorHandler")
    public void onRecords(List<ConsumerRecord<String, String>> consumerRecords) {
        super.onRecords(consumerRecords);
    }

}
