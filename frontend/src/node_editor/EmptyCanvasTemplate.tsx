import type {Node, Edge} from "@xyflow/react";
import { nodeRegistry, type NodeType } from "./nodes/NodeDefinitions";
import {createNodeData} from "./nodes/NodeFactory.ts";

function createNode(type: NodeType, id: string, x: number, y: number): Node {
    const def = nodeRegistry[type];

    return {
        id,
        type,
        position: { x, y },
        deletable: def.deletable ?? true,
        data: createNodeData(type),
    };
}

export function createEmptyWorldTemplate(): {
    nodes: Node[];
    edges: Edge[];
} {
    const nodes: Node[] = [
        createNode("heatLayer", "heat", 100, 100),
        createNode("supplyLayer", "supply", 100, 350),
        createNode("scentLayer", "scent", 100, 600),
        createNode("agents", "agents", 100, 850),

        createNode("world", "world", 400, 300),
    ];

    const edges: Edge[] = [
        {
            id: "e_heat_world",
            source: "heat",
            sourceHandle: "output",
            target: "world",
            targetHandle: "heat",
        },
        {
            id: "e_supply_world",
            source: "supply",
            sourceHandle: "output",
            target: "world",
            targetHandle: "supply",
        },
        {
            id: "e_scent_world",
            source: "scent",
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