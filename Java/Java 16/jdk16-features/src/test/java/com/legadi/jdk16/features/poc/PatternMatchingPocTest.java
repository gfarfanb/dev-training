package com.legadi.jdk16.features.poc;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("JEP-394")
public class PatternMatchingPocTest {

    @Test
    public void patternMatching_instanceOf_implementation() {
        Object raw = "Test";

        if(raw instanceof String name) {
            assertThat(name, is("Test"));
        }

        if (raw instanceof String name && name.length() < 5) {
            assertThat(name.length(), is(4));
        }
    }
}
