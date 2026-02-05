package com.legadi.jdk9.features.module_tester.embedded;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = "com.legadi.jdk9.features.module_tester")
public class EmbeddedConfig {
    
}
