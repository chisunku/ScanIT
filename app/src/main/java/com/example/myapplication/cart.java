package com.example.myapplication;

import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class cart extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        RecyclerView courseRV = findViewById(R.id.items);
        DataController dataController=new DataController(getBaseContext());
        Cursor favs = dataController.retrieveCart();
        ArrayList<CartModel> favslist = new ArrayList<>();
        try {
            while(favs.moveToNext()){
                Log.d("TAG", "onCreate: "+favs.getString(0)+" "+favs.getString(1)
                        +" "+favs.getString(2)+" "+favs.getString(3)
                        +" "+favs.getString(4)+" "+favs.getString(5));
                Log.d("tag", "image url : "+favs.getString(5));
                Log.d("TAG", "onCreate: favs from db"+favs.getString(1) +" "+ favs.getString(2)+" "+ favs.getString(0) );
                CartModel fv = new CartModel(favs.getString(0) ,
                        favs.getString(1), favs.getString(2),
                        Integer.parseInt(favs.getString(3)), favs.getString(4),
                        favs.getString(5), favs.getString(6));
                favslist.add(fv);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        CartAdapter favAdapter = new CartAdapter(this, favslist);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

        // in below two lines we are setting layoutmanager and adapter to our recycler view.
        courseRV.setLayoutManager(linearLayoutManager);
        courseRV.setAdapter(favAdapter);
    }
}