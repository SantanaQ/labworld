export type ClampNodeData = {
    min: number
    max: number
}

export type ParamType =
    | "float"
    | "int"
    | "bool"
    | "select"
    | "boolean"

export interface NodeParam {
    type: ParamType
    default: any
    min?: number
    max?: number
    step?: number
    options?: string[]
}

export interface NodeHandle {
    id: string
    label?: string
}

export interface NodeDefinition {
    label: string
    category: string

    inputs: NodeHandle[]
    outputs: NodeHandle[]

    params: Record<string, NodeParam>
}

export const nodeRegistry = {

    clamp: {
        label: "Clamp",
        category: "Compositing",
        deletable: true,

        inputs: [
            { id: "input", label : "Input", connectionCount: 1 }
        ],

        outputs: [
            { id: "output", label : "Output", connectionCount: 1 }
        ],

        params: {
            min: {
                type: "float",
                default: 0,
                step: 0.01,
                inline: true,
            },

            max: {
                type: "float",
                default: 1,
                step: 0.01,
                inline: true,
            }
        }

    }

} as const;

export type NodeType = keyof typeof nodeRegistry
