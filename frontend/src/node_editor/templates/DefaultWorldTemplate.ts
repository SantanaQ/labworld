import type {Node, Edge} from "@xyflow/react";
import {createNode} from "./CreateNode.ts";



export function createDefaultWorldTemplate(): { nodes: Node[]; edges: Edge[] } {
    const nodes: Node[] = [
        // --- Heat Layer ---
        createNode("fractalNoise", "heatSignal", -100, -500, {
            seed: "create-with-this",
            cellSize: 30,
            octaves: 2,
            persistence: 0.4,
        }),

        createNode("defaultPotentialUpdater", "heatBaseUpdate", 100, -300),

        createNode("softThreshold", "heatCompositing", -50, -200, {
            threshold: 0.2,
            softness: 0.1,
        }),

        createNode("heatLayer", "heatLayer", 500, 50),

        // --- Supply Layer ---
        createNode("clusteredPatchNoise", "supplySignal", -50, 50, {
            seed: "create-with-this",
            cellSizeBase: 18,
            cellSizeHoles: 8,
            octavesBase: 3,
            octavesHoles: 2,
            persistenceBase: 0.5,
            persistenceHoles: 0.6,
            patchThreshold: 0.55,
            patchSoftness: 0.08,
            holeStrength: 0.35,
        }),
        createNode("defaultPotentialUpdater", "supplyBaseUpdate", 200, 275),
        createNode("diffusionGrowthUpdater", "supplyStateUpdate", -50, 510, {
            diffusion: 0.05,
            growth: 0.12,
            stateDecay: 0.998,
            influenceDecay: 0.4,
        }),
        createNode("softThreshold", "supplyCompositing1", -500, 550, { threshold: 0.55, softness: 0.08 }),
        createNode("suitabilityMask", "supplyCompositing2", -300, 650, { min: 0.15, max: 0.35 }),
        createNode("suitabilityDecay", "supplyCompositing3", -50, 750, { min: 0.15, max: 0.65, decay: 0.03 }),
        createNode("supplyLayer", "supplyLayer", 500, 300),

        // --- Scent Layer ---
        createNode("defaultPotentialUpdater", "scentBaseUpdate", 150, 850),
        createNode("diffusionRelaxationUpdater", "scentStateUpdate", 150, 1000, {
            diffusion: 0.5,
            relaxation: 0,
            stateDecay: 0.995,
            influenceDecay: 0.3,
        }),
        createNode("scentLayer", "scentLayer", 500, 550),


        // --- Agents ---
        createNode("agents", "agents", 500, 800, {
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
        { id: "e_scentBaseUpdate_scentLayer", source: "scentBaseUpdate", sourceHandle: "output", target: "scentLayer", targetHandle: "baseUpdate" },
        { id: "e_scentStateUpdate_scentLayer", source: "scentStateUpdate", sourceHandle: "output", target: "scentLayer", targetHandle: "stateUpdate" },

        // --- Supply Layer connections ---
        { id: "e_supplySignal_supplyLayer", source: "supplySignal", sourceHandle: "output", target: "supplyLayer", targetHandle: "signal" },
        { id: "e_supplyBaseUpdate_supplyLayer", source: "supplyBaseUpdate", sourceHandle: "output", target: "supplyLayer", targetHandle: "baseUpdate" },
        { id: "e_supplyStateUpdate_supplyLayer", source: "supplyStateUpdate", sourceHandle: "output", target: "supplyLayer", targetHandle: "stateUpdate" },
        { id: "e_supplyCompositing1_supplyCompositing2", source: "supplyCompositing1", sourceHandle: "output", target: "supplyCompositing2", targetHandle: "input" },
        { id: "e_supplyCompositing2_supplyCompositing3", source: "supplyCompositing2", sourceHandle: "output", target: "supplyCompositing3", targetHandle: "input" },
        { id: "e_supplyCompositing2_refLayer", source: "supplyCompositing2", sourceHandle: "reference", target: "heatLayer", targetHandle: "reference" },
        { id: "e_supplyCompositing3_supplyLayer", source: "supplyCompositing3", sourceHandle: "output", target: "supplyLayer", targetHandle: "compositing" },
        { id: "e_supplyCompositing3_refLayer", source: "supplyCompositing3", sourceHandle: "reference", target: "heatLayer", targetHandle: "reference" },

        // --- Layer → World ---
        { id: "e_heatLayer_world", source: "heatLayer", sourceHandle: "output", target: "world", targetHandle: "heat" },
        { id: "e_supplyLayer_world", source: "supplyLayer", sourceHandle: "output", target: "world", targetHandle: "supply" },
        { id: "e_scentLayer_world", source: "scentLayer", sourceHandle: "output", target: "world", targetHandle: "scent" },

        // --- Agents → World ---
        { id: "e_agents_world", source: "agents", sourceHandle: "output", target: "world", targetHandle: "agent" },
    ];

    return { nodes, edges };
}