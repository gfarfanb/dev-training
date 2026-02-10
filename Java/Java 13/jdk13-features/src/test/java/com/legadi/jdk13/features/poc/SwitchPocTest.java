package com.legadi.jdk13.features.poc;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

public class SwitchPocTest {

    @ParameterizedTest
    @MethodSource("switch_implementation_source")
    public void switch_expression_implementation(Version version, String expected) {
        String result = switch(version) {
            case JDK_9 -> "major";
            case JDK_10, JDK_11 -> "minor";
            case JDK_12, JDK_13 -> {
                String type = """
                        preview""";
                yield type;
            }
            default -> "critical";
        };

        assertThat(result, is(expected));
    }

    @ParameterizedTest
    @MethodSource("switch_implementation_source")
    public void switch_statement_implementation(Version version, String expected) {
        String result;

        switch(version) {
            case JDK_9 -> result = "major";
            case JDK_10, JDK_11 -> result = "minor";
            case JDK_12, JDK_13 -> {
                result = "preview";
            }
            default -> {
                result = "critical";
            }
        }

        assertThat(result, is(expected));
    }

    public static Stream<Arguments> switch_implementation_source() {
        return Stream.of(
            Arguments.of(Version.JDK_8, "critical"),
            Arguments.of(Version.JDK_9, "major"),
            Arguments.of(Version.JDK_10, "minor"),
            Arguments.of(Version.JDK_11, "minor"),
            Arguments.of(Version.JDK_12, "preview"),
            Arguments.of(Version.JDK_13, "preview")
        );
    }

    public enum Version {

        JDK_8, JDK_9, JDK_10, JDK_11, JDK_12, JDK_13
    }
}
