import { Position, type NodeProps, useReactFlow } from "@xyflow/react";
import { nodeRegistry, type NodeType } from "./NodeDefinitions";
import ConnectionLimitHandle from "./ConnectionLimitHandle";
import {useEffect} from "react";

export function GenericNode({ id, type, data, selected }: NodeProps) {
    const rf = useReactFlow();
    const def = nodeRegistry[type as NodeType];

    const handleSpacing = 18;
    const handleOffset = 46;

    const handleCount = Math.max(def.inputs.length, def.outputs.length);
    const paramsOffset = handleCount * handleSpacing;

    useEffect(() => {
        if (!def?.params) return;

        const updates: Record<string, any> = {};

        Object.entries(def.params).forEach(([key, param]) => {
            if (data?.[key] === undefined && param.default !== undefined) {
                updates[key] = param.default;
            }
        });

        if (Object.keys(updates).length > 0) {
            rf.setNodes((nodes) =>
                nodes.map((n) =>
                    n.id === id
                        ? { ...n, data: { ...n.data, ...updates } }
                        : n
                )
            );
        }
    }, []);

    function updateParam(key: string, value: any) {
        rf.setNodes((nodes) =>
            nodes.map((node) => {
                if (node.id !== id) return node;

                return {
                    ...node,
                    data: {
                        ...node.data,
                        [key]: value,
                    },
                };
            })
        );
    }

    function renderParam(key: string, param: any) {
        const value = data?.[key] ?? param.default;

        switch (param.type) {
            case "float":
            case "int":
                return (
                    <input
                        type="number"
                        step={param.step ?? 0.01}
                        min={param.min}
                        max={param.max}
                        value={value}
                        onChange={(e) => updateParam(key, Number(e.target.value))}
                        className="w-full bg-zinc-800 border border-zinc-700 rounded px-2 py-1 text-xs text-lime-300"
                    />
                );

            case "select":
                return (
                    <select
                        value={value}
                        onChange={(e) => updateParam(key, e.target.value)}
                        className="w-full bg-zinc-800 border border-zinc-700 rounded px-2 py-1 text-xs text-lime-300"
                    >
                        {param.options?.map((o: string) => (
                            <option key={o} value={o}>
                                {o}
                            </option>
                        ))}
                    </select>
                );

            case "bool":
            case "boolean":
                return (
                    <input
                        type="checkbox"
                        checked={value}
                        onChange={(e) => updateParam(key, e.target.checked)}
                    />
                );

            case "image":

                return (
                    <div className="flex flex-col gap-2 items-center">

                        {value && (
                            <img
                                src={value}
                                className="w-20 h-20 object-cover rounded border border-zinc-700"
                             alt={"image-preview"}/>
                        )}

                        <input
                            type="file"
                            accept="image/png, image/jpeg"
                            onChange={(e) => {

                                const file = e.target.files?.[0]
                                if (!file) return

                                const reader = new FileReader()

                                reader.onload = () => {
                                    updateParam(key, reader.result)
                                }

                                reader.readAsDataURL(file)
                            }}
                            className="text-[8px] p-1 text-zinc-400 max-w-[100px] border border-dashed border-zinc-200 cursor-pointer rounded-md hover:bg-zinc-700"
                        />

                    </div>
                )

            case "text":
                return (
                    <input className="w-full bg-zinc-800 border border-zinc-700 rounded px-2 py-1 text-xs text-lime-300"
                           type="text"
                           value={value}
                           placeholder={param.default}
                           minLength={param.min}
                           maxLength={param.max}
                           onChange={(e) => updateParam(key, e.target.value)}
                    />
                )
            default:
                return null;
        }
    }

    function groupParams(params: Record<string, any>) {
        const entries = Object.entries(params);
        const rows: [string, any][][] = [];

        let currentRow: [string, any][] = [];

        for (const entry of entries) {
            const [, param] = entry;

            if (param.inline) {
                currentRow.push(entry);

                if (currentRow.length === 2) {
                    rows.push(currentRow);
                    currentRow = [];
                }
            } else {
                if (currentRow.length) {
                    rows.push(currentRow);
                    currentRow = [];
                }

                rows.push([entry]);
            }
        }

        if (currentRow.length) rows.push(currentRow);

        return rows;
    }

    const paramRows = groupParams(def.params);

    return (
        <div
            className={`bg-zinc-900 border ${
                selected ? "border-lime-300" : "border-zinc-700"
            } rounded-md min-w-[140px] relative`}
        >
            {/* Header */}
            <div className="flex flex-col px-3 py-2 border-b border-zinc-700 font-semibold text-zinc-200 text-xs">
                <p className="text-[8px]">{def.category}</p>
                {def.label}
            </div>

            {/* Inputs */}
            {def.inputs.map((input, idx) => (
                <div
                    key={input.id}
                    className="absolute left-0 flex items-center"
                    style={{
                        top: handleOffset + idx * handleSpacing,
                    }}
                >
                    <ConnectionLimitHandle
                        type="target"
                        position={Position.Left}
                        id={input.id}
                        connectionCount={input.connectionCount}
                    />
                    <span className="ml-2 text-[10px] text-zinc-400">{input.label}</span>
                </div>
            ))}

            {/* Outputs */}
            {def.outputs.map((output, idx) => (
                <div
                    key={output.id}
                    className="absolute right-0 flex items-center"
                    style={{
                        top: handleOffset + idx * handleSpacing,
                    }}
                >
          <span className="mr-2 text-[10px] text-zinc-400">
            {output.label}
          </span>
                    <ConnectionLimitHandle
                        type="source"
                        position={Position.Right}
                        id={output.id}
                        connectionCount={output.connectionCount}
                    />
                </div>
            ))}

            {/* Params */}
            <div
                className="p-3 space-y-2"
                style={{
                    marginTop: paramsOffset,
                }}
            >
                {paramRows.map((row, i) => (
                    <div
                        key={i}
                        className={`grid gap-2 ${
                            row.length === 2 ? "grid-cols-2" : "grid-cols-1"
                        }`}
                    >
                        {row.map(([key, param]) => (
                            <div key={key} className="flex flex-col gap-1">
                                {param.hideLabel ? "" :  <label className="text-zinc-400 text-xs">{key}</label>}
                                {param.hideInput ? "" : renderParam(key, param)}
                            </div>
                        ))}
                    </div>
                ))}
            </div>
        </div>
    );
}