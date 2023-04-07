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
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

public class MainActivity extends AppCompatActivity {
    Button scan;
    //    ProgressBar pb = findViewById(R.id.progressBar_cyclic);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
        return false;
    };

}