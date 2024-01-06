package io.github.viacheslavbondarchuk.offersearcher.consumer;

import io.github.viacheslavbondarchuk.offersearcher.domain.KafkaTopicPartitionStatus;
import io.github.viacheslavbondarchuk.offersearcher.domain.KafkaTopicStatus;
import io.github.viacheslavbondarchuk.offersearcher.service.StatusAwareService;
import io.github.viacheslavbondarchuk.offersearcher.util.KeyValuePair;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.TopicPartition;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@Slf4j
public abstract class AbstractStatusAwareRawDataConsumer implements RawDataKafkaConsumer, StatusAwareService<KafkaTopicStatus> {
    protected final Map<TopicPartition, Long> topicMaxOffsetPair;
    protected final Map<TopicPartition, Long> topicCurrentOffsetPair;
    protected final Consumer<String, String> consumer;
    protected final String topic;

    protected AbstractStatusAwareRawDataConsumer(Consumer<String, String> consumer, String topic) {
        this.consumer = consumer;
        this.topicMaxOffsetPair = new HashMap<>();
        this.topicCurrentOffsetPair = new HashMap<>();
        this.topic = topic;
        this.init();
    }

    private void init() {
        consumer.endOffsets(
                consumer.listTopics()
                        .entrySet()
                        .stream()
                        .filter(entry -> Objects.equals(entry.getKey(), topic))
                        .map(Map.Entry::getValue)
                        .flatMap(List::stream)
                        .map(partitionInfo -> new TopicPartition(partitionInfo.topic(), partitionInfo.partition()))
                        .toList()).forEach((partition, offset) -> topicMaxOffsetPair.put(partition, offset - 1)
        );
    }

    @Override
    public KeyValuePair<String, KafkaTopicStatus> getStatus() {
        Map<TopicPartition, KafkaTopicPartitionStatus> partitionStatusMap = new HashMap<>();
        topicMaxOffsetPair.forEach((partition, offset) ->
                partitionStatusMap.put(partition, new KafkaTopicPartitionStatus(offset,
                        Optional.ofNullable(topicCurrentOffsetPair.get(partition)).orElse(0L))));
        return KeyValuePair.of(getServiceName(), KafkaTopicStatus.of(topic, isReady(), partitionStatusMap));
    }

    @Override
    public boolean isReady() {
        return topicMaxOffsetPair.entrySet().stream()
                .map(entry -> entry.getValue() - Optional.ofNullable(topicCurrentOffsetPair.get(entry.getKey())).orElse(0L))
                .reduce(0L, Long::sum) <= 0;
    }

    protected void updateCurrentOffset(String topic, int partition, Long offset) {
        topicCurrentOffsetPair.put(new TopicPartition(topic, partition), offset);
    }

    @Override
    public void onRecords(List<ConsumerRecord<String, String>> records) {
        for (ConsumerRecord<String, String> record : records) {
            try {
                log.debug("Processing kafka record. Key: {}, Value: {}", record.key(), record.value());
                updateCurrentOffset(record.topic(), record.partition(), record.offset());
                onRecord(record);
            } catch (Exception ex) {
                log.error("Can not process kafka record: Key: {}, Value: {}. Exception: ", record.key(), record.value(), ex);
            }
        }
    }
}
