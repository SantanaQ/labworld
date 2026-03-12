import React, { useState, useRef, useEffect, useCallback } from 'react';

//import NodeEditor from "./NodeEditor"
import WorldEditorContainer from "./WorldEditorContainer.tsx";
import {SimBridge} from "./SimBridge.tsx";

const SplitScreen: React.FC = () => {
    const [leftWidth, setLeftWidth] = useState<number>(50);
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

        if (newWidth > 5 && newWidth < 95) {
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
        <div className="flex h-screen w-full overflow-hidden bg-gray-900">

            {/* Left Page */}
            <div
                className="overflow-auto bg-slate-800"
                style={{ width: `${leftWidth}%` }}
            >
                <div className="p-5 pt-2">
                    <h1 className="font-bold mb-4 text-white">World-Editor</h1>

                </div>
                <WorldEditorContainer />

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
                <div className="p-5 pt-2">
                    <h3 className="font-bold mb-4 text-white">World-View</h3>
                </div>
                <div className="flex-1 overflow-hidden relative">
                    <SimBridge/>
                </div>
            </div>

        </div>
    );
};

export default SplitScreen;