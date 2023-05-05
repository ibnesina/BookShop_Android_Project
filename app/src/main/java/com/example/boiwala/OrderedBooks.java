package com.example.boiwala;

public class OrderedBooks {
    private String userId;
    private String purchaseDate;
    private String bookName, bookImage;
    private Double bookPrice;
    private int quantity;
    private double totalPrice;

    public OrderedBooks() {
    }

    public OrderedBooks(String userId, String purchaseDate, String bookName, String bookImage, Double bookPrice, int quantity, double totalPrice) {
        this.userId = userId;
        this.purchaseDate = purchaseDate;
        this.bookName = bookName;
        this.bookImage = bookImage;
        this.bookPrice = bookPrice;
        this.quantity = quantity;
        this.totalPrice = totalPrice;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPurchaseDate() {
        return purchaseDate;
    }

    public void setPurchaseDate(String purchaseDate) {
        this.purchaseDate = purchaseDate;
    }

    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    public String getBookImage() {
        return bookImage;
    }

    public void setBookImage(String bookImage) {
        this.bookImage = bookImage;
    }

    public Double getBookPrice() {
        return bookPrice;
    }

    public void setBookPrice(Double bookPrice) {
        this.bookPrice = bookPrice;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }
}
