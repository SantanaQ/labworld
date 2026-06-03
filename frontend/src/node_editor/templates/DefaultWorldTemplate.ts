import type {Node, Edge} from "@xyflow/react";
import {createNode} from "./CreateNode.ts";



export function createDefaultWorldTemplate(): { nodes: Node[]; edges: Edge[] } {
    const nodes: Node[] = [
        // --- Heat Layer ---
        createNode("fractalNoise", "heatSignal", 100, -500, {
            seed: "create-with-this",
            cellSize: 48,
            octaves: 2,
            persistence: 0.4,
        }),

        createNode("defaultPotentialUpdater", "heatBaseUpdate", -50, -300),

        createNode("softThreshold", "heatCompositing", -50, -200, {
            threshold: 0.4,
            softness: 0.15,
        }),

        createNode("heatLayer", "heatLayer", 500, 50),

        // --- Supply Layer ---
        createNode("clusteredPatchNoise", "supplySignal", -50, 50, {
            seed: "create-with-this",
            cellSizeBase: 5,
            cellSizeHoles: 50,
            octavesBase: 2,
            octavesHoles: 2,
            persistenceBase: 0.5,
            persistenceHoles: 0.6,
            patchThreshold: 0.37,
            patchSoftness: 0.02,
            holeStrength: 0.8,
        }),
        createNode("defaultPotentialUpdater", "supplyBaseUpdate", 220, 275),
        createNode("diffusionGrowthUpdater", "supplyStateUpdate", -50, 480, {
            diffusion: 0.015,
            growth: 0.2,
            stateDecay: 0.998,
            influenceDecay: 0.999,
        }),
        createNode("suitabilityMask", "supplyCompositing", -30, 680, { min: 0.55, max: 0.65 }),
        createNode("supplyLayer", "supplyLayer", 500, 300),

        // --- Scent Layer ---
        createNode("scentLayer", "scentLayer", 500, 550),

        // --- Trail Layer ---
        createNode("defaultPotentialUpdater", "trailBaseUpdate", 250, 800),
        createNode("diffusionRelaxationUpdater", "trailStateUpdate", 150, 900, {
            diffusion: 0.05,
            relaxation: 0,
            stateDecay: 0.924,
            influenceDecay: 0.25,
        }),
        createNode("trailLayer", "trailLayer", 500, 800),

        // --- Stress Layer ---
        createNode("defaultPotentialUpdater", "stressBaseUpdate", 250, 1100),
        createNode("diffusionRelaxationUpdater", "stressStateUpdate", 150, 1200, {
            diffusion: 0.15,
            relaxation: 0,
            stateDecay: 0.995,
            influenceDecay: 0.25,
        }),
        createNode("stressLayer", "stressLayer", 500, 1050),


        // --- Agents ---
        createNode("agents", "agents", 500, 1300, {
            amount: 64,
            hunger: 0.5,
            heat: 0.5,
            curiosity: 1,
            fear: 0,
        }),

        // --- World ---
        createNode("world", "world", 800, 400, {
            seed: "create-with-this",
            width: 256,
            height: 256,
        }),
    ];

    const edges: Edge[] = [
        // --- Heat Layer connections ---
        { id: "e_heatSignal_heatLayer", source: "heatSignal", sourceHandle: "output", target: "heatLayer", targetHandle: "signal" },
        { id: "e_heatBaseUpdate_heatLayer", source: "heatBaseUpdate", sourceHandle: "output", target: "heatLayer", targetHandle: "baseUpdate" },
        { id: "e_heatCompositing_heatLayer", source: "heatCompositing", sourceHandle: "output", target: "heatLayer", targetHandle: "compositing" },

        // --- Scent Layer connections ---


        // --- Trail Layer connections ---
        { id: "e_trailBaseUpdate_scentLayer", source: "trailBaseUpdate", sourceHandle: "output", target: "trailLayer", targetHandle: "baseUpdate" },
        { id: "e_trailStateUpdate_scentLayer", source: "trailStateUpdate", sourceHandle: "output", target: "trailLayer", targetHandle: "stateUpdate" },

        // --- Stress Layer connections ---
        { id: "e_stressBaseUpdate_scentLayer", source: "stressBaseUpdate", sourceHandle: "output", target: "stressLayer", targetHandle: "baseUpdate" },
        { id: "e_stressStateUpdate_scentLayer", source: "stressStateUpdate", sourceHandle: "output", target: "stressLayer", targetHandle: "stateUpdate" },

        // --- Supply Layer connections ---
        { id: "e_supplySignal_supplyLayer", source: "supplySignal", sourceHandle: "output", target: "supplyLayer", targetHandle: "signal" },
        { id: "e_supplyBaseUpdate_supplyLayer", source: "supplyBaseUpdate", sourceHandle: "output", target: "supplyLayer", targetHandle: "baseUpdate" },
        { id: "e_supplyStateUpdate_supplyLayer", source: "supplyStateUpdate", sourceHandle: "output", target: "supplyLayer", targetHandle: "stateUpdate" },
        { id: "e_supplyCompositing2_supplyCompositing", source: "supplyCompositing", sourceHandle: "output", target: "supplyLayer", targetHandle: "compositing" },

        // --- Layer → World ---
        { id: "e_heatLayer_world", source: "heatLayer", sourceHandle: "output", target: "world", targetHandle: "heat" },
        { id: "e_supplyLayer_world", source: "supplyLayer", sourceHandle: "output", target: "world", targetHandle: "supply" },
        { id: "e_scentLayer_world", source: "scentLayer", sourceHandle: "output", target: "world", targetHandle: "scent" },
        { id: "e_trailLayer_world", source: "trailLayer", sourceHandle: "output", target: "world", targetHandle: "trail" },
        { id: "e_stressLayer_world", source: "stressLayer", sourceHandle: "output", target: "world", targetHandle: "stress" },

        // --- Agents → World ---
        { id: "e_agents_world", source: "agents", sourceHandle: "output", target: "world", targetHandle: "agent" },
    ];

    return { nodes, edges };
}