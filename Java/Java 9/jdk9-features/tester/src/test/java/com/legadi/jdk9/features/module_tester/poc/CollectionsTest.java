package com.legadi.jdk9.features.module_tester.poc;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasEntry;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.jupiter.api.Test;

public class CollectionsTest {

    @Test
    public void list_of_Usage() {
        List<String> list = List.of("JDK 8", "JDK 9");

        assertThat(list, hasSize(2));
        assertThrows(UnsupportedOperationException.class,
            () -> list.add("JDK 10"));
    }

    @Test
    public void set_of_Usage() {
        Set<String> set = Set.of("JDK 8", "JDK 9");

        assertThat(set, hasSize(2));
        assertThrows(UnsupportedOperationException.class,
            () -> set.add("JDK 10"));
    }

    @Test
    public void map_of_Usage() {
        Map<String, String> map = Map.of("8", "JDK 8", "9", "JDK 9");

        assertThat(map, hasEntry("8", "JDK 8"));
        assertThat(map, hasEntry("9", "JDK 9"));
        assertThrows(UnsupportedOperationException.class,
            () -> map.put("10", "JDK 10"));
    }
}
