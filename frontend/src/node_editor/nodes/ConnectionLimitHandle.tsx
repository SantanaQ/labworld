import {Handle, type HandleProps, useNodeConnections} from '@xyflow/react';

type ConnectionLimitHandleProps = HandleProps & {
    connectionCount: number;
};

const ConnectionLimitHandle = (props: ConnectionLimitHandleProps) => {
    const connections = useNodeConnections({
        handleType: props.type,
    });

    return (
        <Handle
            {...props}
            isConnectable={connections.length < props.connectionCount}
        />
    );
};

export default ConnectionLimitHandle;
