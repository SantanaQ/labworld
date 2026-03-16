import type {AgentData, LayerData} from "./LayerContainer.ts";
import type {Camera} from "../hooks/UseCanvasCamera.ts";
import React from "react";

export class CanvasRenderer {
    private canvas: HTMLCanvasElement;
    private ctx: CanvasRenderingContext2D;
    private offscreen: HTMLCanvasElement;
    private offCtx: CanvasRenderingContext2D;
    private cameraRef: React.RefObject<Camera>;

    constructor(canvas: HTMLCanvasElement, camera: React.RefObject<Camera>) {
        this.canvas = canvas;
        this.ctx = canvas.getContext('2d')!;
        this.offscreen = document.createElement('canvas');
        this.offCtx = this.offscreen.getContext('2d')!;
        this.cameraRef = camera;
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

        this.ctx.save()

        this.applyCamera()

        this.ctx.drawImage(this.offscreen, 0, 0, layer.width, layer.height)

        this.ctx.restore()
    }



    public drawAgents(agents: AgentData[]) {

        const ctx = this.ctx
        const radius = 1.5

        ctx.save()

        this.applyCamera()

        for (const agent of agents) {

            const x = agent.x
            const y = agent.y

            const r = Math.min(255, agent.fear * 255)
            const g = Math.min(255, agent.curiosity * 255)
            const b = Math.min(255, agent.hunger * 255)

            ctx.fillStyle = `rgb(${r},${g},${b})`

            ctx.beginPath()
            ctx.arc(x, y, radius, 0, Math.PI * 2)
            ctx.fill()
            /*
            const dirLength = 6

            ctx.beginPath()
            ctx.moveTo(x, y)
            ctx.lineTo(
                x + agent.vx * dirLength,
                y + agent.vy * dirLength
            )

            ctx.strokeStyle = "white"
            ctx.lineWidth = 1
            ctx.stroke()*/
        }

        ctx.restore()
    }


    public clear() {
        this.ctx.setTransform(1,0,0,1,0,0)
        this.ctx.clearRect(0,0,this.canvas.width,this.canvas.height)
    }


    public resize(w: number, h: number) {
        this.canvas.width = w;
        this.canvas.height = h;
    }

    private applyCamera(){
        const ctx = this.ctx

        ctx.translate(this.canvas.width/2, this.canvas.height/2)
        ctx.scale(this.cameraRef.current.zoom, this.cameraRef.current.zoom)
        ctx.translate(-this.cameraRef.current.x, -this.cameraRef.current.y)
    }

}