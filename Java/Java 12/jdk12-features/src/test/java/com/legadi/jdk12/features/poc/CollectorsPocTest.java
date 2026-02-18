package com.legadi.jdk12.features.poc;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.aMapWithSize;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.collection.IsMapContaining.hasKey;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;

public class CollectorsPocTest {

    @Test
    public void collectors_teeingForMaxMin_usage() {
        Map<String, Integer> result = Stream.of(1, 2, 3, 4, 5, 6, 7, 8, 9)
            .collect(Collectors.teeing(
                Collectors.maxBy(Integer::compareTo),
                Collectors.minBy(Integer::compareTo),
                (maxOpt, minOpt) -> Map.of("MAX", maxOpt.get(), "MIN", minOpt.get())
            ));

        assertThat(result, aMapWithSize(2));
        assertThat(result, hasKey("MAX"));
        assertThat(result, hasKey("MIN"));
        assertThat(result.get("MAX"), is(9));
        assertThat(result.get("MIN"), is(1));
    }

    @Test
    public void collectors_teeingForFiltering_usage() {
        Map<String, Object> result = Stream.of(1, 2, 3, 4, 5, 6, 7, 8, 9)
            .collect(Collectors.teeing(
                Collectors.filtering(n -> n > 5, Collectors.toList()),
                Collectors.filtering(n -> n > 5, Collectors.counting()),
                (filteredList, filteredCount) -> Map.of("filtered", filteredList, "count", filteredCount)
            ));

        assertThat(result, aMapWithSize(2));
        assertThat(result, hasKey("filtered"));
        assertThat(result, hasKey("count"));
        assertThat((List<Integer>) result.get("filtered"), hasItems(6, 7, 8, 9));
        assertThat(result.get("count"), is(4L));
    }

    @Test
    public void collectors_teeingForOperations_usage() {
        Map<String, Object> result = Stream.of(1, 2, 3, 4, 5, 6, 7, 8, 9)
            .collect(Collectors.teeing(
                Collectors.averagingInt(Integer::intValue),
                Collectors.summingInt(Integer::intValue),
                (average, sum) -> Map.of("average", average, "sum", sum)
            ));

        assertThat(result, aMapWithSize(2));
        assertThat(result, hasKey("average"));
        assertThat(result, hasKey("sum"));
        assertThat(result.get("average"), is(5.0));
        assertThat(result.get("sum"), is(45));
    }
}
