package com.legadi.jdk9.features.module_a.service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public interface FeatureService {

    String platform();

    Stream<String> features();

    default List<String> getPlatformFeatures() {
        return features()
            .map(this::prefix)
            .collect(Collectors.toList());
    }

    private String prefix(String feature) {
        return platform() + ": " + feature;
    }
}
