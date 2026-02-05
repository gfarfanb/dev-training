package com.legadi.jdk9.features.module_b.service;

import java.util.stream.Stream;

import com.legadi.jdk9.features.module_a.service.FeatureService;

public class JDK8FeatureService implements FeatureService {

    @Override
    public String module() {
        return "module.b";
    }

    @Override
    public String platform() {
        return "JDK 8";
    }

    @Override
    public Stream<String> features() {
        return Stream.of(
            "Lambda Expressions",
            "Default Methods",
            "Functional Interfaces",
            "Method References",
            "Stream API",
            "Optional Class",
            "Date and Time API (java.time)",
            "Reactive Streams",
            "Base64 Encoding and Decoding",
            "String Joiner"
        );
    }
}
