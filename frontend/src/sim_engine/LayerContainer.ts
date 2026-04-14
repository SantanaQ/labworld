import type {AgentData} from "./FrameDecoder.ts";

export type LayerName = 'heat' | 'scent' | 'supply';

export interface LayerData {
    width: number;
    height: number;
    data: Uint8Array;
}

export interface WorldDimensions {
    width: number;
    height: number;
}

export class LayerContainer {
    private layers: Record<LayerName, LayerData> = {} as Record<LayerName, LayerData>;
    private agents: Array<AgentData> = []
    private dimensions: WorldDimensions = {} as WorldDimensions;

    public setLayer(name: LayerName, width: number, height: number, data?: Uint8Array) {
        if(!this.layers[name]) {
            this.layers[name] = {
                width,
                height,
                data: data ?? new Uint8Array(width * height),
            };
            this.dimensions.width = width;
            this.dimensions.height = height;
            console.log("SET LAYER", name, width, height);
        }
    }

    public setAgents(agents: Array<AgentData>) {
        this.agents = agents;
    }

    public getLayer(name: LayerName) {
        return this.layers[name];
    }

    public getAgents(): Array<AgentData> {
        return this.agents;
    }


}