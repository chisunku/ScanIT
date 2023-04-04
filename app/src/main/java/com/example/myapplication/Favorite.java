package com.example.myapplication;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class Favorite extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fav);
        RecyclerView courseRV = findViewById(R.id.recycler_view);
        DataController dataController=new DataController(getBaseContext());
        Cursor favs = dataController.retrieve();
        ArrayList<FavoriteModel> favslist = new ArrayList<>();
        try {
            while(favs.moveToNext()){
//                ProductDetails pd = new ProductDetails(getApplicationContext());
//                String s = pd.execute(favs.getString(0), "favs").get();
//                Log.d("TAG", "onCreate: "+s );
//                JSONObject json = new JSONObject(s);
//                JSONObject prods = json.getJSONObject("product");
//                JSONArray images = prods.getJSONArray("images");
                Log.d("TAG", "onCreate: favs from db"+favs.getString(1) +" "+ favs.getString(2)+" "+ favs.getString(0) );
                FavoriteModel fv = new FavoriteModel(favs.getString(1) , favs.getString(2), favs.getString(0));
                favslist.add(fv);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        FavoriteAdapter favAdapter = new FavoriteAdapter(this, favslist);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

        // in below two lines we are setting layoutmanager and adapter to our recycler view.
        courseRV.setLayoutManager(linearLayoutManager);
        courseRV.setAdapter(favAdapter);
    }
}
