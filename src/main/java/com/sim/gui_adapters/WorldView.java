package com.sim.gui_adapters;

import com.sim.snapshot.WorldSnapshot;
public interface WorldView {

    void render(WorldSnapshot worldSnap);

    void updateSnapshot(WorldSnapshot worldSnap);

}
