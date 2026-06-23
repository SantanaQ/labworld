package com.api.encoding;


public enum ChunkID {

    HEAT_CHUNK((byte) 0x01),
    SUPPLY_CHUNK((byte) 0x02),
    SCENT_CHUNK((byte) 0x03),
    TRAILS_CHUNK((byte) 0x04),
    STRESS_CHUNK((byte) 0x05),

    HEAT_DELTA_CHUNK((byte) 0x10),
    SUPPLY_DELTA_CHUNK((byte) 0x11),
    SCENT_DELTA_CHUNK((byte) 0x12),
    TRAILS_DELTA_CHUNK((byte) 0x13),
    STRESS_DELTA_CHUNK((byte) 0x14),

    AGENTS_CHUNK((byte) 0x20),
    ;

    final byte id;

    ChunkID(byte id) {
        this.id = id;
    }
}
