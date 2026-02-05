package com.legadi.jdk9.features.module_poc.poc;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import java.util.UUID;

import org.junit.jupiter.api.Test;

public class InterfacePocTest {

    @Test
    public void interface_privateMethod_Implementation() {
        Feature feature = new Feature() {};

        assertDoesNotThrow(
            () -> UUID.fromString(feature.createName()));
    }

    public static interface Feature {

        default String name() {
            return createName();
        }

        private String createName() {
            return UUID.randomUUID().toString();
        }
    }
}
