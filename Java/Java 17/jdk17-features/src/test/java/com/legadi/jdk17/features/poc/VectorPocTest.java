package com.legadi.jdk17.features.poc;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.anyOf;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import java.util.Arrays;
import java.util.Random;
import java.util.random.RandomGenerator;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import jdk.incubator.vector.ByteVector;
import jdk.incubator.vector.DoubleVector;
import jdk.incubator.vector.FloatVector;
import jdk.incubator.vector.IntVector;
import jdk.incubator.vector.LongVector;
import jdk.incubator.vector.ShortVector;
import jdk.incubator.vector.VectorSpecies;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

@Tag("JEP-414")
@Tag("Incubator")
public class VectorPocTest {

    @ParameterizedTest
    @MethodSource("floatVector_addAndMulAndDivAndSubAndBroadcast_usage_source")
    public void floatVector_addAndMulAndDivAndSubAndBroadcast_usage(VectorSpecies<Float> species) {
        float[] data = initializeFloatData(16);
        int length = lengthFromFloatSpecies(species);
        FloatVector vector = FloatVector.fromArray(species, data, 0);

        vector = vector.add(10.0f);
        vector.intoArray(data, 0);

        IntStream.range(0, length)
            .forEach(i -> assertThat(data[i], greaterThanOrEqualTo(10.0f)));

        vector = vector.mul(10.0f);
        vector.intoArray(data, 0);

        IntStream.range(0, length)
            .forEach(i -> assertThat(data[i], greaterThanOrEqualTo(100.0f)));

        vector = vector.div(10.0f);
        vector.intoArray(data, 0);

        IntStream.range(0, length)
            .forEach(i -> assertThat(data[i], greaterThanOrEqualTo(10.0f)));

        vector = vector.sub(5.0f);
        vector.intoArray(data, 0);

        IntStream.range(0, length)
            .forEach(i -> assertThat(data[i], greaterThanOrEqualTo(5.0f)));

        vector = vector.broadcast(25.0f);
        vector.intoArray(data, 0);

        IntStream.range(0, length)
            .forEach(i -> assertThat(data[i], equalTo(25.0f)));
    }

    public static Stream<Arguments> floatVector_addAndMulAndDivAndSubAndBroadcast_usage_source() {
        return Stream.of(
            Arguments.of(FloatVector.SPECIES_64),
            Arguments.of(FloatVector.SPECIES_128),
            Arguments.of(FloatVector.SPECIES_256),
            Arguments.of(FloatVector.SPECIES_512)
        );
    }

    @ParameterizedTest
    @MethodSource("doubleVector_addAndMulAndDivAndSubAndBroadcast_usage_source")
    public void doubleVector_addAndMulAndDivAndSubAndBroadcast_usage(VectorSpecies<Double> species) {
        double[] data = RandomGenerator.of("L128X1024MixRandom").doubles(16).toArray();
        int length = lengthFromDoubleSpecies(species);
        DoubleVector vector = DoubleVector.fromArray(species, data, 0);

        vector = vector.add(10.0);
        vector.intoArray(data, 0);

        IntStream.range(0, length)
            .forEach(i -> assertThat(data[i], greaterThanOrEqualTo(10.0)));

        vector = vector.mul(10.0);
        vector.intoArray(data, 0);

        IntStream.range(0, length)
            .forEach(i -> assertThat(data[i], greaterThanOrEqualTo(100.0)));

        vector = vector.div(10.0);
        vector.intoArray(data, 0);

        IntStream.range(0, length)
            .forEach(i -> assertThat(data[i], greaterThanOrEqualTo(10.0)));

        vector = vector.sub(5.0);
        vector.intoArray(data, 0);

        IntStream.range(0, length)
            .forEach(i -> assertThat(data[i], greaterThanOrEqualTo(5.0)));

        vector = vector.broadcast(25.0);
        vector.intoArray(data, 0);

        IntStream.range(0, length)
            .forEach(i -> assertThat(data[i], equalTo(25.0)));
    }

    public static Stream<Arguments> doubleVector_addAndMulAndDivAndSubAndBroadcast_usage_source() {
        return Stream.of(
            Arguments.of(DoubleVector.SPECIES_64),
            Arguments.of(DoubleVector.SPECIES_128),
            Arguments.of(DoubleVector.SPECIES_256),
            Arguments.of(DoubleVector.SPECIES_512)
        );
    }

    @ParameterizedTest
    @MethodSource("intVector_addAndMulAndDivAndSubAndBroadcast_usage_source")
    public void intVector_addAndMulAndDivAndSubAndBroadcast_usage(VectorSpecies<Integer> species) {
        int[] data = RandomGenerator.of("L128X1024MixRandom").ints(1, 101).limit(16).toArray();
        int length = lengthFromIntegerSpecies(species);
        IntVector vector = IntVector.fromArray(species, data, 0);

        vector = vector.add(100);
        vector.intoArray(data, 0);

        IntStream.range(0, length)
            .forEach(i -> assertThat(data[i], greaterThanOrEqualTo(100)));

        vector = vector.mul(10);
        vector.intoArray(data, 0);

        IntStream.range(0, length)
            .forEach(i -> assertThat(data[i], greaterThanOrEqualTo(1000)));

        vector = vector.div(10);
        vector.intoArray(data, 0);

        IntStream.range(0, length)
            .forEach(i -> assertThat(data[i], greaterThanOrEqualTo(100)));

        vector = vector.sub(100);
        vector.intoArray(data, 0);

        IntStream.range(0, length)
            .forEach(i -> assertThat(data[i], greaterThanOrEqualTo(1)));

        vector = vector.broadcast(25);
        vector.intoArray(data, 0);

        IntStream.range(0, length)
            .forEach(i -> assertThat(data[i], equalTo(25)));
    }

    public static Stream<Arguments> intVector_addAndMulAndDivAndSubAndBroadcast_usage_source() {
        return Stream.of(
            Arguments.of(IntVector.SPECIES_64),
            Arguments.of(IntVector.SPECIES_128),
            Arguments.of(IntVector.SPECIES_256),
            Arguments.of(IntVector.SPECIES_512)
        );
    }

    @ParameterizedTest
    @MethodSource("longVector_addAndMulAndDivAndSubAndBroadcast_usage_source")
    public void longVector_addAndMulAndDivAndSubAndBroadcast_usage(VectorSpecies<Long> species) {
        long[] data = RandomGenerator.of("L128X1024MixRandom").longs(1L, 101L).limit(16).toArray();
        int length = lengthFromLongSpecies(species);
        LongVector vector = LongVector.fromArray(species, data, 0);

        vector = vector.add(100L);
        vector.intoArray(data, 0);

        IntStream.range(0, length)
            .forEach(i -> assertThat(data[i], greaterThanOrEqualTo(100L)));

        vector = vector.mul(10L);
        vector.intoArray(data, 0);

        IntStream.range(0, length)
            .forEach(i -> assertThat(data[i], greaterThanOrEqualTo(1000L)));

        vector = vector.div(10L);
        vector.intoArray(data, 0);

        IntStream.range(0, length)
            .forEach(i -> assertThat(data[i], greaterThanOrEqualTo(100L)));

        vector = vector.sub(100L);
        vector.intoArray(data, 0);

        IntStream.range(0, length)
            .forEach(i -> assertThat(data[i], greaterThanOrEqualTo(1L)));

        vector = vector.broadcast(25L);
        vector.intoArray(data, 0);

        IntStream.range(0, length)
            .forEach(i -> assertThat(data[i], equalTo(25L)));
    }

    public static Stream<Arguments> longVector_addAndMulAndDivAndSubAndBroadcast_usage_source() {
        return Stream.of(
            Arguments.of(LongVector.SPECIES_64),
            Arguments.of(LongVector.SPECIES_128),
            Arguments.of(LongVector.SPECIES_256),
            Arguments.of(LongVector.SPECIES_512)
        );
    }

    @Test
    public void shortVector_fromCharArray_usage() {
        char[] data = initializeCharData(16);
        ShortVector vector = ShortVector.fromCharArray(ShortVector.SPECIES_64, data, 0);
        short[] output = new short[data.length];

        vector.intoArray(output, 0);

        IntStream.range(0, output.length)
            .forEach(i -> assertDoesNotThrow(() -> (char) output[i]));
    }

    @Test
    public void byteVector_fromBooleanArray_usage() {
        boolean[] data = initializeBooleanData(16);
        ByteVector vector = ByteVector.fromBooleanArray(ByteVector.SPECIES_64, data, 0);
        byte[] output = new byte[data.length];

        vector.intoArray(output, 0);

        IntStream.range(0, output.length)
            .forEach(i -> assertThat((int) output[i], anyOf(equalTo(1), equalTo(0))));
    }

    private int lengthFromFloatSpecies(VectorSpecies<Float> species) {
        final int floatDataTypeBitSize = 32;
        if(FloatVector.SPECIES_64.equals(species)) {
            return 64 / floatDataTypeBitSize;
        }
        if(FloatVector.SPECIES_128.equals(species)) {
            return 128 / floatDataTypeBitSize;
        }
        if(FloatVector.SPECIES_256.equals(species)) {
            return 256 / floatDataTypeBitSize;
        }
        if(FloatVector.SPECIES_512.equals(species)) {
            return 512 / floatDataTypeBitSize;
        }
        return 0;
    }

    private int lengthFromDoubleSpecies(VectorSpecies<Double> species) {
        final int doubleDataTypeBitSize = 64;
        if(DoubleVector.SPECIES_64.equals(species)) {
            return 64 / doubleDataTypeBitSize;
        }
        if(DoubleVector.SPECIES_128.equals(species)) {
            return 128 / doubleDataTypeBitSize;
        }
        if(DoubleVector.SPECIES_256.equals(species)) {
            return 256 / doubleDataTypeBitSize;
        }
        if(DoubleVector.SPECIES_512.equals(species)) {
            return 512 / doubleDataTypeBitSize;
        }
        return 0;
    }

    private int lengthFromIntegerSpecies(VectorSpecies<Integer> species) {
        final int integerDataTypeBitSize = 32;
        if(IntVector.SPECIES_64.equals(species)) {
            return 64 / integerDataTypeBitSize;
        }
        if(IntVector.SPECIES_128.equals(species)) {
            return 128 / integerDataTypeBitSize;
        }
        if(IntVector.SPECIES_256.equals(species)) {
            return 256 / integerDataTypeBitSize;
        }
        if(IntVector.SPECIES_512.equals(species)) {
            return 512 / integerDataTypeBitSize;
        }
        return 0;
    }

    private int lengthFromLongSpecies(VectorSpecies<Long> species) {
        final int longDataTypeBitSize = 64;
        if(LongVector.SPECIES_64.equals(species)) {
            return 64 / longDataTypeBitSize;
        }
        if(LongVector.SPECIES_128.equals(species)) {
            return 128 / longDataTypeBitSize;
        }
        if(LongVector.SPECIES_256.equals(species)) {
            return 256 / longDataTypeBitSize;
        }
        if(LongVector.SPECIES_512.equals(species)) {
            return 512 / longDataTypeBitSize;
        }
        return 0;
    }

    private float[] initializeFloatData(int size) {
        RandomGenerator random = RandomGenerator.of("L128X1024MixRandom");
        float[] data = new float[size];
        IntStream.iterate(0, n -> n + 1)
            .limit(size)
            .forEach(i -> data[i] =  random.nextFloat());
        return data;
    }

    private char[] initializeCharData(int size) {
        RandomGenerator random = RandomGenerator.of("L128X1024MixRandom");
        char[] data = new char[size];
        int minChar = (int) Character.MIN_VALUE;
        int maxChar = (int) Character.MAX_VALUE;
        IntStream.iterate(0, n -> n + 1)
            .limit(size)
            .forEach(i -> data[i] =  (char) (random.nextInt(minChar, maxChar + 1)));
        return data;
    }

    private boolean[] initializeBooleanData(int size) {
        RandomGenerator random = RandomGenerator.of("L128X1024MixRandom");
        boolean[] data = new boolean[size];
        IntStream.iterate(0, n -> n + 1)
            .limit(size)
            .forEach(i -> data[i] = random.nextBoolean());
        return data;
    }
}
