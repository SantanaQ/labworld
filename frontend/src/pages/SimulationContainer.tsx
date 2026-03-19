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
        <div className="h-full w-full bg-slate-900">
            <div className="flex flex-row justify-between border-b border-slate-500 h-15 w-full p-2 items-center bg-slate-900 shadow-md gap-1">
                <h1 className="text-white font-bold">Simulation</h1>
            </div>


            <div ref={containerRef} className="h-full w-full flex gap-4 p-3 items-center bg-slate-700">

                {/* LEFT: Canvas */}
                <div className="flex-none aspect-square w-full max-w-[600px] max-h-[600px] relative overflow-hidden rounded-2xl border border-slate-800 bg-black">
                    <canvas
                        ref={canvasRef}
                        className="w-full h-full"
                        style={{ imageRendering: "pixelated" }}
                    />
                </div>

                <div className="w-full h-full max-h-[600px]">
                    {/* RIGHT: Bento Panel */}
                    <div className="flex-1 flex-col gap-3 max-w-[400px] min-w-[200px]">

                        <div className="grid grid-cols-1 gap-3">
                            {/* Layer Visibility */}
                            <div className="bg-slate-900 p-3 rounded-lg border border-slate-700 shadow-xl">
                                <p className="text-[10px] text-slate-500 font-bold uppercase border-b border-slate-800 pb-1 mb-2 tracking-widest">
                                    Layer Visibility
                                </p>
                                <div className="flex flex-col gap-1">
                                    {['showHeat','showSupply','showScent','showAgents'].map((key) => (
                                        <label key={key} className="flex items-center gap-2 text-[11px] text-slate-300 cursor-pointer">
                                            <input
                                                type="checkbox"
                                                className="w-3.5 h-3.5 rounded bg-slate-800 border-slate-700 text-blue-600 focus:ring-0"
                                                checked={settings[key as keyof SimSettings] as boolean}
                                                onChange={() => toggleLayer(key as keyof SimSettings)}
                                            />
                                            {key.replace('show','')}
                                        </label>
                                    ))}
                                </div>
                            </div>

                            {/* Simulation Settings */}
                            <div className="bg-slate-900/90 p-3 rounded-lg border border-slate-700 shadow-xl">
                                <p className="text-[10px] text-slate-500 font-bold uppercase border-b border-slate-800 pb-1 mb-2 tracking-widest">
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

                        {/* Buttons */}
                        <div className="flex flex-col gap-2 mt-2">
                            {simState === 'idle' &&
                                <button
                                    onClick={() => handleSimulation('start')}
                                    className="w-full py-2 bg-green-600/20 hover:bg-green-600 text-green-500 hover:text-white text-[10px] font-bold uppercase rounded border border-green-600/30 transition-all cursor-pointer">
                                    Play
                                </button>}
                            {simState === 'running' &&
                                <button
                                    onClick={() => handleSimulation('pause')}
                                    className="w-full py-2 bg-yellow-600/20 hover:bg-yellow-600 text-yellow-500 hover:text-white text-[10px] font-bold uppercase rounded border border-yellow-600/30 transition-all cursor-pointer">
                                    Pause
                                </button>
                            }
                            {simState === 'paused' &&
                                <button
                                    onClick={() => handleSimulation('resume')}
                                    className="w-full py-2 bg-green-600/20 hover:bg-green-600 text-green-500 hover:text-white text-[10px] font-bold uppercase rounded border border-green-600/30 transition-all cursor-pointer">
                                    Resume
                                </button>
                            }
                            {(simState === 'running' || simState === 'paused') &&
                                <button
                                    onClick={() => handleSimulation('stop')}
                                    className="w-full py-2 bg-red-600/20 hover:bg-red-600 text-red-500 hover:text-white text-[10px] font-bold uppercase rounded border border-red-600/30 transition-all cursor-pointer">
                                    Stop
                                </button>
                            }
                        </div>
                    </div>
                </div>
            </div>
        </div>
    );
};
