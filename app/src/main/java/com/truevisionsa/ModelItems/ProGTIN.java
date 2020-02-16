package com.truevisionsa.ModelItems;

public class ProGTIN {

    private String product_id , product , gtin , valid_gtin;

    public ProGTIN(String product_id, String product, String gtin, String valid_gtin) {
        this.product_id = product_id;
        this.product = product;
        this.gtin = gtin;
        this.valid_gtin = valid_gtin;
    }

    public ProGTIN() {
    }

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public String getGtin() {
        return gtin;
    }

    public void setGtin(String gtin) {
        this.gtin = gtin;
    }

    public String getValid_gtin() {
        return valid_gtin;
    }

    public void setValid_gtin(String valid_gtin) {
        this.valid_gtin = valid_gtin;
    }
}
