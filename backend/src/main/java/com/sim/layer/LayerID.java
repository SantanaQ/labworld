package com.sim.layer;

public enum LayerID {
    HEAT("heat"),
    SUPPLY("supply"),
    SCENT("scent"),;

    final String name;

    LayerID(String val) {
        this.name = val;
    }

    public static LayerID byString(String id) {
        return switch (id) {
            case "heatLayer", "heat", "Heat" -> HEAT;
            case "supplyLayer", "supply", "Supply" -> SUPPLY;
            case "scentLayer", "scent", "Scent" -> SCENT;
            default -> throw new IllegalArgumentException("Invalid layer ID: " + id);
        };
    }
}
