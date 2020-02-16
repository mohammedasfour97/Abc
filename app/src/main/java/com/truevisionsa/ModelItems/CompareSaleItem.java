package com.truevisionsa.ModelItems;

public class CompareSaleItem {

    private String sales_id , stock_id , pack_qnt;

    public CompareSaleItem() {
    }

    public CompareSaleItem(String sales_id, String stock_id, String pack_qnt) {
        this.sales_id = sales_id;
        this.stock_id = stock_id;
        this.pack_qnt = pack_qnt;
    }

    public String getSales_id() {
        return sales_id;
    }

    public void setSales_id(String sales_id) {
        this.sales_id = sales_id;
    }

    public String getStock_id() {
        return stock_id;
    }

    public void setStock_id(String stock_id) {
        this.stock_id = stock_id;
    }

    public String getPack_qnt() {
        return pack_qnt;
    }

    public void setPack_qnt(String pack_qnt) {
        this.pack_qnt = pack_qnt;
    }
}
