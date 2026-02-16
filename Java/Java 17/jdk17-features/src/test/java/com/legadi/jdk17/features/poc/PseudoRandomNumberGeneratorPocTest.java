package com.legadi.jdk17.features.poc;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;

import java.security.SecureRandom;
import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.SplittableRandom;
import java.util.concurrent.ThreadLocalRandom;
import java.util.random.RandomGenerator;
import java.util.random.RandomGeneratorFactory;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

@Tag("JEP-356")
public class PseudoRandomNumberGeneratorPocTest {

    @Test
    public void randomImplementations_asRandomGenerator_implementation() {
        Random random = new Random();
        SplittableRandom splittableRandom = new SplittableRandom();
        ThreadLocalRandom threadLocalRandom = ThreadLocalRandom.current();
        SecureRandom secureRandom = new SecureRandom();

        assertThat(random, instanceOf(RandomGenerator.class));
        assertThat(splittableRandom, instanceOf(RandomGenerator.class));
        assertThat(threadLocalRandom, instanceOf(RandomGenerator.class));
        assertThat(secureRandom, instanceOf(RandomGenerator.class));
    }

    @ParameterizedTest
    @MethodSource("randomGeneratorFactory_algorithms_usage_source")
    public void randomGeneratorFactory_algorithms_usage(RandomGeneratorFactory algorithm) {
        IntStream randomStream = switch(algorithm.name()) {
                case "L128X1024MixRandom" -> {
                    assertThat(algorithm.group(), is("LXM"));
                    assertThat(algorithm.isJumpable(), is(false));
                    assertThat(algorithm.isSplittable(), is(true));
                    yield RandomGenerator.of(algorithm.name()).ints(1000L);
                }
                case "L128X128MixRandom" -> {
                    assertThat(algorithm.group(), is("LXM"));
                    assertThat(algorithm.isJumpable(), is(false));
                    assertThat(algorithm.isSplittable(), is(true));
                    yield RandomGenerator.of(algorithm.name()).ints(1000L);
                }
                case "L128X256MixRandom" -> {
                    assertThat(algorithm.group(), is("LXM"));
                    assertThat(algorithm.isJumpable(), is(false));
                    assertThat(algorithm.isSplittable(), is(true));
                    yield RandomGenerator.of(algorithm.name()).ints(1000L);
                }
                case "L32X64MixRandom" -> {
                    assertThat(algorithm.group(), is("LXM"));
                    assertThat(algorithm.isJumpable(), is(false));
                    assertThat(algorithm.isSplittable(), is(true));
                    yield RandomGenerator.of(algorithm.name()).ints(1000L);
                }
                case "L64X1024MixRandom" -> {
                    assertThat(algorithm.group(), is("LXM"));
                    assertThat(algorithm.isJumpable(), is(false));
                    assertThat(algorithm.isSplittable(), is(true));
                    yield RandomGenerator.of(algorithm.name()).ints(1000L);
                }
                case "L64X128MixRandom" -> {
                    assertThat(algorithm.group(), is("LXM"));
                    assertThat(algorithm.isJumpable(), is(false));
                    assertThat(algorithm.isSplittable(), is(true));
                    yield RandomGenerator.of(algorithm.name()).ints(1000L);
                }
                case "L64X128StarStarRandom" -> {
                    assertThat(algorithm.group(), is("LXM"));
                    assertThat(algorithm.isJumpable(), is(false));
                    assertThat(algorithm.isSplittable(), is(true));
                    yield RandomGenerator.of(algorithm.name()).ints(1000L);
                }
                case "L64X256MixRandom" -> {
                    assertThat(algorithm.group(), is("LXM"));
                    assertThat(algorithm.isJumpable(), is(false));
                    assertThat(algorithm.isSplittable(), is(true));
                    yield RandomGenerator.of(algorithm.name()).ints(1000L);
                }
                case "Random" -> {
                    assertThat(algorithm.group(), is("Legacy"));
                    assertThat(algorithm.isJumpable(), is(false));
                    assertThat(algorithm.isSplittable(), is(false));
                    yield RandomGenerator.of(algorithm.name()).ints(1000L);
                }
                case "SecureRandom" -> {
                    assertThat(algorithm.group(), is("Legacy"));
                    assertThat(algorithm.isJumpable(), is(false));
                    assertThat(algorithm.isSplittable(), is(false));
                    yield RandomGenerator.of(algorithm.name()).ints(1000L);
                }
                case "SplittableRandom" -> {
                    assertThat(algorithm.group(), is("Legacy"));
                    assertThat(algorithm.isJumpable(), is(false));
                    assertThat(algorithm.isSplittable(), is(true));
                    yield RandomGenerator.of(algorithm.name()).ints(1000L);
                }
                case "Xoroshiro128PlusPlus" -> {
                    assertThat(algorithm.group(), is("Xoroshiro"));
                    assertThat(algorithm.isJumpable(), is(true));
                    assertThat(algorithm.isSplittable(), is(false));
                    yield RandomGenerator.of(algorithm.name()).ints(1000L);
                }
                case "Xoshiro256PlusPlus" -> {
                    assertThat(algorithm.group(), is("Xoshiro"));
                    assertThat(algorithm.isJumpable(), is(true));
                    assertThat(algorithm.isSplittable(), is(false));
                    yield RandomGenerator.of(algorithm.name()).ints(1000L);
                }
                default -> IntStream.empty();
            };
        List<Integer> randomNumbers = randomStream
            .boxed()
            .toList();

        assertThat(randomNumbers, hasSize(1000));
    }

    public static Stream<Arguments> randomGeneratorFactory_algorithms_usage_source() {
        return RandomGeneratorFactory.all()
            .map(Arguments::of);
    }
}
