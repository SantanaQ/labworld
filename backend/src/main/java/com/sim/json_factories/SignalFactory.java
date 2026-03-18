package com.sim.json_factories;

import com.api.resource.EditorGraphNode;
import com.api.resource.nodes.EditorNode;
import com.api.resource.nodes.signal.ClusteredPatchNoiseNode;
import com.api.resource.nodes.signal.FractalNoiseNode;
import com.api.resource.nodes.signal.HoleMaskNoiseNode;
import com.api.resource.nodes.signal.ImageNode;
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

    private static final Map<String, Function<EditorNode, SignalSource>> registry = Map.of(
            "image", SignalFactory::createImageGrid,
            "clusteredPatchNoise", SignalFactory::createClusteredPatchNoise,
            "fractalNoise", SignalFactory::createFractalNoise,
            "holeMaskNoise", SignalFactory::createHoleMaskNoise
    );


    public static SignalSource create(EditorNode node) {
        String type = node.type();
        Function<EditorNode, SignalSource> creator = registry.get(type);
        if (creator == null) throw new IllegalArgumentException("Unknown signal type: " + type);
        return creator.apply(node);
    }

    private static GridSignal createImageGrid(EditorNode node) {
        ImageNode imageNode = (ImageNode) node;
        int width = imageNode.width();
        int height = imageNode.height();
        String base64 = imageNode.imageData();

        try {
            BufferedImage img = decodeImage(base64);
            float[][] grid = imageToGreyValues(img, width, height);

            return new GridSignal(grid);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    private static SignalSource createFractalNoise(EditorNode node) {
        FractalNoiseNode fractalNoiseNode = (FractalNoiseNode) node;
        String seed = fractalNoiseNode.seed();
        int seedCode = seed.hashCode();
        int cellSize = fractalNoiseNode.cellSize();
        int octaves = fractalNoiseNode.octaves();
        float persistence = fractalNoiseNode.persistence();
        return new FractalNoise(seedCode, cellSize, octaves, persistence);
    }

    private static SignalSource createClusteredPatchNoise(EditorNode node) {
        ClusteredPatchNoiseNode clusteredPatchNoiseNode = (ClusteredPatchNoiseNode) node;
        String seed = clusteredPatchNoiseNode.seed();
        int seedCode = seed.hashCode();
        int cellSizeBase = clusteredPatchNoiseNode.cellSizeBase();
        int octavesBase = clusteredPatchNoiseNode.octavesBase();
        float persistenceBase = clusteredPatchNoiseNode.persistenceBase();

        int cellSizeHoles = clusteredPatchNoiseNode.cellSizeHoles();
        int octavesHoles = clusteredPatchNoiseNode.octavesHoles();
        float persistenceHoles = clusteredPatchNoiseNode.persistenceHoles();

        float threshold = clusteredPatchNoiseNode.threshold();
        float softness = clusteredPatchNoiseNode.softness();
        float holeStrength = clusteredPatchNoiseNode.holeStrength();

        SignalSource base = new FractalNoise(seedCode, cellSizeBase, octavesBase, persistenceBase);
        SignalSource holes = new FractalNoise(seedCode, cellSizeHoles, octavesHoles, persistenceHoles);
        return new ClusteredPatchNoise(base, holes, threshold, softness, holeStrength);
    }

    private static SignalSource createHoleMaskNoise(EditorNode node) {
        HoleMaskNoiseNode holeMaskNoiseNode = (HoleMaskNoiseNode) node;
        String seed = holeMaskNoiseNode.seed();
        int seedCode = seed.hashCode();
        int cellSizeBase = holeMaskNoiseNode.cellSizeBase();
        int octavesBase = holeMaskNoiseNode.octavesBase();
        float persistenceBase = holeMaskNoiseNode.persistenceBase();

        int cellSizeHoles = holeMaskNoiseNode.cellSizeHoles();
        int octavesHoles = holeMaskNoiseNode.octavesHoles();
        float persistenceHoles = holeMaskNoiseNode.persistenceHoles();

        float holeThreshold = holeMaskNoiseNode.holeThreshold();
        float holeStrength = holeMaskNoiseNode.holeStrength();
        SignalSource base = new FractalNoise(seedCode, cellSizeBase, octavesBase, persistenceBase);
        SignalSource holes = new FractalNoise(seedCode, cellSizeHoles, octavesHoles, persistenceHoles);
        return new HoleMaskNoise(base, holes, holeThreshold, holeStrength);
    }




    private static BufferedImage decodeImage(String base64) throws IOException {
        byte[] imageBytes = Base64.getDecoder().decode(base64);

        try (ByteArrayInputStream bais = new ByteArrayInputStream(imageBytes)) {
            BufferedImage img = ImageIO.read(bais);
            if (img == null) {
                throw new IllegalArgumentException("Ungültige Bilddaten");
            }
            return img;
        }
    }

    private static float[][] imageToGreyValues(BufferedImage img, int targetW, int targetH) {
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
