import type {NodeDefinition} from "../NodeDefinitions.ts";

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
    trailLayer: {
        label: "Trails",
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
    stressLayer: {
        label: "Stress",
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