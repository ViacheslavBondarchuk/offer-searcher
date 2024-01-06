package io.github.viacheslavbondarchuk.offersearcher.consumer.impl;

import io.github.viacheslavbondarchuk.offersearcher.consumer.AbstractStatusAwareConsumer;
import io.github.viacheslavbondarchuk.offersearcher.processor.impl.SelectionQueuedRecordProcessor;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public final class SelectionKafkaConsumer extends AbstractStatusAwareConsumer {
    private final SelectionQueuedRecordProcessor processor;

    public SelectionKafkaConsumer(@Value("${com.gamesys.sportsbook.common.transport.kafka.topic.selection}") String topic,
                                  Consumer<String, String> consumer, SelectionQueuedRecordProcessor processor) {
        super(consumer, topic);
        this.processor = processor;
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

    @Override
    public void onRecord(ConsumerRecord<String, String> record) {
        processor.offer(record);
    }

}
