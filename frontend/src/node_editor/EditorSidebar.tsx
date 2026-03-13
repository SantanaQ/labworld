import React, { useState } from 'react';
import { ChevronDown, ChevronRight, Plus } from 'lucide-react';

interface SidebarCategory {
    title: string;
    items: { id: string; label: string; type: string }[];
}

const categories: SidebarCategory[] = [
    {
        title: 'Signal',
        items: [
            { id: 'sFN', label: 'Fractal Noise', type: 'output' },
            { id: 'sCPN', label: 'Clustered Patch Noise', type: 'output' },
            { id: 'sHMN', label: 'Hole Mask Noise', type: 'output' },
        ],
    },
    {
        title: 'Time Behavior',
        items: [
            { id: 'tF', label: 'Fixed', type: 'output' },
            { id: 'tD', label: 'Drift', type: 'output' },
            { id: 'tDW', label: 'Domain Warping', type: 'output' },
            { id: 'tC', label: 'Composition', type: 'output' },
        ],
    },
    {
        title: 'Compositing',
        items: [
            { id: 'cN', label: 'Normalize', type: 'default' },
            { id: 'cC', label: 'Clamp', type: 'default' },
            { id: 'cST', label: 'Soft Threshold', type: 'default' },
            { id: 'cBT', label: 'Binary Threshold', type: 'default' },
            { id: 'cSM', label: 'Suitability Mask', type: 'default' },
            { id: 'cSD', label: 'Suitability Decay', type: 'default' },
        ],
    },
    {
        title: 'Base Update',
        items: [
            { id: 'buD', label: 'Default', type: 'output' },
        ],
    },
    {
        title: 'State Update',
        items: [
            { id: 'suDG', label: 'Diffusion and Growth', type: 'output' },
            { id: 'suDR', label: 'Diffusion and Relaxation', type: 'output' },
            { id: 'suCB', label: 'Copy Base', type: 'output' },
        ],
    },
];

interface EditorSidebarProps {
    onAddNode: (label: string, type: string) => void;
}

export const EditorSidebar: React.FC<EditorSidebarProps> = ({ onAddNode }) => {
    const [openCats, setOpenCats] = useState<string[]>(['Logik']);

    const toggleCat = (title: string) => {
        setOpenCats(prev =>
            prev.includes(title) ? prev.filter(t => t !== title) : [...prev, title]
        );
    };

    return (
        <div className="w-64 h-full bg-slate-900 border-r border-slate-700 flex flex-col shrink-0">
            <div className="p-4 border-b border-slate-700">
                <span className="text-xs font-semibold text-slate-500 uppercase tracking-wider">Components</span>
            </div>
            <div className="flex-1 overflow-y-auto">
                {categories.map((cat) => (
                    <div key={cat.title} className="border-b border-slate-800/50">
                        <button
                            onClick={() => toggleCat(cat.title)}
                            className="w-full flex items-center justify-between p-3 hover:bg-slate-800 transition-colors"
                        >
                            <span className="text-sm font-medium text-slate-300">{cat.title}</span>
                            {openCats.includes(cat.title) ? <ChevronDown size={16} /> : <ChevronRight size={16} />}
                        </button>

                        {openCats.includes(cat.title) && (
                            <div className="bg-slate-950/30 py-1">
                                {cat.items.map((item) => (
                                    <button
                                        key={item.id}
                                        onClick={() => onAddNode(item.label, item.type)}
                                        className="w-full flex items-center gap-3 px-6 py-2 text-sm text-slate-400 hover:text-white hover:bg-blue-600/20 transition-all group"
                                    >
                                        <Plus size={14} className="group-hover:scale-125 transition-transform" />
                                        {item.label}
                                    </button>
                                ))}
                            </div>
                        )}
                    </div>
                ))}
            </div>
        </div>
    );
};