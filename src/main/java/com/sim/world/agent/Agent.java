package com.sim.world.agent;

import com.sim.layers.LayerID;
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
        this.speed = 0.001;
        this.needs = new Needs();
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
        needs.applyHunger();
    }

    private void perceive(World world) {
        Velocity toFood = perceiveWeightedCenter(world, world.layer(LayerID.FOOD), needs.hunger());
        Velocity toHeat = perceiveBestFittingCenter(world, world.layer(LayerID.HEAT), needs.needForHeat(), Needs.HEAT_OPTIMUM);
        this.velocity = this.velocity
                .add(toFood)
                .add(toHeat);
    }

    private Velocity perceiveWeightedCenter(World world, WorldLayer layer, float need) {
        Coordinate weightedCenter = LayerAggregation.weightedCenter(world,
                pos.nearestCoordinate(world),
                layer,
                8);
        return applyDirectionAndNeed(weightedCenter, need);
    }

    private Velocity perceiveBestFittingCenter(World world, WorldLayer layer, float need, float targetValue) {
        Coordinate bestFittingCenter = LayerAggregation.bestFittingCenter(
                world,
                pos.nearestCoordinate(world),
                layer,
                8,
                3,
                targetValue
                );
        return applyDirectionAndNeed(bestFittingCenter, need);
    }

    private Velocity applyDirectionAndNeed(Coordinate target, float need) {
        float dirX = Math.clamp(target.x() - pos.x(), -1, 1);
        float dirY = Math.clamp(target.y() - pos.y(), -1, 1);
        float vX = dirX * need;
        float vY = dirY * need;
        return new Velocity(vX,vY);
    }



    private void move(World world) {
        float pX = (float) (pos.x() + velocity().vx() * speed);
        float pY = (float) (pos.y() + velocity().vy() * speed);
        this.pos = new Position(pX,pY);

        needs.applySaturation(world.layerAt(LayerID.FOOD, pos.nearestCoordinate(world)));
        needs.applyHeat(world.layerAt(LayerID.HEAT, pos.nearestCoordinate(world)));
    }

}
