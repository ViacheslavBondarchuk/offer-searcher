package io.github.viacheslavbondarchuk.offersearcher.util;

import java.util.Collection;
import java.util.Queue;
import java.util.function.Supplier;

/**
 * author: vbondarchuk
 * date: 1/6/2024
 * time: 6:31 PM
 **/
public final class QueueUtil {
    private QueueUtil() {}

    public static <T, C extends Collection<T>> C drainTo(Queue<T> source, Supplier<C> factory, int limit) {
        assert limit > 0;
        C destination = factory.get();
        int size = source.size();
        int counter = 0;

        while (counter < limit && size != 0) {
            destination.add(source.poll());
            counter++;
            size--;
        }
        return destination;
    }
}
