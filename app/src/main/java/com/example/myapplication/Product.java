package com.example.myapplication;


import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fasterxml.jackson.core.JsonParser;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Product extends AppCompatActivity {
    String barcode;
    JSONObject product = null;
    JSONArray images = null;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);
        Intent i = getIntent();
        RecyclerView courseRV = findViewById(R.id.recycler_view);
        ArrayList<CourseModel> courseModelArrayList = new ArrayList<CourseModel>();
        barcode = i.getStringExtra("barcode");
//        System.out.println("in product activity");
        BottomNavigationView navigationView = findViewById(R.id.bottom_navigation);
        navigationView.setOnNavigationItemSelectedListener(navListener);
//        navigationView.setItemIconTintList(null);
//        navigationView.setItemTextColor();
//        navigationView.getMenu().findItem(R.id.item_id).setChecked(true);
        String info = i.getStringExtra("json obj");
        try {
//            ProgressBar pb = findViewById(R.id.progressBar_cyclic);
//            pb.setVisibility(View.GONE);
            JSONObject json = new JSONObject(info);
            System.out.println("no product json"+json);
            TextView productName = findViewById(R.id.productName);

            if(json.toString().contains("results")){
                Toast.makeText(getApplicationContext(),"Product not available",Toast.LENGTH_LONG).show();
                Intent it = new Intent(Product.this, MainActivity.class);
                it.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                getApplicationContext().startActivity(it);
            }
            else {
                product = json.getJSONObject("product");
                System.out.println(product.get("title"));

                productName.setText(product.get("title").toString());
                TextView category = findViewById(R.id.category);
                category.setText(product.get("manufacturer").toString());
                TextView manufacturer = findViewById(R.id.manufacturer);
                manufacturer.setText(product.get("description").toString());
                try {
                    ImageView img = (ImageView) findViewById(R.id.image);
                    images = product.getJSONArray("images");
                    new DownloadImageTask(img).execute(images.get(0).toString()).get();
////                JSONArray imgs = product.get("imageUrl").toString();
//                System.out.println("image url : "+images.get(0));
////                System.out.println("image url : "+product.get("imageUrl").toString());
//                Bitmap bitmap = BitmapFactory.decodeStream((InputStream)new URL(images.get(0).toString()).getContent());
//                img.setImageBitmap(bitmap);
                } catch (Exception e) {
                    Log.e("TAG", "onCreate: ", e);
                    e.printStackTrace();
                }
                JSONArray stores = product.getJSONArray("online_stores");
                int k = 0;
                for (k = 0; k < stores.length(); k++) {
//                    Log.d("TAG", "onCreate: " + stores.get(k).toString());
//                    if (!stores.get(k).toString().contains("Walmart")) {
//                        Log.d("TAG", "onCreate: Not available at Walmart");
//                    } else {
//                        val = k;
//                        break;
//                    }
                    JSONObject store = stores.getJSONObject(k);
                    if(store.get("price").toString().startsWith("$")) {
                        CourseModel obj = new CourseModel(barcode, store.get("name").toString(), store.get("price").toString(), store.get("url").toString(), product.get("title").toString(), images.get(0).toString());
                        courseModelArrayList.add(obj);
                    }
                }
//            JSONArray target = stores.(k);
//            System.out.println("target values: "+stores.getJSONObject(0).get("price"));
//                TextView cost = findViewById(R.id.cost);
//                cost.setText(stores.getJSONObject(val).get("price").toString());
            }
            CourseAdapter courseAdapter = new CourseAdapter(this, courseModelArrayList);

            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

            // in below two lines we are setting layoutmanager and adapter to our recycler view.
            courseRV.setLayoutManager(linearLayoutManager);
            courseRV.setAdapter(courseAdapter);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    public class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }

    public void backMtd(View v){
        Intent i = new Intent(Product.this, MainActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        getApplicationContext().startActivity(i);
    }

    public void favorite(View v) throws JSONException {
        //add to fav logic
        DataController db = new DataController(getBaseContext());
        long l = db.insertFav(barcode, product.get("title").toString(),images.get(0).toString());
        Log.d("TAG", "favorite db res val : "+l );
        if(l == -1){
            Toast.makeText(getApplicationContext(),"Already in favs",Toast.LENGTH_LONG).show();
        }
        else
            Toast.makeText(getApplicationContext(), "product added to fav",Toast.LENGTH_LONG).show();
        Cursor c = db.retrieve();
        while(c.moveToNext()){
            Log.d("TAG", "favorite: db data -> "+c.getString(0));
        }
    }

    private final BottomNavigationView.OnNavigationItemSelectedListener navListener = item -> {
        // By using switch we can easily get
        // the selected fragment
        // by using there id.
//        Fragment selectedFragment = null;
        int itemId = item.getItemId();
        if (itemId == R.id.home) {
            Intent i = new Intent(Product.this, MainActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            getApplicationContext().startActivity(i);
        } else if (itemId == R.id.favorite) {
            Intent i = new Intent(Product.this, Favorite.class);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            getApplicationContext().startActivity(i);
        } else if (itemId == R.id.cart) {
            Intent i = new Intent(Product.this, cart.class);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            getApplicationContext().startActivity(i);
        }
        // It will help to replace the
        // one fragment to other.
//        if (selectedFragment != null) {
//            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();
//        }
        return false;
    };

}
