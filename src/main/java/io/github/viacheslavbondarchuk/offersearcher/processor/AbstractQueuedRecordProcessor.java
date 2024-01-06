package io.github.viacheslavbondarchuk.offersearcher.processor;

import io.github.viacheslavbondarchuk.offersearcher.util.QueueUtil;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerRecord;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * author: vbondarchuk
 * date: 1/6/2024
 * time: 6:17 PM
 **/
@RequiredArgsConstructor
public abstract non-sealed class AbstractQueuedRecordProcessor<K, V> implements QueuedRecordProcessor<K, V> {
    protected final Queue<ConsumerRecord<K, V>> queue = new ConcurrentLinkedQueue<>();

    protected final int batchSize;

    @Override
    public void offer(ConsumerRecord<K, V> record) {
        queue.offer(record);
    }

    protected List<ConsumerRecord<K, V>> getBatch() {
        return QueueUtil.drainTo(queue, LinkedList::new, batchSize);
    }
}
