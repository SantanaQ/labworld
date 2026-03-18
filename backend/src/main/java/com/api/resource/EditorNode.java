package com.api.resource;


import com.sim.layer.LayerID;

import java.util.Map;
import java.util.Optional;

public class EditorNode {

    private String id;
    private String category;
    private String type;
    private Map<String, Object> data;

    private LayerID layerRef;

    public String id() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String type() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Map<String, Object> data() {
        return data;
    }

    public Object get(String key) {
        return data.get(key);
    }

    public void setData(Map<String, Object> data) {
        this.data = data;
    }

    public String category() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
