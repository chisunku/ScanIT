package com.example.myapplication;

public class FavoriteModel {
    private String ProductName;
    private String imageUrl;
    private String barcode;

    // Constructor
    public FavoriteModel(String ProductName, String imageUrl, String barcode) {
        this.ProductName = ProductName;
        this.imageUrl = imageUrl;
        this.barcode = barcode;
    }

    public String getBarcode(){
        return this.barcode;
    }
    public String getProductName(){
        return this.ProductName;
    }

    public void setProductName(String ProductName){
        this.ProductName = ProductName;
    }

    public String getImageUrl(){
        return imageUrl;
    }

    public void setImageUrl(String imageUrl){
        this.imageUrl = imageUrl;
    }

}
