package com.sim.json_factories;

import com.fasterxml.jackson.databind.JsonNode;
import com.sim.layer.step.LayerStep;
import com.sim.signal.*;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class SignalFactory {

    private final Map<String, Function<JsonNode, SignalSource>> registry = new HashMap<>();

    public SignalFactory() {
        registry.put("Image", this::createImageGrid);
        registry.put("ClusteredPatchNoise", this::createClusteredPatchNoise);
        registry.put("FractalNoise", this::createFractalNoise);
        registry.put("HoleMaskNoise", this::createHoleMaskNoise);
    }

    public SignalSource create(JsonNode node) {
        String type = node.get("type").asText();
        Function<JsonNode, SignalSource> creator = registry.get(type);
        if (creator == null) throw new IllegalArgumentException("Unknown signal type: " + type);
        return creator.apply(node);
    }

    private SignalSource createImageGrid(JsonNode node) {
        int width = node.get("width").asInt();
        int height = node.get("height").asInt();
        String base64 = node.get("imageData").asText();

        try {
            BufferedImage img = decodeImage(base64);
            float[][] grid = imageToGreyValues(img, width, height);

            return new GridSignal(grid);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    private SignalSource createFractalNoise(JsonNode node) {
        int seed = node.get("seed").asInt();
        int cellSize = node.get("cellSize").asInt();
        int octaves = node.get("octaves").asInt();
        float persistence = (float) node.get("persistence").asDouble();
        return new FractalNoise(seed, cellSize, octaves, persistence);
    }

    private SignalSource createClusteredPatchNoise(JsonNode node) {
        int seed = node.get("seed").asInt();
        int cellSizeBase = node.get("cellSizeBase").asInt();
        int octavesBase = node.get("octavesBase").asInt();
        float persistenceBase = (float) node.get("persistenceBase").asDouble();

        int cellSizeHoles = node.get("cellSizeHoles").asInt();
        int octavesHoles = node.get("octavesHoles").asInt();
        float persistenceHoles = (float) node.get("persistenceHoles").asDouble();

        float threshold = (float) node.get("threshold").asDouble();
        float softness = (float) node.get("softness").asDouble();
        float holeStrength = (float) node.get("holeStrength").asDouble();

        SignalSource base = new FractalNoise(seed, cellSizeBase, octavesBase, persistenceBase);
        SignalSource holes = new FractalNoise(seed, cellSizeHoles, octavesHoles, persistenceHoles);
        return new ClusteredPatchNoise(base, holes, threshold, softness, holeStrength);
    }

    private SignalSource createHoleMaskNoise(JsonNode node) {
        int seed = node.get("seed").asInt();
        int cellSizeBase = node.get("cellSizeBase").asInt();
        int octavesBase = node.get("octavesBase").asInt();
        float persistenceBase = (float) node.get("persistenceBase").asDouble();

        int cellSizeHoles = node.get("cellSizeHoles").asInt();
        int octavesHoles = node.get("octavesHoles").asInt();
        float persistenceHoles = (float) node.get("persistenceHoles").asDouble();

        float holeThreshold = (float) node.get("holeThreshold").asDouble();
        float holeStrength = (float) node.get("holeStrength").asDouble();
        SignalSource base = new FractalNoise(seed, cellSizeBase, octavesBase, persistenceBase);
        SignalSource holes = new FractalNoise(seed, cellSizeHoles, octavesHoles, persistenceHoles);
        return new HoleMaskNoise(base, holes, holeThreshold, holeStrength);
    }




    private BufferedImage decodeImage(String base64) throws IOException {
        byte[] imageBytes = Base64.getDecoder().decode(base64);

        try (ByteArrayInputStream bais = new ByteArrayInputStream(imageBytes)) {
            BufferedImage img = ImageIO.read(bais);
            if (img == null) {
                throw new IllegalArgumentException("Ungültige Bilddaten");
            }
            return img;
        }
    }

    private float[][] imageToGreyValues(BufferedImage img, int targetW, int targetH) {
        BufferedImage resized = new BufferedImage(targetW, targetH, BufferedImage.TYPE_BYTE_GRAY);
        Graphics2D g = resized.createGraphics();
        g.drawImage(img, 0, 0, targetW, targetH, null);
        g.dispose();

        float[][] values = new float[targetW][targetH];
        for(int x=0;x<targetW;x++) {
            for(int y=0;y<targetH;y++) {
                int rgb = resized.getRGB(x, y);
                int grey = rgb & 0xFF;
                values[x][y] = grey / 255f;
            }
        }
        return values;
    }




}
