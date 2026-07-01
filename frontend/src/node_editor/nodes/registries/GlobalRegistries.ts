import {maxDimension, minDimension, type NodeDefinition} from "../NodeDefinitions.ts";

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
                id: "trail",
                label : "Trail Layer",
                connectionCount: 1,
                kind: "world",
                accepts: ["trail"],
            },
            {
                id: "stress",
                label : "Stress Layer",
                connectionCount: 1,
                kind: "world",
                accepts: ["stress"],
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
                default: maxDimension,
                min: minDimension,
                max: maxDimension,
                step: 1,
                inline: false,
            },
            height: {
                type: "int",
                default: maxDimension,
                min: minDimension,
                max: maxDimension,
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
        deletable: false,

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
                min: 0,
                max: 1024,
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