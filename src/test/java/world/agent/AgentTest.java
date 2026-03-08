package world.agent;

import com.sim.world.World;
import com.sim.agent.Agent;
import com.sim.agent.Needs;
import com.sim.agent.Position;
import org.junit.jupiter.api.Test;
import world.TestWorldBuilder;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class AgentTest {

    @Test
    void agent_adjusts_positioning_towards_food_if_in_need() {

        float[][] upperRight = new float[][] {
                {0, 1, 1},
                {0, 0, 1},
                {0, 0, 0}
        };

        Needs onlyHunger = new Needs(
                Needs.MAX,
                Needs.MIN,
                Needs.MIN,
                Needs.MAX,
                Needs.HEAT_OPTIMUM);
        Agent agent = new Agent(new Position(1,1), onlyHunger);
        World world = new TestWorldBuilder()
                .food(upperRight)
                .build();

        Position prePos = agent.position();
        agent.actOn(world);
        Position postPos = agent.position();

        assertTrue(postPos.x() > prePos.x());
        assertTrue(postPos.y() < prePos.y());
    }

    @Test
    void agent_adjusts_positioning_towards_heat_if_low_temperature() {

        float[][] bottomLeft = new float[][] {
                {0, 0, 0},
                {1, 0, 0},
                {1, 1, 0}
        };

        Needs lowHeat = new Needs(
                Needs.MIN,
                Needs.MIN,
                Needs.MIN,
                Needs.MAX,
                Needs.MIN);
        Agent agent = new Agent(new Position(1,1), lowHeat);
        World world = new TestWorldBuilder()
                .heat(bottomLeft)
                .build();

        Position prePos = agent.position();
        agent.actOn(world);
        Position postPos = agent.position();

        assertTrue(postPos.x() < prePos.x());
        assertTrue(postPos.y() > prePos.y());
    }

    @Test
    void agent_adjusts_positioning_away_from_heat_if_high_temperature() {

        float[][] bottomLeft = new float[][] {
                {1, 1, 1},
                {0, 1, 1},
                {0, 0, 1}
        };

        Needs highHeat = new Needs(
                Needs.MIN,
                Needs.MIN,
                Needs.MIN,
                Needs.MAX,
                Needs.MAX);
        Agent agent = new Agent(new Position(1,1), highHeat);
        World world = new TestWorldBuilder()
                .heat(bottomLeft)
                .build();

        Position prePos = agent.position();
        agent.actOn(world);
        Position postPos = agent.position();

        assertTrue(postPos.x() < prePos.x());
        assertTrue(postPos.y() > prePos.y());
    }


    @Test
    void agent_adjusts_positioning_towards_scent_if_high_curiosity() {

        float[][] bottomLeft = new float[][] {
                {0, 0, 0},
                {1, 0, 0},
                {1, 1, 0}
        };

        Needs onlyCurious = new Needs(
                Needs.MIN,
                Needs.MAX,
                Needs.MIN,
                Needs.MAX,
                Needs.HEAT_OPTIMUM);
        Agent agent = new Agent(new Position(1,1), onlyCurious);
        World world = new TestWorldBuilder()
                .scent(bottomLeft)
                .build();

        Position prePos = agent.position();
        agent.actOn(world);
        Position postPos = agent.position();
        assertTrue(postPos.x() < prePos.x());
        assertTrue(postPos.y() > prePos.y());
    }

    @Test
    void agent_adjusts_positioning_away_from_scent_if_high_fear() {

        float[][] bottomLeft = new float[][] {
                {0, 0, 0},
                {1, 0, 0},
                {1, 1, 0}
        };

        Needs highFear = new Needs(
                Needs.MIN,
                Needs.MIN,
                Needs.MAX,
                Needs.MAX,
                Needs.HEAT_OPTIMUM);
        Agent agent = new Agent(new Position(1,1), highFear);
        World world = new TestWorldBuilder()
                .scent(bottomLeft)
                .build();

        Position prePos = agent.position();
        agent.actOn(world);
        Position postPos = agent.position();
        assertTrue(postPos.x() > prePos.x());
        assertTrue(postPos.y() < prePos.y());
    }


}
