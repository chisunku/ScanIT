package com.example.myapplication;

import android.animation.ArgbEvaluator;
import android.animation.TimeAnimator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HomeFragment extends Fragment {
    ImageView scan;
    String TAG = "HomeFragment";
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home_fragment, parent,false);
        scan = view.findViewById(R.id.scanBtn);
        final int start = Color.parseColor("#FF6200EE");
        final int mid = Color.parseColor("#FFBB86FC");
        final int end = Color.TRANSPARENT;
//        CardView cv = view.findViewById(R.id.details);
//        DataController db = new DataController(getContext());
//        try{
//            Cursor c = db.retrieveCart();
//            String items = "";
//            while(c.moveToNext()){
//                items += c.getString(1)+",";
//            }
//            if(items.length()==0){
//                cv.setVisibility(View.INVISIBLE);
//            }
//            else {
//                items="give me 7 unique products similar to these products: "+items;
//                Log.d("TAG", "onCreateView: items in cart" + items);
//                chatGPT gpt = new chatGPT(view, getActivity(), getContext());
//                gpt.execute(items);
////                Log.d(TAG, "onCreateView: response : " + res);
////                JSONObject json = new JSONObject(res);
////                String response = json.getJSONArray("choices").getJSONObject(0).get("text").toString();
////                Log.d(TAG, "onCreateView: answer : "+response);
////                ListView lv = view.findViewById(R.id.list);
////                List<String> listArr = Arrays.asList(response.split("\\n"));
//////                listArr.remove(0);
//////                listArr.remove(1);
////                for(int i=0;i<listArr.size();i++){
////                    Log.d(TAG, "onCreateView: list has : "+listArr.get(i) );
////                }
////                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, listArr);
////                lv.setAdapter(adapter);
//            }
//        }catch(Exception e){
//            Log.e(TAG, "onCreateView: error in home, maybe chatGPT only ");
//            e.printStackTrace();
//        }


//        final ArgbEvaluator evaluator = new ArgbEvaluator();
//        View preloader = activity.findViewById(R.id.gradientPreloaderView);
//        preloader.setVisibility(View.VISIBLE);
//        final GradientDrawable gradient = (GradientDrawable) view.getBackground();

//        ValueAnimator animator = TimeAnimator.ofFloat(0.0f, 1.0f);
//        animator.setDuration(5000);
//        animator.setRepeatCount(ValueAnimator.INFINITE);
//        animator.setRepeatMode(ValueAnimator.REVERSE);
//        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
//            @Override
//            public void onAnimationUpdate(ValueAnimator valueAnimator) {
//                Float fraction = valueAnimator.getAnimatedFraction();
//                int newStrat = (int) evaluator.evaluate(fraction, start, end);
//                int newMid = (int) evaluator.evaluate(fraction, mid, start);
//                int newEnd = (int) evaluator.evaluate(fraction, end, mid);
//                int[] newArray = {newStrat, newMid, newEnd};
//                gradient.setColors(newArray);
//            }
//        });
//
//        animator.start();
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

        ImageView analytics = view.findViewById(R.id.forAnalytics);
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
//                dynamoDB dbb = new dynamoDB(getActivity());
//                dbb.execute();

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
