package com.sim.world.agent;

import com.sim.layers.LayerID;
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

    public Agent(Position pos, Needs needs) {
        this.pos = pos;
        this.velocity = new Velocity(0,0);
        this.speed = 0.001;
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
        needs.applyHunger();
    }

    private void perceive(World world) {
        Velocity toFood = Navigation.towardsTargetValue(
                world.layer(LayerID.FOOD),
                pos,
                needs.hunger()
        ).multiply(needs.hunger());
        Velocity toHeat = Navigation.towardsTargetValue(
                world.layer(LayerID.HEAT),
                pos,
                Needs.HEAT_OPTIMUM
        ).multiply(needs.needForHeat());
        this.velocity
                .add(toFood)
                .add(toHeat);

        if(velocity.length() < 0.025) {
            this.velocity = this.velocity.add(Navigation
                            .randomVector(world.random())
                            .multiply(0.1f));
        }

    }



    private void move(World world) {
        float pX = (float) (pos.x() + velocity().vx() * speed);
        float pY = (float) (pos.y() + velocity().vy() * speed);
        this.pos = new Position(pX,pY);

        needs.applySaturation(world.layerAt(LayerID.FOOD, pos.nearestCoordinate(world)));
        needs.applyHeat(world.layerAt(LayerID.HEAT, pos.nearestCoordinate(world)));
    }

}
