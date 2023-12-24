package io.github.viacheslavbondarchuk.offersearcher.util;

import java.util.function.Predicate;
import java.util.function.Supplier;

public final class Checking {
    private Checking() {}

    public static <T> void check(T value, Predicate<T> predicate, Supplier<? extends RuntimeException> supplier) {
        if (predicate.test(value)) {
            throw supplier.get();
        }
    }

    public static <T> void check(boolean value, Supplier<? extends RuntimeException> supplier) {
        if (value) {
            throw supplier.get();
        }
    }

}
