package io.github.viacheslavbondarchuk.offersearcher.consumer.impl;

import io.github.viacheslavbondarchuk.offersearcher.consumer.AbstractKeepingRecordConsumer;
import io.github.viacheslavbondarchuk.offersearcher.storage.impl.SelectionStorage;
import io.github.viacheslavbondarchuk.offersearcher.storage.impl.SelectionUpdatesStorage;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public final class SelectionKafkaConsumer extends AbstractKeepingRecordConsumer {

    public SelectionKafkaConsumer(@Value("${com.gamesys.sportsbook.common.transport.kafka.topic.selection}") String topic,
                                  Consumer<String, String> consumer, SelectionStorage storage, SelectionUpdatesStorage updateHistoryStorage) {
        super(storage, updateHistoryStorage, consumer, topic);
    }

    @Override
    public String getServiceName() {
        return "Selection consumer";
    }

    @Override
    @KafkaListener(topics = "${com.gamesys.sportsbook.common.transport.kafka.topic.selection}",
            idIsGroup = false, containerFactory = "kafkaListenerContainerFactory", errorHandler = "kafkaListenerErrorHandler")
    public void onRecords(List<ConsumerRecord<String, String>> records) {
        super.onRecords(records);
    }


}
