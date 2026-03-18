import type {NodeType} from "./nodes/NodeDefinitions.ts";

interface NodeJSON {
    id: string;
    category: string;
    type: NodeType;
    position: { x: number; y: number };
    data: Record<string, any>;
}

interface EdgeJSON {
    source: string;
    sourceHandle?: string;
    target: string;
    targetHandle?: string;
}

export interface NodeGraphJSON {
    nodes: NodeJSON[];
    edges: EdgeJSON[];
}