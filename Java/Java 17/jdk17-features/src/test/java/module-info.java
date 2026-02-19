module module.poc.test {

    requires java.logging;

    requires jdk.incubator.vector;

    requires org.junit.jupiter.api;
    requires org.junit.jupiter.params;
    requires org.hamcrest;

    exports com.legadi.jdk17.features.poc;
}
