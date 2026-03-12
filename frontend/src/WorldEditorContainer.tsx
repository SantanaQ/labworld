// In deiner Haupt-Komponente (SplitScreen)
// ... (Imports wie ReactFlow, Sidebar etc.)

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

export default function WorldEditorContainer() {
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

    return (
        <div className="flex h-full w-full overflow-hidden">
            {/* Sidebar ganz links */}
            <EditorSidebar onAddNode={addNode} />

            {/* Der eigentliche Node-Editor füllt den Rest */}
            <div className="flex-1 h-full relative bg-slate-800">
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