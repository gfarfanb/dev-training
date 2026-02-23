package com.legadi.jdk25.challenges.codesignal;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

public class StringPatternMatchingTest {

    @ParameterizedTest
    @MethodSource("matchesPattern_validation_source")
    public void matchesPattern_validation(String pattern, String source, int expected) {
        StringPatternMatching target = new StringPatternMatching();
        int matchCount = target.matchesPattern(pattern, source);

        assertThat(matchCount, is(expected));
    }

    public static Stream<Arguments> matchesPattern_validation_source() {
        return Stream.of(
            Arguments.of("010", "amazing", 2),
            Arguments.of("010", "ama", 1),
            Arguments.of("011", "amazing", 1),
            Arguments.of("100", "codesignal", 0)
        );
    }
}
