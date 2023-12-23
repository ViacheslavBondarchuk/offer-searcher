package io.github.viacheslavbondarchuk.offersearcher.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public final class JsonUtils {
    private static final ObjectMapper mapper = new ObjectMapper();

    private JsonUtils() {}

    @SneakyThrows
    public static String toRawJson(Object object) {
        return mapper.writeValueAsString(object);
    }


}
