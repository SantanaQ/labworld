import { useEffect, useRef } from "react";

export interface Camera {
    x: number;
    y: number;
    zoom: number;
}

export function useCanvasCamera(
    canvasRef: React.RefObject<HTMLCanvasElement | null>,
    worldWidth: number,
    worldHeight: number
) {
    // Kamera als Ref – Renderer liest immer aktuelle Werte
    const cameraRef = useRef<Camera>({
        x: worldWidth / 2,
        y: worldHeight / 2,
        zoom: 1
    });

    useEffect(() => {
        const canvas = canvasRef.current;
        if (!canvas) return;

        let dragging = false;
        let lastX = 0;
        let lastY = 0;

        const clamp = (cam: Camera) => ({
            ...cam,
            x: Math.max(0, Math.min(worldWidth, cam.x)),
            y: Math.max(0, Math.min(worldHeight, cam.y)),
            zoom: Math.min(20, Math.max(0.2, cam.zoom))
        });

        const wheel = (e: WheelEvent) => {
            e.preventDefault();
            const cam = cameraRef.current;
            cam.zoom *= e.deltaY > 0 ? 0.9 : 1.1;
            cameraRef.current = clamp(cam);
        };

        const down = (e: MouseEvent) => {
            dragging = true;
            lastX = e.clientX;
            lastY = e.clientY;
        };

        const up = () => dragging = false;

        const move = (e: MouseEvent) => {
            if (!dragging) return;

            const cam = cameraRef.current;
            cam.x -= (e.clientX - lastX) / cam.zoom;
            cam.y -= (e.clientY - lastY) / cam.zoom;
            cameraRef.current = clamp(cam);

            lastX = e.clientX;
            lastY = e.clientY;
        };

        canvas.addEventListener("wheel", wheel, { passive: false });
        canvas.addEventListener("mousedown", down);
        window.addEventListener("mouseup", up);
        window.addEventListener("mousemove", move);

        return () => {
            canvas.removeEventListener("wheel", wheel);
            canvas.removeEventListener("mousedown", down);
            window.removeEventListener("mouseup", up);
            window.removeEventListener("mousemove", move);
        };
    }, [canvasRef, worldWidth, worldHeight]);

    return cameraRef; // gibt Ref zurück, nicht State
}
