export class BinaryReader {
    offset = 0;
    private view;
    private littleEndian = false;

    constructor(view: DataView, littleEndian: boolean) {
        this.view = view;
        this.littleEndian = littleEndian;
    }

    public require(bytes: number) {
        if (this.offset + bytes > this.view.byteLength) {
            throw new Error("Buffer underflow");
        }
    }

    public uint8() {
        this.require(1);
        const v = this.view.getUint8(this.offset);
        this.offset += 1;
        return v;
    }

    public uint16() {
        this.require(2);
        const v = this.view.getUint16(this.offset, this.littleEndian);
        this.offset += 2;
        return v;
    }

    public uint32() {
        this.require(4);
        const v = this.view.getUint32(this.offset, this.littleEndian);
        this.offset += 4;
        return v;
    }

    public uint64() {
        this.require(8);
        const v = this.view.getBigUint64(this.offset, this.littleEndian);
        this.offset += 8;
        return v;
    }

    public float32() {
        this.require(4);
        const v = this.view.getFloat32(this.offset, this.littleEndian);
        this.offset += 4;
        return v;
    }

    public uint8Array(length: number) {
        this.require(length);
        const arr =  new Uint8Array(this.view.buffer, this.offset, length);
        this.offset += length;
        return arr;
    }
}