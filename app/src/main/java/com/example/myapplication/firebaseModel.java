package com.example.myapplication;

public class firebaseModel {

    public firebaseModel(String storeName, int count){
        this.storeName = storeName;
        this.count = count;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    int count;
    String storeName;

}
