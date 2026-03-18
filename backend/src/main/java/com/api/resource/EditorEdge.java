package com.api.resource;

public class EditorEdge {

    private String source;
    private String sourceHandle;
    private String target;
    private String targetHandle;

    public String source() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String sourceHandle() {
        return sourceHandle;
    }

    public void setSourceHandle(String sourceHandle) {
        this.sourceHandle = sourceHandle;
    }

    public String target() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public String targetHandle() {
        return targetHandle;
    }

    public void setTargetHandle(String targetHandle) {
        this.targetHandle = targetHandle;
    }
}
