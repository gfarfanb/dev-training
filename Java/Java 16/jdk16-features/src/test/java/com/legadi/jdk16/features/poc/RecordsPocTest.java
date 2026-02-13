package com.legadi.jdk16.features.poc;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Random;
import java.util.UUID;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import com.legadi.jdk16.features.poc.RecordsPocTest.CompanyPerson;
import com.legadi.jdk16.features.poc.RecordsPocTest.EmployeeRecord;
import com.legadi.jdk16.features.poc.RecordsPocTest.ForField;
import com.legadi.jdk16.features.poc.RecordsPocTest.ForFieldAndMethod;

@Tag("JEP-395")
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
    public void records_localRecordsAndStatics_implementation() {
        int id = Math.abs(new Random().nextInt());
        String name = UUID.randomUUID().toString();

        record WorkerRecord(int id, String name) {

            public static final long SALARY_BASE = 1L;
        }

        assertThat(WorkerRecord.SALARY_BASE, is(1L));

        WorkerRecord worker = new WorkerRecord(id, name);

        assertThat(worker.id(), is(id));
        assertThat(worker.name(), is(name));
    }

    @Test
    public void records_annotatedFieldsAndMethods_implementation() {
        Field idField = extractMember(EmployeeRecord.class.getDeclaredFields(), "id");
        Field nameField = extractMember(EmployeeRecord.class.getDeclaredFields(), "name");
        Field salaryField = extractMember(EmployeeRecord.class.getDeclaredFields(), "salary");
        Method nameMethod = extractMember(EmployeeRecord.class.getDeclaredMethods(), "name");

        assertThat(idField.getAnnotation(ForField.class), notNullValue());
        assertThat(idField.getAnnotation(ForFieldAndMethod.class), nullValue());

        assertThat(nameField.getAnnotation(ForField.class), nullValue());
        assertThat(nameField.getAnnotation(ForFieldAndMethod.class), notNullValue());

        assertThat(salaryField.getAnnotations().length, is(0));

        assertThat(nameMethod.getAnnotation(ForField.class), nullValue());
        assertThat(nameMethod.getAnnotation(ForFieldAndMethod.class), notNullValue());
    }

    private <T extends Member> T extractMember(T[] source, String name) {
        return Arrays.stream(source)
            .filter(m -> m.getName().equals(name))
            .findAny()
            .orElseThrow(() -> new IllegalArgumentException("Member not found: " + name));
    }

    @Target(ElementType.FIELD)
    @Retention(RetentionPolicy.RUNTIME)
    public @interface ForField{}

    @Target({ElementType.FIELD, ElementType.METHOD})
    @Retention(RetentionPolicy.RUNTIME)
    public @interface ForFieldAndMethod{}

    sealed interface CompanyPerson permits EmployeeRecord {

        public enum IncreaseBase {

            X_10, X_20, X_25
        }

        public interface InternalJob {

            void checkHours();
        }

        long increaseSalary(int percentage);
    }

    record EmployeeRecord(@ForField int id, @ForFieldAndMethod String name, long salary)
            implements CompanyPerson {

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
