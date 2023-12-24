package io.github.viacheslavbondarchuk.offersearcher.properties;

import io.github.viacheslavbondarchuk.offersearcher.service.JKSFileService;
import io.github.viacheslavbondarchuk.offersearcher.util.CommonUtil;
import lombok.Getter;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.ConstructorBinding;
import org.springframework.kafka.support.serializer.ErrorHandlingDeserializer;

import java.util.HashMap;
import java.util.Map;

import static io.github.viacheslavbondarchuk.offersearcher.domain.JKSFileType.OFFER_SEARCHER_KEYSTORE;
import static io.github.viacheslavbondarchuk.offersearcher.domain.JKSFileType.OFFER_SEARCHER_TRUSTSTORE;
import static org.apache.kafka.common.config.SslConfigs.SSL_ENABLED_PROTOCOLS_CONFIG;
import static org.apache.kafka.common.config.SslConfigs.SSL_ENDPOINT_IDENTIFICATION_ALGORITHM_CONFIG;
import static org.apache.kafka.common.config.SslConfigs.SSL_KEYSTORE_LOCATION_CONFIG;
import static org.apache.kafka.common.config.SslConfigs.SSL_KEYSTORE_PASSWORD_CONFIG;
import static org.apache.kafka.common.config.SslConfigs.SSL_KEYSTORE_TYPE_CONFIG;
import static org.apache.kafka.common.config.SslConfigs.SSL_KEY_PASSWORD_CONFIG;
import static org.apache.kafka.common.config.SslConfigs.SSL_TRUSTSTORE_LOCATION_CONFIG;
import static org.apache.kafka.common.config.SslConfigs.SSL_TRUSTSTORE_PASSWORD_CONFIG;
import static org.apache.kafka.common.config.SslConfigs.SSL_TRUSTSTORE_TYPE_CONFIG;

@Getter
@ConfigurationProperties("com.gamesys.sportsbook.common.transport.kafka")
public final class KafkaProperties {
    public static final String SECURITY_INTER_BROKER_PROTOCOL = "security.inter.broker.protocol";


    public static final String SSL_PROTOCOL = "SSL";
    private final String bootstrapServers;
    private final String securityProtocol;
    private final SSLProperties sslProperties;
    private final Topic topic;

    @ConstructorBinding
    public KafkaProperties(String bootstrapServers, String securityProtocol, SSLProperties sslProperties, Topic topic) {
        this.bootstrapServers = bootstrapServers;
        this.securityProtocol = securityProtocol;
        this.sslProperties = sslProperties;
        this.topic = topic;
    }

    @Getter
    public final static class SSLProperties {
        private final String enabledProtocols;
        private final String keyPassword;
        private final String keystore;
        private final String keystorePassword;
        private final String keystoreType;
        private final String securityInterBrokerProtocol;
        private final String truststore;
        private final String truststorePassword;
        private final String truststoreType;
        private final String endpointIdentificationAlgorithm;

        @ConstructorBinding
        public SSLProperties(String enabledProtocols,
                             String keyPassword,
                             String keystore,
                             String keystorePassword,
                             String keystoreType,
                             String securityInterBrokerProtocol,
                             String truststore,
                             String truststorePassword,
                             String truststoreType,
                             String endpointIdentificationAlgorithm) {
            this.enabledProtocols = enabledProtocols;
            this.keyPassword = keyPassword;
            this.keystore = keystore;
            this.keystorePassword = keystorePassword;
            this.keystoreType = keystoreType;
            this.securityInterBrokerProtocol = securityInterBrokerProtocol;
            this.truststore = truststore;
            this.truststorePassword = truststorePassword;
            this.truststoreType = truststoreType;
            this.endpointIdentificationAlgorithm = endpointIdentificationAlgorithm;
        }
    }

    @Getter
    public static class Topic {
        private final String event;
        private final String market;
        private final String selection;

        @ConstructorBinding
        public Topic(String event, String market, String selection) {
            this.event = event;
            this.market = market;
            this.selection = selection;
        }
    }

    public Map<String, Object> load(JKSFileService jksFileService) {
        Map<String, Object> proerties = new HashMap<>();
        proerties.put(org.apache.kafka.clients.consumer.ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        proerties.put(org.apache.kafka.clients.consumer.ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, true);
        proerties.put(org.apache.kafka.clients.consumer.ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        proerties.put(org.apache.kafka.clients.consumer.ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        proerties.put(org.apache.kafka.clients.consumer.ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        proerties.put(org.apache.kafka.clients.consumer.ConsumerConfig.MAX_POLL_RECORDS_CONFIG, 500);
        proerties.put(org.apache.kafka.clients.consumer.ConsumerConfig.FETCH_MAX_WAIT_MS_CONFIG, 500);
        proerties.put(org.apache.kafka.clients.consumer.ConsumerConfig.GROUP_ID_CONFIG, CommonUtil.groupIdWithTimestamp("offer-searcher"));
        proerties.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, 500);
        proerties.put(ConsumerConfig.ALLOW_AUTO_CREATE_TOPICS_CONFIG, false);
        proerties.put(ConsumerConfig.REQUEST_TIMEOUT_MS_CONFIG, 5000);

        if (SSL_PROTOCOL.equals(securityProtocol)) {
            proerties.put(AdminClientConfig.SECURITY_PROTOCOL_CONFIG, securityProtocol);
            CommonUtil.acceptIfNonNull(SECURITY_INTER_BROKER_PROTOCOL, sslProperties.getSecurityInterBrokerProtocol(), proerties::put);
            CommonUtil.acceptIfNonNull(SSL_KEYSTORE_TYPE_CONFIG, sslProperties.getKeystoreType(), proerties::put);
            CommonUtil.acceptIfNonNull(SSL_KEYSTORE_LOCATION_CONFIG, jksFileService.getFilePath(OFFER_SEARCHER_KEYSTORE), proerties::put);
            CommonUtil.acceptIfNonNull(SSL_KEYSTORE_PASSWORD_CONFIG, sslProperties.getKeystorePassword(), proerties::put);
            CommonUtil.acceptIfNonNull(SSL_TRUSTSTORE_TYPE_CONFIG, sslProperties.getTruststoreType(), proerties::put);
            CommonUtil.acceptIfNonNull(SSL_TRUSTSTORE_LOCATION_CONFIG, jksFileService.getFilePath(OFFER_SEARCHER_TRUSTSTORE), proerties::put);
            CommonUtil.acceptIfNonNull(SSL_TRUSTSTORE_PASSWORD_CONFIG, sslProperties.getTruststorePassword(), proerties::put);
            CommonUtil.acceptIfNonNull(SSL_ENABLED_PROTOCOLS_CONFIG, sslProperties.getEnabledProtocols(), proerties::put);
            CommonUtil.acceptIfNonNull(SSL_KEY_PASSWORD_CONFIG, sslProperties.getKeyPassword(), proerties::put);
            CommonUtil.acceptIfNonNull(SSL_ENDPOINT_IDENTIFICATION_ALGORITHM_CONFIG, sslProperties.endpointIdentificationAlgorithm, proerties::put);
        }
        return proerties;
    }


}
