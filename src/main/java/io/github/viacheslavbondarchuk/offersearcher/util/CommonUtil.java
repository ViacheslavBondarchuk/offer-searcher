package io.github.viacheslavbondarchuk.offersearcher.util;

import io.github.viacheslavbondarchuk.offersearcher.domain.EntityType;
import org.apache.kafka.common.header.Header;
import org.apache.kafka.common.header.Headers;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * author: vbondarchuk
 * date: 12/12/2023
 * time: 10:14 PM
 **/
public final class CommonUtil {
    private static final Set<String> excludedHeaders = new HashSet<>();

    static {
        excludedHeaders.add("__TypeId__");
    }

    private CommonUtil() {}

    public static <T> void acceptIfNonNull(T value, Consumer<T> consumer) {
        acceptIfNonNull(value, consumer, (Runnable) null);
    }

    public static <T> void acceptIfNonNull(T value, Consumer<T> consumer, Runnable runnable) {
        if (value != null) {
            consumer.accept(value);
        } else if (runnable != null) {
            runnable.run();
        }
    }

    public static <K, V> void acceptIfNonNull(K key, V value, BiConsumer<K, V> consumer) {
        if (key != null && value != null) {
            consumer.accept(key, value);
        }
    }

    public static String groupIdWithTimestamp(String groupName) {
        return groupName.concat("-").concat(String.valueOf(System.currentTimeMillis()));
    }

    public static String generateHazelcastMapName(EntityType type, String brandId) {
        return type.getValue().concat("_").concat(brandId.replaceAll("-", "_"));
    }

    public static <T> void acceptByPredicate(T value, Predicate<T> predicate, Consumer<T> consumer, Runnable orElse) {
        if (predicate.test(value)) {
            consumer.accept(value);
        } else if (orElse != null) {
            orElse.run();
        }
    }

    public static <T> void acceptByPredicate(T value, Predicate<T> predicate, Consumer<T> consumer) {
        acceptByPredicate(value, predicate, consumer, null);
    }

    @SafeVarargs
    public static <T> void arrayForEach(Consumer<T> consumer, T... values) {
        for (T value : values) {
            consumer.accept(value);
        }
    }

    public static Map<String, String> convertHeadersToMap(Headers headers) {
        Map<String, String> map = new HashMap<>();
        for (Header header : headers) {
            if (header.value() != null && !excludedHeaders.contains(header.key())) {
                map.put(header.key(), new String(header.value()));
            }
        }
        return map;
    }


}
