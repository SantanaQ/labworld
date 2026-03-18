package com.api.resource.nodes.global;

import com.api.resource.nodes.EditorNode;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

import static com.api.resource.ObjectCaster.*;

public class WorldNode extends EditorNode {

    private int  width;
    private int  height;
    private String seed;

    @JsonProperty("data")
    public void unpackData(Map<String, Object> data) {
        this.width =  getInt(data, "width");
        this.height = getInt(data, "height");
        this.seed = getString(data, "seed");
    }

    public int width() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int height() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public String seed() {
        return seed;
    }

    public void setSeed(String seed) {
        this.seed = seed;
    }
}
