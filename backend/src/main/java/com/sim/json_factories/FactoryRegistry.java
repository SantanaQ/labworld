package com.sim.json_factories;

import com.api.resource.EditorGraphNode;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class FactoryRegistry {

    private final Map<String, LayerFactory<?>> registry = new HashMap<>();

    public FactoryRegistry() {
        registry.put("Compositing", new LayerStepFactory());
        registry.put("Signal", new SignalFactory());
        registry.put("Base Update", new PotentialUpdateFactory());
        registry.put("State Update", new StateUpdateFactory());
        registry.put("Time Behavior", new TimeBehaviorFactory());
    }

    public LayerFactory<?> get(String category) {
        return registry.get(category);
    }

}
