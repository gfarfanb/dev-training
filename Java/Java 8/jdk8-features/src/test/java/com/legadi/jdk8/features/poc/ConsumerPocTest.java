package com.legadi.jdk8.features.poc;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.DoubleConsumer;
import java.util.function.IntConsumer;
import java.util.function.LongConsumer;
import java.util.function.ObjDoubleConsumer;
import java.util.function.ObjIntConsumer;
import java.util.function.ObjLongConsumer;

import org.junit.jupiter.api.Test;

public class ConsumerPocTest {

    @Test
    public void biConsumer_accept_usage() {
        ConsumerRef ref = new ConsumerRef();
        String id = UUID.randomUUID().toString();
        BiConsumer<ConsumerRef, String> consumer = (t, u) -> t.executeObject(u);

        assertDoesNotThrow(() -> consumer.accept(ref, id));
        assertThat(ref.getTouched(), is(1));
        assertThat(ref.getIterationArgs(), hasItems(id));
    }

    @Test
    public void biConsumer_andThen_usage() {
        ConsumerRef ref = new ConsumerRef();
        String id = UUID.randomUUID().toString();
        BiConsumer<ConsumerRef, String> consumerBase = (t, u) -> t.executeObject(u);
        BiConsumer<ConsumerRef, String> consumerAfter = (t, u) -> t.executeObject(u);
        BiConsumer<ConsumerRef, String> consumer = consumerBase.andThen(consumerAfter);

        assertDoesNotThrow(() -> consumer.accept(ref, id));
        assertThat(ref.getTouched(), is(2));
        assertThat(ref.getIterationArgs(), hasItems(id, id));
    }

    @Test
    public void consumer_accept_usage() {
        ConsumerRef ref = new ConsumerRef();
        Consumer<ConsumerRef> consumer = t -> t.execute();

        assertDoesNotThrow(() -> consumer.accept(ref));
        assertThat(ref.getTouched(), is(1));
        assertThat(ref.getIterationArgs(), is(empty()));
    }

    @Test
    public void consumer_andThen_usage() {
        ConsumerRef ref = new ConsumerRef();
        Consumer<ConsumerRef> consumerBase = t -> t.execute();
        Consumer<ConsumerRef> consumerAfter = t -> t.execute();
        Consumer<ConsumerRef> consumer = consumerBase.andThen(consumerAfter);

        assertDoesNotThrow(() -> consumer.accept(ref));
        assertThat(ref.getTouched(), is(2));
        assertThat(ref.getIterationArgs(), is(empty()));
    }

    @Test
    public void doubleConsumer_accept_usage() {
        ConsumerRef ref = new ConsumerRef();
        double number = new Random().nextDouble();
        DoubleConsumer consumer = t -> ref.executeDouble(t);

        assertDoesNotThrow(() -> consumer.accept(number));
        assertThat(ref.getTouched(), is(1));
        assertThat(ref.getIterationArgs(), hasItems(number));
    }

    @Test
    public void doubleConsumer_andThen_usage() {
        ConsumerRef ref = new ConsumerRef();
        double number = new Random().nextDouble();
        DoubleConsumer consumerBase = t -> ref.executeDouble(t);
        DoubleConsumer consumerAfter = t -> ref.executeDouble(t);
        DoubleConsumer consumer = consumerBase.andThen(consumerAfter);

        assertDoesNotThrow(() -> consumer.accept(number));
        assertThat(ref.getTouched(), is(2));
        assertThat(ref.getIterationArgs(), hasItems(number, number));
    }

    @Test
    public void intConsumer_accept_usage() {
        ConsumerRef ref = new ConsumerRef();
        int number = new Random().nextInt();
        IntConsumer consumer = t -> ref.executeInt(t);

        assertDoesNotThrow(() -> consumer.accept(number));
        assertThat(ref.getTouched(), is(1));
        assertThat(ref.getIterationArgs(), hasItems(number));
    }

    @Test
    public void intConsumer_andThen_usage() {
        ConsumerRef ref = new ConsumerRef();
        int number = new Random().nextInt();
        IntConsumer consumerBase = t -> ref.executeInt(t);
        IntConsumer consumerAfter = t -> ref.executeInt(t);
        IntConsumer consumer = consumerBase.andThen(consumerAfter);

        assertDoesNotThrow(() -> consumer.accept(number));
        assertThat(ref.getTouched(), is(2));
        assertThat(ref.getIterationArgs(), hasItems(number, number));
    }

    @Test
    public void longConsumer_accept_usage() {
        ConsumerRef ref = new ConsumerRef();
        long number = new Random().nextLong();
        LongConsumer consumer = t -> ref.executeLong(t);

        assertDoesNotThrow(() -> consumer.accept(number));
        assertThat(ref.getTouched(), is(1));
        assertThat(ref.getIterationArgs(), hasItems(number));
    }

    @Test
    public void longConsumer_andThen_usage() {
        ConsumerRef ref = new ConsumerRef();
        long number = new Random().nextLong();
        LongConsumer consumerBase = t -> ref.executeLong(t);
        LongConsumer consumerAfter = t -> ref.executeLong(t);
        LongConsumer consumer = consumerBase.andThen(consumerAfter);

        assertDoesNotThrow(() -> consumer.accept(number));
        assertThat(ref.getTouched(), is(2));
        assertThat(ref.getIterationArgs(), hasItems(number, number));
    }

    @Test
    public void objDoubleConsumer_accept_usage() {
        ConsumerRef ref = new ConsumerRef();
        double number = new Random().nextDouble();
        ObjDoubleConsumer<ConsumerRef> consumer = (t, u) -> t.executeDouble(u);

        assertDoesNotThrow(() -> consumer.accept(ref, number));
        assertThat(ref.getTouched(), is(1));
        assertThat(ref.getIterationArgs(), hasItems(number));
    }

    @Test
    public void objIntConsumer_accept_usage() {
        ConsumerRef ref = new ConsumerRef();
        int number = new Random().nextInt();
        ObjIntConsumer<ConsumerRef> consumer = (t, u) -> t.executeInt(u);

        assertDoesNotThrow(() -> consumer.accept(ref, number));
        assertThat(ref.getTouched(), is(1));
        assertThat(ref.getIterationArgs(), hasItems(number));
    }

    @Test
    public void objLongConsumer_accept_usage() {
        ConsumerRef ref = new ConsumerRef();
        long number = new Random().nextLong();
        ObjLongConsumer<ConsumerRef> consumer = (t, u) -> t.executeLong(u);

        assertDoesNotThrow(() -> consumer.accept(ref, number));
        assertThat(ref.getTouched(), is(1));
        assertThat(ref.getIterationArgs(), hasItems(number));
    }

    public static class ConsumerRef {

        private final List<Object> iterationArgs = new ArrayList<>();
        private int touched;

        public int getTouched() {
            return touched;
        }

        public List<Object> getIterationArgs() {
            return iterationArgs;
        }

        public void execute() {
            this.touched++;
        }

        public void executeObject(Object arg) {
            execute();
            this.iterationArgs.add(arg);
        }

        public void executeDouble(double arg) {
            executeObject(arg);
        }

        public void executeInt(int arg) {
            executeObject(arg);
        }

        public void executeLong(long arg) {
            executeObject(arg);
        }
    }
}
