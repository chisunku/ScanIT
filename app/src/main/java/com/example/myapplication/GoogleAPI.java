package com.example.myapplication;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class GoogleAPI extends AsyncTask<String, Void, String> {
    String barcode = "";
    Context ctx = null;
    public GoogleAPI(Context ctx){
        this.ctx = ctx;
    }
    @Override
    protected String doInBackground(String... strings) {
        barcode = strings[0];
//        StringBuilder sb = new StringBuilder();
//        int responseCode = 0;
        StringBuilder sb = null;
        try {
            String urlStr = "https://www.googleapis.com/customsearch/v1?key=YOUR_API_KEY&cx=YOUR_CUSTOM_SEARCH_ENGINE_ID&q=SEARCH_QUERY";
            urlStr = urlStr.replace("YOUR_API_KEY", "AIzaSyCGvsdavHBbzYYsv1-m02jUDVeMisDMD-Q")
                    .replace("YOUR_CUSTOM_SEARCH_ENGINE_ID", "45ca0d0bd22a542a6")
                    .replace("SEARCH_QUERY", barcode); // replace with your search query
            URL url = new URL(urlStr);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");

            // Read the response into a String
            BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
            String output;
            sb = new StringBuilder();
            while ((output = br.readLine()) != null) {
                sb.append(output);
            }
            Log.d("TAG", "doInBackground: coming here " + sb);
            System.out.println("result : " + sb);

        } catch (Exception e) {
            Log.e("TAG", "doInBackground: in gpi");

            e.printStackTrace();
        }
//        System.out.println(sb);
        return sb.toString();
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        System.out.println(s);
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            // Convert string to JSON object
            JsonNode jsonData = objectMapper.readTree(s);
            Log.d("TAG", "onPostExecute: json data : "+jsonData);
            System.out.println("items: "+jsonData.get("items"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        Bundle bundle = new Bundle();
        bundle.putString("json obj", s);
        bundle.putString("barcode", barcode);
        bundle.putString("fromAPI", "googleAPI");
        Log.d("TAG", "onPostExecute: json:" + s);
        googleApiFragment pf = new googleApiFragment();
        pf.setArguments(bundle);
        FragmentManager fragmentManager =  ((AppCompatActivity)ctx).getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.content, pf);
        fragmentTransaction.addToBackStack("prod");
        fragmentTransaction.commit();
    }
}
