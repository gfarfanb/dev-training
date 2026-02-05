package com.legadi.jdk9.features.module_poc.poc;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;

public class OptionalPocTest {

    @Test
    public void optional_ifPresentOrElse_Usage() {
        Optional<Integer> optional = Optional.empty();

        assertThrows(ExpectedCaseException.class,
            () -> optional.ifPresentOrElse(
                v -> UnnexpectedCaseException.raise(v),
                () -> ExpectedCaseException.raise())
        );
    }

    @Test
    public void optional_or_Usage() {
        Optional<Integer> optional = Optional.empty();

        int result = assertDoesNotThrow(() -> optional.or(() -> Optional.of(5)).get());

        assertThat(result, is(5));
    }

    @Test
    public void optional_stream_Usage() {
        Stream<Optional<Integer>> stream = Stream.of(
            Optional.empty(),
            Optional.of(5),
            Optional.of(10)
        );
        List<Integer> result = stream
            .flatMap(Optional::stream)
            .collect(Collectors.toList());

        assertThat(result, hasSize(2));
        assertThat(result, hasItems(5, 10));
    }

    public static class ExpectedCaseException extends RuntimeException {

        private final Object[] values;

        public ExpectedCaseException(String message, Object... values) {
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
            throw new ExpectedCaseException(message, values);
        }
    }

    public static class UnnexpectedCaseException extends RuntimeException {

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
}
