package com.legadi.jdk8.challenges.math;

public class InversePower {

    public int calculateExponential(int number, int base) {
        return (int) (Math.log(number) / Math.log(base));
    }
}
