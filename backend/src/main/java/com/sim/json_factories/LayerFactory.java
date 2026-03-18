package com.sim.json_factories;

import com.api.resource.EditorGraphNode;

public interface LayerFactory<T> {

    public abstract T create(EditorGraphNode node);

}