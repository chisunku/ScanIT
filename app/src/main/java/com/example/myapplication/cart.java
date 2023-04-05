package com.example.myapplication;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.util.*;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

//import java.util.ArrayList;

public class cart extends AppCompatActivity {
    public TextView total;
    Context ctx;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        RecyclerView courseRV = findViewById(R.id.items);
        ctx = getBaseContext();
        DataController dataController = new DataController(getBaseContext());
        Cursor favs = dataController.retrieveCart();
        ArrayList<CartModel> favslist = new ArrayList<>();
        float cartTotal = 0;
        try {
            while(favs.moveToNext()){
                int quantity = favs.getInt(3);
                cartTotal += quantity*Float.parseFloat(favs.getString(2).replaceAll("[^.0-9]",""));
                Log.d("TAG", "onCreate: qty n tot "+quantity+" "+cartTotal);
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

        total = findViewById(R.id.total);
        total.setText(cartTotal+"");
    }

    public void updateTotal(Context ctx){
        DataController dataController = new DataController(ctx);
        Cursor favs = dataController.retrieveCart();
        float t = 0;
        while(favs.moveToNext()){
            t += favs.getInt(3)*Float.parseFloat(favs.getString(2)
                    .replaceAll("[^.0-9]",""));
        }
        Log.d("in updatetotal", "updateTotal: total = "+t);
        total = findViewById(R.id.total);
        total.setText(t+"");
    }
}