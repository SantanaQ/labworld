import {nodeRegistry, type NodeType} from "../nodes/NodeDefinitions.ts";
import type {Node} from "@xyflow/react";

export function createNode(type: NodeType, id: string, x: number, y: number, data: any = {}): Node {
    const def = nodeRegistry[type];

    return {
        id,
        type,
        position: { x, y },
        deletable: def.deletable ?? true,
        data,
    };
}