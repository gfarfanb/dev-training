package com.legadi.jdk13.features.poc;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import java.io.IOException;
import java.util.Map;

import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

public class TextBlocksPocTest {

    @Test
    public void string_multiline_implementation() throws IOException {
        String json = """
            {
                "name": "Text-Blocks",
                "description": "PoC test to verify the text-blocks implementation"
            }
            """;
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, String> object = objectMapper.readValue(json, new TypeReference<Map<String, String>>() {});

        assertThat(object.get("name"), is("Text-Blocks"));
        assertThat(object.get("description"), is("PoC test to verify the text-blocks implementation"));
    }
}
