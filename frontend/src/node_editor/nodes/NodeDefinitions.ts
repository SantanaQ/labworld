import {compositingRegistry} from "./registries/CompositingRegistry.ts";
import {signalRegistry} from "./registries/SignalRegistry.ts";
import {stateUpdateRegistry} from "./registries/StateUpdateRegistry.ts";
import {baseUpdateRegistry} from "./registries/BaseUpdateRegistry.ts";
import {layerRegistry} from "./registries/LayerRegistry.ts";
import {entityRegistry, worldRegistry} from "./registries/GlobalRegistries.ts";

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
    hideInput?: boolean
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
    requiresDimensions?: boolean

    inputs: NodeHandle[]
    outputs: NodeHandle[]

    params: Record<string, NodeParam>
}

export const minDimension = 64;
export const maxDimension = 256;
export const selectableLayers = ["Heat", "Supply", "Trail", "Stress"];

export const modifierRegistry = {
    ...signalRegistry,
    ...compositingRegistry,
    ...stateUpdateRegistry,
    ...baseUpdateRegistry
} as Record<string, NodeDefinition>;

export const nodeRegistry = {
    ...compositingRegistry,
    ...stateUpdateRegistry,
    ...baseUpdateRegistry,
    ...signalRegistry,
    ...layerRegistry,
    ...worldRegistry,
    ...entityRegistry

} as Record<string, NodeDefinition>;

export type NodeType = keyof typeof nodeRegistry
