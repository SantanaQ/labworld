package com.sim.controller;

import com.sim.config.LayerConfig;
import com.sim.layer.LayerID;

import java.util.HashMap;
import java.util.Map;

public class ConfigurationController {

    Map<LayerID, LayerConfig> layerConfigs;

    public ConfigurationController() {
        layerConfigs = new HashMap<>();
        //layerConfigs.put(LayerID.HEAT, new PotentialLayerConfig());


    }



    public void create() {

    }


}
