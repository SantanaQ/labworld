import React, {useMemo, useState} from "react"
import { ChevronDown, ChevronRight, Plus, Menu } from "lucide-react"

import {modifierRegistry, type NodeType} from "./nodes/NodeDefinitions.ts"

interface EditorSidebarProps {
    onAddNode: (type: NodeType) => void
}

function buildCategories() {
    const categories: Record<string, { type: NodeType; label: string }[]> = {}
    Object.entries(modifierRegistry).forEach(([type, def]) => {
        const nodeType = type as NodeType
        if (!categories[def.category]) {
            categories[def.category] = []
        }
        categories[def.category].push({ type: nodeType, label: def.label })
    })
    return categories
}

export const EditorSidebar: React.FC<EditorSidebarProps> = ({ onAddNode }) => {
    const [openCats, setOpenCats] = useState<string[]>([])
    const [collapsed, setCollapsed] = useState(false)

    const categories = useMemo(buildCategories, [])

    const toggleCat = (title: string) => {
        setOpenCats(prev =>
            prev.includes(title)
                ? prev.filter(t => t !== title)
                : [...prev, title]
        )
    }

    return (
        <div className={`h-full bg-slate-900 border-r border-slate-500 flex flex-col shrink-0 shadow-xl transition-all duration-300 ${collapsed ? 'w-14' : 'w-55'}`}>

            {/* Header */}
            <div className="flex items-center justify-between p-4 border-b border-slate-500 h-15 shrink-0">
                {!collapsed && (
                    <span className="text-xs font-semibold text-slate-500 uppercase tracking-wider truncate">
                    Modifier
                </span>
                )}
                <button
                    onClick={() => setCollapsed(!collapsed)}
                    className="p-1 hover:bg-slate-700 rounded shrink-0"
                >
                    <Menu size={16} color="white"/>
                </button>
            </div>

            {/* Categories */}
            {!collapsed && (
                <div className="flex-1 overflow-y-auto overflow-x-hidden">
                    {Object.entries(categories).map(([catTitle, items]) => (
                        <div key={catTitle} className="border-b border-slate-800/50">
                            <button
                                onClick={() => toggleCat(catTitle)}
                                className="w-full flex items-center justify-between p-3 hover:bg-slate-800 transition-colors gap-2"
                            >
                                {/* truncate sorgt für ... wenn der Titel zu lang ist */}
                                <span className="text-sm font-medium text-slate-300 truncate whitespace-nowrap">
                                {catTitle}
                            </span>
                                <div className="shrink-0">
                                    {openCats.includes(catTitle)
                                        ? <ChevronDown size={16} color={"white"}/>
                                        : <ChevronRight size={16} color={"white"}/>
                                    }
                                </div>
                            </button>

                            {openCats.includes(catTitle) && (
                                <div className="bg-slate-950/30 py-1">
                                    {items.map(item => (
                                        <div key={item.type} className="relative group/item">
                                            <button
                                                title={item.label}
                                                onClick={() => onAddNode(item.type)}
                                                className="w-full flex items-center gap-3 px-6 py-2 text-sm text-slate-400 hover:text-white hover:bg-blue-600/20 transition-all overflow-hidden"
                                            >
                                                <Plus size={14}
                                                      className="group-hover/item:scale-125 transition-transform shrink-0"/>
                                                <span className="truncate whitespace-nowrap">
                                                {item.label}
                                            </span>
                                            </button>

                                            {/* Custom Tailwind Tooltip */}
                                            <div
                                                className="absolute left-full ml-2 px-2 py-1 bg-slate-700 text-white text-[10px] rounded opacity-0 group-hover/item:opacity-100 pointer-events-none transition-opacity whitespace-nowrap z-50 shadow-xl border border-slate-600 before:content-[''] before:absolute before:right-full before:top-1/2 before:-translate-y-1/2 before:border-4 before:border-transparent before:border-r-slate-700">
                                                Add {item.label}
                                            </div>
                                        </div>
                                    ))}
                                </div>
                            )}
                        </div>
                    ))}
                </div>
            )}

            {/* Footer */}
            {!collapsed && (
                <div className="text-slate-600 italic text-[10px] p-2 shrink-0">
                    <p className="truncate whitespace-nowrap">*delete nodes with backspace</p>
                </div>
            )}
        </div>
    )
}