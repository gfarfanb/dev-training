package com.legadi.jdk8.challenges.codesignal;

public class ArrayManipulation {

    public int[] transform(int[] input) {
        if(input == null) {
            return null;
        }

        int[] output = new int[input.length];

        for(int i = 0; i < input.length; i++) {
            output[i] = input[i];

            if(i - 1 >= 0) {
                output[i] += input[i - 1];
            }
            if(i + 1 < input.length) {
                output[i] += input[i + 1];
            }
        }

        return output;
    }
}
