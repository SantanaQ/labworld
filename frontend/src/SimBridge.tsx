import React, { useEffect, useRef, useState } from 'react';
import { SimEngine } from './SimEngine';
import type { SimSettings } from './SimEngine'

export const SimBridge: React.FC = () => {
    const containerRef = useRef<HTMLDivElement>(null);
    const canvasRef = useRef<HTMLCanvasElement>(null);
    const engineRef = useRef<SimEngine | null>(null);

    // UI State
    const [settings, setSettings] = useState<SimSettings>({
        showHeat: true,
        showScent: true,
        showSupply: true
    });

    useEffect(() => {
        if (!canvasRef.current) return;
        const engine = new SimEngine(canvasRef.current);
        engineRef.current = engine;
        engine.start();

        return () => engine.stop();
    }, []);

    // Update Engine inside Component
    const toggleLayer = (key: keyof SimSettings) => {
        const newVal = !settings[key];
        const newSettings = { ...settings, [key]: newVal };

        setSettings(newSettings); // Update React UI
        engineRef.current?.updateSettings(newSettings); // Update Canvas Logic
    };

    return (
        <div ref={containerRef} className="w-full h-full relative bg-slate-950">
            {/* Overlay UI: Engine Controls */}
            <div
                className="absolute top-4 right-4 z-20 flex flex-col gap-2 bg-slate-900/80 p-3 rounded-lg border border-slate-700 backdrop-blur-sm">
                <label className="flex items-center gap-2 text-xs text-slate-300 cursor-pointer">
                    <input type="checkbox" checked={settings.showHeat} onChange={() => toggleLayer('showHeat')}/>
                    Show Heat
                </label>
                <label className="flex items-center gap-2 text-xs text-slate-300 cursor-pointer">
                    <input type="checkbox" checked={settings.showScent} onChange={() => toggleLayer('showScent')}/>
                    Show Scent
                </label>
                <label className="flex items-center gap-2 text-xs text-slate-300 cursor-pointer">
                    <input type="checkbox" checked={settings.showSupply} onChange={() => toggleLayer('showSupply')}/>
                    Show Supply
                </label>
            </div>

            <canvas ref={canvasRef} className="w-full h-full block"/>
        </div>
    );
};