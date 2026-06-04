package com.sim.agent;

import com.sim.layer.LayerID;
import com.sim.world.Coordinate;
import com.sim.world.World;

public class Agent {

    short id;
    Position pos;
    Position lastPos;
    Vector velocity;
    float speed;
    Needs needs;

    int lastTick = 0;

    private final float BASE_SPEED = 0.5f;
    private final float MAX_INTERACTION_MOVEMENT = 0.1f;
    private final int DECISION_TICK_INTERVAL = 3;
    private final int STRESS_WINDOW = 10;
    private final int STRESS_THRESH = 4;

    public Agent(short id, Position pos, Needs needs) {
        this.pos = pos;
        this.lastPos = pos;
        this.velocity = new Vector(0.5f,0.5f);
        this.speed = BASE_SPEED;
        this.needs = needs;
        this.id = id;
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

    public short id() {
        return id;
    }

    public void actOn(World world) {
        Coordinate c = pos.nearestCoordinate(world);
        Senses s = sense(world, c);
        adjustSpeed(s);
        if(world.currentTick() >= lastTick + DECISION_TICK_INTERVAL) {
            decide(s, world);
            lastTick = world.currentTick();
        }
        move();
        metabolism(s);
        interact(world, c);
    }

    private Senses sense(World world, Coordinate c) {
        float supplyHere = world.layerAt(LayerID.SUPPLY, c);
        float heatHere = world.layerAt(LayerID.HEAT, c);
        float scentHere = world.layerAt(LayerID.SCENT, c);
        float trailHere = world.layerAt(LayerID.TRAIL, c);
        float stressHere = world.layerAt(LayerID.STRESS, c);

        return new Senses(
                supplyHere,
                heatHere,
                scentHere,
                trailHere,
                stressHere
        );
    }

    private void adjustSpeed(Senses s) {
        float supplySlow = needs.interestSupply() * s.supply() * 0.5f;
        float energySlow = (1f - needs.energy()) * 0.5f;
        float stressSpeed = needs.interestAvoid();// * 0.5f;

        float totalSlow = Math.max(1f - supplySlow - energySlow, 0);

        speed = BASE_SPEED * totalSlow + stressSpeed;

        // Additional boost on high stress
        if(needs.interestAvoid() > 0.9f) {
            speed += needs.interestAvoid() * 0.3f;
        }

        // slow down if supply found and hungry
        if (needs.currentObjective() == Objective.FIND_SUPPLY && s.supply() > 0.1f) {
            speed *= 0.2f;
        }

        speed = Math.max(0.05f, speed);
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
                world.layer(LayerID.TRAIL),
                pos,
                forward,
                Needs.MAX,
                rayDistance
        ).multiply(needs.interestExplore());

        Vector vAvoid = Navigation.navigateAllNeighbors(
                world.layer(LayerID.STRESS),
                pos,
                Needs.MIN
        ).multiply(needs.interestAvoid());

        /*Objective objective = needs.currentObjective();
        switch (objective) {
            case AVOID_DANGER -> steering.add(vAvoid.multiply(1.5f));
            case FIND_SUPPLY -> steering.add(vSupply);
            case ADJUST_TEMPERATURE -> steering.add(vHeat);
            case EXPLORE_PATH -> steering.add(vTrail.multiply(0.5f));
        }*/
        steering.add(vAvoid.multiply(1.5f));
        steering.add(vSupply);
        steering.add(vHeat);
        steering.add(vTrail.multiply(0.5f));

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

    private void move() {
        this.lastPos = pos;
        float pX = pos.x() + velocity().vx() * speed;
        float pY = pos.y() + velocity().vy() * speed;
        this.pos = new Position(pX,pY);
    }

    private void metabolism(Senses s) {
        float movement =  velocity.length() * speed;

        needs.applyEnergyCost(movement);
        needs.applyHunger(movement);
        needs.reactToHeat(s.heat());

        if (movement < MAX_INTERACTION_MOVEMENT) {
            needs.reactToSupply(s.supply());
        }

        needs.reactToScent(s.scent());
        needs.reactToTrail(s.trail());
        needs.reactToStress(s.stress());
    }


    private void interact(World world, Coordinate c) {
        Coordinate lastCoordinate  = lastPos.nearestCoordinate(world);

        float scentDeposit = 0.2f;

        world.affect(LayerID.TRAIL, lastCoordinate, scentDeposit);

        int occupancy = world.occupancyGrid().agentCount(STRESS_WINDOW, c);
        if(occupancy >= STRESS_THRESH) {
            world.affect(LayerID.STRESS, c, 2 * scentDeposit);
        }

        float movement = velocity.length() * speed;

        if (movement < MAX_INTERACTION_MOVEMENT) {
            world.affect(LayerID.SUPPLY, lastCoordinate, -needs.supplyConsumption());
        }
    }


}
