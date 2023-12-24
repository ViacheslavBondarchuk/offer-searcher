package io.github.viacheslavbondarchuk.offersearcher.domain;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class KafkaTopicPartitionStatus {
    private final Long maxOffset;
    private final Long currentOffset;
    private final Long remaining;

    public KafkaTopicPartitionStatus(Long maxOffset, Long currentOffset) {
        this.maxOffset = maxOffset;
        this.currentOffset = currentOffset;
        this.remaining = Long.max(maxOffset - currentOffset, 0L);
    }
}
