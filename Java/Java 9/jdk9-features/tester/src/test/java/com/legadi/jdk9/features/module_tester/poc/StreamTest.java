package com.legadi.jdk9.features.module_tester.poc;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.not;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;

public class StreamTest {

    @Test
    void stream_filter_Usage() {
        List<Integer> result = Stream.of(1, 2, 3, 4, 5, 4, 3, 2, 1)
            .filter(n -> n < 5)
            .collect(Collectors.toList());

        assertThat(result, hasSize(8));
        assertThat(result, hasItems(1, 2, 3, 4));
        assertThat(result, not(hasItem(5)));
    }

    @Test
    void stream_takeWhile_Usage() {
        List<Integer> result = Stream.of(1, 2, 3, 4, 5, 4, 3, 2, 1)
            .takeWhile(n -> n < 5)
            .collect(Collectors.toList());

        assertThat(result, hasSize(4));
        assertThat(result, hasItems(1, 2, 3, 4));
        assertThat(result, not(hasItem(5)));
    }
}
