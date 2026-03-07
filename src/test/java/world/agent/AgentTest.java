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
                {0, 0, 0, 1, 0},
                {0, 0, 1, 1, 1},
                {0, 0, 0, 1, 0},
                {0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0}
        };

        Needs onlyHunger = new Needs(1, 0, 0, 0, 0.5f);
        Agent agent = new Agent(new Position(2,2), onlyHunger);
        World world = new TestWorldBuilder()
                .food(upperRight)
                .heat(TestWorldBuilder.empty5x5())
                .build();

        Position prePos = agent.position();
        agent.actOn(world);
        Position postPos = agent.position();
        assertTrue(postPos.x() > prePos.x());
        assertTrue(postPos.y() < prePos.y());
    }


}
