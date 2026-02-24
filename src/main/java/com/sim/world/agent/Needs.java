package com.sim.world.agent;

public class Needs {

    public static final float TEMP_OPTIMUM = 0.5f;

    private final float impact = 0.05f;

    // ascending
    private float hunger;
    private float curiosity;
    private float fear;

    // descending
    private float energy;

    // optimal in center
    private float temperature;

    public Needs() {
        this.hunger = 0.5f;
        this.energy = 1f;
        this.temperature = TEMP_OPTIMUM;
        this.curiosity = 0.5f;
        this.fear = 0.5f;
    }

    public float hunger() {
        return hunger;
    }

    public float energy() {
        return energy;
    }

    public float temperature() {
        return temperature;
    }

    public float curiosity() {
        return curiosity;
    }

    public float fear() {
        return fear;
    }

    public void applyHunger() {
        hunger = Math.clamp(hunger + impact, 0f, 1f);
    }

    public void applySaturation(float val) {
        hunger = Math.clamp(hunger - val, 0f, 1f);
        applyEnergy(val);
    }

    public void applyEnergy(float val) {
        energy = Math.clamp(energy + val, 0f, 1f);
    }

    public void reduceEnergy() {
        energy = Math.max(0f, energy - impact);
    }

    public void applyTemperature(float val) {
        float diff =  val - temperature;
        temperature = Math.clamp(temperature + diff, -1f, 2f);
    }

    public float needForTemperature() {
        return Math.abs(temperature - TEMP_OPTIMUM);
    }

    public void reduceTemperature() {
        temperature = Math.max(0f, temperature - impact);
    }

    public void applyCuriosity() {
        curiosity = Math.clamp(curiosity + impact, 0f, 1f);
    }

    public void reduceCuriosity() {
        curiosity = Math.max(0f, curiosity - impact);
    }

    public void applyFear(float val) {
        fear = Math.clamp(fear + val, 0f, 1f);
    }

    public void reduceFear() {
        fear = Math.max(0f, fear - impact);
    }
}
