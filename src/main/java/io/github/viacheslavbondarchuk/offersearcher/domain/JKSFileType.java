package io.github.viacheslavbondarchuk.offersearcher.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum JKSFileType {
    OFFER_SEARCHER_KEYSTORE(
            "offer-searcher-keystore.jks",
            "com.gamesys.sportsbook.common.transport.kafka.ssl-properties.keystore",
            "com.gamesys.sportsbook.common.transport.kafka.ssl-properties.keystore-password"
    ),
    OFFER_SEARCHER_TRUSTSTORE(
            "offer-searcher-truststore.jks",
            "com.gamesys.sportsbook.common.transport.kafka.ssl-properties.truststore",
            "com.gamesys.sportsbook.common.transport.kafka.ssl-properties.truststore-password"
    );

    private final String name;
    private final String value;
    private final String password;
}
