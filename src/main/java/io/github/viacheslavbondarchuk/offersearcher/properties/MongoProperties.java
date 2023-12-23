package io.github.viacheslavbondarchuk.offersearcher.properties;

import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.ConstructorBinding;

import java.util.function.Function;

@Getter
@ConfigurationProperties("mongodb")
public class MongoProperties {
    private final String username;
    private final char[] password;
    private final String database;
    private final String host;
    private final Collection collection;

    @ConstructorBinding
    public MongoProperties(String username, char[] password, String database, String host, Collection collection) {
        this.username = username;
        this.password = password;
        this.database = database;
        this.host = host;
        this.collection = collection;
    }

    public <R> R map(Function<MongoProperties, R> function) {
        return function.apply(this);
    }


    @Getter
    public static class Collection {
        private final String event;
        private final String market;
        private final String selection;

        @ConstructorBinding
        public Collection(String event, String market, String selection) {
            this.event = event;
            this.market = market;
            this.selection = selection;
        }
    }
}
