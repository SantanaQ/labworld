package com.api.resource.nodes.time_behavior;

import com.api.resource.nodes.EditorNode;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

import static com.api.resource.ObjectCaster.*;

public class DomainWarpNode extends EditorNode {
    private int width;
    private int height;

    private int cellSizeX;
    private int octavesX;
    private float persistenceX;

    private int cellSizeY;
    private int octavesY;
    private float persistenceY;

    private float strength;
    private String seed;

    @JsonProperty("data")
    public void unpackData(Map<String, Object> data) {
        this.seed =  getString(data, "seed");
        this.width = getInt(data, "width");
        this.height =  getInt(data, "height");
        this.strength = getFloat(data, "strength");

        this.cellSizeX = getInt(data, "cellSizeX");
        this.octavesX = getInt(data, "octavesX");
        this.persistenceX = getFloat(data, "persistenceX");

        this.cellSizeY = getInt(data, "cellSizeY");
        this.octavesY = getInt(data, "octavesY");
        this.persistenceY = getFloat(data, "persistenceY");
    }

    public int width() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int height() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int cellSizeX() {
        return cellSizeX;
    }

    public void setCellSizeX(int cellSizeX) {
        this.cellSizeX = cellSizeX;
    }

    public int octavesX() {
        return octavesX;
    }

    public void setOctavesX(int octavesX) {
        this.octavesX = octavesX;
    }

    public float persistenceX() {
        return persistenceX;
    }

    public void setPersistenceX(float persistenceX) {
        this.persistenceX = persistenceX;
    }

    public int cellSizeY() {
        return cellSizeY;
    }

    public void setCellSizeY(int cellSizeY) {
        this.cellSizeY = cellSizeY;
    }

    public int octavesY() {
        return octavesY;
    }

    public void setOctavesY(int octavesY) {
        this.octavesY = octavesY;
    }

    public float persistenceY() {
        return persistenceY;
    }

    public void setPersistenceY(float persistenceY) {
        this.persistenceY = persistenceY;
    }

    public float strength() {
        return strength;
    }

    public void setStrength(float strength) {
        this.strength = strength;
    }

    public String seed() {
        return seed;
    }

    public void setSeed(String seed) {
        this.seed = seed;
    }
}
