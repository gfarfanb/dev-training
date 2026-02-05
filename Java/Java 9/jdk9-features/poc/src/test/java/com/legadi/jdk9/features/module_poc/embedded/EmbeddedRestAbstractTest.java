package com.legadi.jdk9.features.module_poc.embedded;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;

@EnableAutoConfiguration
@SpringBootTest(classes = { EmbeddedConfig.class }, webEnvironment = WebEnvironment.RANDOM_PORT)
public abstract class EmbeddedRestAbstractTest {

    @LocalServerPort
    protected int port;
}
