export type LayerName = 'heat' | 'scent' | 'supply' | 'agents';

export interface LayerData {
    width: number;
    height: number;
    data: Float32Array;
}

export class LayerContainer {
    private layers: Record<LayerName, LayerData> = {} as Record<LayerName, LayerData>;

    public setLayer(name: LayerName, width: number, height: number, data?: Float32Array) {
        this.layers[name] = {
            width,
            height,
            data: data ?? new Float32Array(width * height),
        };
    }

    public getLayer(name: LayerName) {
        return this.layers[name];
    }

    public getLayerNames(): LayerName[] {
        return Object.keys(this.layers) as LayerName[];
    }
}