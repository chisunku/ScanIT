package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;

public class HomeFragment extends Fragment {
    ImageView scan;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home_fragment, parent,false);
        scan = view.findViewById(R.id.scanBtn);
        scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ScanOptions options = new ScanOptions();
                options.setDesiredBarcodeFormats(ScanOptions.ONE_D_CODE_TYPES);
                options.setPrompt("Scan a barcode");
                options.setOrientationLocked(true);
                options.setBeepEnabled(true);
                options.setBarcodeImageEnabled(true);
                options.setCaptureActivity(Capture.class);
                barcodeLauncher.launch(options);
            }
        });

        Button analytics = view.findViewById(R.id.analytics);
        analytics.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent i = new Intent(getContext(), )
                Analytics analytics = new Analytics();
                FragmentManager fragmentManager =  ((AppCompatActivity)getActivity()).getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.content, analytics);
                fragmentTransaction.addToBackStack("prod");
                fragmentTransaction.commit();
            }
        });

        return view;
    }

    private final ActivityResultLauncher<ScanOptions> barcodeLauncher = registerForActivityResult(new ScanContract(),
            result -> {
                if(result.getContents() == null) {
                    Toast.makeText(getActivity(), "Cancelled", Toast.LENGTH_LONG).show();
                } else {
//                    Toast.makeText(MainActivity.this, "Scanned: " + result.getContents(), Toast.LENGTH_LONG).show();
                    Log.d("TAG", "scanned barcode: "+result.getContents());
                    Log.d("TAG", "results: "+result);
                    ProductDetails jb = new ProductDetails(getActivity());
                    jb.execute(result.getContents(), "main");
//                    GoogleAPI gpi = new GoogleAPI(getActivity());
//                    gpi.execute(result.getContents());
                }
            });

}
