package com.example.myapplication;

public class CourseModel {

    private String storeName;
    private String cost;
    private String url;
    private String productName;
    private String ImageUrl;
    private String barcode;
    private int quantity;

    // Constructor
    public CourseModel(String barcode, String storeName, String cost, String url, String productName, String ImageUrl) {
        this.barcode = barcode;
        this.storeName = storeName;
        this.cost = cost;
        this.url = url;
        this.productName = productName;
        this.ImageUrl = ImageUrl;
    }
    public CourseModel(String storeName, String cost, String url) {
        this.storeName = storeName;
        this.cost = cost;
        this.url = url;
    }

    public int getQuantity(){
        return quantity;
    }

    public void setQuantity(int qty){
        this.quantity = qty;
    }
    public String getBarcode(){
        return this.barcode;
    }

    public String getImageUrl(){
        return this.ImageUrl;
    }

    public String getProductName(){
        return this.productName;
    }

    public String ImageUrl(){
        return this.ImageUrl;
    }

    public String getUrl(){
        return url;
    }

    public void setUrl(String url){
        this.url = url;
    }

    // Getter and Setter
    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public String getCost() {
        return cost;
    }

    public void setCourse_rating(String cost) {
        this.cost = cost;
    }
}
