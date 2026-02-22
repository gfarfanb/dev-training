package com.legadi.jdk8.challenges.codesignal;

import static com.legadi.jdk8.challenges.codesignal.LookupTable.PowerAlgorithm.LOGARITHM;
import static com.legadi.jdk8.challenges.codesignal.LookupTable.PowerAlgorithm.PRE_CALCULATED;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

public class LookupTableTest {

    @ParameterizedTest
    @MethodSource("findPairs_validation_source")
    public void findPairs_validation_logarithm(int[] numbers, int expected) {
        LookupTable target = new LookupTable(LOGARITHM);
        int output = target.findPairs(numbers);

        assertThat(output, is(expected));
    }

    @ParameterizedTest
    @MethodSource("findPairs_validation_source")
    public void findPairs_validation_preCalculated(int[] numbers, int expected) {
        LookupTable target = new LookupTable(PRE_CALCULATED);
        int output = target.findPairs(numbers);

        assertThat(output, is(expected));
    }

    public static Stream<Arguments> findPairs_validation_source() {
        return Stream.of(
            Arguments.of(new int[] { 1, -1, 2, 3 }, 5),
            Arguments.of(new int[] { -2, -1, 0, 1, 2 }, 5),
            Arguments.of(new int[] { 5 }, 0)
        );
    }
}
