package com.legadi.jdk10.features.poc;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

public class CollectorsPocTest {

    @Test
    public void collectors_toUnmodifiableList_usage() {
        List<Integer> unmodifiable = new Random().ints(100).boxed()
            .collect(Collectors.toUnmodifiableList());
        List<Integer> modifiable = new Random().ints(100).boxed()
            .collect(Collectors.toList());

        assertThrows(UnsupportedOperationException.class,
            () -> unmodifiable.add(1));

        int size = modifiable.size();
        assertDoesNotThrow(() -> modifiable.add(1));
        assertThat(modifiable, hasSize(size + 1));
    }

    @Test
    public void collectors_toUnmodifiableSet_usage() {
        Set<Integer> unmodifiable = new Random().ints(100).boxed()
            .collect(Collectors.toUnmodifiableSet());
        Set<Integer> modifiable = new Random().ints(100).boxed()
            .collect(Collectors.toSet());

        assertThrows(UnsupportedOperationException.class,
            () -> unmodifiable.add(1));

        int size = modifiable.size();
        assertDoesNotThrow(() -> modifiable.add(1));
        assertThat(modifiable, hasSize(size + 1));
    }

    @RepeatedTest(20)
    public void collectors_toUnmodifiableMap_usage() {
        Map<String, Integer> unmodifiable = new Random().ints(100).boxed()
            .collect(Collectors.toUnmodifiableMap(n -> n.toString(), n -> n, (v1, v2) -> v2));
        Map<String, Integer> modifiable = new Random().ints(100).boxed()
            .collect(Collectors.toMap(n -> n.toString(), n -> n, (v1, v2) -> v2));

        assertThrows(UnsupportedOperationException.class,
            () -> unmodifiable.put("1", 1));

        int size = modifiable.size();
        assertDoesNotThrow(() -> modifiable.put("one", 1));
        assertThat(modifiable.entrySet(), hasSize(size + 1));
    }
}
