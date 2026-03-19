export type LayerName = 'heat' | 'scent' | 'supply';

export interface LayerData {
    width: number;
    height: number;
    data: Float32Array;
}

export interface AgentData {
    x: number;
    y: number;
    vx: number;
    vy: number;
    speed: number;
    energy: number;
    hunger: number;
    heat: number;
    curiosity: number;
    fear: number;
}

export interface WorldDimensions {
    width: number;
    height: number;
}

export class LayerContainer {
    private layers: Record<LayerName, LayerData> = {} as Record<LayerName, LayerData>;
    private agents: Array<AgentData> = []
    private dimensions: WorldDimensions = {} as WorldDimensions;

    public setLayer(name: LayerName, width: number, height: number, data?: Float32Array) {
        this.layers[name] = {
            width,
            height,
            data: data ?? new Float32Array(width * height),
        };
        this.dimensions.width = width;
        this.dimensions.height = height;

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