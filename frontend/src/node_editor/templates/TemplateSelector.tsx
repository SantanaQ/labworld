import React, {useState} from "react";
import type {Node, Edge} from "@xyflow/react";
import { createDefaultWorldTemplate } from "./DefaultWorldTemplate.ts";
import {createEmptyCanvasTemplate} from "./EmptyCanvasTemplate.ts";

type TemplateOption = {
    label: string;
    value: string;
    loader: () => { nodes: Node[]; edges: Edge[] };
};

const templates: TemplateOption[] = [
    {
        label: "Empty Canvas",
        value: "empty",
        loader: createEmptyCanvasTemplate,
    },
    {
        label: "Small Noise",
        value: "example",
        loader: createDefaultWorldTemplate,
    },
];

interface TemplateSelectorProps {
    setNodes: (nodes: Node[]) => void;
    setEdges: (edges: Edge[]) => void;
}

const TemplateSelector: React.FC<TemplateSelectorProps> = ({ setNodes, setEdges }) => {
    const [selected, setSelected] = useState<string>("empty");

    const handleChange = (e: React.ChangeEvent<HTMLSelectElement>) => {
        const value = e.target.value;
        setSelected(value);

        const template = templates.find((t) => t.value === value);
        if (template) {
            const { nodes, edges } = template.loader();
            setNodes(nodes);
            setEdges(edges);
        }
    };

    return (
        <div className="flex items-center gap-2 min-w-fit">
            <p className="text-white font-bold">Presets</p>
            <select
                id="templateSelect"
                value={selected}
                onChange={handleChange}
                className="border rounded p-1 bg-slate-800 text-white border border-slate-700 rounded"
            >
                {templates.map((t) => (
                    <option key={t.value} value={t.value}>
                        {t.label}
                    </option>
                ))}
            </select>
        </div>
    );
};

export default TemplateSelector;