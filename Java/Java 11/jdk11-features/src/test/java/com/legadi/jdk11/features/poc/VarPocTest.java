package com.legadi.jdk11.features.poc;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import java.util.List;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("JEP-323")
public class VarPocTest {

    @Test
    public void var_lambdaOnArgs_implementation() {
        String packed = List.of("A", "B", "C")
            .stream()
            .reduce("", (var base, var next) -> base + next);

        assertThat(packed, is("ABC"));
    }
}
