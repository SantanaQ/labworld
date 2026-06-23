export const ChunkID = {
    HEAT_FULL: 0x01,
    SUPPLY_FULL: 0x02,
    SCENT_FULL: 0x03,
    TRAILS_FULL: 0x04,
    STRESS_FULL: 0x05,

    HEAT_DELTA: 0x10,
    SUPPLY_DELTA: 0x11,
    SCENT_DELTA: 0x12,
    TRAILS_DELTA: 0x13,
    STRESS_DELTA: 0x14,

    AGENTS_CHUNK: 0x20,
} as const;

export type ChunkID = typeof ChunkID[keyof typeof ChunkID];