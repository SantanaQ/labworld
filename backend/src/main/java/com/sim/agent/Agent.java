package com.sim.agent;

import com.sim.layer.LayerID;
import com.sim.world.Coordinate;
import com.sim.world.World;

public class Agent {

    Position pos;
    Position lastPos;
    Vector velocity;
    float speed;

    Needs needs;

    private final float BASE_SPEED = 0.3f;

    private final float MAX_INTERACTION_VELOCITY = 0.1f;

    public Agent(Position pos) {
        this.pos = pos;
        this.lastPos = pos;
        this.velocity = new Vector(0,0);
        this.speed = 0.2f;
        this.needs = new Needs();
    }

    public Agent(Position pos, Needs needs) {
        this.pos = pos;
        this.lastPos = pos;
        this.velocity = new Vector(0.5f,0.5f);
        this.speed = 0.2f;
        this.needs = needs;
    }

    public float speed() {
        return speed;
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
        decide(s, world);
        move(world);
        metabolism(s);
        interact(world, c);
    }

    private Senses sense(World world, Coordinate c) {
        float supplyHere = world.layerAt(LayerID.SUPPLY, c);
        float heatHere = world.layerAt(LayerID.HEAT, c);
        float scentHere = world.layerAt(LayerID.SCENT, c);

        float supplySlow = needs.interestSupply() * supplyHere * 0.5f;

        float energySlow = (1f - needs.energy()) * 0.5f;

        float stressSpeed = needs.interestAvoid() * 0.5f;

        speed = BASE_SPEED * (1f - supplySlow - energySlow) + stressSpeed;
        if(needs.interestAvoid() > 0.9f) {
            speed += needs.interestAvoid() * 0.3f;
        }

        speed = Math.max(0.05f, speed);

        return new Senses(
                supplyHere,
                heatHere,
                scentHere
        );
    }

    private void decide(Senses s, World world) {

        float rayDistance = 10f;

        Vector forward = velocity.length() > 0.01f
                ? velocity.copy().normalize()
                : Navigation.randomVector(world.random());

        Vector steering = new Vector(0, 0);

        Vector vSupply = Navigation.navigateWithRays(
                world.layer(LayerID.SUPPLY),
                pos,
                forward,
                Needs.MAX,
                rayDistance
        ).multiply(needs.interestSupply());


        Vector vHeat = Navigation.navigateWithRays(
                world.layer(LayerID.HEAT),
                pos,
                forward,
                Needs.HEAT_OPTIMUM,
                rayDistance
        ).multiply(needs.interestHeat());

        Vector vTrail = Navigation.navigateWithRays(
                world.layer(LayerID.SCENT),
                pos,
                forward,
                0.2f,
                rayDistance
        ).multiply(needs.interestExplore());

        Vector vAvoid = Navigation.navigateAllNeighbors(
                world.layer(LayerID.SCENT),
                pos,
                Needs.MIN
        ).multiply(needs.interestAvoid());

        if (needs.fear() > 0.6f) {
            steering.add(vAvoid.multiply(1.5f));
        }
        else if (needs.hunger() > 0.7f) {
            steering.add(vSupply);
        } else if (needs.interestHeat() > 0.3f) {
            steering.add(vHeat);
        }
        else {
            steering.add(vTrail.multiply(0.5f));
        }

        steering.add(wallAvoidance(world).multiply(2f));

        steering.add(velocity.multiply(0.3f));

        if (steering.length() < 0.5f) {
            steering.add(Navigation.randomVector(world.random()).multiply(0.2f));
        }

        if (steering.length() > 1f) {
            steering.normalize();
        }

        velocity = steering;
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
        this.lastPos = pos;
        float pX = pos.x() + velocity().vx() * speed;
        float pY = pos.y() + velocity().vy() * speed;
        this.pos = new Position(pX,pY);
    }

    private void metabolism(Senses s) {

        float movement =  velocity.length();

        needs.applyEnergyCost(movement);
        needs.applyHunger(movement);
        needs.reactToHeat(s.heat());

        if (movement < MAX_INTERACTION_VELOCITY) {
            needs.reactToSupply(s.supply());
        }

        needs.reactToScent(s.scent());

    }


    private void interact(World world, Coordinate c) {
        Coordinate lastCoordinate  = lastPos.nearestCoordinate(world);

        float scentDeposit = 0.1f;

        world.affect(LayerID.SCENT, lastCoordinate, scentDeposit);

        if (velocity.length() < MAX_INTERACTION_VELOCITY) {
            world.affectKernel(LayerID.SUPPLY, c);
        }
    }
}
