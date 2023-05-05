package com.example.boiwala;

public class MyOrderItemModel {
    private String productImage;
    private String productTitle, deliveryStatus, totalPriceAndQuantity;

    public MyOrderItemModel(String productImage, String productTitle, String deliveryStatus, String totalPriceAndQuantity) {
        this.productImage = productImage;
        this.productTitle = productTitle;
        this.deliveryStatus = deliveryStatus;
        this.totalPriceAndQuantity = totalPriceAndQuantity;
    }

    public String getTotalPriceAndQuantity() {
        return totalPriceAndQuantity;
    }

    public void setTotalPriceAndQuantity(String totalPriceAndQuantity) {
        this.totalPriceAndQuantity = totalPriceAndQuantity;
    }

    public String getProductImage() {
        return productImage;
    }

    public void setProductImage(String productImage) {
        this.productImage = productImage;
    }

    public String getProductTitle() {
        return productTitle;
    }

    public void setProductTitle(String productTitle) {
        this.productTitle = productTitle;
    }

    public String getDeliveryStatus() {
        return deliveryStatus;
    }

    public void setDeliveryStatus(String deliveryStatus) {
        this.deliveryStatus = deliveryStatus;
    }
}
