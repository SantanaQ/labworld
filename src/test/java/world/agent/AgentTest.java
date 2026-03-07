package world.agent;

import com.sim.world.World;
import com.sim.world.agent.Agent;
import com.sim.world.agent.Needs;
import com.sim.world.agent.Position;
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

        Needs onlyHunger = new Needs(1, 0, 0, 1, 0.5f);
        Agent agent = new Agent(new Position(1,1), onlyHunger);
        World world = new TestWorldBuilder()
                .food(upperRight)
                .heat(TestWorldBuilder.empty3x3())
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

        Needs lowHeat = new Needs(0, 0, 0, 1, 0);
        Agent agent = new Agent(new Position(1,1), lowHeat);
        World world = new TestWorldBuilder()
                .food(TestWorldBuilder.empty3x3())
                .heat(bottomLeft)
                .build();

        Position prePos = agent.position();
        agent.actOn(world);
        Position postPos = agent.position();

        assertTrue(postPos.x() < prePos.x());
        assertTrue(postPos.y() > prePos.y());
    }

    @Test
    void agent_adjusts_positioning_towards_heat_if_low_temperature() {

        float[][] bottomLeft = new float[][] {
                {0, 0, 0},
                {1, 0, 0},
                {1, 1, 0}
        };

        Needs lowHeat = new Needs(0, 0, 0, 1, 0);
        Agent agent = new Agent(new Position(1,1), lowHeat);
        World world = new TestWorldBuilder()
                .food(TestWorldBuilder.empty3x3())
                .heat(bottomLeft)
                .build();

        Position prePos = agent.position();
        agent.actOn(world);
        Position postPos = agent.position();

        assertTrue(postPos.x() < prePos.x());
        assertTrue(postPos.y() > prePos.y());
    }


}
