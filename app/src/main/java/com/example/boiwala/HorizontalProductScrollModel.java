package com.example.boiwala;

public class HorizontalProductScrollModel {
    private String productImage, productID;
    private String productTitle, productWritter, productPrice;

    public HorizontalProductScrollModel(String productID, String productImage, String productTitle, String productWritter, String productPrice) {
        this.productID = productID;
        this.productImage = productImage;
        this.productTitle = productTitle;
        this.productWritter = productWritter;
        this.productPrice = productPrice;
    }

    public String getProductID() {
        return productID;
    }

    public void setProductID(String productID) {
        this.productID = productID;
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

    public String getProductWritter() {
        return productWritter;
    }

    public void setProductWritter(String productWritter) {
        this.productWritter = productWritter;
    }

    public String getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(String productPrice) {
        this.productPrice = productPrice;
    }
}
