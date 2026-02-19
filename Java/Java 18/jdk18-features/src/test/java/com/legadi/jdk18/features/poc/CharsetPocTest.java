package com.legadi.jdk18.features.poc;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("JEP-400")
public class CharsetPocTest {

    @Test
    public void system_getPropertyAndCharset_usage() {
        assertThat(Charset.defaultCharset(), is(StandardCharsets.UTF_8));
        assertThat(System.getProperty("file.encoding"), is("UTF-8"));
        assertThat(System.getProperty("native.encoding"), not(is("UTF-8")));
        assertThat(System.getProperty("sun.jnu.encoding"), not(is("UTF-8")));
        assertThat(System.getProperty("sun.stdout.encoding"), not(is("UTF-8")));
        assertThat(System.getProperty("sun.stderr.encoding"), not(is("UTF-8")));

        if(System.console() != null) {
            assertThat(System.console().charset(), not(is("UTF-8")));
        }
    }
}
