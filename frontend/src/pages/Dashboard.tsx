import React, { useState, useRef, useEffect, useCallback } from 'react';

import {EditorContainer} from "./EditorContainer.tsx";
import {SimulationContainer} from "./SimulationContainer.tsx";

export interface WorldConfig {
    sessionId: string;
    width: number;
    height: number;
    layerCount: number;
    agentCount: number;
}

const Dashboard: React.FC = () => {
    const [leftWidth, setLeftWidth] = useState<number>(80);
    const [worldConfig, setWorldConfig] = useState<WorldConfig | null>(null);
    const isResizing = useRef<boolean>(false);

    const startResizing = useCallback(() => {
        isResizing.current = true;
        document.body.style.userSelect = 'none';
        document.body.style.cursor = 'col-resize';
    }, []);

    const stopResizing = useCallback(() => {
        isResizing.current = false;
        document.body.style.userSelect = 'auto';
        document.body.style.cursor = 'default';
    }, []);

    const resize = useCallback((e: MouseEvent) => {
        if (!isResizing.current) return;

        const newWidth = (e.clientX / window.innerWidth) * 100;

        if (newWidth >= 0 && newWidth <= 90) {
            setLeftWidth(newWidth);
        }
    }, []);

    useEffect(() => {
        window.addEventListener('mousemove', resize);
        window.addEventListener('mouseup', stopResizing);

        return () => {
            window.removeEventListener('mousemove', resize);
            window.removeEventListener('mouseup', stopResizing);
        };
    }, [resize, stopResizing]);

    const handleWorldGenerated = (config: WorldConfig) => {
        setWorldConfig({
            sessionId: config.sessionId,
            width: config.width,
            height: config.height,
            layerCount: config.layerCount,
            agentCount: config.agentCount,
        });
    };

    return (
        <div className="flex flex-col h-screen w-full overflow-hidden bg-zinc-900 text-zinc-100">

            {/* Integrierter Header */}
            <header className="flex-none p-2 border-b border-zinc-700 bg-zinc-800/50 backdrop-blur-sm">
                <div className="flex items-center ">
                    <h1 className="pl-2 text-xl font-bold tracking-tight">
                        Labworld <span className="text-lg">🔬 🌍</span>
                    </h1>
                </div>
            </header>

            {/* Main Content Area */}
            <main className="flex-1 flex flex-col md:flex-row overflow-hidden p-2 gap-2">

                {/* Left Page (Editor) */}
                <div
                    className="overflow-auto bg-zinc-800 rounded-xl border border-zinc-700 shadow-lg transition-all"
                    style={{
                        width: typeof window !== 'undefined' && window.innerWidth > 768 ? `${leftWidth}%` : '100%',
                        height: typeof window !== 'undefined' && window.innerWidth <= 768 ? '40%' : '100%'
                    }}
                >
                    <EditorContainer onGenerateSuccess={handleWorldGenerated} />
                </div>

                {/* Divider / Resizer (Nur sichtbar/aktiv ab md-Breakpoint) */}
                <div
                    onMouseDown={startResizing}
                    className="hidden md:flex w-1.5 h-full bg-zinc-950 hover:bg-blue-600 cursor-col-resize transition-colors items-center justify-center relative z-10 rounded-full"
                >
                    <div className="absolute h-10 w-1 bg-zinc-600 rounded-full"></div>
                </div>

                {/* Right Page (Simulation) */}
                <div className="flex-1 overflow-hidden bg-zinc-950 rounded-xl border border-zinc-700 shadow-2xl relative">
                    {worldConfig ? (
                        <SimulationContainer config={worldConfig} />
                    ) : (
                        <div className="flex items-center justify-center h-full text-zinc-500 italic">
                            Waiting for world configuration...
                        </div>
                    )}
                </div>

            </main>
        </div>
    );
};

export default Dashboard;