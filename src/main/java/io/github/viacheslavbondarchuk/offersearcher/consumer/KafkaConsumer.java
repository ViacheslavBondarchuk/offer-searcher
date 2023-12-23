package io.github.viacheslavbondarchuk.offersearcher.consumer;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.support.Acknowledgment;

import java.util.List;

public interface KafkaConsumer<K, V> {

    void onRecords(List<ConsumerRecord<K, V>> records, Acknowledgment acknowledgment);

    void onRecord(ConsumerRecord<K, V> record);

    boolean isReady();

}
