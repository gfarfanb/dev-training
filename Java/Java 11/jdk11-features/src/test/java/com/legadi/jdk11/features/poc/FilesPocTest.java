package com.legadi.jdk11.features.poc;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.io.FileMatchers.anExistingFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("JEP-330")
public class FilesPocTest {

    @Test
    public void files_writeString_usage() throws IOException {
        Path filePath = Files.createTempFile("write-string", ".txt");
        Files.writeString(filePath, "Writing strings....");

        assertThat(filePath.toFile(), is(anExistingFile()));

        Files.deleteIfExists(filePath);

        assertThat(filePath.toFile(), not(anExistingFile()));
    }

    @Test
    public void files_readString_usage() throws IOException {
        Path filePath = Paths.get("src", "test", "resources", "files-read_string.input.txt");
        String content = Files.readString(filePath);

        assertThat(content, is("JEP-330"));
    }
}
