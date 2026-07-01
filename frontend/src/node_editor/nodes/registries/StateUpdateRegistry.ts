import type {NodeDefinition} from "../NodeDefinitions.ts";

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
                step: 0.001,
                inline: true,
            },
            growth: {
                type: "float",
                default: 0.10,
                min: 0,
                max: 1,
                step: 0.001,
                inline: true,
            },
            stateDecay: {
                type: "float",
                default: 1,
                min: 0,
                max: 1,
                step: 0.001,
                inline: true,
            },
            influenceDecay: {
                type: "float",
                default: 1,
                min: 0,
                max: 1,
                step: 0.001,
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
                step: 0.001,
                inline: true,
            },
            relaxation: {
                type: "float",
                default: 0,
                min: 0,
                max: 1,
                step: 0.001,
                inline: true,
            },
            stateDecay: {
                type: "float",
                default: 1,
                min: 0,
                max: 1,
                step: 0.001,
                inline: true,
            },
            influenceDecay: {
                type: "float",
                default: 1,
                min: 0,
                max: 1,
                step: 0.001,
                inline: true,
            },
        },
    },
} as Record<string, NodeDefinition>
