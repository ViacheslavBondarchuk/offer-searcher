package io.github.viacheslavbondarchuk.offersearcher.domain;

import lombok.Getter;
import lombok.ToString;

import static java.lang.Long.max;


@Getter
@ToString
public final class KafkaTopicStatus implements ServiceStatus {
    private final String topic;
    private final Long maxOffset;
    private final Long currentOffset;
    private final Long remaining;
    private final boolean ready;

    public KafkaTopicStatus(String topic, Long maxOffset, Long currentOffset) {
        this.topic = topic;
        this.maxOffset = maxOffset;
        this.currentOffset = currentOffset;
        this.remaining = max(maxOffset - currentOffset, 0L);
        this.ready = this.remaining <= 0;
    }

    public static KafkaTopicStatus of(String topic, Long maxOffset, Long currentOffset) {
        return new KafkaTopicStatus(topic, maxOffset, currentOffset);
    }
}
