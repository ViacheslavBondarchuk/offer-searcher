package io.github.viacheslavbondarchuk.offersearcher.configuration;

import io.github.viacheslavbondarchuk.offersearcher.properties.KafkaProperties;
import io.github.viacheslavbondarchuk.offersearcher.service.JKSFileService;
import org.apache.kafka.clients.consumer.Consumer;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.CommonLoggingErrorHandler;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.util.backoff.BackOff;

/**
 * author: vbondarchuk
 * date: 12/12/2023
 * time: 10:01 PM
 **/

@Configuration
public class KafkaConfiguration {


    @Bean
    public ConsumerFactory<String, String> consumerFactory(KafkaProperties kafkaProperties, JKSFileService jksFileService) {
        return new DefaultKafkaConsumerFactory<>(kafkaProperties.load(jksFileService));
    }

    @Bean
    public Consumer<String, String> consumer(ConsumerFactory<String, String> factory) {
        return factory.createConsumer();
    }

    @Bean
    public KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<String, String>> kafkaListenerContainerFactory(ConsumerFactory<String, String> consumerFactory) {
        ConcurrentKafkaListenerContainerFactory<String, String> kafkaListenerContainerFactory = new ConcurrentKafkaListenerContainerFactory<>();
        kafkaListenerContainerFactory.setConsumerFactory(consumerFactory);
        kafkaListenerContainerFactory.setConcurrency(1);
        kafkaListenerContainerFactory.setBatchListener(true);
        kafkaListenerContainerFactory.setCommonErrorHandler(new DefaultErrorHandler());
        return kafkaListenerContainerFactory;
    }

}
