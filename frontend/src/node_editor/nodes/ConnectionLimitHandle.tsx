import {
    Handle,
    type HandleProps,
    useNodeConnections,
    useReactFlow,
    type IsValidConnection
} from '@xyflow/react';

import { nodeRegistry, type NodeType } from "./NodeDefinitions";

type ConnectionLimitHandleProps = HandleProps & {
    connectionCount: number;
};

const ConnectionLimitHandle = ({ connectionCount, ...props }: ConnectionLimitHandleProps) => {
    const rf = useReactFlow();

    const connections = useNodeConnections({
        handleType: props.type,
        handleId: props.id ?? undefined,
    });

    const isValidConnection: IsValidConnection = (connection) => {
        const sourceNode = rf.getNode(connection.source!);
        const targetNode = rf.getNode(connection.target!);

        if (!sourceNode || !targetNode) return false;

        const sourceDef = nodeRegistry[sourceNode.type as NodeType];
        const targetDef = nodeRegistry[targetNode.type as NodeType];

        const sourceHandle = sourceDef.outputs.find(
            (h) => h.id === connection.sourceHandle
        );

        const targetHandle = targetDef.inputs.find(
            (h) => h.id === connection.targetHandle
        );

        if (!sourceHandle || !targetHandle) return false;

        if (sourceHandle.accepts) {
            if (!sourceHandle.accepts.includes(targetHandle.kind)) {
                return false;
            }
        }

        if (targetHandle.accepts) {
            if (!targetHandle.accepts.includes(sourceHandle.kind)) {
                return false;
            }
        }

        return true;
    };

    return (
        <Handle
            {...props}
            isValidConnection={isValidConnection}
            isConnectable={connections.length < connectionCount}
        />
    );
};

export default ConnectionLimitHandle;