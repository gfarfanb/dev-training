package com.legadi.ui.vacations.common;

import java.util.regex.Pattern;

public final class Utils {

    private static final String NUMBER_REGEX = "[+-]?[0-9]+(\\.[0-9]+)?([Ee][+-]?[0-9]+)?";

    private Utils() {}

    public static boolean isNumber(String value) {
        if(value == null) {
            return false;
        }
        return Pattern.matches(NUMBER_REGEX, value);
    }
}
