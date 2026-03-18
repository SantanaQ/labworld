package com.api.resource.nodes.signal;


import com.api.resource.nodes.EditorNode;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

import static com.api.resource.ObjectCaster.getInt;
import static com.api.resource.ObjectCaster.getString;

public class ImageNode extends EditorNode {

    private int width;
    private int height;
    private String imageData;

    @JsonProperty("data")
    public void unpackData(Map<String, Object> data) {
        this.imageData =  getString(data, "imageData");
        this.width = getInt(data, "width");
        this.height =  getInt(data, "height");
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

    public String imageData() {
        return imageData;
    }

    public void setImageData(String imageData) {
        this.imageData = imageData;
    }
}
