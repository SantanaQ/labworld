import type {LayerData} from "./LayerContainer.ts";

export class CanvasRenderer {
    private canvas: HTMLCanvasElement;
    private ctx: CanvasRenderingContext2D;
    private offscreen: HTMLCanvasElement;
    private offCtx: CanvasRenderingContext2D;

    constructor(canvas: HTMLCanvasElement) {
        this.canvas = canvas;
        this.ctx = canvas.getContext('2d')!;
        this.offscreen = document.createElement('canvas');
        this.offCtx = this.offscreen.getContext('2d')!;
    }

    public drawLayer(layer: LayerData, colorMap: (value: number) => [number, number, number, number]) {
        if (!layer || !layer.data) return;
        const { width, height, data } = layer;

        if (this.offscreen.width !== width || this.offscreen.height !== height) {
            this.offscreen.width = width;
            this.offscreen.height = height;
        }

        const imgData = this.offCtx.createImageData(width, height);
        for (let i = 0; i < data.length; i++) {
            const [r, g, b, a] = colorMap(data[i]);
            const p = i * 4;
            imgData.data[p] = r;
            imgData.data[p + 1] = g;
            imgData.data[p + 2] = b;
            imgData.data[p + 3] = a;
        }

        this.offCtx.putImageData(imgData, 0, 0);
        this.ctx.drawImage(this.offscreen, 0, 0, this.canvas.width, this.canvas.height);
    }

    public clear() {
        this.ctx.clearRect(0, 0, this.canvas.width, this.canvas.height);
    }

    public resize(w: number, h: number) {
        this.canvas.width = Math.min(w, 600);
        this.canvas.height = Math.min(h, 600);
    }
}