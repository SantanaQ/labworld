package com.api.resource.nodes;


import com.api.resource.nodes.layerStep.ClampNode;
import com.api.resource.nodes.layerStep.NormalizeNode;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = ClampNode.class, name = "clamp"),
        @JsonSubTypes.Type(value = NormalizeNode.class, name = "normalize")
})
public abstract class EditorNode<T> {

    private String id;
    private String category;
    private String type;

    public abstract T build();

    public String id() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String category() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String type() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
