package com.legadi.jdk8.features.poc;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import java.util.UUID;

import org.junit.jupiter.api.Test;

public class InterfacePocTest {

    @Test
    public void interface_defaultMethod_implementation() {
        Feature feature = new Feature() {};

        assertDoesNotThrow(
            () -> UUID.fromString(feature.name()));
    }

    public static interface Feature {

        default String name() {
            return UUID.randomUUID().toString();
        }
    }
}
