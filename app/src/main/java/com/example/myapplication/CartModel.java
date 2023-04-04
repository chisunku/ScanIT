package com.example.myapplication;

import android.util.Log;

public class CartModel {
    private String productName;
    private String imageUrl;
    private String barcode;
    String cost;
    int quantity;
    String seller;
    String url;

    // Constructor
    public CartModel(String barcode, String productName, String cost, int quantity, String seller, String url, String imageUrl) {
        this.barcode = barcode;
        this.productName = productName;
        this.cost = cost;
        this.quantity = quantity;
        this.seller = seller;
        this.url = url;
        this.imageUrl = imageUrl;
    }

    public String getBarcode(){
        return this.barcode;
    }
    public String getProductName(){
        return this.productName;
    }

    public String getImageUrl(){
        Log.d("TAG", "getImageUrl: "+imageUrl);
        return this.imageUrl;
    }
    public String getUrl(){
        return this.url;
    }
    public String getCost(){
        return this.cost;
    }
    public String getSeller(){
        return this.seller;
    }
}
