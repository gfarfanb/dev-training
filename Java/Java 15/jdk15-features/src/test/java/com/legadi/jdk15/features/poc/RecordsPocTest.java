package com.legadi.jdk15.features.poc;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Random;
import java.util.UUID;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("JEP-384")
@Tag("Preview")
public class RecordsPocTest {

    @Test
    public void records_defaultDefinition_implementation() {
        int id = Math.abs(new Random().nextInt());
        String name = UUID.randomUUID().toString();
        long salary = Math.abs(new Random().nextLong()) + 1;

        EmployeeRecord recordA = new EmployeeRecord(id, name, salary);
        EmployeeRecord recordB = new EmployeeRecord(id, name, salary);
        EmployeeRecord recordC = new EmployeeRecord(id, name + ".", salary);

        assertThat(recordA.id(), is(id));
        assertThat(recordA.name(), is(name));
        assertThat(recordA.salary(), is(salary));
        assertThat(recordA.hashCode(), is(recordB.hashCode()));
        assertThat(recordA, equalTo(recordB));
        assertThat(recordA, not(equalTo(recordC)));
        assertThat(recordA.toString(), containsString("id=" + id));
        assertThat(recordA.toString(), containsString("name=" + name));
        assertThat(recordA.toString(), containsString("salary=" + salary));
    }

    @Test
    public void records_extendedConstructor_implementation() {
        int id = Math.abs(new Random().nextInt());
        String name = UUID.randomUUID().toString();

        assertThrows(IllegalArgumentException.class,
            () -> new EmployeeRecord(id, name, 0L));
    }

    @Test
    public void records_additionalMethods_implementation() {
        int id = Math.abs(new Random().nextInt());
        String name = UUID.randomUUID().toString();

        EmployeeRecord record = new EmployeeRecord(id, name, 500_000L);

        assertThat(record.increaseSalary(25), is(625_000L));
    }

    @Test
    public void records_localRecords_implementation() {
        int id = Math.abs(new Random().nextInt());
        String name = UUID.randomUUID().toString();

        record WorkerRecord(int id, String name) { }

        WorkerRecord worker = new WorkerRecord(id, name);

        assertThat(worker.id(), is(id));
        assertThat(worker.name(), is(name));
    }

    sealed interface CompanyPerson permits EmployeeRecord {

        long increaseSalary(int percentage);
    }

    record EmployeeRecord(int id, String name, long salary) implements CompanyPerson {

        EmployeeRecord {
            if(salary < 1) {
                throw new IllegalArgumentException("salary must be greater than 0");
            }
        }

        @Override
        public long increaseSalary(int percentage) {
            double pct = (double) percentage / 100;
            double increase = this.salary * pct;
            return this.salary + Math.round(increase);
        }
    }
}
