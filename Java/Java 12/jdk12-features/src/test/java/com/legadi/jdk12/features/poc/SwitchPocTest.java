package com.legadi.jdk12.features.poc;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

public class SwitchPocTest {

    @ParameterizedTest
    @MethodSource("switch_expressions_implementation_source")
    public void switch_expressions_implementation(Version version, String expected) {
        String result = switch(version) {
            case JDK_9 -> "major";
            case JDK_10, JDK_11, JDK_12 -> "minor";
            default -> "unknown";
        };

        assertThat(result, is(expected));
    }

    public static Stream<Arguments> switch_expressions_implementation_source() {
        return Stream.of(
            Arguments.of(Version.JDK_8, "unknown"),
            Arguments.of(Version.JDK_9, "major"),
            Arguments.of(Version.JDK_10, "minor"),
            Arguments.of(Version.JDK_11, "minor"),
            Arguments.of(Version.JDK_12, "minor")
        );
    }

    public enum Version {

        JDK_8, JDK_9, JDK_10, JDK_11, JDK_12
    }
}
