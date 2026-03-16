import { Position, type NodeProps, useReactFlow } from '@xyflow/react';
import {nodeRegistry, type NodeType, type ParamType} from "./NodeDataTypes"
import ConnectionLimitHandle from "./ConnectionLimitHandle";

export function GenericNode({ id, type, data , selected}: NodeProps) {

    const rf = useReactFlow()
    const def = nodeRegistry[type as NodeType]

    function updateParam(key: string, value: any) {

        rf.setNodes(nodes =>
            nodes.map(node => {

                if (node.id !== id)
                    return node

                return {
                    ...node,
                    data: {
                        ...node.data,
                        [key]: value
                    }
                }

            })
        )

    }

    function renderParam(key: string, param: any) {

        const value = data?.[key]

        switch (param.type) {

            case "float":
            case "int":

                return (
                    <input
                        type="number"
                        step={param.step ?? 0.01}
                        min={param.min}
                        max={param.max}
                        value={value as ParamType}
                        onChange={(e) => updateParam(key, Number(e.target.value))}
                        className="w-full bg-slate-800 border border-slate-700 rounded px-2 py-1 text-xs text-white"
                    />
                )

            case "select":

                return (
                    <select
                        value={value as ParamType}
                        onChange={(e) => updateParam(key, e.target.value)}
                        className="w-full bg-slate-800 border border-slate-700 rounded px-2 py-1 text-xs"
                    >
                        {param.options?.map((o: string) => (
                            <option key={o} value={o}>
                                {o}
                            </option>
                        ))}
                    </select>
                )

            default:
                return null
        }

    }

    return (
        <div className={`bg-slate-900 border ${selected ? "border-green-400" : "border-slate-700"} rounded-md text-xs min-w-[120px] relative`}>

            {/* Title */}
            <div className="px-3 py-2 border-b border-slate-700 font-semibold text-slate-200">
                {def.label}
            </div>

            {/* Params */}
            <div className="p-3 space-y-2 mt-3">
                {Object.entries(def.params).map(([key, param]) => (
                    <div key={key} className="flex flex-col gap-1">
                        <label className="text-slate-400">{key}</label>
                        {renderParam(key, param)}
                    </div>
                ))}
            </div>

            {/* Inputs */}
            {def.inputs.map((input, idx) => (
                <div
                    key={input.id}
                    className="absolute left-0 flex items-center gap"
                    style={{
                        top: `${40 + idx * 30}px`,
                    }}
                >
                    <ConnectionLimitHandle
                        type="target"
                        position={Position.Left}
                        id={input.id}
                        connectionCount={input.connectionCount}
                    />
                    <span className="ml-2 text-xs text-slate-400">{input.label}</span>
                </div>
            ))}

            {/* Outputs */}
            {def.outputs.map((output, idx) => (
                <div
                    key={output.id}
                    className="absolute right-0 flex items-center gap-1"
                    style={{
                        top: `${40 + idx * 30}px`,
                    }}
                >
                    <span className="mr-2  text-xs text-slate-400">{output.label}</span>
                    <ConnectionLimitHandle
                        type="source"
                        position={Position.Right}
                        id={output.id}
                        connectionCount={output.connectionCount}
                    />
                </div>
            ))}

        </div>
    )

}