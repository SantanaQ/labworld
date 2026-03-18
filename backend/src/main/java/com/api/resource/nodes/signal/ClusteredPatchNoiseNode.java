package com.api.resource.nodes.signal;

import com.api.resource.nodes.EditorNode;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

import static com.api.resource.ObjectCaster.*;

public class ClusteredPatchNoiseNode extends EditorNode {

    private String seed;
    private int cellSizeBase;
    private int octavesBase;
    private float persistenceBase;

    private int cellSizeHoles;
    private int octavesHoles;
    private float persistenceHoles;

    private float patchThreshold;
    private float patchSoftness;
    private float holeStrength;

    @JsonProperty("data")
    public void unpackData(Map<String, Object> data) {
        this.seed =  getString(data, "seed");
        this.cellSizeBase = getInt(data, "cellSizeBase");
        this.octavesBase =  getInt(data, "octavesBase");
        this.persistenceBase = getFloat(data, "persistenceBase");

        this.cellSizeHoles = getInt(data, "cellSizeHoles");
        this.octavesHoles = getInt(data, "octavesHoles");
        this.persistenceHoles = getFloat(data, "persistenceHoles");

        this.patchThreshold = getFloat(data, "patchThreshold");
        this.patchSoftness = getFloat(data, "patchSoftness");
        this.holeStrength = getFloat(data, "holeStrength");
    }

    public String seed() {
        return seed;
    }

    public void setSeed(String seed) {
        this.seed = seed;
    }

    public int cellSizeBase() {
        return cellSizeBase;
    }

    public void setCellSizeBase(int cellSizeBase) {
        this.cellSizeBase = cellSizeBase;
    }

    public int octavesBase() {
        return octavesBase;
    }

    public void setOctavesBase(int octavesBase) {
        this.octavesBase = octavesBase;
    }

    public float persistenceBase() {
        return persistenceBase;
    }

    public void setPersistenceBase(float persistenceBase) {
        this.persistenceBase = persistenceBase;
    }

    public int cellSizeHoles() {
        return cellSizeHoles;
    }

    public void setCellSizeHoles(int cellSizeHoles) {
        this.cellSizeHoles = cellSizeHoles;
    }

    public int octavesHoles() {
        return octavesHoles;
    }

    public void setOctavesHoles(int octavesHoles) {
        this.octavesHoles = octavesHoles;
    }

    public float persistenceHoles() {
        return persistenceHoles;
    }

    public void setPersistenceHoles(float persistenceHoles) {
        this.persistenceHoles = persistenceHoles;
    }

    public float threshold() {
        return patchThreshold;
    }

    public void setPatchThreshold(float patchThreshold) {
        this.patchThreshold = patchThreshold;
    }

    public float softness() {
        return patchSoftness;
    }

    public void setPatchSoftness(float patchSoftness) {
        this.patchSoftness = patchSoftness;
    }

    public float holeStrength() {
        return holeStrength;
    }

    public void setHoleStrength(float holeStrength) {
        this.holeStrength = holeStrength;
    }
}
