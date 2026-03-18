package com.api.resource.nodes.signal;

import com.api.resource.nodes.EditorNode;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

import static com.api.resource.ObjectCaster.getString;

public class FractalNoiseNode extends EditorNode {

    private String seed;
    private int cellSize;
    private int octaves;
    private float persistence;

    @JsonProperty("data")
    public void unpackData(Map<String, Object> data) {
        this.seed =  getString(data, "seed");
        this.cellSize = ((Number) data.get("cellSize")).intValue();
        this.octaves =  ((Number) data.get("octaves")).intValue();
        this.persistence = ((Number) data.get("persistence")).floatValue();
    }

    public String seed() {
        return seed;
    }

    public void setSeed(String seed) {
        this.seed = seed;
    }

    public int cellSize() {
        return cellSize;
    }

    public void setCellSize(int cellSize) {
        this.cellSize = cellSize;
    }

    public int octaves() {
        return octaves;
    }

    public void setOctaves(int octaves) {
        this.octaves = octaves;
    }

    public float persistence() {
        return persistence;
    }

    public void setPersistence(float persistence) {
        this.persistence = persistence;
    }
}
