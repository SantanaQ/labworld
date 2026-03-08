package com.sim.agent;

public class Needs {

    public static final float MAX = 1f;
    public static final float MIN = 0;
    public static final float HEAT_OPTIMUM = 0.5f;

    private final float energyCost = 0.05f;
    private final float curiosityFactor = 0.1f;
    private final float fearFactor = 0.3f;
    private final float fearThreshold = 0.7f;
    private final float heatFactor = 0.5f;

    // ascending
    private float hunger;
    private float curiosity;
    private float fear;

    // descending
    private float energy;

    // optimal in center
    private float heat;

    // consumption
    float foodConsumption;

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
        hunger = Math.clamp(hunger + energyCost, 0f, 1f);
    }

    public void applySaturation(float val) {
        float diff = Math.clamp(hunger - val, 0f, 1f);
        hunger -= diff;
        foodConsumption = diff;
        applyEnergy(val);
    }

    public void applyEnergy(float val) {
        energy = Math.clamp(energy + val, 0f, 1f);
        reduceEnergy();
    }

    public void reduceEnergy() {
        energy = Math.max(0f, energy - energyCost);
    }

    public void applyHeat(float val) {
        float influence =  (val - heat) * heatFactor;
        heat = Math.clamp(heat + influence, 0, 1);
    }

    public float needForHeat() {
        return Math.abs(heat - HEAT_OPTIMUM);
    }


    public void applyCuriosity() {
        curiosity = Math.clamp(curiosity + curiosityFactor, 0f, 1f);
        if (fear > fearThreshold) {
            reduceCuriosity(3 * curiosityFactor);
        }
    }

    public void reduceCuriosity(float val) {
        curiosity = Math.max(0f, curiosity - val);
    }

    public void applyFear(float val) {
        if(val > fearThreshold) {
            fear = Math.clamp(fear + val * fearFactor, 0f, 1f);
        } else {
            reduceFear();
        }

    }

    public void reduceFear() {
        fear = Math.max(0f, fear - fearFactor);
    }
}
