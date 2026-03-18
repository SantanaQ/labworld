package com.api.resource;

import java.util.Map;

public class ObjectCaster {

    private ObjectCaster() {}

    public static float getFloat(Map<String, Object> data, String key) {
        Object value = data.get(key);

        if (value == null) {
            throw new IllegalArgumentException("Missing required field: " + key);
        }

        if (!(value instanceof Number number)) {
            throw new IllegalArgumentException("Field '" + key + "' is not a number");
        }

        return number.floatValue();
    }

    public static int getInt(Map<String, Object> data, String key) {
        Object value = data.get(key);

        if (value == null) {
            throw new IllegalArgumentException("Missing required field: " + key);
        }

        if (!(value instanceof Number number)) {
            throw new IllegalArgumentException("Field '" + key + "' is not a number");
        }

        return number.intValue();
    }

    public static String getString(Map<String, Object> data, String key) {
        Object value = data.get(key);

        if (value == null) {
            throw new IllegalArgumentException("Missing required field: " + key);
        }

        if (!(value instanceof String str)) {
            throw new IllegalArgumentException("Field '" + key + "' is not a string");
        }

        return str;
    }

}
