import {useEffect, useState} from "react";
import type {Edge} from "@xyflow/react";
import {EditorSidebar} from "./node_editor/EditorSidebar.tsx";
import NodeEditor from "./node_editor/NodeEditor.tsx";

import {
    type Node
} from '@xyflow/react';

import {nodeRegistry, type NodeType} from "./node_editor/nodes/NodeDefinitions.ts";
import {createNodeData} from "./node_editor/nodes/NodeFactory.ts";
import FetchButton from "./components/FetchButton.tsx";
import {createEmptyWorldTemplate} from "./node_editor/EmptyCanvasTemplate.tsx";

interface EditorProps {
    onGenerateSuccess: (config: any) => void
}

export const  EditorContainer: React.FC<EditorProps> = ({ onGenerateSuccess }) => {
    const [nodes, setNodes] = useState<Node[]>([]);
    const [edges, setEdges] = useState<Edge[]>([]);

    const loadTemplate = () => {
        const { nodes, edges } = createEmptyWorldTemplate();

        setNodes(nodes);
        setEdges(edges);
    };

    useEffect(() => {
        loadTemplate();
    },[]);

    const addNode = (type: NodeType) => {

        const def = nodeRegistry[type]

        const node = {
            id: `node_${crypto.randomUUID()}`,
            type,
            position: { x: 300, y: 100 },
            deletable: def.deletable ?? true,
            data: createNodeData(type)
        }

        setNodes((nds) => [...nds, node])
    }

    const handleGenerate = async () => {
        await new Promise(resolve => setTimeout(resolve, 2000));

        const response = await fetch('/api/sim/config/load-default', {
            method: 'POST',
        });
        if (!response.ok) return;

        const preview = await response.json();

        onGenerateSuccess(preview);
    };

    return (
        <div className="flex h-full w-full overflow-hidden bg-slate-700 ">
            <EditorSidebar onAddNode={addNode}/>

            <div className="flex-1 h-full relative">
                <div className="flex flex-row justify-between border-b border-slate-500 w-full p-2 items-center bg-slate-900 shadow-md">
                    <FetchButton
                        baseStyle={"font-bold flex flex-row p-2 text-white rounded focus:outline-none"}
                        styleOnLoad={"bg-grey cursor-not-allowed"}
                        styleOnReady={"bg-blue-600 cursor-pointer hover:bg-blue-800 delay-50 duration-300"}
                        onClick={loadTemplate}
                    >
                        Empty Canvas
                    </FetchButton>

                    <FetchButton
                        baseStyle={"font-bold flex flex-row p-2 text-white rounded focus:outline-none"}
                        styleOnLoad={"bg-grey cursor-not-allowed"}
                        styleOnReady={"bg-blue-600 cursor-pointer hover:bg-blue-800 delay-50 duration-300"}
                        onClick={handleGenerate}
                    >
                        Build
                    </FetchButton>
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