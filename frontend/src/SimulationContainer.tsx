import React, { useEffect, useRef, useState } from 'react';
import { SimEngine } from './sim_engine/SimEngine';
import type { SimSettings } from './sim_engine/SimEngine'
import {EditorSlider} from "./settings/EditorSlider";
import type {WorldConfig} from "./Dashboard.tsx";
import {useDebouncedCallback} from "./hooks/DebounceCallback.tsx";

interface Props {
    config: WorldConfig | null;
}

export const SimulationContainer: React.FC<Props> = ({ config }) => {
    const containerRef = useRef<HTMLDivElement>(null);
    const canvasRef = useRef<HTMLCanvasElement>(null);
    const engineRef = useRef<SimEngine | null>(null);

    const [simSpeed, setSimSpeed] = useState(1);
    const [sliderValue, setSliderValue] = useState(simSpeed);
    const [simState, setSimState] = useState<'stopped' | 'running' | 'paused'>('stopped');

    // UI State
    const [settings, setSettings] = useState<SimSettings>({
        showHeat: true,
        showScent: true,
        showSupply: true,
        showAgents: true,
        speed: 1,
    });

    const changeSpeed = useDebouncedCallback(async (value: number) => {
        const response = await fetch(`/api/sim/speed/${value}`, { method: 'POST' });
        if (!response.ok) console.error("Simulation: Changing speed failed!");
        else setSimSpeed(value);
    }, 200);

    const handleSliderChange = (value: number) => {
        setSliderValue(value);
        changeSpeed(value);
    };

    const handleSimulation = async (action: 'start' | 'pause' | 'resume' | 'stop') => {
        if (!engineRef.current || !config) return;

        if (action === 'start') {
            engineRef.current.connect(config.worldId);
        }

        const response = await fetch(`/api/sim/${action}`, { method: 'POST' });

        if (!response.ok) {
            console.error(`Simulation ${action} failed!`);
            return;
        }

        switch(action) {
            case 'start':
            case 'resume':
                engineRef.current.start();
                setSimState('running');
                break;
            case 'pause':
                setSimState('paused');
                break;
            case 'stop':
                engineRef.current.stop();
                setSimState('stopped');
                break;
        }
    }


    useEffect(() => {
        if (!canvasRef.current || !containerRef.current) return;

        const engine = new SimEngine(canvasRef.current);
        engineRef.current = engine;

        const observer = new ResizeObserver((entries) => {
            for (const entry of entries) {
                const { width, height } = entry.contentRect;

                const size = Math.min(width, height);

                if (size > 0) {
                    console.log("resizing");
                    engine.resize(size, size);
                }
            }
        });

        observer.observe(containerRef.current);

        return () => {
            engine.stop();
            observer.disconnect();
        };
    }, []);

    useEffect(() => {
        if (config && engineRef.current) {
            engineRef.current.reconfigure(config.width, config.height);
        }
    }, [config]);

    const toggleLayer = (key: keyof SimSettings) => {
        const newVal = !settings[key];
        const newSettings = { ...settings, [key]: newVal };

        setSettings(newSettings);
        engineRef.current?.updateSettings(newSettings);
    };

    return (
        <div className="flex flex-col h-full w-full overflow-hidden">
            <div className="border-b border-slate-600 w-full p-4">
                <h1 className="text-white font-bold">World View</h1>
            </div>

            {/* Main Content Area */}
            <div ref={containerRef} className="flex-1 flex flex-col items-center justify-between p-4 min-h-0 overflow-hidden">

                {/* Layer Controls */}
                <div className="w-full max-w-[450px] shrink-0 z-20 bg-slate-900/90 p-3 rounded-lg border border-slate-700 shadow-xl">
                    <p className="text-[10px] text-slate-500 font-bold uppercase border-b border-slate-800 pb-1 mb-2 tracking-widest">
                        Layer Visibility
                    </p>
                    <div className="flex flex-wrap gap-x-4 gap-y-2">
                        {[
                            {id: 'showHeat', label: 'Heat', checked: settings.showHeat},
                            {id: 'showSupply', label: 'Supply', checked: settings.showSupply},
                            {id: 'showScent', label: 'Scent', checked: settings.showScent},
                            {id: 'showAgents', label: 'Agents', checked: settings.showAgents},
                        ].map((layer) => (
                            <label key={layer.id} className="flex items-center gap-2 text-[11px] text-slate-300 cursor-pointer">
                                <input type="checkbox" className="w-3.5 h-3.5 rounded bg-slate-800 border-slate-700 text-blue-600 focus:ring-0"
                                       checked={layer.checked} onChange={() => toggleLayer(layer.id as keyof SimSettings)} />
                                <span>{layer.label}</span>
                            </label>
                        ))}
                    </div>
                </div>

                {/* Canvas Area */}
                <div className="flex-1 w-full flex items-center justify-center min-h-0 my-4 overflow-hidden">
                    <div className="relative h-full rounded-3xl border border-slate-800 bg-black flex items-center justify-center">
                        <canvas
                            ref={canvasRef}
                            className="max-w-full max-h-full block rounded-3xl"
                            style={{ imageRendering: 'pixelated' }}
                        />
                    </div>
                </div>

                {/* Sim Controls */}
                <div
                    className="w-full max-w-[450px] shrink-0 z-20 bg-slate-900/90 p-3 rounded-lg border border-slate-700 shadow-xl">
                    <p className="text-[10px] text-slate-500 font-bold uppercase border-b border-slate-800 pb-1 mb-2 tracking-widest">
                        Sim Settings
                    </p>
                    <EditorSlider label="Speed" value={sliderValue} min={0.1} max={2} step={0.1} onChange={handleSliderChange}/>

                    <div className="flex gap-2">
                        {simState === 'stopped' && (
                            <button
                                type="button"
                                onClick={() => handleSimulation('start')}
                                className="w-full py-2 bg-green-600/20 hover:bg-green-600 text-green-500 hover:text-white text-[10px] font-bold uppercase rounded border border-green-600/30 transition-all"
                            >
                                Play
                            </button>
                        )}

                        {simState === 'running' && (
                            <>
                                <button
                                    type="button"
                                    onClick={() => handleSimulation('pause')}
                                    className="w-full py-2 bg-yellow-600/20 hover:bg-yellow-600 text-yellow-500 hover:text-white text-[10px] font-bold uppercase rounded border border-yellow-600/30 transition-all"
                                >
                                    Pause
                                </button>
                                <button
                                    type="button"
                                    onClick={() => handleSimulation('stop')}
                                    className="w-full py-2 bg-red-600/20 hover:bg-red-600 text-red-500 hover:text-white text-[10px] font-bold uppercase rounded border border-red-600/30 transition-all"
                                >
                                    Stop
                                </button>
                            </>
                        )}

                        {simState === 'paused' && (
                            <>
                                <button
                                    type="button"
                                    onClick={() => handleSimulation('resume')}
                                    className="w-full py-2 bg-green-600/20 hover:bg-green-600 text-green-500 hover:text-white text-[10px] font-bold uppercase rounded border border-green-600/30 transition-all"
                                >
                                    Resume
                                </button>
                                <button
                                    type="button"
                                    onClick={() => handleSimulation('stop')}
                                    className="w-full py-2 bg-red-600/20 hover:bg-red-600 text-red-500 hover:text-white text-[10px] font-bold uppercase rounded border border-red-600/30 transition-all"
                                >
                                    Stop
                                </button>
                            </>
                        )}
                    </div>
                </div>
            </div>
        </div>
    );
};