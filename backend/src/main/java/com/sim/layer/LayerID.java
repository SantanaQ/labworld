package com.sim.layer;

public enum LayerID {
    HEAT("heat"),
    FOOD("food"),
    SCENT("scent"),;

    final String name;

    LayerID(String val) {
        this.name = val;
    }

    public static LayerID byString(String id) {
        return switch (id) {
            case "heat" -> HEAT;
            case "food" -> FOOD;
            case "scent" -> SCENT;
            default -> throw new IllegalArgumentException("Invalid layer ID: " + id);
        };
    }
}
