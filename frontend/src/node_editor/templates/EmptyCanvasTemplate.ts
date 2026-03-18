import type {Node, Edge} from "@xyflow/react";
import {createNode} from "./CreateNode.ts";

export function createEmptyCanvasTemplate(): {
    nodes: Node[];
    edges: Edge[];
} {
    const nodes: Node[] = [
        createNode("heatLayer", "heatLayer", 100, 100),
        createNode("supplyLayer", "supplyLayer", 100, 350),
        createNode("scentLayer", "scentLayer", 100, 600),
        createNode("agents", "agents", 100, 850, {
            amount: 128,
            hunger: 0.5,
            heat: 0.5,
            curiosity: 1,
            fear: 0,
        }),

        createNode("world", "world", 400, 300, {
            seed: "create-with-this",
            width: 256,
            height: 256,
        }),
    ];

    const edges: Edge[] = [
        {
            id: "e_heat_world",
            source: "heatLayer",
            sourceHandle: "output",
            target: "world",
            targetHandle: "heat",
        },
        {
            id: "e_supply_world",
            source: "supplyLayer",
            sourceHandle: "output",
            target: "world",
            targetHandle: "supply",
        },
        {
            id: "e_scent_world",
            source: "scentLayer",
            sourceHandle: "output",
            target: "world",
            targetHandle: "scent",
        },
        {
            id: "e_agent_world",
            source: "agents",
            sourceHandle: "output",
            target: "world",
            targetHandle: "agent",
        },
    ];

    return { nodes, edges };
}