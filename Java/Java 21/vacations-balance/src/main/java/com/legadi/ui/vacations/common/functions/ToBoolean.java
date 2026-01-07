package com.legadi.ui.vacations.common.functions;

import java.util.Arrays;
import java.util.List;

public class ToBoolean implements ParseFunction<Boolean> {

    private static final List<String> TRUE_VALUES = Arrays.asList("true", "1");

    @Override
    public Class<Boolean> type() {
        return Boolean.class;
    }

    @Override
    public Boolean apply(Object value) {
        if (value instanceof Boolean) {
            return (boolean) value;
        }
        if (value instanceof Object) {
            String raw = value.toString();
            return TRUE_VALUES.stream().anyMatch(v -> v.equalsIgnoreCase(raw));
        }
        return null;
    }
}
