package com.legadi.jdk15.features.poc;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("JEP-358")
public class NullPointerPocTest {

    @Test
    @SuppressWarnings("null")
    public void exception_methodCall_implementation() {
        try {
            String name = null;
            name.length();
        } catch(Exception ex) {
            assertThat(ex.getMessage(), containsString("invoke \"String.length()\""));
            assertThat(ex.getMessage(), containsString("\"name\" is null"));
        }
    }

    @Test
    @SuppressWarnings("null")
    public void exception_propertyCall_implementation() {
        try {
            Spec spec = null;
            spec.name = "Test";
        } catch(Exception ex) {
            assertThat(ex.getMessage(), containsString("field \"name\""));
            assertThat(ex.getMessage(), containsString("\"spec\" is null"));
        }
    }

    public static class Spec {

        public String name;
    }
}
