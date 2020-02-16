package com.truevisionsa.ModelItems;

public class Transfer {

    private String Transfer_id , dest_id , dest_branch , items;

    public Transfer(String transfer_id, String dest_id, String dest_branch, String items) {
        Transfer_id = transfer_id;
        this.dest_id = dest_id;
        this.dest_branch = dest_branch;
        this.items = items;
    }

    public Transfer() {
    }

    public String getTransfer_id() {
        return Transfer_id;
    }

    public void setTransfer_id(String transfer_id) {
        Transfer_id = transfer_id;
    }

    public String getDest_id() {
        return dest_id;
    }

    public void setDest_id(String dest_id) {
        this.dest_id = dest_id;
    }

    public String getDest_branch() {
        return dest_branch;
    }

    public void setDest_branch(String dest_branch) {
        this.dest_branch = dest_branch;
    }

    public String getItems() {
        return items;
    }

    public void setItems(String items) {
        this.items = items;
    }
}
