import React, { useEffect, useRef, useState } from 'react';
import { SimEngine } from '../sim_engine/SimEngine.ts';
import type { SimSettings } from '../sim_engine/SimEngine.ts';
import { EditorSlider } from "../components/EditorSlider.tsx";
import type { WorldConfig } from "./Dashboard.tsx";
import { useDebouncedCallback } from "../hooks/useDebouncedCallback.ts";
import { useCanvasCamera } from "../hooks/useCanvasCamera.ts";

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

    return (
        <div className="flex flex-col h-screen w-full bg-slate-900 overflow-hidden">
            {/* Header - fest fixiert */}
            <header className="flex-none flex flex-row justify-between border-b border-slate-700 h-14 w-full p-3 items-center bg-slate-900 shadow-md z-20">
                <h1 className="text-white font-bold tracking-wide text-sm md:text-base">Simulation</h1>
                <div className="text-[10px] text-slate-500 uppercase font-mono bg-slate-800 px-2 py-1 rounded">
                    Status: {simState}
                </div>
            </header>

            {/* Main Content Area - darf niemals scrollen */}
            <div
                ref={containerRef}
                className="flex-1 flex flex-col md:flex-row gap-4 p-4 md:p-6 bg-slate-800/50 items-start justify-center overflow-hidden"
            >
                {/* LEFT: Canvas Container */}
                {/* Erklärt: aspect-square sorgt für die Form, max-h-full verhindert das Herausragen */}
                <div className="relative h-full max-h-9/10 aspect-square flex-none rounded-2xl border border-slate-700 bg-black shadow-2xl overflow-hidden self-center md:self-start">
                    <canvas
                        ref={canvasRef}
                        className="w-full h-full object-cover"
                        style={{ imageRendering: "pixelated" }}
                    />
                    <div className="absolute bottom-2 right-2 text-[9px] text-white/20 pointer-events-none uppercase tracking-tighter">
                        Fixed Ratio Viewport
                    </div>
                </div>

                {/* RIGHT: Bento Panel - Scrollt nur intern wenn nötig */}
                <div className="w-full min-w-[200px]  md:w-[280px] lg:w-[350px] flex flex-col gap-4 self-start max-h-full overflow-y-auto pr-1 custom-scrollbar">

                    <div className="grid grid-cols-1 gap-3">
                        {/* Layer Visibility */}
                        <div className="bg-slate-900 p-4 rounded-xl border border-slate-700 shadow-lg">
                            <p className="text-[10px] text-slate-500 font-bold uppercase border-b border-slate-800 pb-2 mb-3 tracking-widest">
                                Layer Visibility
                            </p>
                            <div className="flex flex-col gap-2">
                                {['showHeat','showSupply','showScent','showAgents'].map((key) => (
                                    <label key={key} className="flex items-center gap-3 text-[11px] text-slate-300 cursor-pointer hover:text-white transition-colors">
                                        <input
                                            type="checkbox"
                                            className="w-4 h-4 rounded border-slate-700 bg-slate-800 text-blue-500 focus:ring-0 transition-all"
                                            checked={settings[key as keyof SimSettings] as boolean}
                                            onChange={() => toggleLayer(key as keyof SimSettings)}
                                        />
                                        {key.replace('show', '')}
                                    </label>
                                ))}
                            </div>
                        </div>

                        {/* Simulation Controls */}
                        <div className="bg-slate-900/90 p-4 rounded-xl border border-slate-700 shadow-lg">
                            <p className="text-[10px] text-slate-500 font-bold uppercase border-b border-slate-800 pb-2 mb-4 tracking-widest">
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
                            <button
                                onClick={() => handleSimulation('start')}
                                className="w-full py-3 bg-green-500/10 hover:bg-green-600 text-green-500 hover:text-white text-[11px] font-black uppercase rounded-lg border border-green-500/20 transition-all active:scale-95 shadow-lg">
                                Start
                            </button>
                        ) : (
                            <div className="flex gap-2">
                                {simState !== 'stopped' ? (
                                    <>
                                    <button
                                        onClick={() => handleSimulation(simState === 'running' ? 'pause' : 'resume')}
                                        className="flex-1 py-3 bg-slate-700 hover:bg-slate-600 text-white text-[11px] font-black uppercase rounded-lg border border-slate-600 transition-all active:scale-95">
                                        {simState === 'running' ? 'Pause' : 'Resume'}
                                    </button>
                                    <button
                                        onClick={() => handleSimulation('stop')}
                                        className="px-4 py-3 bg-red-500/10 hover:bg-red-600 text-red-500 hover:text-white text-[11px] font-black uppercase rounded-lg border border-red-500/20 transition-all active:scale-95">
                                        Stop
                                    </button>
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
