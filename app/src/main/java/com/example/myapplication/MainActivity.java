package com.example.myapplication;

import android.database.Cursor;
import android.os.Bundle;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.preference.PreferenceFragmentCompat;


import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;

import org.bson.Document;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;

import io.realm.Realm;
import io.realm.mongodb.App;
import io.realm.mongodb.AppConfiguration;
import io.realm.mongodb.Credentials;
import io.realm.mongodb.User;
import io.realm.mongodb.mongo.MongoClient;
import io.realm.mongodb.mongo.MongoCollection;
import io.realm.mongodb.mongo.MongoDatabase;

public class MainActivity extends AppCompatActivity {
    Button scan;
    //    ProgressBar pb = findViewById(R.id.progressBar_cyclic);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        FirebaseApp.initializeApp(this);
//        //firebase
//        FirebaseDatabase database = FirebaseDatabase.getInstance();
//        DatabaseReference myRef = database.getReference("analytics");
//        myRef.child("message").setValue("Hello, World!");
//        myRef.child("message").addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                String message = dataSnapshot.getValue(String.class);
//                Log.d("in firebase main", "Value is: " + message);
//            }
//
//            @Override
//            public void onCancelled(DatabaseError error) {
//                Log.w("in firebase main", "Failed to read value.", error.toException());
//            }
//        });


//        Realm.init(this);
//        RealmApp realmApp = new RealmApp.Builder()
//                .build("scanit-oebpn");
//        App app = new App(new AppConfiguration.Builder("scanit-oebpn").build());
////        User user = app.loginAsync(Credentials.Anonymous()).get();
//        User user = app.login(Credentials.anonymous());
////mongodb+srv://root:<password>@cluster0.eob7rat.mongodb.net/?retryWrites=true&w=majority
//        MongoClient client = user.getMongoClient("mongodb+srv://root:root@cluster0.eob7rat.mongodb.net/?retryWrites=true&w=majority");
//        MongoDatabase db = client.getDatabase("ScanIT");
//        MongoCollection<Document> collection = db.getCollection("mba");
//        HashMap<String, Object> map = new HashMap<>();
//        map.put("productTitle","coke");
//        map.put("websitesClicked","amazon, target");
//        map.put("addedToFav", "yes");
//        map.put("cartStores","target");
//        collection.insertOne(new Document(map));
//        mongo
//        dynamoDB mongo = new dynamoDB();
//        mongo.execute();
//        getSupportActionBar().setDisplayShowTitleEnabled(false);
//        scan = findViewById(R.id.scanBtn);
        BottomNavigationView navigationView = findViewById(R.id.bottom_navigation);
        navigationView.setOnNavigationItemSelectedListener(navListener);
        HomeFragment fragment = new HomeFragment();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.content, fragment, "");
        fragmentTransaction.commit();

    }
    private final BottomNavigationView.OnNavigationItemSelectedListener navListener = item -> {
        Fragment selectedFragment = null;
        int itemId = item.getItemId();
        if(itemId == R.id.home){
            HomeFragment fragment = new HomeFragment();
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.content, fragment, "");
            fragmentTransaction.addToBackStack("home");
            fragmentTransaction.commit();
            return true;
        } else if (itemId == R.id.favorite) {
            FavoriteFragment fragment2 = new FavoriteFragment();
            FragmentTransaction fragmentTransaction1 = getSupportFragmentManager().beginTransaction();
            fragmentTransaction1.replace(R.id.content, fragment2);
            fragmentTransaction1.addToBackStack("fav");
            fragmentTransaction1.commit();
            return true;
        } else if (itemId == R.id.cart) {
            CartFragment fragment1 = new CartFragment();
            FragmentTransaction fragmentTransaction1 = getSupportFragmentManager().beginTransaction();
            fragmentTransaction1.replace(R.id.content, fragment1);
            fragmentTransaction1.addToBackStack("cart");
            fragmentTransaction1.commit();
            return true;
        }
        // It will help to replace the
        // one fragment to other.
//        if (selectedFragment != null) {
//            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();
//        }
        return false;
    };

}