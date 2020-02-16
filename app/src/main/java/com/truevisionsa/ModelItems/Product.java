package com.truevisionsa.ModelItems;

public class Product {

    private String product_name , product_id , stock_id , expiry , batch_no , sale_price , units_in_pack , product_hidden , item_expired;


    public Product(String product_name, String product_id, String stock_id, String expiry, String batch_no, String sale_price,
                   String units_in_pack, String product_hidden , String item_expired) {
        this.product_name = product_name;
        this.product_id = product_id;
        this.stock_id = stock_id;
        this.expiry = expiry;
        this.batch_no = batch_no;
        this.sale_price = sale_price;
        this.units_in_pack = units_in_pack;
        this.product_hidden = product_hidden;
        this.item_expired = item_expired;
    }

    public Product() {
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public String getStock_id() {
        return stock_id;
    }

    public void setStock_id(String stock_id) {
        this.stock_id = stock_id;
    }

    public String getExpiry() {
        return expiry;
    }

    public void setExpiry(String expiry) {
        this.expiry = expiry;
    }

    public String getBatch_no() {
        return batch_no;
    }

    public void setBatch_no(String batch_no) {
        this.batch_no = batch_no;
    }

    public String getSale_price() {
        return sale_price;
    }

    public void setSale_price(String sale_price) {
        this.sale_price = sale_price;
    }

    public String getUnits_in_pack() {
        return units_in_pack;
    }

    public void setUnits_in_pack(String units_in_pack) {
        this.units_in_pack = units_in_pack;
    }

    public String getProduct_hidden() {
        return product_hidden;
    }

    public void setProduct_hidden(String product_hidden) {
        this.product_hidden = product_hidden;
    }

    public String getItem_expired() {
        return item_expired;
    }

    public void setItem_expired(String item_expired) {
        this.item_expired = item_expired;
    }
}
