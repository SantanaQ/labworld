package com.sim.json_factories;

import com.api.resource.EditorGraphNode;
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

public class SignalFactory implements LayerFactory<SignalSource> {

    private final Map<String, Function<EditorGraphNode, SignalSource>> registry = new HashMap<>();

    public SignalFactory() {
        registry.put("Image", this::createImageGrid);
        registry.put("ClusteredPatchNoise", this::createClusteredPatchNoise);
        registry.put("FractalNoise", this::createFractalNoise);
        registry.put("HoleMaskNoise", this::createHoleMaskNoise);
    }

    public SignalSource create(EditorGraphNode node) {
        String type = node.nodeData().type();
        Function<EditorGraphNode, SignalSource> creator = registry.get(type);
        if (creator == null) throw new IllegalArgumentException("Unknown signal type: " + type);
        return creator.apply(node);
    }

    private SignalSource createImageGrid(EditorGraphNode node) {
        int width = (int) node.nodeData().get("width");
        int height = (int) node.nodeData().get("height");
        String base64 = (String) node.nodeData().get("imageData");

        try {
            BufferedImage img = decodeImage(base64);
            float[][] grid = imageToGreyValues(img, width, height);

            return new GridSignal(grid);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    private SignalSource createFractalNoise(EditorGraphNode node) {
        String seed = (String) node.nodeData().get("seed");
        int seedCode = seed.hashCode();
        int cellSize = (int) node.nodeData().get("cellSize");
        int octaves = (int) node.nodeData().get("octaves");
        float persistence = (float) node.nodeData().get("persistence");
        return new FractalNoise(seedCode, cellSize, octaves, persistence);
    }

    private SignalSource createClusteredPatchNoise(EditorGraphNode node) {
        String seed = (String) node.nodeData().get("seed");
        int seedCode = seed.hashCode();
        int cellSizeBase = (int) node.nodeData().get("cellSizeBase");
        int octavesBase = (int) node.nodeData().get("octavesBase");
        float persistenceBase = (float) node.nodeData().get("persistenceBase");

        int cellSizeHoles = (int) node.nodeData().get("cellSizeHoles");
        int octavesHoles = (int) node.nodeData().get("octavesHoles");
        float persistenceHoles = (float) node.nodeData().get("persistenceHoles");

        float threshold = (float) node.nodeData().get("threshold");
        float softness = (float) node.nodeData().get("softness");
        float holeStrength = (float) node.nodeData().get("holeStrength");

        SignalSource base = new FractalNoise(seedCode, cellSizeBase, octavesBase, persistenceBase);
        SignalSource holes = new FractalNoise(seedCode, cellSizeHoles, octavesHoles, persistenceHoles);
        return new ClusteredPatchNoise(base, holes, threshold, softness, holeStrength);
    }

    private SignalSource createHoleMaskNoise(EditorGraphNode node) {
        String seed = (String) node.nodeData().get("seed");
        int seedCode = seed.hashCode();
        int cellSizeBase = (int) node.nodeData().get("cellSizeBase");
        int octavesBase = (int) node.nodeData().get("octavesBase");
        float persistenceBase = (float) node.nodeData().get("persistenceBase");

        int cellSizeHoles = (int) node.nodeData().get("cellSizeHoles");
        int octavesHoles = (int) node.nodeData().get("octavesHoles");
        float persistenceHoles = (float) node.nodeData().get("persistenceHoles");

        float holeThreshold = (float) node.nodeData().get("holeThreshold");
        float holeStrength = (float) node.nodeData().get("holeStrength");
        SignalSource base = new FractalNoise(seedCode, cellSizeBase, octavesBase, persistenceBase);
        SignalSource holes = new FractalNoise(seedCode, cellSizeHoles, octavesHoles, persistenceHoles);
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
