package io.github.viacheslavbondarchuk.offersearcher.consumer;

import org.apache.kafka.clients.consumer.ConsumerRecord;

import java.util.List;

public interface KafkaConsumer<K, V> {

    void onRecords(List<ConsumerRecord<K, V>> records);

    void onRecord(ConsumerRecord<K, V> record);

    boolean isReady();

}
