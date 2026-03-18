package com.api.resource;

import com.api.resource.nodes.EditorEdge;
import com.api.resource.nodes.EditorNode;
import com.sim.layer.LayerID;

import java.util.List;
import java.util.Map;

public class EditorConfig {

    private List<EditorNode> nodes;
    private List<EditorEdge> edges;

    public List<EditorNode> nodes() {
        return nodes;
    }

    public void setNodes(List<EditorNode> nodes) {
        this.nodes = nodes;
    }

    public List<EditorEdge> edges() {
        return edges;
    }

    public void setEdges(List<EditorEdge> edges) {
        this.edges = edges;
    }
}
