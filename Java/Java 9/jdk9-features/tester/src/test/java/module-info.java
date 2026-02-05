module module.tester.test {

    requires module.b;
    requires module.c;

    requires jdk.incubator.httpclient;

    requires lombok;
    requires org.junit.jupiter.api;
    requires org.hamcrest;

    opens com.legadi.jdk9.features.module_tester.embedded to spring.core;

    exports com.legadi.jdk9.features.module_tester.embedded;
    exports com.legadi.jdk9.features.module_tester.embedded.controller;
    exports com.legadi.jdk9.features.module_tester.embedded.model;
    exports com.legadi.jdk9.features.module_tester.exception;
    exports com.legadi.jdk9.features.module_tester.poc;
}
