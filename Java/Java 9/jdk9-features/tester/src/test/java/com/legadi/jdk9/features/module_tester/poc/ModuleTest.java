package com.legadi.jdk9.features.module_tester.poc;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.everyItem;
import static org.hamcrest.Matchers.startsWith;

import org.junit.jupiter.api.Test;

import com.legadi.jdk9.features.module_a.service.FeatureService;
import com.legadi.jdk9.features.module_b.service.JDK8FeatureService;
import com.legadi.jdk9.features.module_c.service.JDK9FeatureService;

public class ModuleTest {

    @Test
    void module_Requires_Instantiation() {
        JDK8FeatureService service = new JDK8FeatureService();

        assertThat(service.getPlatformFeatures(), everyItem(startsWith("JDK 8")));
    }

    @Test
    void module_RequiresTransitive_Instantiation() {
        FeatureService service = new JDK9FeatureService();

        assertThat(service.getPlatformFeatures(), everyItem(startsWith("JDK 9")));
    }
}
