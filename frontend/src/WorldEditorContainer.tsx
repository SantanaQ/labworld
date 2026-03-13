import {useCallback, useState} from "react";
import type {Edge} from "@xyflow/react";
import {EditorSidebar} from "./EditorSidebar.tsx";
import NodeEditor from "./NodeEditor.tsx";

import {
    type Node
} from '@xyflow/react';

const initialNodes: Node[] = [
    { id: '1', data: { label: 'Node 1' }, position: { x: 5, y: 5 } },
    { id: '2', data: { label: 'Node 2' }, position: { x: 5, y: 100 } },
    { id: '3', data: { label: 'Node 3' }, position: { x: 5, y: 150 } },
];

const initialEdges: Edge[] = [
    { id: 'e1-2', source: '1', target: '2' },
    { id: 'e3-1', source: '3', target: '1' }
];

interface EditorProps {
    onGenerateSuccess: (config: any) => void
}

export const  WorldEditorContainer: React.FC<EditorProps> = ({ onGenerateSuccess }) => {
    const [nodes, setNodes] = useState<Node[]>(initialNodes);
    const [edges, setEdges] = useState<Edge[]>(initialEdges);

    const addNode = useCallback((label: string, type: string) => {
        const newNode: Node = {
            id: `node_${Date.now()}`, // Eindeutige ID
            type: type,
            data: { label: label },
            position: { x: 50, y: 50 }, // Standard-Position
        };
        setNodes((nds) => [...nds, newNode]);
    }, []);

    const handleGenerate = async () => {
        //const response = await fetch('/api/world/configure', { /* ... */ });
        //const data = await response.json();

        console.log("EDITOR: Generierung gestartet...");

        // Hier würde später der fetch() stehen.
        // Wir simulieren die API-Antwort:
        const mockConfig = {
            worldId: "sim_" + Math.random().toString(36).substr(2, 9),
            width: 2000,  // Teste hier mal verschiedene Werte
            height: 2000
        };

        // Das "Kabel" zum WorldDashboard aktivieren
        onGenerateSuccess(mockConfig);

        //nGenerateSuccess(data);
    };

    return (
        <div className="flex h-full w-full overflow-hidden bg-slate-800">
            <EditorSidebar onAddNode={addNode}/>

            <div className="flex-1 h-full relative bg-slate-800">
                <div className="flex flex-row justify-between border-b border-slate-700 w-full p-2 items-center">
                    <h1 className="text-white font-bold">World Editor</h1>
                    <div className="">
                        <button
                            onClick={handleGenerate}
                            className="bg-blue-600 font-bold flex flex-row p-2 text-white rounded cursor-pointer hover:bg-blue-800 delay-50 duration-300 focus:outline-none"
                        >
                            Generate
                        </button>
                    </div>
                </div>

                <NodeEditor
                    nodes={nodes}
                    setNodes={setNodes}
                    edges={edges}
                    setEdges={setEdges}
                />

            </div>
        </div>
    );
}