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
        const vp = this.getViewport();

        const x0 = Math.max(0, vp.x0);
        const y0 = Math.max(0, vp.y0);
        const x1 = Math.min(width, vp.x1);
        const y1 = Math.min(height, vp.y1);

        const w = x1 - x0;
        const h = y1 - y0;

        if (w <= 0 || h <= 0) return;

        if (!this.imageData || this.imageData.width !== w || this.imageData.height !== h) {
            this.offscreen.width = w;
            this.offscreen.height = h;
            this.imageData = this.offCtx.createImageData(w, h);
        }

        const pixels = this.imageData.data;

        for (let y = 0; y < h; y++) {
            for (let x = 0; x < w; x++) {
                const worldIndex = (y + y0) * width + (x + x0);
                const v = data[worldIndex] * 4;
                const p = (y * w + x) * 4;

                pixels[p]     = lut[v];
                pixels[p + 1] = lut[v + 1];
                pixels[p + 2] = lut[v + 2];
                pixels[p + 3] = lut[v + 3];
            }
        }

        this.offCtx.putImageData(this.imageData, 0, 0);

        const cam = this.cameraRef.current;

        this.ctx.save();

        this.ctx.globalCompositeOperation = blend;

        this.ctx.translate(this.canvas.width / 2, this.canvas.height / 2);
        this.ctx.scale(cam.zoom, cam.zoom);

        this.ctx.drawImage(
            this.offscreen,
            0, 0, w, h,
            -vp.width / 2 + (x0 - vp.x0),
            -vp.height / 2 + (y0 - vp.y0),
            w,
            h
        );

        this.ctx.restore();
    }



    public drawAgents(agents: AgentData[]) {
        const ctx = this.ctx;

        const cam = this.cameraRef.current;

        const viewWidth = this.canvas.width / cam.zoom;
        const viewHeight = this.canvas.height / cam.zoom;

        const x0 = cam.x - viewWidth / 2;
        const y0 = cam.y - viewHeight / 2;
        const x1 = cam.x + viewWidth / 2;
        const y1 = cam.y + viewHeight / 2;

        ctx.save();
        this.applyCamera();

        for (const a of agents) {
            const x = a.posX;
            const y = a.posY;

            if (x < x0 || x > x1 || y < y0 || y > y1) continue;

            const speed = a.speed / 255;

            // movement angle
            const dx = a.vX ?? 0;
            const dy = a.vY ?? 0;
            const targetAngle = Math.atan2(dy, dx);

            // smooth rotate
            a.angle = this.lerpAngle(a.angle ?? targetAngle, targetAngle, 0.15);

            // stretch
            const targetStretch = 1 + Math.pow(speed, 1.5) * 0.5;

            // sluggishness
            a.stretch = this.lerp(a.stretch ?? 1, targetStretch, 0.2);

            // wobble
            const wobble = Math.sin(performance.now() * 0.01) * 0.15;

            const stretch = a.stretch + wobble;
            const squash = 1 / stretch;

            const baseSize = 0.6;

            ctx.save();
            ctx.translate(x, y);
            ctx.rotate(a.angle);
            ctx.scale(stretch, squash);

            const r = a.fear;
            const g = a.curiosity;
            const b = a.hunger;

            // glow
            ctx.beginPath();
            ctx.fillStyle = `rgba(${r},${g},${b},0.15)`;
            ctx.shadowBlur = 12;
            ctx.shadowColor = `rgba(${r},${g},${b},0.6)`;
            ctx.arc(0, 0, baseSize * 2.2, 0, Math.PI * 2);
            ctx.fill();

            // body
            ctx.shadowBlur = 0;
            ctx.beginPath();
            ctx.fillStyle = `rgb(${r},${g},${b})`;
            ctx.arc(0, 0, baseSize, 0, Math.PI * 2);
            ctx.fill();

            ctx.restore();
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

    private getViewport() {
        const cam = this.cameraRef.current;

        const viewWidth = this.canvas.width / cam.zoom;
        const viewHeight = this.canvas.height / cam.zoom;

        return {
            x0: Math.floor(cam.x - viewWidth / 2),
            y0: Math.floor(cam.y - viewHeight / 2),
            x1: Math.ceil(cam.x + viewWidth / 2),
            y1: Math.ceil(cam.y + viewHeight / 2),
            width: Math.ceil(viewWidth),
            height: Math.ceil(viewHeight)
        };
    }

    private lerp(a: number, b: number, t: number) {
        return a + (b - a) * t;
    }

    private lerpAngle(a: number, b: number, t: number) {
        let diff = b - a;

        while (diff > Math.PI) diff -= Math.PI * 2;
        while (diff < -Math.PI) diff += Math.PI * 2;

        return a + diff * t;
    }

}