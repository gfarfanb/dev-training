package com.legadi.jdk18.features.poc;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import com.legadi.jdk18.features.poc.doc.SnippetRegion;

@Tag("JEP-413")
public class SnippetPocTest {

    /**
     * The following method shows in its documentation the use of {@code {@snippet ...}}
     * {@snippet :
     *  public static void main(String... args) {
     *      System.out.println("Hello, World!");
     *  }
     * }
     */
    @Test
    public void snippet_inline_implementation() {
        assertThat("inline", is("inline"));
    }

    /**
     * {@snippet file="snippet-files/SnippetRegion.java" region=generate }
     */
    @Test
    public void snippet_external_implementation() {
        assertThat("external", is("external"));
    }

    /**
     * {@snippet lang="properties" :
     *  application.name=Snippet
     *  # @highlight regex="[0-9]+" :
     *  application.port=8080
     * }
     */
    @Test
    public void snippet_lang_implementation() {
        assertThat("lang", is("lang"));
    }
}
