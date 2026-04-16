import type {LayerData} from "./LayerContainer.ts";
import type {Camera} from "../hooks/useCanvasCamera.ts";
import React from "react";
import type {AgentData} from "./FrameDecoder.ts";

export class CanvasRenderer {
    private canvas: HTMLCanvasElement;
    private ctx: CanvasRenderingContext2D;
    private offscreen: HTMLCanvasElement;
    private offCtx: CanvasRenderingContext2D;
    private cameraRef: React.RefObject<Camera>;

    private imageData: ImageData | null = null;

    public heatLUT = new Uint8ClampedArray(256 * 4);
    public supplyLUT = new Uint8ClampedArray(256 * 4);
    public scentLUT = new Uint8ClampedArray(256 * 4);

    constructor(canvas: HTMLCanvasElement, camera: React.RefObject<Camera>) {
        this.canvas = canvas;
        this.ctx = canvas.getContext('2d')!;
        this.offscreen = document.createElement('canvas');
        this.offCtx = this.offscreen.getContext('2d')!;
        this.cameraRef = camera;

        this.buildLUTs();
    }

    private buildLUTs() {
        for (let i = 0; i < 256; i++) {
            const t = i / 255;

            this.heatLUT[i*4+0] = 255;
            this.heatLUT[i*4+1] = Math.floor(80 * t);
            this.heatLUT[i*4+2] = 30;
            this.heatLUT[i*4+3] = i;

            this.supplyLUT[i*4+0] = 30;
            this.supplyLUT[i*4+1] = Math.floor(120 * t);
            this.supplyLUT[i*4+2] = 255;
            this.supplyLUT[i*4+3] = i;

            this.scentLUT[i*4+0] = Math.floor(80 * t);
            this.scentLUT[i*4+1] = 255;
            this.scentLUT[i*4+2] = Math.floor(120 * t);
            this.scentLUT[i*4+3] = i * 0.6;
        }
    }

    public drawLayer(layer: LayerData, lut: Uint8ClampedArray, blend: GlobalCompositeOperation = "lighter") {
        if (!layer) return;

        const { width, height, data } = layer;

        if (!this.imageData || this.imageData.width !== width || this.imageData.height !== height) {
            this.offscreen.width = width;
            this.offscreen.height = height;
            this.imageData = this.offCtx.createImageData(width, height);
        }

        const pixels = this.imageData.data;

        for (let i = 0; i < data.length; i++) {
            const v = data[i] * 4;
            const p = i * 4;

            pixels[p]     = lut[v];
            pixels[p + 1] = lut[v + 1];
            pixels[p + 2] = lut[v + 2];
            pixels[p + 3] = lut[v + 3];
        }

        this.offCtx.putImageData(this.imageData, 0, 0);

        this.ctx.save();
        this.applyCamera();

        this.ctx.globalCompositeOperation = blend;

        // subtle bloom feel
        this.ctx.shadowBlur = 20;
        this.ctx.shadowColor = "rgba(255,255,255,0.05)";

        this.ctx.drawImage(this.offscreen, 0, 0, width, height);

        this.ctx.restore();
    }



    public drawAgents(agents: AgentData[]) {
        const ctx = this.ctx;

        ctx.save();
        this.applyCamera();

        ctx.lineCap = "round";

        for (const a of agents) {
            const x = a.posX;
            const y = a.posY;

            const speed = a.speed / 255;

            const r = a.fear;
            const g = a.curiosity;
            const b = a.hunger;


            //const pulse = 1 + Math.sin(Date.now() * 0.002 + x * 0.001) * 0.2;
            //const size = (1 + speed * 2) * pulse;
            const size = 0.5;
            //const size = 1.5 + speed * 2;

            //const vx = a.vX * 6;
            //const vy = a.vY * 6;

            // outer glow
            /*ctx.beginPath();
            ctx.fillStyle = `rgba(${r},${g},${b},0.15)`;
            ctx.shadowBlur = 12;
            ctx.shadowColor = `rgba(${r},${g},${b},0.6)`;
            ctx.arc(x, y, size * 2.2, 0, Math.PI * 2);
            ctx.fill();*/

            // body
            ctx.shadowBlur = 0;
            ctx.beginPath();
            ctx.fillStyle = `rgb(${r},${g},${b})`;
            ctx.arc(x, y, size, 0, Math.PI * 2);
            ctx.fill();

            // direction line
            /*ctx.globalAlpha = 0.6;
            ctx.strokeStyle = `rgba(255,255,255,0.4)`;
            ctx.lineWidth = 1;

            ctx.beginPath();
            ctx.moveTo(x, y);
            ctx.lineTo(x + vx, y + vy);
            ctx.stroke();*/

            ctx.globalAlpha = 1;
        }

        ctx.restore();
    }


    public clear() {
        this.ctx.setTransform(1,0,0,1,0,0);
        this.ctx.clearRect(0,0,this.canvas.width,this.canvas.height);
    }

    private applyCamera(){
        const ctx = this.ctx

        ctx.translate(this.canvas.width/2, this.canvas.height/2)
        ctx.scale(this.cameraRef.current.zoom, this.cameraRef.current.zoom)
        ctx.translate(-this.cameraRef.current.x, -this.cameraRef.current.y)
    }

}