import { Handle, useNodeConnections } from '@xyflow/react';

const ConnectionLimitHandle = (props) => {
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
