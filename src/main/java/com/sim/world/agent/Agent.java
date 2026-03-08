package com.sim.world.agent;

import com.sim.layers.InteractiveLayer;
import com.sim.layers.LayerID;
import com.sim.layers.LayerRuntime;
import com.sim.layers.WorldLayer;
import com.sim.world.Coordinate;
import com.sim.world.World;

public class Agent {


    Position pos;
    Velocity velocity;
    double speed;

    Needs needs;

    public Agent(Position pos) {
        this.pos = pos;
        this.velocity = new Velocity(0,0);
        this.speed = 0.02;
        this.needs = new Needs();
    }

    public Agent(Position pos, Needs needs) {
        this.pos = pos;
        this.velocity = new Velocity(0,0);
        this.speed = 0.2;
        this.needs = needs;
    }

    public Position position() {
        return pos;
    }

    public Velocity velocity() {
        return velocity;
    }

    public double speed() {
        return speed;
    }

    public void actOn(World world) {
        perceive(world);
        move(world);
        applyNeeds(world);
        interact(world);
        needs.applyHunger();
    }

    private void perceive(World world) {
        Velocity vHunger = Navigation.towardsTargetValue(
                world.layer(LayerID.FOOD),
                pos,
                needs.hunger()
        ).multiply(needs.hunger());
        Velocity vHeat = Navigation.towardsTargetValue(
                world.layer(LayerID.HEAT),
                pos,
                Needs.HEAT_OPTIMUM
        ).multiply(needs.needForHeat());
        Velocity vCuriosity = Navigation.towardsTargetValue(
                world.layer(LayerID.SCENT),
                pos,
                needs.curiosity()
        ).multiply(needs.curiosity());
        Velocity vFear = Navigation.towardsTargetValue(
                world.layer(LayerID.SCENT),
                pos,
                Needs.MIN
        ).multiply(needs.fear());

        this.velocity
                .add(vHunger)
                .add(vHeat)
                .add(vCuriosity)
                .add(vFear);

        if(velocity.length() < 0.02) {
            this.velocity.add(Navigation
                            .randomVector(world.random())
                            .multiply(0.1f));
        }

    }



    private void move(World world) {
        float pX = (float) (pos.x() + velocity().vx() * speed);
        float pY = (float) (pos.y() + velocity().vy() * speed);
        if(pX < 0) pX = 0;
        if(pY < 0) pY = 0;
        if(pX > world.width()) pX = world.width();
        if(pY > world.height()) pY = world.height();
        this.pos = new Position(pX,pY);
    }

    private void applyNeeds(World world) {
        Coordinate cell = pos.nearestCoordinate(world);
        if(velocity.length() < 0.05f) {
            needs.applySaturation(world.layerAt(LayerID.FOOD, cell));
            needs.applyHeat(world.layerAt(LayerID.HEAT, cell));
            needs.applyFear(world.layerAt(LayerID.SCENT, cell));
            needs.applyCuriosity();
        }
    }

    private void interact(World world) {
        applyScent(world);
        applyFood(world);
    }

    private void applyScent(World world) {
        ((InteractiveLayer) world.layer(LayerID.SCENT))
                .applyAgentInfluence(pos.nearestCoordinate(world), 1);
    }

    private void applyFood(World world) {
        Coordinate cell = pos.nearestCoordinate(world);
        float val = world.layerAt(LayerID.FOOD, cell);
        ((InteractiveLayer) world.layer(LayerID.FOOD))
                .applyAgentInfluence(cell, -needs.foodConsumption);
    }

}
