import {type NodeDefinition, selectableLayers} from "../NodeDefinitions.ts";

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
                options: selectableLayers,
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
                options: selectableLayers,
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