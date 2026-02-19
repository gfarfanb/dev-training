package com.legadi.jdk18.features.poc.doc;

import java.util.Random;
import java.util.random.RandomGenerator;
import java.util.logging.Logger;

public class SnippetRegion {

    private final Logger logger = Logger.getLogger(SnippetRegion.class.getName());

    public int generateInt() {
        // @start region=generate
        RandomGenerator random = RandomGenerator.of("L128X1024MixRandom"); // @replace regex="\bL128.*\b" replacement="[algorithm]"
        int number = random.nextInt(); // @highlight substring="random"
        logger.info(String.format("Generated number: %d", number)); // @highlight regex="\"(.*)\"" 
        int normalized = Math.abs(number); // @link substring="Math.abs" target="Math#abs"
        // @end region=generate
        return normalized;
    }
}
