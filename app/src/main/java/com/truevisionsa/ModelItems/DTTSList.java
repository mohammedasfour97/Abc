package com.truevisionsa.ModelItems;

public class DTTSList {

    private String id, refId, typeName;

    public DTTSList() {
    }

    public DTTSList(String id, String refId, String typeName) {
        this.id = id;
        this.refId = refId;
        this.typeName = typeName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRefId() {
        return refId;
    }

    public void setRefId(String refId) {
        this.refId = refId;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    @Override
    public String toString() {
        return refId ;
    }
}
