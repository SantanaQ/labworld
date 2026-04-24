package com.sim.agent;

public class Needs {

    public static final float MAX = 1f;
    public static final float MIN = 0;
    public static final float HEAT_OPTIMUM = 0.5f;

    private final float energyCost = 0.002f;
    private final float heatAbsorption = 0.05f;
    private final float supplyAbsorption = 0.5f;
    private final float curiosityFactor = 0.02f;

    // ascending
    private float hunger;
    private float curiosity;
    private float fear;

    // descending
    private float energy;

    // optimal in center
    private float heat;

    // consumption
    float supplyConsumption;

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
        this.curiosity = 1f;
        this.fear = 0.0f;
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


    public float interestSupply() {
        return Math.max(0f, hunger - 0.3f);
    }

    public float interestHeat() {
        return Math.abs(heat - HEAT_OPTIMUM);
    }

    public float interestExplore() {
        return curiosity * (1f - fear);
    }

    public float interestAvoid() {
        return fear;
    }

    public void applyEnergyCost(float movement) {

        float cost = energyCost * (0.5f + movement);
        energy = Math.max(energy - cost, MIN);
    }

    public void applyHunger(float movement) {

        hunger = Math.clamp(
                hunger + energyCost * (0.8f + movement),
                MIN,
                MAX
        );
    }

    public void reactToSupply(float val) {

        float eat = val;// * supplyAbsorption;

        supplyConsumption = eat;

        hunger = Math.max(hunger - eat, MIN);
        energy = Math.min(energy + eat, MAX);
    }

    public void reactToHeat(float val) {

        float diff = val - heat;

        heat += diff * heatAbsorption;

        heat = Math.clamp(heat, MIN, MAX);
    }

    public void reactToScent(float val) {

        float curiosityGain = val * curiosityFactor;

        curiosity = Math.clamp(curiosity + curiosityGain * 0.1f, MIN, MAX);

        if (val > 0.5f) {
            fear = Math.clamp(fear + val, MIN, MAX);
        } else {
            fear = Math.clamp(fear - 0.01f, MIN, MAX);
        }
    }

    public Needs copy() {
        return new Needs(hunger, curiosity, fear, energy, heat);
    }

}
