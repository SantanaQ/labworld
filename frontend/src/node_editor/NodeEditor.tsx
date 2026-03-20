import React, { useCallback } from 'react';
import {
    ReactFlow,
    addEdge,
    applyNodeChanges,
    applyEdgeChanges,
    type Node,
    type Edge,
    type OnConnect,
    type OnNodesChange,
    type OnEdgesChange, Background, BackgroundVariant,
} from '@xyflow/react';

import '@xyflow/react/dist/style.css';

import {GenericNode} from "./nodes/GenericNode.tsx";

export const nodeTypes = {
    clamp: GenericNode,
    normalize: GenericNode,
    binaryThreshold: GenericNode,
    softThreshold: GenericNode,
    suitabilityMask: GenericNode,
    suitabilityDecay: GenericNode,

    image: GenericNode,
    fractalNoise: GenericNode,
    clusteredPatchNoise: GenericNode,
    holeMaskNoise: GenericNode,

    copyStateUpdater: GenericNode,
    diffusionGrowthUpdater: GenericNode,
    diffusionRelaxationUpdater: GenericNode,

    defaultPotentialUpdater: GenericNode,

    heatLayer: GenericNode,
    supplyLayer: GenericNode,
    scentLayer: GenericNode,

    world: GenericNode,

    agents: GenericNode,
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
        (deletedNodes: Node[]) => {
            let currentEdges = [...edges];
            let currentNodes = [...nodes];

            deletedNodes.forEach((node) => {
                const incomingEdges = currentEdges.filter((e) => e.target === node.id);
                const outgoingEdges = currentEdges.filter((e) => e.source === node.id);

                const newEdges: Edge[] = [];

                incomingEdges.forEach((inEdge) => {
                    outgoingEdges.forEach((outEdge) => {
                        newEdges.push({
                            id: `reconnect-${inEdge.source}-${outEdge.target}`,
                            source: inEdge.source,
                            sourceHandle: inEdge.sourceHandle,
                            target: outEdge.target,
                            targetHandle: outEdge.targetHandle,
                        });
                    });
                });

                currentEdges = currentEdges.filter(
                    (e) => e.target !== node.id && e.source !== node.id
                );

                currentEdges = [...currentEdges, ...newEdges];

                currentNodes = currentNodes.filter((n) => n.id !== node.id);
            });

            setEdges(currentEdges);
        },
        [nodes, edges, setEdges]
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
            >
                <Background color="#ccc" variant={BackgroundVariant.Dots} />
            </ReactFlow>
        </div>
    );
}