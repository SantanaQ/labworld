import {nodeRegistry, type NodeType} from "./NodeDefinitions.ts";

export function createNodeData(type: NodeType) {
    const def = nodeRegistry[type]

    const data: Record<string, any> = {}

    for (const [key, param] of Object.entries(def.params)) {
        data[key] = param.default
    }

    return data
}