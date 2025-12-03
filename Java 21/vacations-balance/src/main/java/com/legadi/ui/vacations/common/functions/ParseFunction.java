package com.legadi.ui.vacations.common.functions;

import java.util.Optional;

public interface ParseFunction<T> {

    Class<T> type();

    T apply(Object value);

    default Optional<T> applyOpt(Object value) {
        return Optional.ofNullable(apply(value));
    }
}
