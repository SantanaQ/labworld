package com.sim.agent;

import com.sim.layer.LayerID;
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
        this.speed = 0.2;
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
        Coordinate c = pos.nearestCoordinate(world);
        Senses s = sense(world, c);
        decide(world);
        move(world);
        metabolism(s);
        interact(world, c);
    }

    private Senses sense(World world, Coordinate c) {
        return new Senses(
                world.layerAt(LayerID.FOOD, c),
                world.layerAt(LayerID.HEAT, c),
                world.layerAt(LayerID.SCENT, c)
        );
    }

    private void decide(World world) {
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

        if(velocity.length() > 1f) {
            this.velocity.normalize();
        }

        if(velocity.length() < 0.02f) {
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

    private void metabolism(Senses s) {

        needs.applyHunger();
        needs.applyEnergyCost();

        /*needs.reactToFood(s.food());
        needs.reactToHeat(s.heat());
        needs.reactToScent(s.scent());
        */
    }

    private void interact(World world, Coordinate c) {
        world.affect(LayerID.SCENT, c, 1);
        if(this.velocity.length() < 0.05f) {
            world.affect(LayerID.FOOD, c, -needs.foodConsumption);
        }
    }

}
