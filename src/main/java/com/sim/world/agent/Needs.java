package com.sim.world.agent;

public class Needs {

    public static final float HEAT_OPTIMUM = 0.5f;

    private final float impact = 0.05f;

    // ascending
    private float hunger;
    private float curiosity;
    private float fear;

    // descending
    private float energy;

    // optimal in center
    private float heat;

    public Needs(float hunger,
                 float curiosity,
                 float fear,
                 float energy,
                 float heat) {
        this.hunger = hunger;
        this.energy = energy;
        this.heat = heat;
        this.curiosity = curiosity;
        this.fear = fear;
    }

    public Needs() {
        this.hunger = 0.5f;
        this.energy = 1f;
        this.heat = HEAT_OPTIMUM;
        this.curiosity = 0.5f;
        this.fear = 0.5f;
    }

    public float hunger() {
        return hunger;
    }

    public float energy() {
        return energy;
    }

    public float heat() {
        return heat;
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

    public void applyHeat(float val) {
        float diff =  val - heat;
        heat = Math.clamp(heat + diff, -1f, 2f);
    }

    public float needForHeat() {
        return Math.abs(heat - HEAT_OPTIMUM);
    }

    public void reduceHeat() {
        heat = Math.max(0f, heat - impact);
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
