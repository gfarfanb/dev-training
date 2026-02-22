package com.legadi.jdk8.challenges.codesignal;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class StringPatternMatching {

    public static final Set<Character> VOWELS = new HashSet<>(Arrays.asList('a', 'e', 'i', 'o', 'u', 'y'));

    public int matchesPattern(String pattern, String source) {
        if(pattern == null || source == null) {
            return 0;
        }

        int matchCount = 0;
        int length = source.length() - pattern.length() + 1; 

        for(int i = 0; i < length; i++) {
            int partMatchCount = 0;

            for(int j = 0; j < pattern.length(); j++) {
                char letter = source.charAt(i + j);

                if(VOWELS.contains(letter)) {
                    partMatchCount += pattern.charAt(j) == '0' ? 1 : 0;
                } else {
                    partMatchCount += pattern.charAt(j) == '1' ? 1 : 0;
                }
            }

            if(partMatchCount == pattern.length()) {
                matchCount++;
            }
        }

        return matchCount;
    }
}
