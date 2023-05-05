package com.example.boiwala;

public class ImageModel {
    private String uderId, imageUrl;

    public ImageModel() {

    }

    public ImageModel(String uderId, String imageUrl) {
        this.uderId = uderId;
        this.imageUrl = imageUrl;
    }

    public String getUderId() {
        return uderId;
    }

    public void setUderId(String uderId) {
        this.uderId = uderId;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
