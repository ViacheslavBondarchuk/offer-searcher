package io.github.viacheslavbondarchuk.offersearcher.util;

import org.apache.kafka.common.header.Header;
import org.apache.kafka.common.header.Headers;
import org.apache.kafka.common.header.internals.RecordHeaders;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

/**
 * author: vbondarchuk
 * date: 1/8/2024
 * time: 2:49 PM
 **/
public final class KafkaUtil {
    private static final Set<String> excludedHeaders = Set.of("__TypeId__");

    private KafkaUtil() {}

    public static Map<String, String> headersToMap(Headers headers) {
        Map<String, String> map = new HashMap<>();
        for (Header header : headers) {
            if (header.value() != null && !excludedHeaders.contains(header.key())) {
                map.put(header.key(), new String(header.value()));
            }
        }
        return map;
    }

    public static Headers mapToHeaders(Map<String, String> map, Header... additional) {
        RecordHeaders headers = new RecordHeaders();
        map.forEach((key, value) -> headers.add(key, value.getBytes()));
        Optional.ofNullable(additional)
                .ifPresent(additionalHeaders -> Arrays.stream(additionalHeaders)
                        .forEach(headers::add));
        return headers;
    }
}
