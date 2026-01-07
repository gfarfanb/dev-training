package com.legadi.ui.vacations.common.functions;

public class ToString implements ParseFunction<String> {

    @Override
    public Class<String> type() {
        return String.class;
    }

    @Override
    public String apply(Object value) {
        return value != null ? value.toString() : null;
    }
}
