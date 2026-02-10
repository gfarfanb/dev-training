package com.legadi.jdk11.features.poc;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.is;

import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;

public class StringPocTest {

    @Test
    public void string_strip_usage() {
        String resultA = "     5     ".strip();
        String resultB = " 5 5 5 5 5 ".strip();
        String resultC = "     ".strip();
        String resultD = "".strip();

        assertThat(resultA, is("5"));
        assertThat(resultB, is("5 5 5 5 5"));
        assertThat(resultC, is(""));
        assertThat(resultD, is(""));
    }

    @Test
    public void string_stripLeading_usage() {
        String resultA = "     5     ".stripLeading();
        String resultB = " 5 5 5 5 5 ".stripLeading();
        String resultC = "     ".stripLeading();
        String resultD = "".stripLeading();

        assertThat(resultA, is("5     "));
        assertThat(resultB, is("5 5 5 5 5 "));
        assertThat(resultC, is(""));
        assertThat(resultD, is(""));
    }

    @Test
    public void string_stripTrailing_usage() {
        String resultA = "     5     ".stripTrailing();
        String resultB = " 5 5 5 5 5 ".stripTrailing();
        String resultC = "     ".stripTrailing();
        String resultD = "".stripTrailing();

        assertThat(resultA, is("     5"));
        assertThat(resultB, is(" 5 5 5 5 5"));
        assertThat(resultC, is(""));
        assertThat(resultD, is(""));
    }

    @Test
    public void string_isBlank_usage() {
        boolean resultA = "     5     ".isBlank();
        boolean resultB = " 5 5 5 5 5 ".isBlank();
        boolean resultC = " ".isBlank();
        boolean resultD = "".isBlank();

        assertThat(resultA, is(false));
        assertThat(resultB, is(false));
        assertThat(resultC, is(true));
        assertThat(resultD, is(true));
    }

    @Test
    public void string_lines_usage() {
        String text = String.join(System.lineSeparator(), "A", "B", "C");
        List<String> lines = text.lines().collect(Collectors.toList());

        assertThat(lines, hasItems("A", "B", "C"));
    }

    @Test
    public void string_repeat_usage() {
        String resultA = "5".repeat(10);
        String resultB = "55555".repeat(10);
        String resultC = "     ".repeat(10);
        String resultD = "".repeat(10);

        assertThat(resultA, is("5555555555"));
        assertThat(resultB, is("55555555555555555555555555555555555555555555555555"));
        assertThat(resultC, is("                                                  "));
        assertThat(resultD, is(""));
    }
}
