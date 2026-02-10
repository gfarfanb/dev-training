package com.legadi.jdk11.features.poc;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.arrayContaining;

import java.util.List;

import org.junit.jupiter.api.Test;

public class CollectionPocTest {

    @Test
    public void list_toArray_usage() {
        String[] array = List.of("A", "B", "C").toArray(String[]::new);

        assertThat(array, arrayContaining("A", "B", "C"));
    }
}
