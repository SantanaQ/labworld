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
    const [leftWidth, setLeftWidth] = useState<number>(85);
    const [worldConfig, setWorldConfig] = useState<WorldConfig | null>(null);
    const [resizing, setResizing] = useState(false);
    const isResizing = useRef<boolean>(false);
    const containerRef = useRef<HTMLDivElement>(null);

    const startResizing = useCallback(() => {
        isResizing.current = true;
        setResizing(true);
        document.body.style.userSelect = 'none';
        document.body.style.cursor = 'col-resize';
    }, []);

    const stopResizing = useCallback(() => {
        isResizing.current = false;
        setResizing(false);
        document.body.style.userSelect = 'auto';
        document.body.style.cursor = 'default';
    }, []);

    const resize = useCallback((e: MouseEvent) => {
        if (!isResizing.current || !containerRef.current) return;

        const containerRect = containerRef.current.getBoundingClientRect();
        const newWidth = ((e.clientX - containerRect.left) / containerRect.width) * 100;

        if (newWidth >= 0 && newWidth <= 85) {
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

    return (
        <div className="flex flex-col h-screen w-full overflow-hidden bg-zinc-900 text-zinc-100 relative">

            {/* Overlay */}
            {resizing && (
                <div
                    className="fixed inset-0 z-50 cursor-col-resize"
                    style={{ background: 'transparent' }}
                />
            )}

            <header className="flex-none p-2 border-b border-zinc-700 bg-zinc-800/50 backdrop-blur-sm">
                <h1 className="pl-2 text-xl font-bold tracking-tight">Labworld 🔬🌍</h1>
            </header>

            <main
                ref={containerRef}
                className="flex-1 flex flex-col md:flex-row overflow-hidden p-2 gap-2"
            >
                {/* Left Page (Editor) */}
                <div
                    className="overflow-auto bg-zinc-800 rounded-xl border border-zinc-700 shadow-lg"
                    style={{ width: `${leftWidth}%` }}
                >
                    <EditorContainer onGenerateSuccess={setWorldConfig} />
                </div>

                {/* Resizer Handle */}
                <div
                    onMouseDown={startResizing}
                    className={`hidden md:flex w-1.5 h-full hover:bg-blue-600 cursor-col-resize transition-colors items-center justify-center relative z-20 rounded-full ${resizing ? 'bg-blue-500' : 'bg-zinc-950'}`}
                >
                    <div className="absolute h-10 w-1 bg-zinc-600 rounded-full"></div>
                </div>

                {/* Right Page (Simulation) */}
                <div className="flex-1 overflow-hidden bg-zinc-950 rounded-xl border border-zinc-700 shadow-2xl">
                    {worldConfig ? (
                        <SimulationContainer key={worldConfig.sessionId} config={worldConfig} />
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