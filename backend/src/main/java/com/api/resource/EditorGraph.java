package com.api.resource;

import com.api.resource.nodes.EditorEdge;
import com.sim.layer.LayerID;
import com.api.resource.nodes.EditorNode;


import java.util.*;

public class EditorGraph {

    private final EditorGraphNode root;
    private final Map<String, EditorGraphNode> nodeMap = new HashMap<>();

    public EditorGraph(EditorConfig config) {
        for (EditorNode node : config.nodes()) {
            nodeMap.put(node.id(), new EditorGraphNode(node));
        }
        System.out.println(nodeMap.size());
        this.root = nodeMap.get("world");
        build(config);
    }

    private void build(EditorConfig config) {
        for (EditorEdge edge : config.edges()) {
            EditorGraphNode source = nodeMap.get(edge.source());
            EditorGraphNode target = nodeMap.get(edge.target());

            target.addChild(source);
            source.addParent(target);
            source.addEdgeData(edge);
        }
    }

    public EditorGraphNode getNode(String id) {
        return nodeMap.get(id);
    }

    public List<EditorNode> executionOrderOfLayer(LayerID layerID) {
        switch (layerID) {
            case FOOD -> {
                EditorGraphNode foodLayer = nodeMap.get("supplyLayer");
                return executionOrder(foodLayer);
            }
            case HEAT -> {
                EditorGraphNode heatLayer = nodeMap.get("heatLayer");
                return executionOrder(heatLayer);
            }
            case SCENT -> {
                EditorGraphNode scentLayer = nodeMap.get("scentLayer");
                return executionOrder(scentLayer);
            }
            default ->
                throw new RuntimeException("Unsupported layer ID: " + layerID);
        }
    }

    public List<EditorNode> executionOrder(EditorGraphNode node) {
        List<EditorNode> order = new ArrayList<>();
        Set<String> visited = new HashSet<>();

        buildOrder(node, order, visited);

        return order;
    }

    private void buildOrder(EditorGraphNode node, List<EditorNode> order, Set<String> visited) {
        System.out.println(node);
        if(visited.contains(node.nodeData().id())) return;

        visited.add(node.nodeData().id());
        for (EditorGraphNode child : node.children()) {
            buildOrder(child, order, visited);
        }

        order.add(node.nodeData());
    }

    public void printGraph() {
        printNode(root, 0);
    }

    private void printNode(EditorGraphNode node, int depth) {
        String indent = "  ".repeat(depth);

        System.out.println(indent + "- " + node.nodeData().id());

        for (EditorGraphNode child : node.children()) {
            printNode(child, depth + 1);
        }
    }

    public EditorGraphNode root() {
        return root;
    }


}
