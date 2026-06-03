package com.sim.world.agent;

import com.sim.world.World;
import com.sim.agent.Agent;
import com.sim.agent.Needs;
import com.sim.agent.Position;
import org.junit.jupiter.api.Test;
import com.sim.world.TestWorldBuilder;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class AgentTest {
/*
    @Test
    void if_agent_is_hungry_it_steers_toward_supply() {
        int size = 10;
        World world = new TestWorldBuilder(size)
                .withSupply(TestWorldBuilder.gradientGrid('E', 0, 1, size))
                .build();


        float x = (float) size / 2;
        float y = (float) size / 2;
        Position pos = new Position(x, y);
        Needs needs = new Needs(
                Needs.MAX, // hunger
                Needs.MIN, // curiosity
                Needs.MIN, // fear
                Needs.MAX, // energy
                Needs.HEAT_OPTIMUM // heat
        );
        Agent agent = new Agent((short) 1, pos, needs);
        agent.actOn(world);
        agent.actOn(world);
        agent.actOn(world);

        assertTrue(agent.position().x() > x);
    }

    @Test
    void if_agent_is_curious_it_steers_toward_scent() {
        int size = 10;
        World world = new TestWorldBuilder(size)
                .withScent(TestWorldBuilder.gradientGrid('W', 0, 1, size))
                .build();


        float x = size - 2;
        float y = (float) size / 2;
        Position pos = new Position(x, y);
        Needs needs = new Needs(
                Needs.MIN, // hunger
                Needs.MAX, // curiosity
                Needs.MIN, // fear
                Needs.MAX, // energy
                Needs.HEAT_OPTIMUM // heat
        );
        Agent agent = new Agent((short) 1, pos, needs);
        agent.actOn(world);
        agent.actOn(world);
        agent.actOn(world);

        assertTrue(agent.position().x() < x);
    }

    @Test
    void if_agent_is_fearful_it_steers_away_from_scent() {
        int size = 10;
        World world = new TestWorldBuilder(size)
                .withScent(TestWorldBuilder.gradientGrid('W', 0, 1, size))
                .build();


        float x = 2;
        float y = (float) size / 2;
        Position pos = new Position(x, y);
        Needs needs = new Needs(
                Needs.MIN, // hunger
                Needs.MIN, // curiosity
                Needs.MAX, // fear
                Needs.MAX, // energy
                Needs.HEAT_OPTIMUM // heat
        );
        Agent agent = new Agent((short) 1, pos, needs);
        agent.actOn(world);
        agent.actOn(world);
        agent.actOn(world);

        assertTrue(agent.position().x() > x);
    }

    @Test
    void if_agent_is_overheated_it_steers_away_from_heat() {
        int size = 10;
        World world = new TestWorldBuilder(size)
                .withHeat(TestWorldBuilder.gradientGrid('S', 0, 1, size))
                .build();


        float x = (float) size / 2;
        float y = size - 2;
        Position pos = new Position(x, y);
        Needs needs = new Needs(
                Needs.MIN, // hunger
                Needs.MIN, // curiosity
                Needs.MIN, // fear
                Needs.MAX, // energy
                Needs.MAX // heat
        );
        Agent agent = new Agent((short) 1, pos, needs);
        agent.actOn(world);
        agent.actOn(world);
        agent.actOn(world);

        assertTrue(agent.position().y() < y);
    }

    @Test
    void if_agent_is_hypothermic_it_steers_towards_heat() {
        int size = 10;
        World world = new TestWorldBuilder(size)
                .withHeat(TestWorldBuilder.gradientGrid('N', 0, 1, size))
                .build();


        float x = (float) size / 2;
        float y = size - 2;
        Position pos = new Position(x, y);
        Needs needs = new Needs(
                Needs.MIN, // hunger
                Needs.MIN, // curiosity
                Needs.MIN, // fear
                Needs.MAX, // energy
                Needs.MIN // heat
        );
        Agent agent = new Agent((short) 1, pos, needs);
        agent.actOn(world);
        agent.actOn(world);
        agent.actOn(world);


        assertTrue(agent.position().y() < y);
    }

    @Test
    void if_conditions_are_perfect_but_agent_is_located_near_northern_world_border_it_steers_south() {
        int size = 10;
        World world = new TestWorldBuilder(size)
                .withSupply(TestWorldBuilder.valueGrid(1, size))
                .withHeat(TestWorldBuilder.valueGrid(1, size))
                .build();


        float x = (float) size / 2;
        float y = 0;
        Position pos = new Position(x, y);
        Needs needs = new Needs(
                Needs.MIN, // hunger
                Needs.MIN, // curiosity
                Needs.MIN, // fear
                Needs.MAX, // energy
                Needs.HEAT_OPTIMUM // heat
        );
        Agent agent = new Agent((short) 1, pos, needs);
        agent.actOn(world);
        agent.actOn(world);
        agent.actOn(world);

        assertTrue(agent.position().y() > y);
    }

    @Test
    void if_conditions_are_perfect_but_agent_is_located_near_southern_world_border_it_steers_north() {
        int size = 10;
        World world = new TestWorldBuilder(size)
                .withSupply(TestWorldBuilder.valueGrid(1, size))
                .withHeat(TestWorldBuilder.valueGrid(1, size))
                .build();


        float x = (float) size / 2;
        float y = size - 0.1f;
        Position pos = new Position(x, y);
        Needs needs = new Needs(
                Needs.MIN, // hunger
                Needs.MIN, // curiosity
                Needs.MIN, // fear
                Needs.MAX, // energy
                Needs.HEAT_OPTIMUM // heat
        );
        Agent agent = new Agent((short) 1, pos, needs);
        agent.actOn(world);
        agent.actOn(world);
        agent.actOn(world);

        assertTrue(agent.position().y() < y);
    }

    @Test
    void if_conditions_are_perfect_but_agent_is_located_near_western_world_border_it_steers_east() {
        int size = 10;
        World world = new TestWorldBuilder(size)
                .withSupply(TestWorldBuilder.valueGrid(1, size))
                .withHeat(TestWorldBuilder.valueGrid(1, size))
                .build();


        float x = 0 + 0.1f;
        float y = (float) size / 2;
        Position pos = new Position(x, y);
        Needs needs = new Needs(
                Needs.MIN, // hunger
                Needs.MIN, // curiosity
                Needs.MIN, // fear
                Needs.MAX, // energy
                Needs.HEAT_OPTIMUM // heat
        );
        Agent agent = new Agent((short) 1, pos, needs);
        agent.actOn(world);
        agent.actOn(world);
        agent.actOn(world);

        assertTrue(agent.position().x() > x);
    }

    @Test
    void if_conditions_are_perfect_but_agent_is_located_near_eastern_world_border_it_steers_west() {
        int size = 10;
        World world = new TestWorldBuilder(size)
                .withSupply(TestWorldBuilder.valueGrid(1, size))
                .withHeat(TestWorldBuilder.valueGrid(1, size))
                .build();


        float x = size - 0.1f;
        float y = (float) size / 2;
        Position pos = new Position(x, y);
        Needs needs = new Needs(
                Needs.MIN, // hunger
                Needs.MIN, // curiosity
                Needs.MIN, // fear
                Needs.MAX, // energy
                Needs.HEAT_OPTIMUM // heat
        );
        Agent agent = new Agent((short) 1, pos, needs);
        agent.actOn(world);
        agent.actOn(world);
        agent.actOn(world);

        assertTrue(agent.position().x() < x);
    }

    // fear -> hunger -> heat -> curiosity
    @Test
    void if_fear_dominates_although_other_needs_are_high_it_avoids_scent() {
        int size = 10;
        World world = new TestWorldBuilder(size)
                .withSupply(TestWorldBuilder.gradientGrid('W', 0, 1, size))
                .withHeat(TestWorldBuilder.gradientGrid('W', 0, 1, size))
                .withScent(TestWorldBuilder.gradientGrid('W', 0, 1, size))
                .build();


        float x = (float) size / 2;
        float y = (float) size / 2;
        Position pos = new Position(x, y);
        Needs needs = new Needs(
                Needs.MAX, // hunger
                Needs.MAX, // curiosity
                Needs.MAX, // fear
                Needs.MAX, // energy
                Needs.MAX // heat
        );
        Agent agent = new Agent((short) 1, pos, needs);
        agent.actOn(world);
        agent.actOn(world);
        agent.actOn(world);

        assertTrue(agent.position().x() > x);
    }

    @Test
    void if_hunger_dominates_although_heat_and_curiosity_are_high_it_steers_towards_supply() {
        int size = 10;
        World world = new TestWorldBuilder(size)
                .withSupply(TestWorldBuilder.gradientGrid('N', 0, 1, size))
                .withHeat(TestWorldBuilder.gradientGrid('N', 0, 1, size))
                .withScent(TestWorldBuilder.gradientGrid('N', 0, 1, size))
                .build();


        float x = (float) size / 2;
        float y = (float) size / 2;
        Position pos = new Position(x, y);
        Needs needs = new Needs(
                Needs.MAX, // hunger
                Needs.MAX, // curiosity
                Needs.MIN, // fear
                Needs.MAX, // energy
                Needs.MAX // heat
        );
        Agent agent = new Agent((short) 1, pos, needs);
        agent.actOn(world);
        agent.actOn(world);
        agent.actOn(world);
        agent.actOn(world);

        assertTrue(agent.position().y() < y);
    }

    @Test
    void if_agent_is_overheated_although_curiosity_is_high_it_steers_towards_heat_optimum() {
        int size = 10;
        World world = new TestWorldBuilder(size)
                .withSupply(TestWorldBuilder.gradientGrid('N', 0, 1, size))
                .withHeat(TestWorldBuilder.gradientGrid('N', 0, 1, size))
                .withScent(TestWorldBuilder.gradientGrid('E', 0, 1, size))
                .build();


        float x = 2;
        float y = 2;
        Position pos = new Position(x, y);
        Needs needs = new Needs(
                Needs.MIN, // hunger
                Needs.MAX, // curiosity
                Needs.MIN, // fear
                Needs.MAX, // energy
                Needs.MAX // heat
        );
        Agent agent = new Agent((short) 1, pos, needs);
        int ticks = 10;
        for(int i = 0; i < ticks; i++) {
            agent.actOn(world);
        }

        assertTrue(agent.position().y() > y);
    }

    @Test
    void if_agent_is_hungry_and_in_vicinity_of_supply_hunger_is_being_satisfied() {
        int size = 10;
        World world = new TestWorldBuilder(size)
                .withHeat(TestWorldBuilder.valueGrid(Needs.HEAT_OPTIMUM, size))
                .withSupply(TestWorldBuilder.valueGrid(1, size))
                .build();


        float x = (float) size / 2;
        float y = (float) size / 2;
        Position pos = new Position(x, y);
        Needs needs = new Needs(
                Needs.MAX, // hunger
                Needs.MIN, // curiosity
                Needs.MIN, // fear
                Needs.MAX, // energy
                Needs.HEAT_OPTIMUM // heat
        );
        Agent agent = new Agent((short) 1, pos, needs);
        agent.actOn(world);

        assertTrue(needs.hunger() < Needs.MAX);
    }

    @Test
    void if_agent_is_overheated_and_in_cool_area_heat_is_reduced() {
        int size = 10;
        World world = new TestWorldBuilder(size)
                .build();


        float x = (float) size / 2;
        float y = (float) size / 2;
        Position pos = new Position(x, y);
        Needs needs = new Needs(
                Needs.MIN, // hunger
                Needs.MIN, // curiosity
                Needs.MIN, // fear
                Needs.MAX, // energy
                Needs.MAX // heat
        );
        Agent agent = new Agent((short) 1, pos, needs);
        agent.actOn(world);

        assertTrue(needs.heat() < Needs.MAX);
    }

    @Test
    void if_agent_is_hypothermic_and_in_warm_area_heat_is_reduced() {
        int size = 10;
        World world = new TestWorldBuilder(size)
                .withHeat(TestWorldBuilder.valueGrid(Needs.MAX, size))
                .build();


        float x = (float) size / 2;
        float y = (float) size / 2;
        Position pos = new Position(x, y);
        Needs needs = new Needs(
                Needs.MIN, // hunger
                Needs.MIN, // curiosity
                Needs.MIN, // fear
                Needs.MAX, // energy
                Needs.MIN // heat
        );
        Agent agent = new Agent((short) 1, pos, needs);
        agent.actOn(world);

        assertTrue(needs.heat() > Needs.MIN);
    }
    */
    /*
    @Test
    void if_light_scent_is_present_and_agent_is_not_fearful_curiosity_increases() {
        int size = 10;
        World world = new TestWorldBuilder(size)
                .withScent(TestWorldBuilder.valueGrid(0.1f, size))
                .build();


        float x = (float) size / 2;
        float y = (float) size / 2;
        Position pos = new Position(x, y);
        Needs needs = new Needs(
                Needs.MIN, // hunger
                Needs.MIN, // curiosity
                Needs.MIN, // fear
                Needs.MAX, // energy
                Needs.HEAT_OPTIMUM // heat
        );
        Agent agent = new Agent((short) 1, pos, needs);
        System.out.println(agent.needs().curiosity());
        agent.actOn(world);

        System.out.println(agent.needs().curiosity());

        assertTrue(needs.curiosity() > Needs.MIN);
    }*/




}
