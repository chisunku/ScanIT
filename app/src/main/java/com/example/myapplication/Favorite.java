package com.example.myapplication;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class Favorite extends AppCompatActivity {
    FavoriteModel fv;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fav);
        RecyclerView courseRV = findViewById(R.id.recycler_view);
        DataController dataController=new DataController(getBaseContext());
        Cursor favs = dataController.retrieve();
        BottomNavigationView navigationView = findViewById(R.id.bottom_navigation);
        navigationView.setOnNavigationItemSelectedListener(navListener);
        navigationView.setSelectedItemId(R.id.favorite);
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
                fv = new FavoriteModel(favs.getString(1) , favs.getString(2), favs.getString(0));
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


        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                // this method is called
                // when the item is moved.
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                // this method is called when we swipe our item to right direction.
                // on below line we are getting the item at a particular position.
                FavoriteModel deletedCourse = favslist.get(viewHolder.getAdapterPosition());

                // below line is to get the position
                // of the item at that position.
                int position = viewHolder.getAdapterPosition();
                // this method is called when item is swiped.
                // below line is to remove item from our array list.
                favslist.remove(viewHolder.getAdapterPosition());

                // below line is to notify our item is removed from adapter.
                favAdapter.notifyItemRemoved(viewHolder.getAdapterPosition());
                DataController db = new DataController(getApplicationContext());
                db.deleteFav(deletedCourse.getBarcode());

                // below line is to display our snackbar with action.
                Snackbar.make(courseRV, deletedCourse.getProductName(), Snackbar.LENGTH_LONG).setAction("Undo", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // adding on click listener to our action of snack bar.
                        // below line is to add our item to array list with a position.
                        favslist.add(position, deletedCourse);
                        db.insertFav(fv.getBarcode(), fv.getProductName(),fv.getImageUrl());

                        // below line is to notify item is
                        // added to our adapter class.
                        favAdapter.notifyItemInserted(position);
                    }
                }).show();
            }
            // at last we are adding this
            // to our recycler view.
        }).attachToRecyclerView(courseRV);

    }

    private final BottomNavigationView.OnNavigationItemSelectedListener navListener = item -> {
        // By using switch we can easily get
        // the selected fragment
        // by using there id.
//        Fragment selectedFragment = null;
        int itemId = item.getItemId();
        if (itemId == R.id.home) {
            Intent i = new Intent(Favorite.this, MainActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            getApplicationContext().startActivity(i);
        } else if (itemId == R.id.cart) {
            Intent i = new Intent(Favorite.this, cart.class);
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
