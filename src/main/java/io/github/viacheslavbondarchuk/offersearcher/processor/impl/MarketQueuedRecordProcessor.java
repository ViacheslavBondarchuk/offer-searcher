package io.github.viacheslavbondarchuk.offersearcher.processor.impl;

import io.github.viacheslavbondarchuk.offersearcher.processor.AbstractQueuedRecordProcessor;
import io.github.viacheslavbondarchuk.offersearcher.service.MarketStorage;
import io.github.viacheslavbondarchuk.offersearcher.service.MarketUpdateStorage;
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
 * time: 6:21 PM
 **/

@Component
public final class MarketQueuedRecordProcessor extends AbstractQueuedRecordProcessor<String, String> {
    private final MarketUpdateStorage marketUpdatesStorage;
    private final MarketStorage marketStorage;

    public MarketQueuedRecordProcessor(@Value("${processor.batch-size:500}") int batchSize,
                                       MarketUpdateStorage marketUpdatesStorage,
                                       MarketStorage marketStorage) {
        super(batchSize);
        this.marketUpdatesStorage = marketUpdatesStorage;
        this.marketStorage = marketStorage;
    }


    @Override
    @Scheduled(fixedRateString = "${processor.schedule.fixed-rate-ms}")
    public void handle() {
        List<ConsumerRecord<String, String>> markets = getBatch();
        if (!markets.isEmpty()) {
            marketUpdatesStorage.saveAll(markets.stream()
                    .map(record -> DocumentUtil.fromRecord(
                            DocumentUtil.offsetAndPartitionAsId(record.offset(), record.partition()), record))
                    .toList());
            markets.forEach(record -> CommonUtil.acceptByPredicate(record.value(), Objects::nonNull, value ->
                    marketStorage.save(DocumentUtil.fromRecordWithoutEntityId(record.key(), record)), () -> marketStorage.remove(record.key())));
        }
    }
}
