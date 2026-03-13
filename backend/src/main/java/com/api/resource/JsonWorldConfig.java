package com.api.resource;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

public class JsonWorldConfig {

    private final JsonNode root;

    private JsonWorldConfig(JsonNode root) {
        this.root = root;
    }


    public static JsonWorldConfig fromJson(String json) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            JsonNode rootNode = mapper.readTree(json);
            return new JsonWorldConfig(rootNode);
        } catch (IOException e) {
            throw new RuntimeException("Invalid JSON for world config", e);
        }
    }


    public int worldWidth() {
        return root.get("worldWidth").asInt();
    }


    public int worldHeight() {
        return root.get("worldHeight").asInt();
    }

    public int seed() {
        return root.get("seed").asInt();
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


    public JsonNode rawNode() {
        return root;
    }
}
