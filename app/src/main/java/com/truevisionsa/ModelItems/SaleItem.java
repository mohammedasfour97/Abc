package com.truevisionsa.ModelItems;

public class SaleItem {

    private String sale_id , stock_id , packs_qnt , product , product_value , expiry , batch , count ;
    private boolean sign;

    public SaleItem(String sale_id, String stock_id, String packs_qnt, String product, String product_value, String expiry, String batch , boolean sign) {
        this.sale_id = sale_id;
        this.stock_id = stock_id;
        this.packs_qnt = packs_qnt;
        this.product = product;
        this.product_value = product_value;
        this.expiry = expiry;
        this.batch = batch;
        this.count = "0";
        this.sign = false;
    }

    public SaleItem() {
        this.count = "0" ;
    }

    public String getSale_id() {
        return sale_id;
    }

    public void setSale_id(String sale_id) {
        this.sale_id = sale_id;
    }

    public String getStock_id() {
        return stock_id;
    }

    public void setStock_id(String stock_id) {
        this.stock_id = stock_id;
    }

    public String getPacks_qnt() {
        return packs_qnt;
    }

    public void setPacks_qnt(String packs_qnt) {
        this.packs_qnt = packs_qnt;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public String getProduct_value() {
        return product_value;
    }

    public void setProduct_value(String product_value) {
        this.product_value = product_value;
    }

    public String getExpiry() {
        return expiry;
    }

    public void setExpiry(String expiry) {
        this.expiry = expiry;
    }

    public String getBatch() {
        return batch;
    }

    public void setBatch(String batch) {
        this.batch = batch;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public boolean isSign() {
        return sign;
    }

    public void setSign(boolean sign) {
        this.sign = sign;
    }
}
