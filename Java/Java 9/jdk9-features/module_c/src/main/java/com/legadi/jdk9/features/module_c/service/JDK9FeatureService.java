package com.legadi.jdk9.features.module_c.service;

import java.util.stream.Stream;

import com.legadi.jdk9.features.module_a.service.FeatureService;

public class JDK9FeatureService implements FeatureService {

    @Override
    public String module() {
        return "module.c";
    }

    @Override
    public String platform() {
        return "JDK 9";
    }

    @Override
    public Stream<String> features() {
        return Stream.of(
            "Modular System (Project Jigsaw)",
            "JShell (Read-Eval-Print Loop)",
            "Private Methods in Interfaces",
            "Diamond Operator for Anonymous Classes",
            "Factory Methods in Interfaces",
            "Compact Number Formatting",
            "Deprecation of java.util.* for Optional",
            "Improved java.time API",
            "Stack Walking API",
            "Javadoc Improvements"
        );
    }
}
