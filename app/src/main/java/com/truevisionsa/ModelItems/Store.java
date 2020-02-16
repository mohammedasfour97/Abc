package com.truevisionsa.ModelItems;

public class Store {

    private String stroe_id , store_name ;
    private String store_items ;

    public Store(String stroe_id, String store_name, String store_items) {
        this.stroe_id = stroe_id;
        this.store_name = store_name;
        this.store_items = store_items;
    }

    public Store() {
    }

    public String getStroe_id() {
        return stroe_id;
    }

    public void setStroe_id(String stroe_id) {
        this.stroe_id = stroe_id;
    }

    public String getStore_name() {
        return store_name;
    }

    public void setStore_name(String store_name) {
        this.store_name = store_name;
    }

    public String getStore_items() {
        return store_items;
    }

    public void setStore_items(String store_items) {
        this.store_items = store_items;
    }
}
