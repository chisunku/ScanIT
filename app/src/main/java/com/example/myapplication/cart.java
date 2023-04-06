package com.example.myapplication;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.util.*;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.snackbar.Snackbar;

//import java.util.ArrayList;

public class cart extends AppCompatActivity {
    public static cart instance = null;
    public TextView total;
    CartAdapter favAdapter;
    Context ctx;
    @Override
    public void onResume()
    {
        super.onResume();
        instance = this;
    }
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
//        RecyclerView courseRV = findViewById(R.id.items);
//        ctx = getBaseContext();
//        BottomNavigationView navigationView = findViewById(R.id.bottom_navigation);
//        navigationView.setOnNavigationItemSelectedListener(navListener);
//        navigationView.setSelectedItemId(R.id.cart);
//        DataController dataController = new DataController(getBaseContext());
//        Cursor favs = dataController.retrieveCart();
//        ArrayList<CartModel> favslist = new ArrayList<>();
//        float cartTotal = 0;
//        try {
//            while(favs.moveToNext()){
//                int quantity = favs.getInt(3);
//                cartTotal += quantity*Float.parseFloat(favs.getString(2).replaceAll("[^.0-9]",""));
//                Log.d("TAG", "onCreate: qty n tot "+quantity+" "+cartTotal);
//                CartModel fv = new CartModel(favs.getString(0) ,
//                        favs.getString(1), favs.getString(2),
//                        Integer.parseInt(favs.getString(3)), favs.getString(4),
//                        favs.getString(5), favs.getString(6));
//                favslist.add(fv);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        favAdapter = new CartAdapter(this, favslist);
//
//        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
//
//        // in below two lines we are setting layoutmanager and adapter to our recycler view.
//        courseRV.setLayoutManager(linearLayoutManager);
//        courseRV.setAdapter(favAdapter);
//
//        total = findViewById(R.id.total);
//        total.setText(cartTotal+"");
//
//        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
//            @Override
//            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
//                // this method is called
//                // when the item is moved.
//                return false;
//            }
//
//            @Override
//            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
//                // this method is called when we swipe our item to right direction.
//                // on below line we are getting the item at a particular position.
//                CartModel deletedCourse = favslist.get(viewHolder.getAdapterPosition());
//
//                // below line is to get the position
//                // of the item at that position.
//                int position = viewHolder.getAdapterPosition();
//                // this method is called when item is swiped.
//                // below line is to remove item from our array list.
//                favslist.remove(viewHolder.getAdapterPosition());
//
//                // below line is to notify our item is removed from adapter.
//                favAdapter.notifyItemRemoved(viewHolder.getAdapterPosition());
//                DataController db = new DataController(getApplicationContext());
//                db.deleteCart(deletedCourse.getBarcode());
//
//                // below line is to display our snackbar with action.
//                Snackbar.make(courseRV, deletedCourse.getProductName(), Snackbar.LENGTH_LONG).setAction("Undo", new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        // adding on click listener to our action of snack bar.
//                        // below line is to add our item to array list with a position.
//                        favslist.add(position, deletedCourse);
//                        db.insertCart(deletedCourse.getBarcode(), deletedCourse.getProductName(), deletedCourse.getCost(),
//                                deletedCourse.getQuantity(), deletedCourse.getSeller(), deletedCourse.getUrl(),
//                                deletedCourse.getImageUrl(), "undo");
//
//                        // below line is to notify item is
//                        // added to our adapter class.
//                        favAdapter.notifyItemInserted(position);
//                    }
//                }).show();
//            }
//            // at last we are adding this
//            // to our recycler view.
//        }).attachToRecyclerView(courseRV);

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
        total = instance.findViewById(R.id.total);
        total.setText("hello");
        total.setText(t+"");
    }

    private final BottomNavigationView.OnNavigationItemSelectedListener navListener = item -> {
        // By using switch we can easily get
        // the selected fragment
        // by using there id.
//        Fragment selectedFragment = null;
        int itemId = item.getItemId();
        if (itemId == R.id.home) {
            Intent i = new Intent(cart.this, MainActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            getApplicationContext().startActivity(i);
        } else if (itemId == R.id.favorite) {
            Intent i = new Intent(cart.this, Favorite.class);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            getApplicationContext().startActivity(i);
        }
        // It will help to replace the
        // one fragment to other.
//        if (selectedFragment != null) {
//            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();
//        }
        return true;
    };
}