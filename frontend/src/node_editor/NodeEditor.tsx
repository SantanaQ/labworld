import React, { useCallback } from 'react';
import {
    ReactFlow,
    addEdge,
    applyNodeChanges,
    applyEdgeChanges,
    type Node,
    type Edge,
    //type FitViewOptions,
    type OnConnect,
    type OnNodesChange,
    type OnEdgesChange, getIncomers, getOutgoers, getConnectedEdges,
    //type OnNodeDrag,
    //type DefaultEdgeOptions,
} from '@xyflow/react';

import '@xyflow/react/dist/style.css';

import {GenericNode} from "./nodes/GenericNode.tsx";

export const nodeTypes = {
    clamp: GenericNode
}

interface NodeEditorProps {
    nodes: Node[];
    setNodes: React.Dispatch<React.SetStateAction<Node[]>>;
    edges: Edge[];
    setEdges: React.Dispatch<React.SetStateAction<Edge[]>>;
}

export default function NodeEditor({ nodes, setNodes, edges, setEdges }: NodeEditorProps) {
    const onNodesChange: OnNodesChange = useCallback(
        (changes) => setNodes((nds) => applyNodeChanges(changes, nds)),
        [setNodes],
    );

    const onEdgesChange: OnEdgesChange = useCallback(
        (changes) => setEdges((eds) => applyEdgeChanges(changes, eds)),
        [setEdges],
    );

    const onConnect: OnConnect = useCallback(
        (connection) => setEdges((eds) => addEdge(connection, eds)),
        [setEdges],
    );

    const onEdgeClick = (_ : React.MouseEvent<Element, MouseEvent>, edge : Edge) => {
        setEdges((eds) => eds.filter((e) => e.id !== edge.id))
    }

    const onNodesDelete = useCallback(
        (deleted : Node[]) => {
            let remainingNodes = [...nodes];
            setEdges(
                deleted.reduce((acc, node: Node) => {
                    const incomers = getIncomers(node, remainingNodes, acc);
                    const outgoers = getOutgoers(node, remainingNodes, acc);
                    const connectedEdges = getConnectedEdges([node], acc);

                    const remainingEdges = acc.filter((edge : Edge) => !connectedEdges.includes(edge));

                    const createdEdges = incomers.flatMap(({ id: source }) =>
                        outgoers.map(({ id: target }) => ({
                            id: `${source}->${target}`,
                            source,
                            target,
                        })),
                    );

                    remainingNodes = remainingNodes.filter((rn) => rn.id !== node.id);

                    return [...remainingEdges, ...createdEdges];
                }, edges),
            );
        },
        [nodes, edges],
    );

    return (
        <div className="h-full w-full">
            <ReactFlow
                nodes={nodes}
                edges={edges}
                nodeTypes={nodeTypes}
                onNodesChange={onNodesChange}
                onNodesDelete={onNodesDelete}
                onEdgesChange={onEdgesChange}
                onConnect={onConnect}
                onEdgeClick={onEdgeClick}
                fitView
                proOptions={{hideAttribution: true}}
            />
        </div>
    );
}