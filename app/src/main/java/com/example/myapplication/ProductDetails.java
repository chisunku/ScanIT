package com.example.myapplication;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.Toast;


import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.progressindicator.LinearProgressIndicator;

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
    ProgressDialog progressDialog;
    LinearProgressIndicator pd;
    String barcode;
    String activity;
    ProductFragment pf;

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
            connection.setRequestProperty("X-RapidAPI-Key","107297c8c4msh813718b79ab95abp135b6djsn549defa91b08");
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

    protected void onPreExecute() {
//        pd = LinearProgressIndicator.
        progressDialog = ProgressDialog.show(ctx,
                "ProgressDialog",
                "Fetching data");
    }

    @Override
    protected void onPostExecute(String s) {
        Log.d("ProductDetails.java", "onPostExecute: "+s);
        try {
            if(s.contains("\"results\":[]")){
//                System.out.println(s);
//                GoogleAPI api = new GoogleAPI(ctx);
//                api.execute(barcode);
                progressDialog.dismiss();
                Toast.makeText(ctx, "Sorry this product was not found :(", Toast.LENGTH_SHORT).show();
                return;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        Log.d("TAG", "onPostExecute: product is there"+s);
            Bundle bundle = new Bundle();
            bundle.putString("json obj", s);
            bundle.putString("barcode", barcode);
            bundle.putString("fromAPI", "Rapid");
            Log.d("TAG", "onPostExecute: json:" + s);
            ProductFragment pf = new ProductFragment();
            pf.setArguments(bundle);
            FragmentManager fragmentManager =  ((AppCompatActivity)ctx).getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.content, pf);
            fragmentTransaction.addToBackStack("prod");
            fragmentTransaction.commit();
        super.onPostExecute(s);
        progressDialog.dismiss();
    }

}
