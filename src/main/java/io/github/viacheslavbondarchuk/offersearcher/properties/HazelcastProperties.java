package io.github.viacheslavbondarchuk.offersearcher.properties;

import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.ConstructorBinding;

import java.util.Set;

@Getter
@ConfigurationProperties(prefix = "hazelcast")
public class HazelcastProperties {
    private final Set<String> members;

    @ConstructorBinding
    public HazelcastProperties(Set<String> members) {
        this.members = members;
    }
}
