import type {AgentData} from "./decoding/FrameDecoder.ts";

export type LayerName = 'heat' | 'scent' | 'supply' | 'trail' | 'stress';

export interface LayerData {
    width: number;
    height: number;
    data: Uint8Array;
}

export interface WorldDimensions {
    width: number;
    height: number;
}

export interface Agent {
    agentData: AgentData;
    angle: number;
    stretch: number;
}

export class SimData {
    private layers: Record<LayerName, LayerData> = {} as Record<LayerName, LayerData>;
    private agents: Map<number, Agent> = new Map<number, Agent>();
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

    public setAgents(agentData: Array<AgentData>) {
        for (const data of agentData) {
            let agent = this.agents.get(data.id);

            if (!agent) {
                agent = {
                    agentData: data,
                    angle: 0,
                    stretch: 1
                };
                this.agents.set(data.id, agent);
            } else {
                agent.agentData = data;
            }
        }
    }

    public getLayer(name: LayerName) {
        return this.layers[name];
    }

    public getAgents(): Map<number, Agent> {
        return this.agents;
    }


}