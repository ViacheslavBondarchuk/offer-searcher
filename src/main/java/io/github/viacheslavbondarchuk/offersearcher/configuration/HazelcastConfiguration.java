package io.github.viacheslavbondarchuk.offersearcher.configuration;

import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.client.config.ClientNetworkConfig;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.map.IMap;
import io.github.viacheslavbondarchuk.offersearcher.domain.HazelcastSession;
import io.github.viacheslavbondarchuk.offersearcher.properties.HazelcastProperties;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

/**
 * author: vbondarchuk
 * date: 12/13/2023
 * time: 9:50 AM
 **/

@Configuration
public class HazelcastConfiguration {

    @Bean
    @Primary
    public ClientConfig hazelcastConfig(HazelcastProperties properties) {
        ClientConfig clientConfig = new ClientConfig();
        clientConfig.setClassLoader(Thread.currentThread().getContextClassLoader());

        ClientNetworkConfig network = clientConfig.getNetworkConfig();
        network.addAddress(properties.getMembers().toArray(String[]::new))
                .setSmartRouting(false)
                .setRedoOperation(true);
        return clientConfig;
    }

    @Bean
    public IMap<String, HazelcastSession> hazelcastSessionMap(HazelcastInstance hazelcastInstance, @Value("${session.map-name}") String mapName) {
        return hazelcastInstance.getMap(mapName);
    }


}
