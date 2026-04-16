package com.sim.agent;

import com.sim.layer.WorldLayer;
import com.sim.utils.MathHelpers;

import java.util.Random;

public class Navigation {

    static Vector navigateAllNeighbors(WorldLayer layer,
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

        Vector v = new Vector(dx * direction, dy * direction);

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

        float v00 = layer.valueAt(x0, y0);
        float v10 = layer.valueAt(x1, y0);
        float v01 = layer.valueAt(x0, y1);
        float v11 = layer.valueAt(x1, y1);

        float v0 = MathHelpers.lerp(v00, v10, sx);
        float v1 = MathHelpers.lerp(v01, v11, sx);

        return MathHelpers.lerp(v0, v1, sy);
    }

    static Vector randomVector(Random random) {
        float angle = random.nextFloat() * MathHelpers.TWO_PI;

        return new Vector(
                (float)Math.cos(angle),
                (float)Math.sin(angle)
        );
    }


    static Vector navigateWithRays(WorldLayer layer,
                                   Position position,
                                   Vector forward,
                                   float targetVal,
                                   float rayDistance) {

        float angleOffset = 0.5f; // ~30°
        int steps = 5;

        Vector left = forward.copy().rotate(-angleOffset);
        Vector right = forward.copy().rotate(angleOffset);

        float centerVal = sampleRay(layer, position.x(), position.y(), forward, rayDistance, steps);
        float leftVal   = sampleRay(layer, position.x(), position.y(), left, rayDistance, steps);
        float rightVal  = sampleRay(layer, position.x(), position.y(), right, rayDistance, steps);

        float direction = Math.signum(targetVal - centerVal);

        float errorCenter = Math.abs(targetVal - centerVal);
        float errorLeft   = Math.abs(targetVal - leftVal);
        float errorRight  = Math.abs(targetVal - rightVal);

        Vector bestDir = forward;
        float bestError = errorCenter;

        if (errorLeft < bestError) {
            bestError = errorLeft;
            bestDir = left;
        }

        if (errorRight < bestError) {
            bestError = errorRight;
            bestDir = right;
        }

        return bestDir.normalize().scale(direction);
    }


    static float sampleRay(WorldLayer layer, float startX, float startY,
                           Vector dir, float distance, int steps) {

        float sum = 0f;

        for (int i = 1; i <= steps; i++) {
            float t = (i / (float) steps) * distance;

            float x = startX + dir.vx() * t;
            float y = startY + dir.vy() * t;

            sum += sampleBilinear(layer, x, y);
        }

        return sum / steps;
    }


}
