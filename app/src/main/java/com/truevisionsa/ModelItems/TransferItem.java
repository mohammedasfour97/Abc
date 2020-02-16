package com.truevisionsa.ModelItems;

public class TransferItem {

    private String trans_id , stock_id , pack_qnt , units_qnt , units_in_pack , product , pack_value , expiry , batch , packs_count , units_in_pack_count;
    private boolean sign;

    public TransferItem(String trans_id, String stock_id, String pack_qnt, String units_qnt, String units_in_pack, String product, String pack_value,
                        String expiry, String batch , String packs_count , String units_in_pack_count , boolean sign) {
        this.trans_id = trans_id;
        this.stock_id = stock_id;
        this.pack_qnt = pack_qnt;
        this.units_qnt = units_qnt;
        this.units_in_pack = units_in_pack;
        this.product = product;
        this.pack_value = pack_value;
        this.expiry = expiry;
        this.batch = batch;
        this.packs_count = packs_count;
        this.units_in_pack_count = units_in_pack_count;
        this.sign = sign;
    }

    public TransferItem() {
    }

    public String getTrans_id() {
        return trans_id;
    }

    public void setTrans_id(String trans_id) {
        this.trans_id = trans_id;
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

    public String getUnits_qnt() {
        return units_qnt;
    }

    public void setUnits_qnt(String units_qnt) {
        this.units_qnt = units_qnt;
    }

    public String getUnits_in_pack() {
        return units_in_pack;
    }

    public void setUnits_in_pack(String units_in_pack) {
        this.units_in_pack = units_in_pack;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public String getPack_value() {
        return pack_value;
    }

    public void setPack_value(String pack_value) {
        this.pack_value = pack_value;
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

    public String getPacks_count() {
        return packs_count;
    }

    public void setPacks_count(String count) {
        this.packs_count = count;
    }

    public String getUnits_in_pack_count() {
        return units_in_pack_count;
    }

    public void setUnits_in_pack_count(String units_in_pack_count) {
        this.units_in_pack_count = units_in_pack_count;
    }

    public boolean isSign() {
        return sign;
    }

    public void setSign(boolean sign) {
        this.sign = sign;
    }
}
