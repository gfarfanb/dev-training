package com.legadi.jdk8.features.poc;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import java.util.Random;
import java.util.UUID;
import java.util.function.BiPredicate;
import java.util.function.DoublePredicate;
import java.util.function.IntPredicate;
import java.util.function.LongPredicate;
import java.util.function.Predicate;

import org.junit.jupiter.api.Test;

public class PredicatePocTest {

    @Test
    public void biPredicate_test_usage() {
        BiPredicate<String, Integer> predicate = (t, u) -> Integer.parseInt(t) == u;
        int number = new Random().nextInt();
        int another = new Random().nextInt();
        String raw = Integer.toString(number);

        assertThat(predicate.test(raw, number), is(true));
        assertThat(predicate.test(raw, another), is(false));
    }

    @Test
    public void biPredicate_negate_usage() {
        BiPredicate<String, Integer> predicateBase = (t, u) -> Integer.parseInt(t) == u;
        BiPredicate<String, Integer> predicate = predicateBase.negate();
        int number = new Random().nextInt();
        int another = new Random().nextInt();
        String raw = Integer.toString(number);

        assertThat(predicate.test(raw, number), is(false));
        assertThat(predicate.test(raw, another), is(true));
    }

    @Test
    public void biPredicate_and_usage() {
        BiPredicate<String, Integer> predicateBase = (t, u) -> Integer.parseInt(t) == u;
        BiPredicate<String, Integer> predicateAfter = (t, u) -> t.equals(Integer.toString(u));
        BiPredicate<String, Integer> predicate = predicateBase.and(predicateAfter);
        int number = new Random().nextInt();
        int another = new Random().nextInt();
        String raw = Integer.toString(number);

        assertThat(predicate.test(raw, number), is(true));
        assertThat(predicate.test(raw, another), is(false));
    }

    @Test
    public void biPredicate_or_usage() {
        BiPredicate<String, Integer> predicateBase = (t, u) -> Integer.parseInt(t) == u + 1;
        BiPredicate<String, Integer> predicateAfter = (t, u) -> t.equals(Integer.toString(u));
        BiPredicate<String, Integer> predicate = predicateBase.or(predicateAfter);
        int number = new Random().nextInt();
        int another = new Random().nextInt();
        String raw = Integer.toString(number);

        assertThat(predicate.test(raw, number), is(true));
        assertThat(predicate.test(raw, another), is(false));
    }

    @Test
    public void doublePredicate_test_usage() {
        double number = new Random().nextDouble();
        double another = new Random().nextDouble();
        DoublePredicate predicate = t -> t == number;
        
        assertThat(predicate.test(number), is(true));
        assertThat(predicate.test(another), is(false));
    }

    @Test
    public void doublePredicate_negate_usage() {
        double number = new Random().nextDouble();
        double another = new Random().nextDouble();
        DoublePredicate predicateBase = t -> t == number;
        DoublePredicate predicate = predicateBase.negate();

        assertThat(predicate.test(number), is(false));
        assertThat(predicate.test(another), is(true));
    }

    @Test
    public void doublePredicate_and_usage() {
        double number = new Random().nextDouble();
        double another = new Random().nextDouble();
        DoublePredicate predicateBase = t -> t == number;
        DoublePredicate predicateAfter = t -> t != another;
        DoublePredicate predicate = predicateBase.and(predicateAfter);

        assertThat(predicate.test(number), is(true));
        assertThat(predicate.test(another), is(false));
    }

    @Test
    public void doublePredicate_or_usage() {
        double number = new Random().nextDouble();
        double another = new Random().nextDouble();
        DoublePredicate predicateBase = t -> t == number + 1;
        DoublePredicate predicateAfter = t -> t == number;
        DoublePredicate predicate = predicateBase.or(predicateAfter);

        assertThat(predicate.test(number), is(true));
        assertThat(predicate.test(another), is(false));
    }

    @Test
    public void intPredicate_test_usage() {
        int number = new Random().nextInt();
        int another = new Random().nextInt();
        IntPredicate predicate = t -> t == number;
        
        assertThat(predicate.test(number), is(true));
        assertThat(predicate.test(another), is(false));
    }

    @Test
    public void intPredicate_negate_usage() {
        int number = new Random().nextInt();
        int another = new Random().nextInt();
        IntPredicate predicateBase = t -> t == number;
        IntPredicate predicate = predicateBase.negate();

        assertThat(predicate.test(number), is(false));
        assertThat(predicate.test(another), is(true));
    }

    @Test
    public void intPredicate_and_usage() {
        int number = new Random().nextInt();
        int another = new Random().nextInt();
        IntPredicate predicateBase = t -> t == number;
        IntPredicate predicateAfter = t -> t != another;
        IntPredicate predicate = predicateBase.and(predicateAfter);

        assertThat(predicate.test(number), is(true));
        assertThat(predicate.test(another), is(false));
    }

    @Test
    public void intPredicate_or_usage() {
        int number = new Random().nextInt();
        int another = new Random().nextInt();
        IntPredicate predicateBase = t -> t == number + 1;
        IntPredicate predicateAfter = t -> t == number;
        IntPredicate predicate = predicateBase.or(predicateAfter);

        assertThat(predicate.test(number), is(true));
        assertThat(predicate.test(another), is(false));
    }

    @Test
    public void longPredicate_test_usage() {
        long number = new Random().nextLong();
        long another = new Random().nextLong();
        LongPredicate predicate = t -> t == number;
        
        assertThat(predicate.test(number), is(true));
        assertThat(predicate.test(another), is(false));
    }

    @Test
    public void longPredicate_negate_usage() {
        long number = new Random().nextLong();
        long another = new Random().nextLong();
        LongPredicate predicateBase = t -> t == number;
        LongPredicate predicate = predicateBase.negate();

        assertThat(predicate.test(number), is(false));
        assertThat(predicate.test(another), is(true));
    }

    @Test
    public void longPredicate_and_usage() {
        long number = new Random().nextLong();
        long another = new Random().nextLong();
        LongPredicate predicateBase = t -> t == number;
        LongPredicate predicateAfter = t -> t != another;
        LongPredicate predicate = predicateBase.and(predicateAfter);

        assertThat(predicate.test(number), is(true));
        assertThat(predicate.test(another), is(false));
    }

    @Test
    public void longPredicate_or_usage() {
        long number = new Random().nextLong();
        long another = new Random().nextLong();
        LongPredicate predicateBase = t -> t == number + 1;
        LongPredicate predicateAfter = t -> t == number;
        LongPredicate predicate = predicateBase.or(predicateAfter);

        assertThat(predicate.test(number), is(true));
        assertThat(predicate.test(another), is(false));
    }

    @Test
    public void predicate_test_usage() {
        UUID uuid = UUID.randomUUID();
        String raw = uuid.toString();
        String another = UUID.randomUUID().toString();
        Predicate<String> predicate = t -> UUID.fromString(t).equals(uuid);
        
        assertThat(predicate.test(raw), is(true));
        assertThat(predicate.test(another), is(false));
    }

    @Test
    public void predicate_negate_usage() {
        UUID uuid = UUID.randomUUID();
        String raw = uuid.toString();
        String another = UUID.randomUUID().toString();
        Predicate<String> predicateBase = t -> UUID.fromString(t).equals(uuid);
        Predicate<String> predicate = predicateBase.negate();

        assertThat(predicate.test(raw), is(false));
        assertThat(predicate.test(another), is(true));
    }

    @Test
    public void predicate_and_usage() {
        UUID uuid = UUID.randomUUID();
        String raw = uuid.toString();
        String another = UUID.randomUUID().toString();
        Predicate<String> predicateBase = t -> UUID.fromString(t).equals(uuid);
        Predicate<String> predicateAfter = t -> !t.equals(another);
        Predicate<String> predicate = predicateBase.and(predicateAfter);

        assertThat(predicate.test(raw), is(true));
        assertThat(predicate.test(another), is(false));
    }

    @Test
    public void predicate_or_usage() {
        String raw = UUID.randomUUID().toString();
        String another = UUID.randomUUID().toString();
        Predicate<String> predicateBase = t -> UUID.fromString(t).equals(UUID.randomUUID());
        Predicate<String> predicateAfter = t -> t.equals(raw);
        Predicate<String> predicate = predicateBase.or(predicateAfter);

        assertThat(predicate.test(raw), is(true));
        assertThat(predicate.test(another), is(false));
    }

    @Test
    public void predicate_isEquals_usage() {
        UUID uuid = UUID.randomUUID();
        String raw = uuid.toString();
        String another = UUID.randomUUID().toString();
        Predicate<String> predicate = Predicate.isEqual(uuid.toString());
        
        assertThat(predicate.test(raw), is(true));
        assertThat(predicate.test(another), is(false));
    }
}
