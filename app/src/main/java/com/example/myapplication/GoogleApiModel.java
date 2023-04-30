package com.example.myapplication;

public class GoogleApiModel {
    String title;
    String URL;
    String dispUrl;
    String imageUrl;

    GoogleApiModel(String title, String URL, String dispUrl, String imageUrl){
        this.title = title;
        this.URL = URL;
        this.dispUrl = dispUrl;
        this.imageUrl = imageUrl;
    }

    public void setImageUrl(String imageUrl){
        this.imageUrl = imageUrl;
    }
    public String getImageUrl(){
        return this.imageUrl;
    }
    public void setTitle(String title){
        this.title = title;
    }
    public String getTitle(){
        return this.title;
    }
    public void setURL(String URL){
        this.URL = URL;
    }
    public String getURL(){
        return this.URL;
    }
    public void setDispUrl(String dispUrl){
        this.dispUrl = dispUrl;
    }
    public String getDispUrl(){
        return this.dispUrl;
    }
}
