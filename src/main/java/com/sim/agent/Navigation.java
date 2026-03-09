package com.sim.agent;

import com.sim.layer.WorldLayer;
import com.sim.utils.MathHelpers;

import java.util.Random;

public class Navigation {

    static Velocity towardsTargetValue(WorldLayer layer,
                                       Position position,
                                       float targetVal) {
        float eps = 0.5f;

        float agentX = position.x();
        float agentY = position.y();

        float h = sampleBilinear(layer, agentX, agentY);

        float dx = sampleBilinear(layer, agentX + eps, agentY)
                - sampleBilinear(layer, agentX - eps, agentY);
        float dy = sampleBilinear(layer, agentX, agentY + eps)
                - sampleBilinear(layer, agentX, agentY - eps);

        float direction = Math.signum(targetVal - h);

        Velocity v = new Velocity(dx * direction, dy * direction);

        if (v.length() > 1f) {
            v = v.normalize();
        }

        return v;
    }


    private static float sampleBilinear(WorldLayer layer, float x, float y) {
        int x0 = (int) Math.floor(x);
        int x1 = x0 + 1;
        int y0 = (int) Math.floor(y);
        int y1 = y0 + 1;

        float sx = x - x0;
        float sy = y - y0;

        float v00 = layer.accessibleAtSafe(x0, y0);
        float v10 = layer.accessibleAtSafe(x1, y0);
        float v01 = layer.accessibleAtSafe(x0, y1);
        float v11 = layer.accessibleAtSafe(x1, y1);

        float v0 = MathHelpers.lerp(v00, v10, sx);
        float v1 = MathHelpers.lerp(v01, v11, sx);

        return MathHelpers.lerp(v0, v1, sy);
    }

    static Velocity randomVector(Random random) {
        float angle = random.nextFloat() * MathHelpers.TWO_PI;

        return new Velocity(
                (float)Math.cos(angle),
                (float)Math.sin(angle)
        );
    }


}
