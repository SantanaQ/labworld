import React from 'react';

interface EditorSliderProps {
    label: string;
    value: number;
    min?: number;
    max?: number;
    step?: number;
    onChange: (value: number) => void;
}

export const EditorSlider: React.FC<EditorSliderProps> = ({
                                                              label, value, min = 0, max = 100, step = 1, onChange
                                                          }) => {
    return (
        <div className="flex flex-col gap-2 w-full p-2">
            <div className="flex justify-between items-center">
                <label className="text-[10px] uppercase tracking-widest text-slate-400 font-bold">
                    {label}
                </label>
                <span className="text-xs font-mono text-blue-400 bg-blue-400/10 px-1.5 py-0.5 rounded">
          {value}
        </span>
            </div>

            <input
                type="range"
                min={min}
                max={max}
                step={step}
                value={value}
                onChange={(e) => onChange(Number(e.target.value))}
                className="w-full h-1.5 bg-slate-700 rounded-lg appearance-none cursor-pointer
                   accent-blue-500 hover:accent-blue-400 transition-all
                   [&::-webkit-slider-thumb]:appearance-none [&::-webkit-slider-thumb]:w-3
                   [&::-webkit-slider-thumb]:h-3 [&::-webkit-slider-thumb]:bg-white
                   [&::-webkit-slider-thumb]:rounded-full [&::-webkit-slider-thumb]:shadow-[0_0_10px_rgba(59,130,246,0.5)]"
            />
        </div>
    );
};