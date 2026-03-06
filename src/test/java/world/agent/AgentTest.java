package world.agent;

import com.sim.world.World;
import com.sim.world.agent.Agent;
import com.sim.world.agent.Position;
import org.junit.jupiter.api.Test;
import world.TestWorldBuilder;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class AgentTest {

    float[][] empty5x5 = new float[][] {
            {0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0}
    };

    /*@Test
    void test() {

        float[][] upperRight = new float[][] {
                {0, 0, 0, 0, 1},
                {0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0}
        };

        Agent agent = new Agent(new Position(2,2));
        World world = new TestWorldBuilder()
                .food(upperRight)
                .heat(empty5x5)
                .agent(agent)
                .build();

        Position prePos = agent.position();
        agent.actOn(world);
        Position postPos = agent.position();
        assertTrue(postPos.x() > prePos.x());
        assertTrue(postPos.y() < prePos.y());
    }*/


}
