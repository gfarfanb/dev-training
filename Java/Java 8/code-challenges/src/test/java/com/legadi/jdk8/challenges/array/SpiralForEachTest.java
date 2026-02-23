package com.legadi.jdk8.challenges.array;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

public class SpiralForEachTest {

    @ParameterizedTest
    @MethodSource("spiralWalk_validation_source")
    public void spiralWalk_validation(int[][] field) {
        SpiralForEach target = new SpiralForEach();
        int[] walk = target.spiralWalk(field);

        assertArrayEquals(walk, expectedPath(field));
    }

    private int[] expectedPath(int[][] field) {
        List<Integer> path = new ArrayList<>();

        for(int[] row : field) {
            for(int element : row) {
                path.add(element);
            }
        }

        return path.stream().mapToInt(Integer::intValue).sorted().toArray();
    }

    public static Stream<Arguments> spiralWalk_validation_source() {
        return Stream.of(
            Arguments.of(new int[][] {
                { 1 }
            }, "1x1"),
            Arguments.of(new int[][] {
                { 1, 2 },
                { 4, 3 }
            }, "2x2"),
            Arguments.of(new int[][] {
                { 1, 2, 3 },
                { 8, 9, 4 },
                { 7, 6, 5 }
            }, "3x3"),
            Arguments.of(new int[][] {
                { 1, 2, 3, 4 },
                { 12, 13, 14, 5 },
                { 11, 16, 15, 6 },
                { 10, 9, 8, 7 }
            }, "4x4"),
            Arguments.of(new int[][] {
                { 1, 2, 3, 4, 5 },
                { 16, 17, 18, 19, 6 },
                { 15, 24, 25, 20, 7 },
                { 14, 23, 22, 21, 8 },
                { 13, 12, 11, 10, 9 }
            }, "5x5"),
            Arguments.of(new int[][] {
                { 1, 2, 3, 4, 5 },
                { 26, 27, 28, 29, 6 },
                { 25, 44, 45, 30, 7 },
                { 24, 43, 46, 31, 8 },
                { 23, 42, 47, 32, 9 },
                { 22, 41, 48, 33, 10 },
                { 21, 40, 49, 34, 11 },
                { 20, 39, 50, 35, 12 },
                { 19, 38, 37, 36, 13 },
                { 18, 17, 16, 15, 14 }
            }, "5x10")
        );
    }
}
