package com.legadi.jdk8.features.poc;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import java.util.UUID;
import java.util.function.BooleanSupplier;
import java.util.function.DoubleSupplier;
import java.util.function.IntSupplier;
import java.util.function.LongSupplier;
import java.util.function.Supplier;

import org.junit.jupiter.api.Test;

public class SupplierPocTest {

    @Test
    public void booleanSupplier_getAsBoolean_usage() {
        BooleanSupplier supplier = () -> Boolean.TRUE;

        assertThat(supplier.getAsBoolean(), is(Boolean.TRUE));
    }

    @Test
    public void doubleSupplier_getAsDouble_usage() {
        DoubleSupplier supplier = () -> Double.MAX_VALUE;

        assertThat(supplier.getAsDouble(), is(Double.MAX_VALUE));
    }

    @Test
    public void intSupplier_getAsInt_usage() {
        IntSupplier supplier = () -> Integer.MAX_VALUE;

        assertThat(supplier.getAsInt(), is(Integer.MAX_VALUE));
    }

    @Test
    public void longSupplier_getAsLong_usage() {
        LongSupplier supplier = () -> Long.MAX_VALUE;

        assertThat(supplier.getAsLong(), is(Long.MAX_VALUE));
    }

    @Test
    public void supplier_get_usage() {
        Supplier<String> supplier = () -> UUID.randomUUID().toString();

        assertDoesNotThrow(
            () -> UUID.fromString(supplier.get()));
    }
}
