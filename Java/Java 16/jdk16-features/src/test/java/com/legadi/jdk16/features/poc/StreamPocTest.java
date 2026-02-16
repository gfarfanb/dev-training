package com.legadi.jdk16.features.poc;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;

public class StreamPocTest {

    @Test
    public void stream_mapMulti_usage() {
        String methodsCsv = """
            mapMulti
            mapMultiToInt,mapMultiToLong
            mapMultiToDouble
            toList
            """;
    
        List<String> methods = methodsCsv.lines()
            .map(line -> line.split(","))
            .<String>mapMulti((names, consumer) ->
                Arrays.stream(names)
                    .filter(n -> n.startsWith("mapMulti"))
                    .forEach(consumer::accept)
            )
            .collect(Collectors.toList());

        assertThat(methods, hasSize(4));
        assertThat(methods, hasItems("mapMulti", "mapMultiToInt", "mapMultiToLong", "mapMultiToDouble"));
        assertThat(methods, not(hasItems("toList")));
    }

    @Test
    public void stream_mapMultiToInt_usage() {
        final int PROMO_PRICE = 20;
        final int PROMO_PIECES = 5;

        int[][] daily = {
            { 15, 30 },
            { 45, 80, 10 }
        };

        int promos = Arrays.stream(daily)
            .mapMultiToInt((purchases, consumer) ->
                Arrays.stream(purchases)
                    .filter(p -> p >= PROMO_PRICE)
                    .map(p -> Math.floorDiv(p, PROMO_PRICE) * PROMO_PIECES)
                    .forEach(consumer::accept)
            )
            .sum();

        assertThat(promos, is(35));
    }

    @Test
    public void stream_mapMultiToLong_usage() {
        final long PROMO_PRICE = 20L;
        final long PROMO_PIECES = 5L;

        long[][] daily = {
            { 15L, 30L },
            { 45L, 80L, 10L }
        };

        long promos = Arrays.stream(daily)
            .mapMultiToLong((purchases, consumer) ->
                Arrays.stream(purchases)
                    .filter(p -> p >= PROMO_PRICE)
                    .map(p -> Math.floorDiv(p, PROMO_PRICE) * PROMO_PIECES)
                    .forEach(consumer::accept)
            )
            .sum();

        assertThat(promos, is(35L));
    }

    @Test
    public void stream_mapMultiToDouble_usage() {
        final int PROMO_PRICE = 20;
        final int PROMO_PIECES = 5;

        double[][] daily = {
            { 15, 30 },
            { 45, 80, 10 }
        };

        double promos = Arrays.stream(daily)
            .mapMultiToDouble((purchases, consumer) ->
                Arrays.stream(purchases)
                    .mapToInt(p -> (int) p)
                    .filter(p -> p >= PROMO_PRICE)
                    .map(p -> Math.floorDiv(p, PROMO_PRICE) * PROMO_PIECES)
                    .forEach(consumer::accept)
            )
            .sum();

        assertThat(promos, is(35.0));
    }

    @Test
    public void stream_toList_usage() {
        String methodsCsv = """
            mapMulti
            mapMultiToInt
            mapMultiToLong
            mapMultiToDouble
            toList
            """;

        List<String> methods = methodsCsv.lines().toList();

        assertThat(methods, hasSize(5));
        assertThat(methods, hasItems("mapMulti", "mapMultiToInt", "mapMultiToLong", "mapMultiToDouble", "toList"));
    }
}
