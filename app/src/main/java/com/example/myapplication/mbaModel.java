package com.example.myapplication;

import io.realm.RealmObject;

public class mbaModel extends RealmObject {
    String productName;
    String storesClickOn;
    String favorite;
    String cartStores;

    public mbaModel(){}

    public mbaModel(String productName, String storesClickOn, String favorite, String cartStores){
        this.productName = productName;
        this.storesClickOn = storesClickOn;
        this.favorite = favorite;
        this.cartStores = cartStores;
    }

    public String getProductName(){
        return this.productName;
    }
    public String getStoresClickOn(){
        return this.storesClickOn;
    }
    public String getFavorite(){
        return this.favorite;
    }
    public String getCartStores(){
        return this.cartStores;
    }
}
