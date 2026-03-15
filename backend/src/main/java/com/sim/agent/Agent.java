package com.sim.agent;

import com.sim.layer.LayerID;
import com.sim.world.Coordinate;
import com.sim.world.World;

public class Agent {

    Position pos;
    Vector velocity;
    Vector lastVelocity;
    float speed;

    Needs needs;

    private final float baseSpeed = 0.3f;
    private float actualSpeed;

    public Agent(Position pos) {
        this.pos = pos;
        this.velocity = new Vector(0,0);
        this.lastVelocity = new Vector(0,0);
        this.speed = 0.2f;
        this.needs = new Needs();
    }

    public Agent(Position pos, Needs needs) {
        this.pos = pos;
        this.velocity = new Vector(0,0);
        this.lastVelocity = new Vector(0,0);
        this.speed = 0.2f;
        this.needs = needs;
    }

    public float speed() {
        return baseSpeed * (0.2f + 0.8f * needs.energy());
        //return baseSpeed * needs.energy() * (0.5 + needs.hunger());
    }

    public Position position() {
        return pos;
    }

    public Vector velocity() {
        return velocity;
    }

    public Needs needs() {
        return needs;
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
        float foodHere = world.layerAt(LayerID.FOOD, c);
        float heatHere = world.layerAt(LayerID.HEAT, c);
        float scentHere = world.layerAt(LayerID.SCENT, c);

        float foodSlowdown =
                1f - needs.hunger() * foodHere * 0.8f;

        float heatDiff =
                Math.abs(heatHere - Needs.HEAT_OPTIMUM);

        float heatSpeed =
                1f + heatDiff * 0.8f;

        float fearBoost =
                1f + needs.fear() * scentHere * 1.5f;

        float energyFactor =
                0.3f + needs.energy() * 0.7f;

        actualSpeed = baseSpeed
                        * energyFactor
                        * foodSlowdown
                        * heatSpeed
                        * fearBoost;
        return new Senses(
                foodHere,
                heatHere,
                scentHere
        );
    }

    private void decide(World world) {
        velocity.set(0,0);

        Vector vFood = Navigation.towardsTargetValue(
                world.layer(LayerID.FOOD),
                pos,
                Needs.MAX
        ).multiply(needs.interestFood());

        Vector vHeat = Navigation.towardsTargetValue(
                world.layer(LayerID.HEAT),
                pos,
                Needs.HEAT_OPTIMUM
        ).multiply(needs.interestHeat() * 0.6f);

        Vector vTrail = Navigation.towardsTargetValue(
                world.layer(LayerID.SCENT),
                pos,
                Needs.MAX
        ).multiply(needs.curiosity() * 0.6f);

        Vector vExplore = Navigation.towardsTargetValue(
                world.layer(LayerID.SCENT),
                pos,
                Needs.MAX
        ).multiply(needs.interestExplore() * 0.4f);


        Vector vAvoid = Navigation.towardsTargetValue(
                world.layer(LayerID.SCENT),
                pos,
                Needs.MIN
        ).multiply(needs.interestAvoid() * 0.7f);

        velocity
                .add(vFood)
                .add(vHeat)
                //.add(vTrail)
                //.add(vExplore)
                .add(vAvoid);

        if (velocity.length() < 0.05f) {
            velocity.add(
                    Navigation.randomVector(world.random())
                            .multiply(0.2f)
            );
        }

        velocity.add(lastVelocity.multiply(0.4f));

        Vector vWall = wallAvoidance(world);
        velocity.add(vWall);


        if (velocity.length() > 1f) {
            velocity.normalize();
        }
    }

    private Vector wallAvoidance(World world) {

        float margin = 3f;
        float strength = 0.4f;

        float vx = 0;
        float vy = 0;

        if (pos.x() < margin)
            vx += (margin - pos.x()) / margin;

        if (pos.x() > world.width() - margin)
            vx -= (pos.x() - (world.width() - margin)) / margin;

        if (pos.y() < margin)
            vy += (margin - pos.y()) / margin;

        if (pos.y() > world.height() - margin)
            vy -= (pos.y() - (world.height() - margin)) / margin;

        return new Vector(vx, vy).multiply(strength);
    }

    private void move(World world) {
        float pX = (float) (pos.x() + velocity().vx() * actualSpeed);
        float pY = (float) (pos.y() + velocity().vy() * actualSpeed);
        this.pos = new Position(pX,pY);
        this.lastVelocity.copyOf(velocity);
    }

    private void metabolism(Senses s) {

        float movement =  velocity.length();

        needs.applyEnergyCost(movement);
        needs.applyHunger(movement);

        needs.reactToHeat(s.heat());
        needs.reactToFood(s.food());
        /*if (movement < 0.05f) {
            needs.reactToFood(s.food());
        }*/

        needs.reactToScent(s.scent());
    }


    private void interact(World world, Coordinate c) {

        float scentDeposit = 0.2f;

        // stronger scent when food found
        if(world.layerAt(LayerID.FOOD, c) > 0.4f) {
            scentDeposit += 0.6f;
        }

        world.affect(LayerID.SCENT, c, scentDeposit);
        //world.affect(LayerID.FOOD, c, -needs.foodConsumption());
        if (velocity.length() < 0.10f) {
            world.affect(LayerID.FOOD, c, -1);
        }
    }
}
