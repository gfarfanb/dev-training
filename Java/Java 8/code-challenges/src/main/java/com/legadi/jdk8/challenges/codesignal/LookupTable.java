package com.legadi.jdk8.challenges.codesignal;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class LookupTable {

    // The constraint says that any number can be greater that 10^6 (1,000,000).
    // 2^21 exceeds the duplicated constraint so two numbers couldn't sum 2^21.
    private static final Set<Integer> PRE_CALCULATED_POWER_2 = IntStream.range(0, 21)
        .mapToObj(i -> (int) Math.pow(2, i))
        .collect(Collectors.toSet());

    private final PowerAlgorithm powerAlgorithm;

    public LookupTable(PowerAlgorithm powerAlgorithm) {
        this.powerAlgorithm = powerAlgorithm;
    }

    public int findPairs(int[] numbers) {
        int maxIndex = numbers.length - 1;
        int i = 0, j = 0;
        int pairCount = 0;

        // Loop for secuence (all i,j combinations) 
        // i0,j0|i0,j1|...i0,jn|i1,j1|i1,j2|...i1,jn|i2,j2|...i2,jn|...in,jn
        // n = array length - 1
        while(i < numbers.length) {
            int sum =  numbers[i] + numbers[j];

            if(isPowerOf2(sum)) {
                pairCount++;
            }

            // j increases in each iteration
            // except when is equals to maxIndex
            // in that case i is increased and
            // the new value is assigned to j
            j = (j == maxIndex ? ++i : j + 1);
        }

        return pairCount;
    }

    private boolean isPowerOf2(int n) {
        switch (powerAlgorithm) {
            case LOGARITHM:
                return (Math.log(n) / Math.log(2)) % 1 == 0;
            case PRE_CALCULATED:
                return PRE_CALCULATED_POWER_2.contains(n);
            default:
                throw new IllegalStateException("Invalid power algorithm");
        }
        
    }

    public enum PowerAlgorithm {

        LOGARITHM, PRE_CALCULATED
    }
}
