module module.poc.test {

    requires module.b;
    requires module.c;

    requires jdk.incubator.httpclient;

    requires lombok;
    requires org.junit.jupiter.api;
    requires org.hamcrest;

    opens com.legadi.jdk9.features.module_poc.embedded to spring.core;

    exports com.legadi.jdk9.features.module_poc.embedded;
    exports com.legadi.jdk9.features.module_poc.embedded.controller;
    exports com.legadi.jdk9.features.module_poc.embedded.model;
    exports com.legadi.jdk9.features.module_poc.poc;
}
