package io.github.viacheslavbondarchuk.offersearcher.processor;

import org.apache.kafka.clients.consumer.ConsumerRecord;

/**
 * author: vbondarchuk
 * date: 1/6/2024
 * time: 6:12 PM
 **/
public sealed interface QueuedRecordProcessor<K, V> permits AbstractQueuedRecordProcessor {

    void offer(ConsumerRecord<K, V> record);

    void handle();
}
