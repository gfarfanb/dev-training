package com.legadi.jdk8.challenges.codesignal;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

public class ArrayManipulationTest {

    @ParameterizedTest
    @MethodSource("transformValidation_source")
    public void transformValidation(int[] input, int[] expected) {
        ArrayManipulation target = new ArrayManipulation();
        int[] output = target.transform(input);

        assertArrayEquals(output, expected);
    }

    public static Stream<Arguments> transformValidation_source() {
        return Stream.of(
            Arguments.of(new int[] { 4, 0, 1, -2, 3 }, new int[] { 4, 5, -1, 2, 1 })
        );
    }
}
