package com.legadi.jdk14.features.poc;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("previewfeature")
public class PatternMatchingPocTest {

    @Test
    public void patternMatching_instanceOf_implementation() {
        Object raw = "Test";

        if(raw instanceof String name) {
            assertThat(name, is("Test"));
            assertThat(name.length(), is(4));
        }
    }
}
