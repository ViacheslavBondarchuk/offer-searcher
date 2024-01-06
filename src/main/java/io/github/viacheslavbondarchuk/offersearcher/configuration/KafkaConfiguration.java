package io.github.viacheslavbondarchuk.offersearcher.configuration;

import io.github.viacheslavbondarchuk.offersearcher.properties.KafkaProperties;
import io.github.viacheslavbondarchuk.offersearcher.service.JKSFileService;
import org.apache.kafka.clients.consumer.Consumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.kafka.listener.KafkaListenerErrorHandler;

import java.util.Map;

/**
 * author: vbondarchuk
 * date: 12/12/2023
 * time: 10:01 PM
 **/

@Configuration
public class KafkaConfiguration {
    private final Map<String, Object> properties;

    public KafkaConfiguration(KafkaProperties properties, JKSFileService jksFileService) {
        this.properties = properties.load(jksFileService);
    }

    @Bean
    public ConsumerFactory<String, String> consumerFactory() {
        return new DefaultKafkaConsumerFactory<>(properties);
    }

    @Bean
    public Consumer<String, String> consumer(ConsumerFactory<String, String> factory) {
        return factory.createConsumer();
    }

    @Bean
    public KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<String, String>> kafkaListenerContainerFactory(
            ConsumerFactory<String, String> consumerFactory) {
        ConcurrentKafkaListenerContainerFactory<String, String> kafkaListenerContainerFactory = new ConcurrentKafkaListenerContainerFactory<>();
        kafkaListenerContainerFactory.setConsumerFactory(consumerFactory);
        kafkaListenerContainerFactory.setConcurrency(1);
        kafkaListenerContainerFactory.setBatchListener(true);
        kafkaListenerContainerFactory.setCommonErrorHandler(new DefaultErrorHandler());
        return kafkaListenerContainerFactory;
    }

    @Bean
    public KafkaListenerErrorHandler kafkaListenerErrorHandler() {
        Logger logger = LoggerFactory.getLogger("KafkaErrorHandler");
        return (message, exception) -> {
            logger.error("Message: {}. Exception: ", message, exception);
            return null;
        };
    }

    @Bean
    public ProducerFactory<String, String> producerFactory() {
        return new DefaultKafkaProducerFactory<>(properties);
    }

    @Bean
    public KafkaTemplate<String, String> kafkaTemplate(ProducerFactory<String, String> factory) {
        return new KafkaTemplate<>(factory, true);
    }

}
