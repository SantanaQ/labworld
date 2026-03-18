package com.api.resource.nodes.time_behavior;

import com.api.resource.nodes.EditorNode;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.Map;

public class CompositeNode extends EditorNode {

    private List<String> children;

    @JsonProperty("data")
    public void unpackData(Map<String, Object> data) {
        Object raw = data.get("children");

        if (raw instanceof List<?> list) {
            this.children = list.stream()
                    .map(Object::toString)
                    .toList();
        } else {
            this.children = List.of();
        }
    }

    public List<String> children() {
        return children;
    }

    public void setChildren(List<String> children) {
        this.children = children;
    }
}
