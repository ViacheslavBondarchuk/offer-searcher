package io.github.viacheslavbondarchuk.offersearcher.domain;

import lombok.Getter;
import lombok.ToString;
import org.apache.kafka.common.TopicPartition;

import java.util.Map;


@Getter
@ToString
public final class KafkaTopicStatus implements ServiceStatus {
    private final String topic;
    private final boolean ready;
    private final Map<TopicPartition, KafkaTopicPartitionStatus> partitions;


    public KafkaTopicStatus(String topic, boolean ready, Map<TopicPartition, KafkaTopicPartitionStatus> partitions) {
        this.topic = topic;
        this.ready = ready;
        this.partitions = partitions;
    }


    public static KafkaTopicStatus of(String topic, boolean ready, Map<TopicPartition, KafkaTopicPartitionStatus> partitions) {
        return new KafkaTopicStatus(topic, ready, partitions);
    }
}
