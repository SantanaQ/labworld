import {useState} from "react";
import type {Edge} from "@xyflow/react";
import {EditorSidebar} from "./node_editor/EditorSidebar.tsx";
import NodeEditor from "./node_editor/NodeEditor.tsx";

import {
    type Node
} from '@xyflow/react';

import {nodeRegistry, type NodeType} from "./node_editor/nodes/NodeDataTypes.ts";
import {createNodeData} from "./node_editor/nodes/NodeFactory.ts";

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

export const  EditorContainer: React.FC<EditorProps> = ({ onGenerateSuccess }) => {
    const [nodes, setNodes] = useState<Node[]>(initialNodes);
    const [edges, setEdges] = useState<Edge[]>(initialEdges);
    const [loading, setLoading] = useState(false);

    const addNode = (type: NodeType) => {

        const def = nodeRegistry[type]

        const node = {
            id: `node_${Date.now()}`,
            type,
            position: { x: 100, y: 100 },
            deletable: def.deletable ?? true,
            data: createNodeData(type)
        }

        setNodes((nds) => [...nds, node])
    }

    const handleGenerate = async () => {
        if(loading) return;

        setLoading(true);
        try {
            await new Promise(resolve => setTimeout(resolve, 2000));

            const response = await fetch('/api/sim/config/load-default', {
                method: 'POST',
            });
            if (!response.ok) return;

            const preview = await response.json();

            onGenerateSuccess(preview);
        } catch (e) {
            console.error(e);
        } finally {
            setLoading(false);
        }

    };

    return (
        <div className="flex h-full w-full overflow-hidden bg-slate-700">
            <EditorSidebar onAddNode={addNode}/>

            <div className="flex-1 h-full relative">
                <div className="flex flex-row justify-end border-b border-slate-500 w-full p-2 items-center bg-slate-900 shadow-md">
                    {/*<h1 className="text-white font-bold">World Editor</h1>*/}
                    <div className="">
                        <button
                            onClick={handleGenerate}
                            disabled={loading}
                            className={`${loading ? "bg-grey cursor-not-allowed" :  "bg-blue-600"} font-bold flex flex-row p-2 text-white rounded cursor-pointer hover:bg-blue-800 delay-50 duration-300 focus:outline-none`}
                        >
                            {loading ? (
                                <div className="flex justify-center items-center">
                                    <svg className="animate-spin -ml-1 mr-3 h-5 w-5 text-white" xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24">
                                        <circle className="opacity-25" cx="12" cy="12" r="10" stroke="currentColor" strokeWidth="4"></circle>
                                        <path className="opacity-75" fill="currentColor" d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4zm2 5.291A7.962 7.962 0 014 12H0c0 3.042 1.135 5.824 3 7.938l3-2.647z"></path>
                                    </svg>
                                    Loading...
                                </div>
                            ) : (
                                "Build"
                            )}
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