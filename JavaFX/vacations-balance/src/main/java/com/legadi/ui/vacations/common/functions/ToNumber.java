package com.legadi.ui.vacations.common.functions;

import static com.legadi.ui.vacations.common.Utils.isNumber;

import java.math.BigDecimal;

public class ToNumber implements ParseFunction<Number> {

    @Override
    public Class<Number> type() {
        return Number.class;
    }

    @Override
    public Number apply(Object value) {
        if (value instanceof Boolean) {
            return ((boolean) value) ? 1 : 0;
        }
        if (value instanceof Integer) {
            return (int) value;
        }
        if (value instanceof Long) {
            return (long) value;
        }
        if (value instanceof Float) {
            return (float) value;
        }
        if (value instanceof Double) {
            return (double) value;
        }
        if (value instanceof Object) {
            String raw = value.toString();
            return isNumber(raw) ? new BigDecimal(raw) : null;
        }
        return null;
    }
}
