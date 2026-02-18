package com.legadi.jdk17.features.poc;

import static java.util.logging.Level.SEVERE;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.instanceOf;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.IOException;
import java.io.InvalidClassException;
import java.io.ObjectInputFilter;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.time.LocalDateTime;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.UUID;
import java.util.logging.Logger;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("JEP-415")
public class DeserializationFilterPocTest {

    private final Logger logger = Logger.getLogger(DeserializationFilterPocTest.class.getName());

    private final Deque<Closeable> ioDeque = new ArrayDeque<>();

    @AfterEach
    public void cleanup() {
        Iterator<Closeable> it = ioDeque.descendingIterator();

        while(it.hasNext()) {
            Closeable closeable = it.next();
            try {
                if(closeable != null) {
                    closeable.close();
                }
            } catch(IOException ex) {
                logger.log(SEVERE, "Error on closing - " + ex.getMessage(), ex);
            }
        }
    }

    @Test
    public void objectInputFilter_allowedObjectWithPattern_implementation() throws IOException, ClassNotFoundException {
        ByteArrayOutputStream byteArrayOut = writeObject(UUID.randomUUID());
        ObjectInputFilter filter = ObjectInputFilter.Config.createFilter("java.base/*;!*;maxdepth=5");

        Object raw = readObjectWithFilter(byteArrayOut, filter);

        assertThat(raw, instanceOf(UUID.class));
    }

    @Test
    public void objectInputFilter_rejectedObjectWithPattern_implementation() throws IOException, ClassNotFoundException {
        ByteArrayOutputStream byteArrayOutA = writeObject(new LinkedList<String>());
        ObjectInputFilter filterA = ObjectInputFilter.Config.createFilter("!java.util.*");

        assertThrows(InvalidClassException.class,
            () -> readObjectWithFilter(byteArrayOutA, filterA));

        ByteArrayOutputStream byteArrayOutB = writeObject(UUID.randomUUID());
        ObjectInputFilter filterB = ObjectInputFilter.Config.createFilter("java.base/*;!java.util.*");

        // It can't reject java.util.* since java.base/* was already allowed
        Object raw = assertDoesNotThrow(
            () -> readObjectWithFilter(byteArrayOutB, filterB));

        assertThat(raw, instanceOf(UUID.class));
    }

    @Test
    public void objectInputFilter_allowObjectProgrammaticFilter_implementation() throws IOException, ClassNotFoundException {
        ByteArrayOutputStream byteArrayOut = writeObject(LocalDateTime.now());

        Object rawA = readObjectWithFilter(byteArrayOut, DateTimeObjectInputFilter::filter);

        assertThat(rawA, instanceOf(LocalDateTime.class));

        Object rawB = readObjectWithFilter(byteArrayOut, new DateTimeObjectInputFilter());

        assertThat(rawB, instanceOf(LocalDateTime.class));
    }

    @Test
    public void objectInputFilter_rejectObjectProgrammaticFilter_implementation() throws IOException, ClassNotFoundException {
        ByteArrayOutputStream byteArrayOut = writeObject(UUID.randomUUID());

        assertThrows(InvalidClassException.class,
            () -> readObjectWithFilter(byteArrayOut, DateTimeObjectInputFilter::filter));

        assertThrows(InvalidClassException.class,
            () -> readObjectWithFilter(byteArrayOut, new DateTimeObjectInputFilter()));
    }

    private ByteArrayOutputStream writeObject(Object object) throws IOException {
        ByteArrayOutputStream byteArrayOut = register(new ByteArrayOutputStream());
        ObjectOutputStream objectOut = new ObjectOutputStream(byteArrayOut);

        objectOut.writeObject(object);
        objectOut.close();

        return byteArrayOut;
    }

    private Object readObjectWithFilter(ByteArrayOutputStream byteArrayOut, ObjectInputFilter filter)
            throws IOException, ClassNotFoundException {
        ByteArrayInputStream byteArrayIn = register(new ByteArrayInputStream(byteArrayOut.toByteArray()));
        ObjectInputStream objectIn = register(new ObjectInputStream(byteArrayIn));
        
        objectIn.setObjectInputFilter(filter);

        return objectIn.readObject();
    }

    private <T extends Closeable> T register(T closeable) {
        ioDeque.add(closeable);
        return closeable;
    }

    public static class DateTimeObjectInputFilter implements ObjectInputFilter {

        @Override
        public ObjectInputFilter.Status checkInput(ObjectInputFilter.FilterInfo info) {
            return filter(info);
        }

        static ObjectInputFilter.Status filter(ObjectInputFilter.FilterInfo info) {
            Class<?> serialClass = info.serialClass();
            if (serialClass != null) {
                return serialClass.getPackageName().equals("java.time")
                        ? ObjectInputFilter.Status.ALLOWED
                        : ObjectInputFilter.Status.REJECTED;
            }
            return ObjectInputFilter.Status.UNDECIDED;
        }
    }
}
