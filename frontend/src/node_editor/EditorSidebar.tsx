import React, {useMemo, useState} from "react"
import { ChevronDown, ChevronRight, Plus } from "lucide-react"

import { nodeRegistry, type NodeType } from "./nodes/NodeDefinitions.ts"

interface EditorSidebarProps {
    onAddNode: (type: NodeType) => void
}

/*
Build category structure from registry
*/
function buildCategories() {

    const categories: Record<string, { type: NodeType; label: string }[]> = {}

    Object.entries(nodeRegistry).forEach(([type, def]) => {

        const nodeType = type as NodeType

        if (!categories[def.category]) {
            categories[def.category] = []
        }

        categories[def.category].push({
            type: nodeType,
            label: def.label
        })

    })

    return categories
}

export const EditorSidebar: React.FC<EditorSidebarProps> = ({ onAddNode }) => {

    const [openCats, setOpenCats] = useState<string[]>([])
    const categories = useMemo(buildCategories, [])

    const toggleCat = (title: string) => {
        setOpenCats(prev =>
            prev.includes(title)
                ? prev.filter(t => t !== title)
                : [...prev, title]
        )
    }

    return (
        <div className="w-55 h-full bg-slate-900 border-r border-slate-500 flex flex-col shrink-0">

            <div className="p-4 border-b border-slate-500">
                <span className="text-xs font-semibold text-slate-500 uppercase tracking-wider">
                  Modifier
                </span>
            </div>

            <div className="flex-1 overflow-y-auto">

                {Object.entries(categories).map(([catTitle, items]) => (

                    <div key={catTitle} className="border-b border-slate-800/50">

                        <button
                            onClick={() => toggleCat(catTitle)}
                            className="w-full flex items-center justify-between p-3 hover:bg-slate-800 transition-colors"
                        >

              <span className="text-sm font-medium text-slate-300">
                {catTitle}
              </span>

                            {openCats.includes(catTitle)
                                ? <ChevronDown size={16} color={"white"}/>
                                : <ChevronRight size={16} color={"white"}/>
                            }

                        </button>

                        {openCats.includes(catTitle) && (

                            <div className="bg-slate-950/30 py-1">

                                {items.map(item => (

                                    <button
                                        key={item.type}
                                        onClick={() => onAddNode(item.type)}
                                        className="w-full flex items-center gap-3 px-6 py-2 text-sm text-slate-400 hover:text-white hover:bg-blue-600/20 transition-all group"
                                    >

                                        <Plus size={14} className="group-hover:scale-125 transition-transform"/>

                                        {item.label}

                                    </button>

                                ))}

                            </div>

                        )}

                    </div>

                ))}

            </div>
            <div className="text-slate-600 italic test-sx">
                <p>*delete nodes with backspace</p>
            </div>

        </div>
    )
}