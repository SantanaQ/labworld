import React, { useEffect, useRef, useState } from 'react';
import { SimEngine } from '../sim_engine/SimEngine.ts';
import type { SimSettings } from '../sim_engine/SimEngine.ts';
import { EditorSlider } from "../components/EditorSlider.tsx";
import type { WorldConfig } from "./Dashboard.tsx";
import { useDebouncedCallback } from "../hooks/useDebouncedCallback.ts";
import {type Camera, useCanvasCamera} from "../hooks/useCanvasCamera.ts";
import FetchButton from "../components/FetchButton.tsx";
import {Hand, ScanSearch, View, ZoomIn, ZoomOut} from "lucide-react";

interface Props {
    config: WorldConfig | null;
}

export const SimulationContainer: React.FC<Props> = ({ config }) => {
    const containerRef = useRef<HTMLDivElement>(null);
    const canvasRef = useRef<HTMLCanvasElement>(null);
    const engineRef = useRef<SimEngine | null>(null);
    const hasConnected = useRef(false);

    const worldWidth = config?.width ?? 100;
    const worldHeight = config?.height ?? 100;

    const cameraRef = useCanvasCamera(canvasRef, worldWidth, worldHeight);

    const [simSpeed, setSimSpeed] = useState(1);
    const [sliderValue, setSliderValue] = useState(simSpeed);
    const [simState, setSimState] = useState<'idle' | 'stopped' | 'running' | 'paused'>('idle');

    const [settings, setSettings] = useState<SimSettings>({
        showHeat: true,
        showScent: true,
        showSupply: true,
        showAgents: true,
        speed: 1,
    });

    const changeSpeed = useDebouncedCallback(async (value: number) => {
        const response = await fetch(`/api/sim/speed/${config?.sessionId}/${value}`, { method: 'POST' });
        if (!response.ok) console.error("Simulation: Changing speed failed!");
        else setSimSpeed(value);
    }, 200);

    useEffect(() => {
        if (!canvasRef.current) return;

        const engine = new SimEngine(canvasRef.current, cameraRef);
        engineRef.current = engine;

        return () => {
            engine.stop();
        };
    }, []);

    useEffect(() => {
        if (!canvasRef.current || !containerRef.current) return;

        // Initial Zoom
        const cam = cameraRef.current;
        cam.zoom = Math.min(canvasRef.current.width / worldWidth, canvasRef.current.height / worldHeight);
        cam.x = worldWidth / 2;
        cam.y = worldHeight / 2;

        const observer = new ResizeObserver(() => {
            if (!canvasRef.current) return;
            const rect = canvasRef.current.parentElement!.getBoundingClientRect();
            canvasRef.current.width = rect.width;
            canvasRef.current.height = rect.height;

            cam.zoom = Math.min(rect.width / worldWidth, rect.height / worldHeight);
        });

        observer.observe(canvasRef.current.parentElement!);

        return () => {
            observer.disconnect();
        };
    }, [worldWidth, worldHeight]);

    useEffect(() => {
        if (!config || !engineRef.current) return;
        if (hasConnected.current) return;

        const engine = engineRef.current;

        engine.stop();
        engine.reconfigure(config);
        engine.connect(config.sessionId);
        engine.start();

        setSimState("idle");

    }, [config?.sessionId]);


    const handleSliderChange = (value: number) => {
        setSliderValue(value);
        changeSpeed(value);
    };

    const handleSimulation = async (action: 'start' | 'pause' | 'resume' | 'stop') => {
        if (!engineRef.current || !config) return;

        const response = await fetch(`/api/sim/${action}/${config.sessionId}`, { method: 'POST' });
        if (!response.ok) return console.error(`Simulation ${action} failed!`);

        switch (action) {
            case 'start':
            case 'resume': engineRef.current.start(); setSimState('running'); break;
            case 'pause': setSimState('paused'); break;
            case 'stop': engineRef.current.stop(); setSimState('stopped'); break;
        }
    };

    const toggleLayer = (key: keyof SimSettings) => {
        const newVal = !settings[key];
        setSettings(s => ({ ...s, [key]: newVal }));
        engineRef.current?.updateSettings({ [key]: newVal });
    };

    const resetCamera = () => {
        if(!canvasRef.current) return;
        const cam = cameraRef.current;
        cam.zoom = Math.min(canvasRef.current.width / worldWidth, canvasRef.current.height / worldHeight);
        cam.x = worldWidth / 2;
        cam.y = worldHeight / 2;
    }

    const applyZoom = (zoom: number) => {
        if(!canvasRef.current) return;
        const cam = cameraRef.current;
        cam.zoom *= zoom > 0 ? 0.9 : 1.1;
        cameraRef.current = clamp(cam);
    }

    const clamp = (cam: Camera) => ({
        ...cam,
        x: Math.max(0, Math.min(worldWidth, cam.x)),
        y: Math.max(0, Math.min(worldHeight, cam.y)),
        zoom: Math.min(20, Math.max(0.2, cam.zoom))
    });

    return (
        <div className="flex flex-col h-screen w-full bg-zinc-900 overflow-hidden">
            {/* Header */}
            <header className="flex-none flex flex-row justify-between border-b border-zinc-700 h-15 w-full p-2 gap-2 items-center bg-zinc-900 shadow-md z-20">
                <h1 className="text-white font-bold tracking-wide text-sm md:text-base">Simulation</h1>
                <div className="text-[10px] text-zinc-500 uppercase font-mono bg-zinc-800 p-3 rounded text-nowrap overflow-hidden">
                    Status: {simState}
                </div>
            </header>

            {/* Main Content Area */}
            <div
                ref={containerRef}
                className="flex-1 flex flex-col md:flex-row gap-4 p-4 md:p-6 bg-zinc-800/50 items-start justify-center overflow-hidden"
            >
                {/* LEFT: Canvas Container */}
                <div
                    className="relative h-full max-h-9/10 aspect-square flex-none rounded-2xl border border-zinc-700 bg-black  overflow-hidden self-center md:self-start">
                    <canvas
                        ref={canvasRef}
                        className="w-full h-full object-cover"
                        style={{imageRendering: "pixelated"}}
                    />
                    <div
                        className="absolute bottom-2 right-2 text-[9px] text-white pointer-events-none uppercase tracking-tighter">
                        Viewport
                    </div>
                    <div
                        className="absolute gap-1 flex bottom-2 left-2 text-[9px] text-white uppercase tracking-tighter">
                        <button className="bg-transparent cursor-pointer" onClick={resetCamera}>
                            <ScanSearch/>
                        </button>

                        <button className="bg-transparent cursor-pointer" onClick={() => applyZoom(1)}>
                            <ZoomOut/>
                        </button>

                        <button className="bg-transparent cursor-pointer" onClick={() => applyZoom(-1)}>
                            <ZoomIn/>
                        </button>
                    </div>
                    <div
                        className="absolute gap-1 flex top-2 left-2 text-[9px] text-white uppercase tracking-tighter">
                        {simState === 'paused' ? <Hand /> : <View />}
                    </div>
                </div>

                {/* RIGHT: Bento Panel - Scrollt nur intern wenn nötig */}
                <div
                    className="w-full min-w-[200px]  md:w-[280px] lg:w-[350px] flex flex-col gap-4 self-start max-h-full overflow-y-auto pr-1 custom-scrollbar">

                    <div className="grid grid-cols-1 gap-3">
                        {/* Layer Visibility */}
                        <div className="bg-zinc-900 p-4 rounded-xl border border-zinc-700 shadow-lg">
                            <p className="text-[10px] text-zinc-500 font-bold uppercase border-b border-zinc-800 pb-2 mb-3 tracking-widest">
                                Layer Visibility
                            </p>
                            <div className="flex flex-col gap-2">
                                {['showHeat','showSupply','showScent','showAgents'].map((key) => (
                                    <label key={key} className="flex items-center gap-3 text-[11px] text-zinc-300 cursor-pointer hover:text-white transition-colors">
                                        <input
                                            type="checkbox"
                                            className="w-4 h-4 rounded border-zinc-700 bg-zinc-800 text-blue-500 focus:ring-0 transition-all"
                                            checked={settings[key as keyof SimSettings] as boolean}
                                            onChange={() => toggleLayer(key as keyof SimSettings)}
                                        />
                                        {key.replace('show', '')}
                                    </label>
                                ))}
                            </div>
                        </div>

                        {/* Simulation Controls */}
                        <div className="bg-zinc-900/90 p-4 rounded-xl border border-zinc-700 shadow-lg">
                            <p className="text-[10px] text-zinc-500 font-bold uppercase border-b border-zinc-800 pb-2 mb-4 tracking-widest">
                                Simulation
                            </p>
                            <EditorSlider
                                label="Speed"
                                value={sliderValue}
                                min={0.1}
                                max={2}
                                step={0.1}
                                onChange={handleSliderChange}
                            />
                        </div>
                    </div>

                    {/* Buttons Container */}
                    <div className="flex flex-col gap-2 sticky bottom-0 bg-transparent pt-2">
                        {simState === 'idle' ? (
                            <FetchButton
                                baseStyle={"w-full py-3  text-white  font-black uppercase rounded-lg border border-green-500/20 transition-all active:scale-95 shadow-lg cursor-pointer"}
                                styleOnLoad={"cursor-not-allowed"}
                                styleOnReady={"bg-emerald-800 hover:bg-emerald-600 hover:text-white text-[11px] cursor-pointer delay-50 duration-300"}
                                onClick={() => handleSimulation('start')}
                            >
                                Start
                            </FetchButton>
                        ) : (
                            <div className="flex gap-2">
                                {simState !== 'stopped' ? (
                                    <>
                                    <FetchButton
                                        baseStyle={"flex-1 py-3 text-white  text-[11px] font-black uppercase rounded-lg border border-zinc-600 transition-all active:scale-95"}
                                        styleOnLoad={"cursor-not-allowed"}
                                        styleOnReady={"bg-zinc-700 hover:bg-zinc-600 cursor-pointer delay-50 duration-300"}
                                        onClick={() => handleSimulation(simState === 'running' ? 'pause' : 'resume')}
                                    >
                                        {simState === 'running' ? 'Pause' : 'Resume'}
                                    </FetchButton>
                                    <FetchButton
                                        baseStyle={"px-4 py-3 bg-red-500/10 hover:bg-red-600 text-red-500 hover:text-white text-[11px] font-black uppercase rounded-lg border border-red-500/20 transition-all active:scale-95"}
                                        styleOnLoad={"bg-grey cursor-not-allowed"}
                                        styleOnReady={"bg-red-500/10 cursor-pointer hover:bg-red-700 delay-50 duration-300"}
                                        onClick={() => handleSimulation('stop')}
                                    >
                                        Stop
                                    </FetchButton>
                                    </>
                                    ) : ""}
                            </div>
                        )}
                    </div>
                </div>
            </div>
        </div>
    );
};
