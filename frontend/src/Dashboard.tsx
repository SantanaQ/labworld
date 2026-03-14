import React, { useState, useRef, useEffect, useCallback } from 'react';

import {EditorContainer} from "./EditorContainer.tsx";
import {SimulationContainer} from "./SimulationContainer.tsx";

export interface WorldConfig {
    worldId: number;
    width: number;
    height: number;
    heat: Float32Array;
    supply: Float32Array;
    scent: Float32Array;
}



const Dashboard: React.FC = () => {
    const [leftWidth, setLeftWidth] = useState<number>(50);
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

        if (newWidth > 1 && newWidth < 90) {
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
            worldId: config.worldId,
            width: config.width,
            height: config.height,
            heat: config.heat,
            supply: config.supply,
            scent: config.scent,
        });
    };

    return (
        <div className="flex h-screen w-full overflow-hidden bg-gray-900">

            {/* Left Page */}
            <div
                className="overflow-auto bg-slate-800"
                style={{ width: `${leftWidth}%` }}
            >
                <EditorContainer onGenerateSuccess={handleWorldGenerated} />
            </div>

            {/* Divider / Resizer */}
            <div
                onMouseDown={startResizing}
                className="w-1.5 h-full bg-black hover:bg-blue-500 cursor-col-resize transition-colors flex items-center justify-center relative z-10"
            >
                {/* Handle */}
                <div className="absolute h-12 w-1 bg-gray-600 rounded-full"></div>
            </div>

            {/* Right Page */}
            <div className="flex-1 overflow-auto bg-slate-700">
                <div className="flex-1 overflow-hidden relative">
                    <SimulationContainer config={worldConfig}/>
                </div>
            </div>

        </div>
    );
};

export default Dashboard;