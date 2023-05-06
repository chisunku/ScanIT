package com.example.myapplication;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import android.widget.Button;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.FirebaseApp;

public class MainActivity extends AppCompatActivity {
    Button scan;
    BottomNavigationView navigationView = null;
    //    ProgressBar pb = findViewById(R.id.progressBar_cyclic);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FirebaseApp.initializeApp(this);
        navigationView = findViewById(R.id.bottom_navigation);
        navigationView.setOnNavigationItemSelectedListener(navListener);
        HomeFragment fragment = new HomeFragment();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.content, fragment, "");
        fragmentTransaction.commit();

    }

//    @Override
//    public void onBackPressed() {
//        int selectedItemId = navigationView.getSelectedItemId();
////        if (R.id.home != selectedItemId) {
////            // Set the selected item to the home menu item
//            navigationView.setSelectedItemId(selectedItemId);
////        } else {
//            super.onBackPressed();
////        }
//    }

    private final BottomNavigationView.OnNavigationItemSelectedListener navListener = item -> {
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
        else if (itemId == R.id.notes) {
            NotesFragment fragment1 = new NotesFragment();
            FragmentTransaction fragmentTransaction1 = getSupportFragmentManager().beginTransaction();
            fragmentTransaction1.replace(R.id.content, fragment1);
            fragmentTransaction1.addToBackStack("notes");
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