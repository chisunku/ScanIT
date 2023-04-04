package com.example.myapplication;


import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ProgressBar;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.util.stream.Collectors;

public class ProductDetails extends AsyncTask<String, Void, String> {

    Context ctx;
    String barcode;
    String activity;

    ProductDetails(Context c){
        ctx = c;
    }

    @Override
    protected String doInBackground(String... voids) {
        activity = voids[1];
        StringBuilder sb = new StringBuilder();
        try {
            URL url = new URL("https://barcodes1.p.rapidapi.com/?query="+voids[0]);
            barcode = voids[0];
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestProperty("X-RapidAPI-Key","9fa917d25amshb66b38820f4307fp133c26jsn5be5aa0cbf1c");
            connection.setRequestProperty("X-RapidAPI-Host", "barcodes1.p.rapidapi.com");
            connection.setRequestMethod("GET");
            // Log the server response code
            int responseCode = connection.getResponseCode();
            Log.i("TAG", "Server responded with: " + responseCode);

            // And if the code was HTTP_OK then parse the contents
            if (responseCode == HttpURLConnection.HTTP_OK) {

                // Convert request content to string
                InputStream is = connection.getInputStream();
//                String content = convertInputStream(is, "UTF-8");
                BufferedReader in = new BufferedReader(new InputStreamReader(is));
                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    sb.append(inputLine);
                }
                in.close();
                is.close();

//                return inputLine;
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

    @Override
    protected void onPostExecute(String s) {
        System.out.println("in on post execute: "+s);
        if(activity.equals("main")) {
            Intent i = new Intent(ctx, Product.class);
            i.putExtra("json obj", s);
            i.putExtra("barcode", barcode);
            Log.d("TAG", "onPostExecute: json:" + s);
            ctx.startActivity(i);
        }
//        else if(activity == "favs"){
//            Intent i = new Intent(ctx, Favorite.class);
//            i.putExtra("json",s);
//            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            ctx.startActivity(i);
//        }
        super.onPostExecute(s);
    }

}
