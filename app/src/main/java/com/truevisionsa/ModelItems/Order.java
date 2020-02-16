package com.truevisionsa.ModelItems;

public class Order {

    private String order_id , customer_id , customer_name , items , note;

    public Order(String order_id, String customer_id, String customer_name, String items, String note) {
        this.order_id = order_id;
        this.customer_id = customer_id;
        this.customer_name = customer_name;
        this.items = items;
        this.note = note;
    }

    public Order() {
    }

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public String getCustomer_id() {
        return customer_id;
    }

    public void setCustomer_id(String customer_id) {
        this.customer_id = customer_id;
    }

    public String getCustomer_name() {
        return customer_name;
    }

    public void setCustomer_name(String customer_name) {
        this.customer_name = customer_name;
    }

    public String getItems() {
        return items;
    }

    public void setItems(String items) {
        this.items = items;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
