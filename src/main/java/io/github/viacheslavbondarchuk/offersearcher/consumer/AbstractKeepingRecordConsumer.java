package io.github.viacheslavbondarchuk.offersearcher.consumer;

import io.github.viacheslavbondarchuk.offersearcher.domain.KafkaTopicPartitionStatus;
import io.github.viacheslavbondarchuk.offersearcher.domain.KafkaTopicStatus;
import io.github.viacheslavbondarchuk.offersearcher.service.StatusAwareService;
import io.github.viacheslavbondarchuk.offersearcher.storage.AbstractDocumentStorage;
import io.github.viacheslavbondarchuk.offersearcher.util.CommonUtil;
import io.github.viacheslavbondarchuk.offersearcher.util.DocumentUtil;
import io.github.viacheslavbondarchuk.offersearcher.util.KeyValuePair;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.TopicPartition;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

public abstract class AbstractKeepingRecordConsumer implements RawDataKafkaConsumer, StatusAwareService<KafkaTopicStatus> {
    protected final Map<TopicPartition, Long> topicMaxOffsetPair;
    protected final Map<TopicPartition, Long> topicCurrentOffsetPair;
    protected final Consumer<String, String> consumer;
    protected final String topic;

    private final AbstractDocumentStorage storage;
    private final AbstractDocumentStorage updateHistoryStorage;

    protected AbstractKeepingRecordConsumer(AbstractDocumentStorage storage, AbstractDocumentStorage updateHistoryStorage,
                                            Consumer<String, String> consumer, String topic) {
        this.consumer = consumer;
        this.updateHistoryStorage = updateHistoryStorage;
        this.topicMaxOffsetPair = new HashMap<>();
        this.topicCurrentOffsetPair = new HashMap<>();
        this.topic = topic;
        this.storage = storage;
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
        CommonUtil.executeByPredicate(!records.isEmpty(), () -> {
            updateHistoryStorage.saveAll(records.stream()
                    .peek(record -> updateCurrentOffset(record.topic(), record.partition(), record.offset()))
                    .map(record -> DocumentUtil.fromRecord(
                            DocumentUtil.offsetAndPartitionAsId(record.offset(), record.partition()), record))
                    .toList());
            records.forEach(record -> CommonUtil.acceptByPredicate(
                    record.value(), Objects::nonNull,
                    value -> storage.save(DocumentUtil.fromRecordWithoutEntityId(record.key(), record)),
                    () -> storage.remove(record.key())
            ));
        });
    }
}
