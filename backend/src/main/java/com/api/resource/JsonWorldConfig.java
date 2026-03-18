package com.api.resource;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

public class JsonWorldConfig {

    private final JsonNode root;
    private final JsonNode nodes;
    private final JsonNode edges;

    private JsonWorldConfig(JsonNode root, JsonNode nodes, JsonNode edges) {
        this.root = root;
        this.nodes = nodes;
        this.edges = edges;
    }


    public static JsonWorldConfig fromJson(String json) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            JsonNode rootNode = mapper.readTree(json);
            JsonNode nodes = rootNode.get("nodes");
            JsonNode edges = nodes.get("edges");
            return new JsonWorldConfig(rootNode, nodes, edges);
        } catch (IOException e) {
            throw new RuntimeException("Invalid JSON for world config", e);
        }
    }


    public int worldWidth() {
        return 0;
    }


    public int worldHeight() {
        return root.get("worldHeight").asInt();
    }

    public String seed() {
        return root.get("seed").asText();
    }

    public int agentCount() {
        return root.get("agentCount").asInt();
    }

    public Iterable<JsonNode> layers() {
        JsonNode layersNode = root.get("layers");
        if (layersNode == null || !layersNode.isArray()) {
            throw new IllegalArgumentException("World config must contain a 'layers' array");
        }

        return layersNode::elements;
    }

}
