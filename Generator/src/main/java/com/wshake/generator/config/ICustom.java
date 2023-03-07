package com.wshake.generator.config;

import java.util.Objects;
import java.util.function.Function;

/**
 * @author Wshake
 * @version 1.0.0
 * @date 2023/3/6
 */
@FunctionalInterface
public interface ICustom<T, U, Y, R> {

    R apply(T t, U u,Y y);


    default <V> ICustom<T, U, Y, V> andThen(Function<? super R, ? extends V> after) {
        Objects.requireNonNull(after);
        return (T t, U u, Y y) -> after.apply(apply(t, u,y));
    }
}

