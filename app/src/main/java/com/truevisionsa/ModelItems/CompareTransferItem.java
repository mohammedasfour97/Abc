package com.truevisionsa.ModelItems;

public class CompareTransferItem {

    private String trans_id , stock_id , pack_qnt , units_in_pack;

    public CompareTransferItem(String trans_id, String stock_id, String pack_qnt, String units_in_pack) {
        this.trans_id = trans_id;
        this.stock_id = stock_id;
        this.pack_qnt = pack_qnt;
        this.units_in_pack = units_in_pack;
    }

    public CompareTransferItem() {
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

    public String getUnits_in_pack() {
        return units_in_pack;
    }

    public void setUnits_in_pack(String units_in_pack) {
        this.units_in_pack = units_in_pack;
    }
}
