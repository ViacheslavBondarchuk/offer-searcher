package io.github.viacheslavbondarchuk.offersearcher.consumer;

import io.github.viacheslavbondarchuk.offersearcher.domain.KafkaTopicStatus;
import io.github.viacheslavbondarchuk.offersearcher.service.StatusAwareService;
import io.github.viacheslavbondarchuk.offersearcher.util.KeyValuePair;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.TopicPartition;
import org.springframework.kafka.support.Acknowledgment;

import java.util.List;
import java.util.Map;
import java.util.Objects;

import static java.lang.Long.max;

@Slf4j
public abstract class AbstractKafkaTopicStatusAwareConsumer<K, V> implements KafkaConsumer<K, V>, StatusAwareService<KafkaTopicStatus> {
    protected final KeyValuePair<String, Long> topicMaxOffsetPair;
    protected final KeyValuePair<String, Long> topicCurrentOffsetPair;
    protected final String topic;

    protected AbstractKafkaTopicStatusAwareConsumer(Consumer<String, String> consumer, String topic) {
        this.topicMaxOffsetPair = KeyValuePair.of(topic, consumer.endOffsets(consumer.listTopics()
                        .entrySet()
                        .stream()
                        .filter(entry -> Objects.equals(entry.getKey(), topic))
                        .map(Map.Entry::getValue)
                        .flatMap(List::stream)
                        .map(partitionInfo -> new TopicPartition(partitionInfo.topic(), partitionInfo.partition()))
                        .toList()).values()
                .stream()
                .reduce(0L, Long::sum) - 1);
        this.topicCurrentOffsetPair = KeyValuePair.of(topic, 0L);
        this.topic = topic;
    }

    @Override
    public KeyValuePair<String, KafkaTopicStatus> getStatus() {
        return KeyValuePair.of(getServiceName(), KafkaTopicStatus.of(topic, topicMaxOffsetPair.getValue(), topicCurrentOffsetPair.getValue()));
    }

    @Override
    public boolean isReady() {
        return max(topicMaxOffsetPair.getValue() - topicCurrentOffsetPair.getValue(), 0L) <= 0;
    }

    protected void updateCurrentOffset(long offset) {
        topicCurrentOffsetPair.updateValue(offset);
    }

    @Override
    public void onRecords(List<ConsumerRecord<K, V>> records, Acknowledgment acknowledgment) {
        for (ConsumerRecord<K, V> record : records) {
            try {
                log.info("Processing kafka record. Key: {}, Value: {}", record.key(), record.value());
                updateCurrentOffset(record.offset());
                onRecord(record);
            } catch (Exception ex) {
                log.error("Can not process kafka record: Key: {}, Value: {}. Exception: ", record.key(), record.value(), ex);
            }
        }
        acknowledgment.acknowledge();
    }
}
