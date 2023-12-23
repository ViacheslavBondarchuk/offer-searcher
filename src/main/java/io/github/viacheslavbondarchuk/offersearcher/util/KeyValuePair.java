package io.github.viacheslavbondarchuk.offersearcher.util;


import lombok.Getter;

@Getter
public final class KeyValuePair<K, V> {
    private final K key;
    private V value;

    private KeyValuePair(K key, V value) {
        this.key = key;
        this.value = value;
    }

    public static <K, V> KeyValuePair<K, V> of(K key, V value) {
        return new KeyValuePair<>(key, value);
    }

    public synchronized void updateValue(V value) {
        this.value = value;
    }
}
