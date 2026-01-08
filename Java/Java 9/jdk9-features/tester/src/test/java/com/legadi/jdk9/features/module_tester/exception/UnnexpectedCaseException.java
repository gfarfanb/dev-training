package com.legadi.jdk9.features.module_tester.exception;

public class UnnexpectedCaseException extends RuntimeException {

    private final Object[] values;

    public UnnexpectedCaseException(String message, Object... values) {
        super(message);
        this.values = values;
    }

    public Object[] getValues() {
        return values;
    }

    public static void raise() {
        Object[] values = null;
        raise(null, values);
    }

    public static void raise(Object... values) {
        raise(null, values);
    }

    public static void raise(String message) {
        Object[] values = null;
        raise(message, values);
    }

    public static void raise(String message, Object... values) {
        throw new UnnexpectedCaseException(message, values);
    }
}
