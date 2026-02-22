package com.legadi.jdk8.challenges.codesignal;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

public class TwoDimensionalArrayTraversalTest {

    @ParameterizedTest
    @MethodSource("choosePositionValidation_source")
    public void choosePositionValidation(int[][] field, int[][] figure, int expected) {
        TwoDimensionalArrayTraversal target = new TwoDimensionalArrayTraversal();
        int position = target.choosePosition(field, figure);

        assertThat(position, is(expected));
    }

    public static Stream<Arguments> choosePositionValidation_source() {
        return Stream.of(
            Arguments.of(
                new int[][] {
                    { 0, 0, 0 },
                    { 0, 0, 0 },
                    { 0, 0, 0 },
                    { 1, 0, 0 },
                    { 1, 1, 0 }
                },
                new int[][] {
                    { 0, 0, 1 },
                    { 0, 1, 1 },
                    { 0, 0, 1 }
                },
                0
            ),
            Arguments.of(
                new int[][] {
                    { 0, 0, 0, 0, 0, 0 },
                    { 0, 0, 0, 0, 0, 0 },
                    { 0, 0, 0, 0, 0, 0 },
                    { 1, 0, 0, 0, 0, 0 },
                    { 1, 1, 0, 0, 0, 1 },
                    { 1, 1, 0, 0, 0, 1 },
                    { 1, 1, 1, 0, 1, 1 }
                },
                new int[][] {
                    { 1, 1, 1 },
                    { 0, 1, 1 },
                    { 0, 1, 0 }
                },
                2
            ),
            Arguments.of(
                new int[][] {
                    { 0, 0, 0, 0, 0 },
                    { 0, 0, 0, 0, 0 },
                    { 0, 0, 0, 0, 0 },
                    { 1, 1, 0, 1, 0 },
                    { 1, 0, 1, 0, 1 }
                },
                new int[][] {
                    { 1, 1, 1 },
                    { 1, 0, 1 },
                    { 1, 0, 1 }
                },
                2
            ),
            Arguments.of(
                new int[][] {
                    { 0, 0, 0, 0 },
                    { 0, 0, 0, 0 },
                    { 0, 0, 0, 0 },
                    { 1, 0, 0, 1 },
                    { 1, 1, 0, 1 }
                },
                new int[][] {
                    { 1, 1, 0 },
                    { 1, 0, 0 },
                    { 1, 0, 0 }
                },
                -1
            )
        );
    }
}
