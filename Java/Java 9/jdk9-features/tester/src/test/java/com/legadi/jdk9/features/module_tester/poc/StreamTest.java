package com.legadi.jdk9.features.module_tester.poc;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.everyItem;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.lessThan;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;

public class StreamTest {

    @Test
    public void filter_Usage() {
        List<Integer> result = Stream.of(1, 2, 3, 4, 5, 4, 3, 2, 1)
            .filter(n -> n < 5)
            .collect(Collectors.toList());

        assertThat(result, hasSize(8));
        assertThat(result, hasItems(1, 2, 3, 4));
        assertThat(result, not(hasItem(5)));
    }

    @Test
    public void takeWhile_Usage() {
        List<Integer> result = Stream.of(1, 2, 3, 4, 5, 4, 3, 2, 1)
            .takeWhile(n -> n < 5)
            .collect(Collectors.toList());

        assertThat(result, hasSize(4));
        assertThat(result, hasItems(1, 2, 3, 4));
        assertThat(result, not(hasItem(5)));
    }

    @Test
    public void dropWhile_Usage() {
        List<Integer> result = Stream.of(1, 2, 3, 4, 5, 4, 3, 2, 1)
            .dropWhile(n -> n < 5)
            .collect(Collectors.toList());

        assertThat(result, hasSize(5));
        assertThat(result, hasItems(1, 2, 3, 4, 5));
    }

    @Test
    public void ofNullable_Usage() {
        Integer value = null;
        Stream<Integer> stream = assertDoesNotThrow(
            () -> Stream.ofNullable(value)
        );

        assertThat(stream.findAny().isPresent(), is(false));
    }

    @Test
    public void iterate_Usage() {
        final int limit = 100;
        List<Integer> result = Stream.iterate(0, n -> n < limit, n -> n + 1)
            .collect(Collectors.toList());

        assertThat(result, hasSize(100));
        assertThat(result, everyItem(is(greaterThanOrEqualTo(0))));
        assertThat(result, everyItem(is(lessThan(100))));
    }
}
