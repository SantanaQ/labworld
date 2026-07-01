import {maxDimension, minDimension, type NodeDefinition} from "../NodeDefinitions.ts";

export const signalRegistry = {
    image: {
        label: "Image",
        category: "Signal",
        deletable: true,
        requiresDimensions: true,

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
            imageData: {
                type: "image",
                default: null,
                inline: false,
                hideLabel: true,
                requiresDimension: true,
            },
            width: {
                type: "int",
                default: maxDimension,
                min: minDimension,
                max: maxDimension,
                step: 1,
                inline: false,
                hideLabel: true,
                hideInput: true,
            },
            height: {
                type: "int",
                default: maxDimension,
                min: minDimension,
                max: maxDimension,
                step: 1,
                inline: false,
                hideLabel: true,
                hideInput: true,
            },
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
    constantValue: {
        label: "Constant Value",
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
            value: {
                type: "float",
                default: 0.5,
                min: 0,
                max: 1,
                step: 0.01,
                inline: false,
            }
        },
    },
} as Record<string, NodeDefinition>