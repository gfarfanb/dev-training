package com.legadi.jdk9.features.module_tester.poc;

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

import com.legadi.jdk9.features.module_tester.exception.ExpectedCaseException;
import com.legadi.jdk9.features.module_tester.exception.UnnexpectedCaseException;

public class OptionalTest {

    @Test
    void ifPresentOrElse_Usage() {
        Optional<Integer> optional = Optional.empty();

        assertThrows(ExpectedCaseException.class,
            () -> optional.ifPresentOrElse(
                v -> UnnexpectedCaseException.raise(v),
                () -> ExpectedCaseException.raise())
        );
    }

    @Test
    void or_Usage() {
        Optional<Integer> optional = Optional.empty();

        int result = assertDoesNotThrow(() -> optional.or(() -> Optional.of(5)).get());

        assertThat(result, is(5));
    }

    @Test
    void stream_Usage() {
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
}
