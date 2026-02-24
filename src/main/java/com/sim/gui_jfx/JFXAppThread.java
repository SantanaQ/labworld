package com.sim.gui_jfx;

import com.sim.gui_adapters.ThreadScheduler;
import javafx.application.Platform;

public class JFXAppThread  implements ThreadScheduler {

    @Override
    public void schedule(Runnable runnable) {
        Platform.runLater(runnable);
    }

}
