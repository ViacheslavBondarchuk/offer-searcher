package io.github.viacheslavbondarchuk.offersearcher.processor.impl;

import io.github.viacheslavbondarchuk.offersearcher.processor.AbstractQueuedRecordProcessor;
import io.github.viacheslavbondarchuk.offersearcher.service.EventStorage;
import io.github.viacheslavbondarchuk.offersearcher.service.EventUpdateStorage;
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
 * time: 6:20 PM
 **/

@Component
public final class EventQueuedRecordProcessor extends AbstractQueuedRecordProcessor<String, String> {
    private final EventUpdateStorage eventUpdatesStorage;
    private final EventStorage eventStorage;

    public EventQueuedRecordProcessor(@Value("${processor.batch-size:500}") int batchSize,
                                      EventUpdateStorage eventUpdatesStorage,
                                      EventStorage eventStorage) {
        super(batchSize);
        this.eventUpdatesStorage = eventUpdatesStorage;
        this.eventStorage = eventStorage;
    }


    @Override
    @Scheduled(fixedRateString = "${processor.schedule.fixed-rate-ms}")
    public void handle() {
        List<ConsumerRecord<String, String>> events = getBatch();
        if (!events.isEmpty()) {
            eventUpdatesStorage.saveAll(events.stream()
                    .map(record -> DocumentUtil.fromRecord(
                            DocumentUtil.offsetAndPartitionAsId(record.offset(), record.partition()), record))
                    .toList());
            events.forEach(record -> CommonUtil.acceptByPredicate(record.value(), Objects::nonNull, value ->
                    eventStorage.save(DocumentUtil.fromRecordWithoutEntityId(record.key(), record)), () -> eventStorage.remove(record.key())));
        }
    }
}
