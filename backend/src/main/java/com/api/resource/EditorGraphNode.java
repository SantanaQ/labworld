package com.api.resource;

import com.api.resource.nodes.EditorEdge;
import com.api.resource.nodes.EditorNode;
import com.sim.layer.LayerID;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class EditorGraphNode {
    private final EditorNode nodeData;
    private final List<EditorEdge> edgeData;
    private final List<EditorGraphNode> parents;
    private final List<EditorGraphNode> children;

    public EditorGraphNode(EditorNode nodeData) {
        this.nodeData = nodeData;
        this.parents = new ArrayList<>();
        this.children = new ArrayList<>();
        this.edgeData = new ArrayList<>();
    }

    public boolean hasParent() {
        return !parents.isEmpty();
    }

    public boolean hasChildren() {
        return !children.isEmpty();
    }

    public void addParent(EditorGraphNode parent) {
        this.parents.add(parent);
    }

    public List<EditorGraphNode> parents() {
        return parents;
    }

    public void addChild(EditorGraphNode child) {
        this.children.add(child);
    }

    public List<EditorGraphNode> children() {
        return children;
    }

    public void addEdgeData(EditorEdge edge) {
        this.edgeData.add(edge);
    }

    public List<EditorEdge> edgeData() {
        return edgeData;
    }

    public EditorNode nodeData() {
        return nodeData;
    }

    public Optional<LayerID> referenceLayer() {
        for (EditorEdge edge : edgeData) {
            if(edge.sourceHandle().equals("reference")
                    && edge.targetHandle().equals("reference")) {
                return Optional.ofNullable(LayerID.byString(edge.target()));
            }
        }
        return Optional.empty();
    }

}
