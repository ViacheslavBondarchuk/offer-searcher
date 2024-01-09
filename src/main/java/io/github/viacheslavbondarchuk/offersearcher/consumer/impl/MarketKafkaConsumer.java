package io.github.viacheslavbondarchuk.offersearcher.consumer.impl;

import io.github.viacheslavbondarchuk.offersearcher.consumer.AbstractKeepingRecordConsumer;
import io.github.viacheslavbondarchuk.offersearcher.storage.impl.MarketStorage;
import io.github.viacheslavbondarchuk.offersearcher.storage.impl.MarketUpdateStorage;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public final class MarketKafkaConsumer extends AbstractKeepingRecordConsumer {

    public MarketKafkaConsumer(@Value("${com.gamesys.sportsbook.common.transport.kafka.topic.market}") String topic,
                               Consumer<String, String> consumer, MarketStorage storage, MarketUpdateStorage updateHistoryStorage) {
        super(storage, updateHistoryStorage, consumer, topic);
    }

    @Override
    public String getServiceName() {
        return "Market consumer";
    }

    @Override
    @KafkaListener(topics = "${com.gamesys.sportsbook.common.transport.kafka.topic.market}",
            idIsGroup = false, containerFactory = "kafkaListenerContainerFactory", errorHandler = "kafkaListenerErrorHandler")
    public void onRecords(List<ConsumerRecord<String, String>> records) {
        super.onRecords(records);
    }
}
