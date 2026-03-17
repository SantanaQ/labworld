export type ParamType =
    | "float"
    | "int"
    | "bool"
    | "select"
    | "boolean"
    | "image"

export interface NodeParam {
    type: ParamType
    default: any
    min?: number
    max?: number
    step?: number
    inline?: boolean
    options?: string[]
    hideLabel?: boolean
}

export interface NodeHandle {
    id: string
    label?: string
    connectionCount : number
}

export interface NodeDefinition {
    label: string
    category: string
    deletable: boolean

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
                min: 0,
                max: 1,
                step: 0.01,
                inline: true,
            },

            max: {
                type: "float",
                default: 1,
                min: 0,
                max: 1,
                step: 0.01,
                inline: true,
            },
        },
    },
    normalize: {
        label: "Normalize",
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
                min: 0,
                max: 1,
                step: 0.01,
                inline: true,
            },

            max: {
                type: "float",
                default: 1,
                min: 0,
                max: 1,
                step: 0.01,
                inline: true,
            },
        },
    },
    binaryThreshold: {
        label: "Binary Threshold",
        category: "Compositing",
        deletable: true,

        inputs: [
            { id: "input", label : "Input", connectionCount: 1 }
        ],

        outputs: [
            { id: "output", label : "Output", connectionCount: 1 }
        ],

        params: {
            threshold: {
                type: "float",
                default: 0,
                min: 0,
                max: 1,
                step: 0.01,
                inline: true,
            },
        },
    },
    softThreshold: {
        label: "Soft Threshold",
        category: "Compositing",
        deletable: true,

        inputs: [
            { id: "input", label : "Input", connectionCount: 1 }
        ],

        outputs: [
            { id: "output", label : "Output", connectionCount: 1 }
        ],

        params: {
            threshold: {
                type: "float",
                default: 0,
                min: 0,
                max: 1,
                step: 0.01,
                inline: false,
            },
            softness: {
                type: "float",
                default: 0,
                min: 0,
                max: 1,
                step: 0.01,
                inline: false,
            },
        },
    },
    suitabilityMask: {
        label: "Suitability Mask",
        category: "Compositing",
        deletable: true,

        inputs: [
            { id: "input", label : "Input", connectionCount: 1 }
        ],

        outputs: [
            { id: "reference", label : "Reference Layer", connectionCount: 1 },
            { id: "output", label : "Output", connectionCount: 1 }
        ],

        params: {
            min: {
                type: "float",
                default: 0,
                min: 0,
                max: 1,
                step: 0.01,
                inline: true,
            },

            max: {
                type: "float",
                default: 1,
                min: 0,
                max: 1,
                step: 0.01,
                inline: true,
            },
        },
    },
    suitabilityDecay: {
        label: "Suitability Decay",
        category: "Compositing",
        deletable: true,

        inputs: [
            { id: "input", label : "Input", connectionCount: 1 }
        ],

        outputs: [
            { id: "reference", label : "Reference Layer", connectionCount: 1 },
            { id: "output", label : "Output", connectionCount: 1 }
        ],

        params: {
            min: {
                type: "float",
                default: 0,
                min: 0,
                max: 1,
                step: 0.01,
                inline: true,
            },

            max: {
                type: "float",
                default: 1,
                min: 0,
                max: 1,
                step: 0.01,
                inline: true,
            },
            decay: {
                type: "float",
                default: 1,
                min: 0,
                max: 1,
                step: 0.001,
                inline: false,
            },
        },
    },
    imageSignal: {
        label: "Image",
        category: "Signal",
        deletable: true,

        inputs: [
        ],

        outputs: [
            { id: "output", label : "Output", connectionCount: 1 }
        ],

        params: {
            image: {
                type: "image",
                default: null,
                inline: false,
                hideLabel: true,
            }
        },
    },


} as Record<string, NodeDefinition>;

export type NodeType = keyof typeof nodeRegistry
