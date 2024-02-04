package com.truevisionsa.ModelItems;

public class IDName {

    private String id, arbName, engName;

    public IDName(String id, String arbName, String engName) {
        this.id = id;
        this.arbName = arbName;
        this.engName = engName;
    }

    public IDName() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getArbName() {
        return arbName;
    }

    public void setArbName(String arbName) {
        this.arbName = arbName;
    }

    public String getEngName() {
        return engName;
    }

    public void setEngName(String engName) {
        this.engName = engName;
    }

    @Override
    public String toString() {
        return engName ;
    }
}
