package com.truevisionsa.ModelItems;

public class InvProduct {

    private String UnitsInPack , InvPacksQty , InvUnitsQty , ProductHidden , WarePacksQty , WareUnitsQty , SalePrice , TotalPrice , Expiry ,
            ProductId , InventoryId , InvFindId , ProductName , UserName , StockId , item_expired , batch_no;

    public InvProduct(String unitsInPack, String invPacksQty, String invUnitsQty, String productHidden, String warePacksQty,
                      String wareUnitsQty, String salePrice, String totalPrice, String expiry, String productId, String inventoryId,
                      String invFindId, String productName, String userName, String stockId , String item_expired , String batch_no) {

        UnitsInPack = unitsInPack;
        InvPacksQty = invPacksQty;
        InvUnitsQty = invUnitsQty;
        ProductHidden = productHidden;
        WarePacksQty = warePacksQty;
        WareUnitsQty = wareUnitsQty;
        SalePrice = salePrice;
        TotalPrice = totalPrice;
        Expiry = expiry;
        ProductId = productId;
        InventoryId = inventoryId;
        InvFindId = invFindId;
        ProductName = productName;
        UserName = userName;
        StockId = stockId;
        this.item_expired = item_expired;
        this.batch_no = batch_no;
    }

    public InvProduct() {
    }

    public String getUnitsInPack() {
        return UnitsInPack;
    }

    public void setUnitsInPack(String unitsInPack) {
        UnitsInPack = unitsInPack;
    }

    public String getInvPacksQty() {
        return InvPacksQty;
    }

    public void setInvPacksQty(String invPacksQty) {
        InvPacksQty = invPacksQty;
    }

    public String getInvUnitsQty() {
        return InvUnitsQty;
    }

    public void setInvUnitsQty(String invUnitsQty) {
        InvUnitsQty = invUnitsQty;
    }

    public String getProductHidden() {
        return ProductHidden;
    }

    public void setProductHidden(String productHidden) {
        ProductHidden = productHidden;
    }

    public String getWarePacksQty() {
        return WarePacksQty;
    }

    public void setWarePacksQty(String warePacksQty) {
        WarePacksQty = warePacksQty;
    }

    public String getWareUnitsQty() {
        return WareUnitsQty;
    }

    public void setWareUnitsQty(String wareUnitsQty) {
        WareUnitsQty = wareUnitsQty;
    }

    public String getSalePrice() {
        return SalePrice;
    }

    public void setSalePrice(String salePrice) {
        SalePrice = salePrice;
    }

    public String getTotalPrice() {
        return TotalPrice;
    }

    public void setTotalPrice(String totalPrice) {
        TotalPrice = totalPrice;
    }

    public String getExpiry() {
        return Expiry;
    }

    public void setExpiry(String expiry) {
        Expiry = expiry;
    }

    public String getProductId() {
        return ProductId;
    }

    public void setProductId(String productId) {
        ProductId = productId;
    }

    public String getInventoryId() {
        return InventoryId;
    }

    public void setInventoryId(String inventoryId) {
        InventoryId = inventoryId;
    }

    public String getInvFindId() {
        return InvFindId;
    }

    public void setInvFindId(String invFindId) {
        InvFindId = invFindId;
    }

    public String getProductName() {
        return ProductName;
    }

    public void setProductName(String productName) {
        ProductName = productName;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public String getStockId() {
        return StockId;
    }

    public void setStockId(String stockId) {
        StockId = stockId;
    }

    public String getItem_expired() {
        return item_expired;
    }

    public void setItem_expired(String item_expired) {
        this.item_expired = item_expired;
    }

    public String getBatch_no() {
        return batch_no;
    }

    public void setBatch_no(String batch_no) {
        this.batch_no = batch_no;
    }
}
