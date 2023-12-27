package io.github.viacheslavbondarchuk.offersearcher.consumer;

import org.apache.kafka.clients.consumer.ConsumerRecord;

import java.util.List;

public interface RawDataKafkaConsumer {

    void onRecords(List<ConsumerRecord<String, String>> records);

    void onRecord(ConsumerRecord<String, String> record);

    boolean isReady();

}
