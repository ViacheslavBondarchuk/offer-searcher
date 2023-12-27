package io.github.viacheslavbondarchuk.offersearcher.consumer;

import io.github.viacheslavbondarchuk.offersearcher.service.AbstractDocumentStorage;
import io.github.viacheslavbondarchuk.offersearcher.util.CommonUtil;
import io.github.viacheslavbondarchuk.offersearcher.util.DocumentUtil;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;

import java.text.MessageFormat;
import java.util.Objects;


public abstract class AbstractRecordKeepingConsumer extends AbstractStatusAwareRawDataConsumer {
    private static final String UPDATE_STORAGE_ID_TEMPLATE = "{0}-{1}";

    private final AbstractDocumentStorage updateStorage;
    private final AbstractDocumentStorage storage;

    protected AbstractRecordKeepingConsumer(Consumer<String, String> consumer, String topic,
                                            AbstractDocumentStorage updateStorage, AbstractDocumentStorage storage) {
        super(consumer, topic);
        this.updateStorage = updateStorage;
        this.storage = storage;
    }

    @Override
    public void onRecord(ConsumerRecord<String, String> record) {
        CommonUtil.acceptByPredicate(record.value(), Objects::nonNull, value -> {
            storage.save(DocumentUtil.fromRecordWithoutEntityId(record.key(), record));
            updateStorage.save(DocumentUtil.fromRecord(MessageFormat.format(UPDATE_STORAGE_ID_TEMPLATE, String.valueOf(record.offset()), record.partition()), record));
        }, () -> {
            storage.remove(record.key());
            updateStorage.save(DocumentUtil.fromRecord(MessageFormat.format(UPDATE_STORAGE_ID_TEMPLATE, String.valueOf(record.offset()), record.partition()), record));
        });
    }
}
