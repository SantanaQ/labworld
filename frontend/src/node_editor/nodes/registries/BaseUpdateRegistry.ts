import type {NodeDefinition} from "../NodeDefinitions.ts";

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