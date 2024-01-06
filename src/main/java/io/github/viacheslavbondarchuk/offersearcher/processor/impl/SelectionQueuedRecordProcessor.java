package io.github.viacheslavbondarchuk.offersearcher.processor.impl;

import io.github.viacheslavbondarchuk.offersearcher.processor.AbstractQueuedRecordProcessor;
import io.github.viacheslavbondarchuk.offersearcher.service.SelectionStorage;
import io.github.viacheslavbondarchuk.offersearcher.service.SelectionUpdateStorage;
import io.github.viacheslavbondarchuk.offersearcher.util.CommonUtil;
import io.github.viacheslavbondarchuk.offersearcher.util.DocumentUtil;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

/**
 * author: vbondarchuk
 * date: 1/6/2024
 * time: 6:22 PM
 **/

@Component
public final class SelectionQueuedRecordProcessor extends AbstractQueuedRecordProcessor<String, String> {
    private final SelectionUpdateStorage selectionUpdatesStorage;
    private final SelectionStorage selectionStorage;

    public SelectionQueuedRecordProcessor(@Value("${processor.batch-size:500}") int batchSize,
                                          SelectionUpdateStorage selectionUpdatesStorage,
                                          SelectionStorage selectionStorage) {
        super(batchSize);
        this.selectionUpdatesStorage = selectionUpdatesStorage;
        this.selectionStorage = selectionStorage;
    }

    @Override
    @Scheduled(fixedRateString = "${processor.schedule.fixed-rate-ms}")
    public void handle() {
        List<ConsumerRecord<String, String>> selections = getBatch();
        if (!selections.isEmpty()) {
            selectionUpdatesStorage.saveAll(selections.stream()
                    .map(record -> DocumentUtil.fromRecord(
                            DocumentUtil.offsetAndPartitionAsId(record.offset(), record.partition()), record))
                    .toList());
            selections.forEach(record -> CommonUtil.acceptByPredicate(record.value(), Objects::nonNull, value ->
                            selectionStorage.save(DocumentUtil.fromRecordWithoutEntityId(record.key(), record)), () -> selectionStorage.remove(record.key())));
        }
    }
}
