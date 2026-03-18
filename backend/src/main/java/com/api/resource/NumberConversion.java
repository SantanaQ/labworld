package com.api.resource;

public class NumberConversion {

    private NumberConversion() {}

    public static float floatValue(Object o) {
        Number n = (Number) o;
        return n.floatValue();
    }

    public static double doubleValue(Object o) {
        Number n = (Number) o;
        return n.doubleValue();
    }

    public static int intValue(Object o) {
        Number n = (Number) o;
        return n.intValue();
    }

}
