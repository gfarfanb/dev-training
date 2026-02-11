package com.legadi.jdk14.features.poc;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("previewfeature")
public class RecordsPocTest {

    @Test
    public void records_defaultDefinition_implementation() {
        EmployeeRecord recordA = new EmployeeRecord(5, "Tester", 500_000L);
        EmployeeRecord recordB = new EmployeeRecord(5, "Tester", 500_000L);
        EmployeeRecord recordC = new EmployeeRecord(5, "tester", 500_000L);

        assertThat(recordA.id(), is(5));
        assertThat(recordA.name(), is("Tester"));
        assertThat(recordA.salary(), is(500000L));
        assertThat(recordA.hashCode(), is(recordB.hashCode()));
        assertThat(recordA, equalTo(recordB));
        assertThat(recordA, not(equalTo(recordC)));
        assertThat(recordA.toString(), containsString("id=5"));
        assertThat(recordA.toString(), containsString("name=Tester"));
        assertThat(recordA.toString(), containsString("salary=500000"));
    }

    @Test
    public void records_extendedConstructor_implementation() {
        assertThrows(IllegalArgumentException.class,
            () -> new EmployeeRecord(5, "Tester", 0L));
    }

    @Test
    public void records_additionalMethods_implementation() {
        EmployeeRecord record = new EmployeeRecord(5, "Tester", 500_000L);

        assertThat(record.increaseSalary(25), is(625_000L));
    }

    record EmployeeRecord(int id, String name, long salary) {

        public EmployeeRecord {
            if(salary < 1) {
                throw new IllegalArgumentException("salary must be greater than 0");
            }
        }

        public long increaseSalary(int percentage) {
            double pct = (double) percentage / 100;
            double increase = this.salary * pct;
            return this.salary + Math.round(increase);
        }
    }
}
