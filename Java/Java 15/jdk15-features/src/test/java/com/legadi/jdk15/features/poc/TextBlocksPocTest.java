package com.legadi.jdk15.features.poc;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.endsWith;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;

import java.io.IOException;
import java.util.Map;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@Tag("JEP-378")
public class TextBlocksPocTest {

    @Test
    public void textBlock_multiline_implementation() throws IOException {
        String json = """
            {
                "name": "Text-Blocks",
                "description": "PoC test to verify the text-blocks implementation"
            }
            """;
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, String> object = objectMapper.readValue(json, new TypeReference<Map<String, String>>() {});

        assertThat(json.lines().count(), is(4L));
        assertThat(object.get("name"), is("Text-Blocks"));
        assertThat(object.get("description"), is("PoC test to verify the text-blocks implementation"));
    }

    @Test
    public void textBlock_suppressNewlineCharacter_implementation() throws IOException {
        String text = """
                Lorem ipsum dolor sit amet, consectetur adipiscing \
                elit, sed do eiusmod tempor incididunt ut labore \
                et dolore magna aliqua.\
                """;

        assertThat(text.lines().count(), is(1L));
        assertThat(text, containsString("adipiscing elit"));
        assertThat(text, containsString("labore et"));
    }

    @Test
    public void textBlock_singleSpaceCharacter_implementation() throws IOException {
        String text = """
                Lorem ipsum dolor sit amet, consectetur adipiscing\s
                elit, sed do eiusmod tempor incididunt ut labore\s
                et dolore magna aliqua.\s
                """;

        assertThat(text.lines().count(), is(3L));
        assertThat(text.lines().toArray(String[]::new)[0], endsWith("adipiscing\u0020"));
        assertThat(text.lines().toArray(String[]::new)[1], endsWith("labore\u0020"));
        assertThat(text.lines().toArray(String[]::new)[2], endsWith("aliqua.\u0020"));
        assertThat(text, endsWith("aliqua.\u0020\n"));
        assertThat(text, not(endsWith("aliqua.\u0020\r")));
    }
}
