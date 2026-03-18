export type ParamType =
    | "float"
    | "int"
    | "bool"
    | "select"
    | "boolean"
    | "image"
    | "text"

export type HandleKind =
    | "signal"
    | "compositing"
    | "stateUpdate"
    | "baseUpdate"
    | "layer"
    | "agent"
    | "world"
    | "reference"
    | "heat"
    | "supply"
    | "scent"

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

    kind: HandleKind
    accepts?: HandleKind[]
}

export interface NodeDefinition {
    label: string
    category: string
    deletable: boolean

    inputs: NodeHandle[]
    outputs: NodeHandle[]

    params: Record<string, NodeParam>
}

export const compositingRegistry = {
    clamp: {
        label: "Clamp",
        category: "Compositing",
        deletable: true,

        inputs: [
            {
                id: "input",
                label: "Input",
                connectionCount: 1,
                kind: "compositing",
                accepts: ["compositing"],
            }
        ],

        outputs: [
            {
                id: "output",
                label: "Output",
                connectionCount: 1,
                kind: "compositing",
                accepts: ["compositing", "layer"],
            }
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
            {
                id: "input",
                label : "Input",
                connectionCount: 1,
                kind: "compositing",
                accepts: ["compositing"],
            }
        ],

        outputs: [
            {
                id: "output",
                label : "Output",
                connectionCount: 1,
                kind: "compositing",
                accepts: ["compositing", "layer"],
            }
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
            {
                id: "input",
                label : "Input",
                connectionCount: 1,
                kind: "compositing",
                accepts: ["compositing"],
            }
        ],

        outputs: [
            {
                id: "output",
                label : "Output",
                connectionCount: 1,
                kind: "compositing",
                accepts: ["compositing", "layer"],
            }
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
            {
                id: "input",
                label : "Input",
                connectionCount: 1,
                kind: "compositing",
                accepts: ["compositing"],
            }
        ],

        outputs: [
            {
                id: "output",
                label : "Output",
                connectionCount: 1,
                kind: "compositing",
                accepts: ["compositing", "layer"],
            }
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
            {
                id: "input",
                label : "Input",
                connectionCount: 1,
                kind: "compositing",
                accepts: ["compositing"],
            }
        ],

        outputs: [
            {
                id: "output",
                label : "Output",
                connectionCount: 1,
                kind: "compositing",
                accepts: ["compositing", "layer"],
            },
        ],

        params: {
            reference: {
                type: "select",
                default: "Heat",
                inline: false,
                options: [
                    "Heat",
                    "Supply",
                    "Scent",
                ]
            },
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
            {
                id: "input",
                label : "Input",
                connectionCount: 1,
                kind: "compositing",
                accepts: ["compositing"],
            }
        ],

        outputs: [
            {
                id: "output",
                label : "Output",
                connectionCount: 1,
                kind: "compositing",
                accepts: ["compositing", "layer"],
            }
        ],

        params: {
            reference: {
                type: "select",
                default: "Heat",
                inline: false,
                options: [
                    "Heat",
                    "Supply",
                    "Scent",
                ]
            },

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
} as Record<string, NodeDefinition>

export const signalRegistry = {
    imageSignal: {
        label: "Image",
        category: "Signal",
        deletable: true,

        inputs: [
        ],

        outputs: [
            {
                id: "output",
                label : "Output",
                connectionCount: 10,
                kind: "signal",
                accepts: ["layer"],
            }
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
    fractalNoise: {
        label: "Fractal Noise",
        category: "Signal",
        deletable: true,

        inputs: [
        ],

        outputs: [
            {
                id: "output",
                label : "Output",
                connectionCount: 10,
                kind: "signal",
                accepts: ["layer"],
            }
        ],

        params: {
            seed: {
                type: "text",
                default: "your seed here",
                min: 1,
                max: 16,
                inline: false,
            },
            cellSize: {
                type: "int",
                default: 16,
                min: 2,
                max: 128,
                step: 1,
                inline: true,
            },
            octaves: {
                type: "int",
                default: 1,
                min: 1,
                max: 4,
                step: 1,
                inline: true,
            },
            persistence: {
                type: "float",
                default: 0.5,
                min: 0,
                max: 1,
                step: 0.01,
                inline: false,
            }
        },
    },
    clusteredPatchNoise: {
        label: "Clustered Patch Noise",
        category: "Signal",
        deletable: true,

        inputs: [
        ],

        outputs: [
            {
                id: "output",
                label: "Output",
                connectionCount: 10,
                kind: "signal",
                accepts: ["layer"],
            }
        ],

        params: {
            seed: {
                type: "text",
                default: "your seed here",
                min: 1,
                max: 16,
                inline: false,
            },
            cellSizeBase: {
                type: "int",
                default: 16,
                min: 2,
                max: 128,
                step: 1,
                inline: true,
            },
            cellSizeHoles: {
                type: "int",
                default: 16,
                min: 2,
                max: 128,
                step: 1,
                inline: true,
            },
            octavesBase: {
                type: "int",
                default: 1,
                min: 1,
                max: 4,
                step: 1,
                inline: true,
            },
            octavesHoles: {
                type: "int",
                default: 1,
                min: 1,
                max: 4,
                step: 1,
                inline: true,
            },
            persistenceBase: {
                type: "float",
                default: 0.5,
                min: 0,
                max: 1,
                step: 0.01,
                inline: true,
            },
            persistenceHoles: {
                type: "float",
                default: 0.5,
                min: 0,
                max: 1,
                step: 0.01,
                inline: true,
            },
            patchThreshold: {
                type: "float",
                default: 0,
                min: 0,
                max: 1,
                step: 0.01,
                inline: true,
            },
            patchSoftness: {
                type: "float",
                default: 0,
                min: 0,
                max: 1,
                step: 0.01,
                inline: true,
            },
            holeStrength: {
                type: "float",
                default: 0,
                min: 0,
                max: 1,
                step: 0.01,
                inline: false,
            },
        },
    },
    holeMaskNoise: {
        label: "Hole Mask Noise",
        category: "Signal",
        deletable: true,

        inputs: [
        ],

        outputs: [
            {
                id: "output",
                label : "Output",
                connectionCount: 10,
                kind: "signal",
                accepts: ["layer"],
            }
        ],

        params: {
            seed: {
                type: "text",
                default: "your seed here",
                min: 1,
                max: 16,
                inline: false,
            },
            cellSizeBase: {
                type: "int",
                default: 16,
                min: 2,
                max: 128,
                step: 1,
                inline: true,
            },
            cellSizeHoles: {
                type: "int",
                default: 16,
                min: 2,
                max: 128,
                step: 1,
                inline: true,
            },
            octavesBase: {
                type: "int",
                default: 1,
                min: 1,
                max: 4,
                step: 1,
                inline: true,
            },
            octavesHoles: {
                type: "int",
                default: 1,
                min: 1,
                max: 4,
                step: 1,
                inline: true,
            },
            persistenceBase: {
                type: "float",
                default: 0.5,
                min: 0,
                max: 1,
                step: 0.01,
                inline: true,
            },
            persistenceHoles: {
                type: "float",
                default: 0.5,
                min: 0,
                max: 1,
                step: 0.01,
                inline: true,
            },
            holeThreshold: {
                type: "float",
                default: 0,
                min: 0,
                max: 1,
                step: 0.01,
                inline: true,
            },
            holeStrength: {
                type: "float",
                default: 0,
                min: 0,
                max: 1,
                step: 0.01,
                inline: false,
            },
        },
    },
} as Record<string, NodeDefinition>

export const stateUpdateRegistry = {
    copyStateUpdater: {
        label: "Copy State",
        category: "State Update",
        deletable: true,

        inputs: [
        ],

        outputs: [
            {
                id: "output",
                label : "Output",
                connectionCount: 1,
                kind: "stateUpdate",
                accepts: ["layer"],
            }
        ],

        params: {

        },
    },
    diffusionGrowthUpdater: {
        label: "Diffusion + Growth",
        category: "State Update",
        deletable: true,

        inputs: [
        ],

        outputs: [
            {
                id: "output",
                label : "Output",
                connectionCount: 1,
                kind: "stateUpdate",
                accepts: ["layer"],
            }
        ],

        params: {
            diffusion: {
                type: "float",
                default: 0.05,
                min: 0,
                max: 1,
                step: 0.01,
                inline: true,
            },
            growth: {
                type: "float",
                default: 0.10,
                min: 0,
                max: 1,
                step: 0.01,
                inline: true,
            },
            stateDecay: {
                type: "float",
                default: 1,
                min: 0,
                max: 1,
                step: 0.01,
                inline: true,
            },
            influenceDecay: {
                type: "float",
                default: 1,
                min: 0,
                max: 1,
                step: 0.01,
                inline: true,
            },
        },
    },
    diffusionRelaxationUpdater: {
        label: "Diffusion + Relaxation",
        category: "State Update",
        deletable: true,

        inputs: [
        ],

        outputs: [
            {
                id: "output",
                label : "Output",
                connectionCount: 1,
                kind: "stateUpdate",
                accepts: ["layer"],
            }
        ],

        params: {
            diffusion: {
                type: "float",
                default: 0.05,
                min: 0,
                max: 1,
                step: 0.01,
                inline: true,
            },
            relaxation: {
                type: "float",
                default: 0,
                min: 0,
                max: 1,
                step: 0.01,
                inline: true,
            },
            stateDecay: {
                type: "float",
                default: 1,
                min: 0,
                max: 1,
                step: 0.01,
                inline: true,
            },
            influenceDecay: {
                type: "float",
                default: 1,
                min: 0,
                max: 1,
                step: 0.01,
                inline: true,
            },
        },
    },
} as Record<string, NodeDefinition>

export const baseUpdateRegistry = {
    defaultPotentialUpdater: {
        label: "Default",
        category: "Base Update",
        deletable: true,

        inputs: [
        ],

        outputs: [
            {
                id: "output",
                label : "Output",
                connectionCount: 1,
                kind: "baseUpdate",
                accepts: ["layer"],
            }
        ],

        params: {

        },
    },
} as Record<string, NodeDefinition>

export const layerRegistry = {
    heatLayer: {
        label: "Heat",
        category: "Layer",
        deletable: false,

        inputs: [
            {
                id: "signal",
                label : "Signal",
                connectionCount: 1,
                kind: "layer",
                accepts: ["signal"],
            },
            {
                id: "baseUpdate",
                label : "Base Update",
                connectionCount: 1,
                kind: "layer",
                accepts: ["baseUpdate"],
            },
            {
                id: "compositing",
                label : "Compositing",
                connectionCount: 1,
                kind: "layer",
                accepts: ["compositing"],
            },
            {
                id: "timeBehavior",
                label : "Time Behavior",
                connectionCount: 1,
                kind: "layer",
                accepts: ["timeBehavior"],
            },
        ],

        outputs: [
            {
                id: "output",
                label : "Output",
                connectionCount: 1,
                kind: "heat",
                accepts: ["world"],
            },
        ],

        params: {

        },
    },
    supplyLayer: {
        label: "Supply",
        category: "Layer",
        deletable: false,

        inputs: [
            {
                id: "signal",
                label : "Signal",
                connectionCount: 1,
                kind: "layer",
                accepts: ["signal"],
            },
            {
                id: "baseUpdate",
                label : "Base Update",
                connectionCount: 1,
                kind: "layer",
                accepts: ["baseUpdate"],
            },
            {
                id: "stateUpdate",
                label : "State Update",
                connectionCount: 1,
                kind: "layer",
                accepts: ["stateUpdate"],
            },
            {
                id: "compositing",
                label : "Compositing",
                connectionCount: 1,
                kind: "layer",
                accepts: ["compositing"],
            },
            {
                id: "timeBehavior",
                label : "Time Behavior",
                connectionCount: 1,
                kind: "layer",
                accepts: ["timeBehavior"],
            },
        ],

        outputs: [
            {
                id: "output",
                label : "Output",
                connectionCount: 1,
                kind: "supply",
                accepts: ["world"],
            },
        ],

        params: {

        },
    },
    scentLayer: {
        label: "Scent",
        category: "Layer",
        deletable: false,

        inputs: [
            {
                id: "signal",
                label : "Signal",
                connectionCount: 1,
                kind: "layer",
                accepts: ["signal"],
            },
            {
                id: "baseUpdate",
                label : "Base Update",
                connectionCount: 1,
                kind: "layer",
                accepts: ["baseUpdate"],
            },
            {
                id: "stateUpdate",
                label : "State Update",
                connectionCount: 1,
                kind: "layer",
                accepts: ["stateUpdate"],
            },
            {
                id: "compositing",
                label : "Compositing",
                connectionCount: 1,
                kind: "layer",
                accepts: ["compositing"],
            },
            {
                id: "timebehavior",
                label : "Time Behavior",
                connectionCount: 1,
                kind: "layer",
                accepts: ["timeBehavior"],
            },
        ],

        outputs: [
            {
                id: "output",
                label : "Output",
                connectionCount: 1,
                kind: "scent",
                accepts: ["world"],
            },
        ],

        params: {

        },
    },
} as Record<string, NodeDefinition>

export const worldRegistry = {
    world: {
        label: "World",
        category: "World",
        deletable: false,

        inputs: [
            {
                id: "heat",
                label : "Heat Layer",
                connectionCount: 1,
                kind: "world",
                accepts: ["heat"],
            },
            {
                id: "supply",
                label : "Supply Layer",
                connectionCount: 1,
                kind: "world",
                accepts: ["supply"],
            },
            {
                id: "scent",
                label : "Scent Layer",
                connectionCount: 1,
                kind: "world",
                accepts: ["scent"],
            },
            {
                id: "agent",
                label : "Agents",
                connectionCount: 1,
                kind: "world",
                accepts: ["agent"],
            },
        ],

        outputs: [

        ],

        params: {
            seed: {
                type: "text",
                default: "your seed here",
                min: 1,
                max: 16,
                inline: false,
            },
            width: {
                type: "int",
                default: 256,
                min: 2,
                max: 2048,
                step: 1,
                inline: false,
            },
            height: {
                type: "int",
                default: 256,
                min: 2,
                max: 2048,
                step: 1,
                inline: false,
            },
        },
    },
} as Record<string, NodeDefinition>

export const entityRegistry = {
    agents: {
        label: "Agent",
        category: "Entity",
        deletable: true,

        inputs: [

        ],

        outputs: [
            {
                id: "output",
                label : "Output",
                connectionCount: 1,
                kind: "agent",
                accepts: ["world"],
            },
        ],

        params: {
            amount: {
                type: "int",
                default: 128,
                min: 1,
                max: 2048,
                step: 1,
                inline: false,
            },
            hunger: {
                type: "float",
                default: 0.5,
                min: 0,
                max: 1,
                step: 0.01,
                inline: true,
            },
            heat: {
                type: "float",
                default: 0.5,
                min: 0,
                max: 1,
                step: 0.01,
                inline: true,
            },
            curiosity: {
                type: "float",
                default: 0.5,
                min: 0,
                max: 1,
                step: 0.01,
                inline: true,
            },
            fear: {
                type: "float",
                default: 0.5,
                min: 0,
                max: 1,
                step: 0.01,
                inline: true,
            },
        },
    },
} as Record<string, NodeDefinition>;

export const modifierRegistry = {
    ...signalRegistry,
    ... compositingRegistry,
    ...stateUpdateRegistry,
    ...baseUpdateRegistry,
    ...entityRegistry
}

export const nodeRegistry = {
    ... compositingRegistry,
    ...stateUpdateRegistry,
    ...baseUpdateRegistry,
    ...signalRegistry,
    ...layerRegistry,
    ...worldRegistry,
    ...entityRegistry

} as Record<string, NodeDefinition>;

export type NodeType = keyof typeof nodeRegistry
