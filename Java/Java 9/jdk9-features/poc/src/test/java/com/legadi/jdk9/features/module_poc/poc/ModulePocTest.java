package com.legadi.jdk9.features.module_poc.poc;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.everyItem;
import static org.hamcrest.Matchers.startsWith;

import org.junit.jupiter.api.Test;

import com.legadi.jdk9.features.module_a.service.FeatureService;
import com.legadi.jdk9.features.module_a.service.FeatureServiceImpl;
import com.legadi.jdk9.features.module_b.service.JDK8FeatureService;
import com.legadi.jdk9.features.module_c.service.JDK9FeatureService;

/*
 * # Module definition                
 *                                    
 * +----------+                       
 * |          |                       
 * | Module A <---------+             
 * |          |         |             
 * +----^-----+         |<transitive> 
 *      |               |             
 *      |               |             
 * +----+-----+    +----+-----+       
 * |          |    |          |       
 * | Module B |    | Module C |       
 * |          |    |          |       
 * +---^------+    +-------^--+       
 *     |                   |          
 *     |                   +--+       
 *     |  +-----------------+ |       
 *     |  |                 | |       
 *     +--+ Module POC Test +-+       
 *        |                 |         
 *        +-----------------+         
 */
public class ModulePocTest {

    @Test
    public void moduleA_classInstantiation_Implementation() {
        FeatureService service = new FeatureServiceImpl();

        assertThat(service.getPlatformFeatures(), everyItem(startsWith("[module.a]")));
    }

    @Test
    public void moduleB_classInstantiation_Implementation() {
        JDK8FeatureService service = new JDK8FeatureService();

        assertThat(service.getPlatformFeatures(), everyItem(startsWith("[module.b]")));
    }

    @Test
    public void moduleC_classInstantiation_Implementation() {
        FeatureService service = new JDK9FeatureService();

        assertThat(service.getPlatformFeatures(), everyItem(startsWith("[module.c]")));
    }
}
