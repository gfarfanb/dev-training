module module.poc.test {

    requires jdk.incubator.vector;

    requires org.junit.jupiter.api;
    requires org.junit.jupiter.params;
    requires org.hamcrest;

    exports com.legadi.jdk16.features.poc;
}
