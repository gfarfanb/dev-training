package com.legadi.jdk9.features.module_a.service;

import java.util.stream.Stream;

public class FeatureServiceImpl implements FeatureService {

    @Override
    public String module() {
        return "module.a";
    }

    @Override
    public String platform() {
        return "N/A";
    }

    @Override
    public Stream<String> features() {
        return Stream.of(
            "Default Implementation"
        );
    }
}
