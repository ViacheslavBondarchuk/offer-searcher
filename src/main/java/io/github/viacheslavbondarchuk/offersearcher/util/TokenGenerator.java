package io.github.viacheslavbondarchuk.offersearcher.util;

import io.github.viacheslavbondarchuk.offersearcher.domain.HazelcastSession;

import java.util.Base64;

public final class TokenGenerator {

    private TokenGenerator() {}

    public static String generate(HazelcastSession session) {
        return Base64.getEncoder()
                .encodeToString(session.getIp().getBytes());
    }
}
