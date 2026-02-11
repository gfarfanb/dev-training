package com.legadi.jdk10.features.poc;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import java.util.ArrayList;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("JEP-286")
public class VarPocTest {

    @Test
    public void var_typedVsVar_implementation() {
        String typedVar = "Test";
        var localVar = "Test";

        assertThat(localVar, is(typedVar));
    }

    @Test
    public void var_variableInference_implementation() {
        var stringVar = "Test";
        var listVar = new ArrayList<String>();
        var intVar = 5;

        assertThat(stringVar, is("Test"));
        assertThat(listVar, instanceOf(ArrayList.class));
        assertDoesNotThrow(() -> listVar.add("A"));
        assertThat(intVar, is(5));
    }
}
