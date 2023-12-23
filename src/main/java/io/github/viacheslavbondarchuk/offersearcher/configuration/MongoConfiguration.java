package io.github.viacheslavbondarchuk.offersearcher.configuration;

import com.mongodb.MongoClientSettings;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import io.github.viacheslavbondarchuk.offersearcher.properties.MongoProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.MongoTransactionManager;
import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class MongoConfiguration extends AbstractMongoClientConfiguration {
    private final MongoProperties properties;

    @Override
    protected String getDatabaseName() {
        return properties.getDatabase();
    }

    @Override
    public MongoClient mongoClient() {
        return MongoClients.create(MongoClientSettings.builder()
                .retryReads(true)
                .retryWrites(true)
                .credential(MongoCredential.createCredential(properties.getUsername(), properties.getDatabase(), properties.getPassword()))
                .applicationName("offer-searcher")
                .applyToClusterSettings(builder -> builder.hosts(List.of(new ServerAddress(properties.getHost()))))
                .build());
    }

    @Bean
    public MongoTransactionManager transactionManager(MongoDatabaseFactory databaseFactory) {
        return new MongoTransactionManager(databaseFactory);
    }


}
