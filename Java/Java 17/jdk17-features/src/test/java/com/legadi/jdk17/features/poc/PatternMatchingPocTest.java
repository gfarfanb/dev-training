package com.legadi.jdk17.features.poc;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.anyOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.function.Supplier;
import java.util.stream.Stream;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import com.legadi.jdk17.features.poc.PatternMatchingPocTest.PatternRecord;

@Tag("JEP-406")
@Tag("Preview")
public class PatternMatchingPocTest {

    @ParameterizedTest
    @MethodSource("patternMatching_switch_implementation_source")
    public void patternMatching_switch_implementation(Object raw) {
        switch (raw) {
            case Integer intValue -> assertThat(intValue, is(5));
            case Long longValue -> assertThat(longValue, is(10L));
            case Double doubleValue -> assertThat(doubleValue, is(15.5));
            case String stringValue  -> assertThat(stringValue, is("Test"));
            case PatternRecord recordValue -> assertThat(recordValue.value, is("Record"));
            default -> assertThat(raw, notNullValue());
        }
    }

    public static Stream<Arguments> patternMatching_switch_implementation_source() {
        return Stream.of(
            Arguments.of(5),
            Arguments.of(10L),
            Arguments.of(15.5),
            Arguments.of("Test"),
            Arguments.of(new PatternRecord("Record")),
            Arguments.of(new Object())
        );
    }

    @Test
    public void patternMatching_switchWithNulls_implementation() {
        Object[] raws = { "null", null, 5 };

        for(Object raw : raws) {
            switch(raw) {
                case null -> assertThat(raw, nullValue());
                default -> assertThat(raw, notNullValue());
            }

            switch(raw) {
                case null, String value -> assertThat(raw, anyOf(nullValue(), is("null")));
                default -> assertThat(raw, notNullValue());
            }
        }
    }

    record PatternRecord(String value) {
    }
}
