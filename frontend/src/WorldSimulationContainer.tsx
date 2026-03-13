import React, { useEffect, useRef, useState } from 'react';
import { SimEngine } from './SimEngine';
import type { SimSettings } from './SimEngine'
import {EditorSlider} from "./EditorSlider.tsx";
import type {WorldConfig} from "./WorldDashboard.tsx";

interface Props {
    config: WorldConfig | null;
}

export const WorldSimulationContainer: React.FC<Props> = ({ config }) => {
    const containerRef = useRef<HTMLDivElement>(null);
    const canvasRef = useRef<HTMLCanvasElement>(null);
    const engineRef = useRef<SimEngine | null>(null);

    const [simSpeed, setSimSpeed] = useState(1);

    // UI State
    const [settings, setSettings] = useState<SimSettings>({
        showHeat: true,
        showScent: true,
        showSupply: true,
        showAgents: true,
        speed: 1,
    });


    useEffect(() => {
        if (!canvasRef.current || !containerRef.current) return;

        // 1. Engine erstellen
        const engine = new SimEngine(canvasRef.current);
        engineRef.current = engine;
        engine.start();

        // 2. ResizeObserver: Er überwacht das Parent-Div (containerRef)
        const observer = new ResizeObserver((entries) => {
            for (const entry of entries) {
                // Wir nehmen die Maße des Containers, in dem das Canvas liegt
                const { width, height } = entry.contentRect;

                // Wir berechnen die Größe für ein perfektes Quadrat,
                // das in den verfügbaren Platz passt
                const size = Math.min(width, height);

                if (size > 0) {
                    console.log(`📏 UI Resize: Setting Canvas to ${size}px`);
                    // HIER wird resize in der Engine aufgerufen!
                    engine.resize(size, size);
                }
            }
        });

        // Wir beobachten den containerRef (das flexible Mittelfeld)
        observer.observe(containerRef.current);

        return () => {
            engine.stop();
            observer.disconnect();
        };
    }, []);

    useEffect(() => {
        console.log("5. Config Update Effect", config);
        if (config && engineRef.current) {
            console.log("6. Calling Reconfigure");
            engineRef.current.reconfigure(config.width, config.height);
        }
    }, [config]);

    // Update Engine inside Component
    const toggleLayer = (key: keyof SimSettings) => {
        const newVal = !settings[key];
        const newSettings = { ...settings, [key]: newVal };

        setSettings(newSettings); // Update React UI
        engineRef.current?.updateSettings(newSettings); // Update Canvas Logic
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
                            {id: 'showScent', label: 'Scent', checked: settings.showScent},
                            {id: 'showSupply', label: 'Supply', checked: settings.showSupply},
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
                    <div className="relative h-full  border border-slate-800 rounded bg-black flex items-center justify-center">
                        <canvas
                            ref={canvasRef}
                            className="max-w-full max-h-full block"
                            style={{ imageRendering: 'pixelated' }}
                        />
                    </div>
                </div>

                {/* Sim Controls */}
                <div className="w-full max-w-[450px] shrink-0 z-20 bg-slate-900/90 p-3 rounded-lg border border-slate-700 shadow-xl">
                    <p className="text-[10px] text-slate-500 font-bold uppercase border-b border-slate-800 pb-1 mb-2 tracking-widest">
                        Sim Settings
                    </p>
                    <EditorSlider label="Speed" value={simSpeed} min={0.1} max={2} step={0.1} onChange={setSimSpeed} />
                    <button type="button" className="w-full mt-3 py-2 bg-red-600/20 hover:bg-red-600 text-red-500 hover:text-white text-[10px] font-bold uppercase tracking-widest rounded border border-red-600/30 transition-all">
                        Stop Simulation
                    </button>
                </div>
            </div>
        </div>
    );
};