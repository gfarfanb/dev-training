package com.legadi.jdk25.challenges.math;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import java.util.stream.IntStream;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

public class InversePowerTest {

    public static final int INT_MAX_BITS = 31;

    @ParameterizedTest
    @MethodSource("calculateExponential_validation_source")
    public void calculateExponential_validation(int number, int base, int expected) {
        InversePower target = new InversePower();
        int exponential = target.calculateExponential(number, base);

        assertThat(exponential, is(expected));
    }

    public static Stream<Arguments> calculateExponential_validation_source() {
        int base = 2;
        return IntStream.range(0, INT_MAX_BITS)
            .mapToObj(i -> Arguments.of((int) Math.pow(base, i), base, i));
    }

    @Test   
    public void calculateExponential_truncatedInteger() {
        InversePower target = new InversePower();
        int base = 2;
        int number = (int) Math.pow(base, INT_MAX_BITS);
        int exponential = target.calculateExponential(number, base);

        assertThat(exponential, is(INT_MAX_BITS - 1));
        assertThat(number, is(Integer.MAX_VALUE));
    }
}
