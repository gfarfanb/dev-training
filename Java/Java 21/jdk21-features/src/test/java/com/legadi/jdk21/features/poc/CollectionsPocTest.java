package com.legadi.jdk21.features.poc;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;
import java.util.SequencedCollection;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;

public class CollectionsPocTest {

    @Test
    public void sequencedCollection_reversed_usage() {
        SequencedCollection<Integer> arrayList = Stream.of(1, 2, 3, 4, 5)
            .collect(Collectors.toCollection(ArrayList::new));
        SequencedCollection<Integer> arrayDeque = Stream.of(1, 2, 3, 4, 5)
            .collect(Collectors.toCollection(ArrayDeque::new));
        SequencedCollection<Integer> set = Stream.of(1, 2, 3, 4, 5)
            .collect(Collectors.toCollection(LinkedHashSet::new));

        assertThat(arrayList.reversed().iterator().next(), is(5));
        assertThat(arrayDeque.reversed().iterator().next(), is(5));
        assertThat(set.reversed().iterator().next(), is(5));
    }

    @Test
    public void sequencedCollection_getFirstAndAddFirstAndRemoveFirst_usage() {
        SequencedCollection<Integer> arrayList = Stream.of(1, 2, 3, 4, 5)
            .collect(Collectors.toCollection(ArrayList::new));

        assertThat(arrayList.getFirst(), is(1));
        assertThat(arrayList.removeFirst(), is(1));
        assertThat(arrayList, hasSize(4));

        arrayList.addFirst(100);

        assertThat(arrayList.getFirst(), is(100));
        assertThat(arrayList, hasSize(5));
    }

    @Test
    public void sequencedCollection_getLastAndAddLastAndRemoveLast_usage() {
        SequencedCollection<Integer> arrayList = Stream.of(1, 2, 3, 4, 5)
            .collect(Collectors.toCollection(ArrayList::new));

        assertThat(arrayList.getLast(), is(5));
        assertThat(arrayList.removeLast(), is(5));
        assertThat(arrayList, hasSize(4));

        arrayList.addLast(-100);

        assertThat(arrayList.getLast(), is(-100));
        assertThat(arrayList, hasSize(5));
    }
}
