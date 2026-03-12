package com.sim.layer;
import com.sim.layer.step.LayerReferenceStep;
import com.sim.layer.step.LayerStep;

import java.util.*;

public class LayerDependencyGraph {

    private final Map<LayerID, LayerRuntime> runtimes;

    public LayerDependencyGraph(Map<LayerID, LayerRuntime> runtimes) {
        this.runtimes = runtimes;
    }

    public List<LayerID> topologicalSort() {
        List<LayerID> order = new ArrayList<>();
        Set<LayerID> visited = new HashSet<>();
        Set<LayerID> visiting = new HashSet<>();

        for (LayerID id : runtimes.keySet()) {
            visit(id, visited, visiting, order);
        }

        return order;
    }

    private void visit(LayerID id,
                       Set<LayerID> visited,
                       Set<LayerID> visiting,
                       List<LayerID> order) {
        if (visited.contains(id)) return;

        if (visiting.contains(id))
            throw new IllegalStateException("Cycle detected at Layer: " + id);

        visiting.add(id);

        LayerRuntime runtime = runtimes.get(id);
        if(runtime != null) {
            for(LayerID dep : dependencies(runtime)) {
                if (!visited.contains(dep)) {
                    visit(dep, visited, visiting, order);
                }
            }
        }

        visiting.remove(id);
        visited.add(id);
        order.add(id);
    }

    private Set<LayerID> dependencies(LayerRuntime runtime) {
        Set<LayerID> deps = new HashSet<>();
        for(LayerStep step : runtime.layerSteps()) {
            if(step instanceof LayerReferenceStep refStep) {
                deps.add(refStep.dependencyId());
            }
        }
        return deps;
    }

    public void propagateDirty(LayerID id) {
        LayerRuntime runtime = runtimes.get(id);
        if(runtime == null) return;

        for(LayerID dep : findDependents(id)) {
            LayerRuntime depRuntime = runtimes.get(dep);
            if(depRuntime != null && !depRuntime.config().isDirty()) {
                depRuntime.config().markDirty();
                propagateDirty(dep);
            }
        }
    }

    private List<LayerID> findDependents(LayerID id) {
        List<LayerID> dependents = new ArrayList<>();
        for(var entry : runtimes.entrySet()) {
            LayerRuntime runtime = entry.getValue();
            for(LayerStep step : runtime.layerSteps()) {
                if(step instanceof LayerReferenceStep refStep
                        && refStep.dependencyId() == id) {
                    dependents.add(entry.getKey());
                    break;
                }
            }
        }
        return dependents;
    }
}
