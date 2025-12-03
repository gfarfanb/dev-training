package com.legadi.ui.vacations.common.functions;

import java.util.function.Function;

public interface ParseFunction<T> extends Function<Object, T> {

    Class<T> type();
}
