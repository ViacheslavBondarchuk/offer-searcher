package io.github.viacheslavbondarchuk.offersearcher;

import io.github.viacheslavbondarchuk.offersearcher.properties.KafkaProperties;
import io.github.viacheslavbondarchuk.offersearcher.properties.MongoProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.mongo.MongoDataAutoConfiguration;
import org.springframework.boot.autoconfigure.data.mongo.MongoRepositoriesAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.scheduling.annotation.EnableScheduling;

@Slf4j
@EnableScheduling
@SpringBootApplication(exclude = {
        MongoAutoConfiguration.class,
        MongoRepositoriesAutoConfiguration.class,
        MongoDataAutoConfiguration.class,
})
@EnableConfigurationProperties({
        KafkaProperties.class,
        MongoProperties.class,
})
public class OfferSearcherApplication {

    public static void main(String[] args) {
        try {
            SpringApplication.run(OfferSearcherApplication.class, args);
        } catch (Exception ex) {
            log.error("Can not start application. Exception: ", ex);
        }
    }

}
