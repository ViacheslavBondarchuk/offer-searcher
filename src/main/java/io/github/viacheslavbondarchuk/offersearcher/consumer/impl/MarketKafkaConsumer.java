package io.github.viacheslavbondarchuk.offersearcher.consumer.impl;

import io.github.viacheslavbondarchuk.offersearcher.consumer.AbstractStatusAwareConsumer;
import io.github.viacheslavbondarchuk.offersearcher.processor.impl.MarketQueuedRecordProcessor;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public final class MarketKafkaConsumer extends AbstractStatusAwareConsumer {
    private final MarketQueuedRecordProcessor processor;

    public MarketKafkaConsumer(@Value("${com.gamesys.sportsbook.common.transport.kafka.topic.market}") String topic,
                               Consumer<String, String> consumer, MarketQueuedRecordProcessor processor) {
        super(consumer, topic);
        this.processor = processor;
    }

    @Override
    public String getServiceName() {
        return "Market consumer";
    }

    @Override
    @KafkaListener(topics = "${com.gamesys.sportsbook.common.transport.kafka.topic.market}",
            idIsGroup = false, containerFactory = "kafkaListenerContainerFactory", errorHandler = "kafkaListenerErrorHandler")
    public void onRecords(List<ConsumerRecord<String, String>> consumerRecords) {
        super.onRecords(consumerRecords);
    }

    @Override
    public void onRecord(ConsumerRecord<String, String> record) {
        processor.offer(record);
    }
}
