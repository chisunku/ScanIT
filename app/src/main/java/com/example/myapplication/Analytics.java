package com.example.myapplication;

import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.app.ProgressDialog;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

//import org.eazegraph.lib.charts.PieChart;
//import org.eazegraph.lib.models.PieModel;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.eazegraph.lib.charts.PieChart;
import org.eazegraph.lib.models.PieModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;

public class Analytics extends Fragment {

    ProgressDialog progressDialog;
    private CountDownLatch latch;
    PieChart pieChart;
    List<firebaseModel> data;

    RecyclerView parent = null;

    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.analytics_fragment, parent, false);
        Log.d("TAG", "onCreateView: in analytics class");
//        parent = view.findViewById(R.id.parentLinearLayout);
        latch = new CountDownLatch(1);
        try {
            new datafetch().execute().get();
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
//        System.out.println("analytics : "+getArguments().getString("analytics"));
        System.out.println("map values : "+data);
        pieChart = view.findViewById(R.id.piechart);
        return view;
    }



    private void processData(List<firebaseModel> data) {
        this.data = data;
        latch.countDown();
        String[] c = new String[]{"#66BB6A", "#EF5350", "#29B6F6","#FFA726","#FC46AA"};

//        parent.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
//        parent.setOrientation(LinearLayout.HORIZONTAL);

        parent = getView().findViewById(R.id.recycler_view);



        int[] rainbow = getContext().getResources().getIntArray(R.array.pie);
        ArrayList<firebaseModel> pieList = new ArrayList<>();
        ArrayList<firebaseModel> tableList = new ArrayList<>();

        int j = 0;
        Collections.sort(data, (a,b)->b.getCount()-a.getCount());
        for(firebaseModel d : data){
            if(j<5) {
                pieChart.addPieSlice(new PieModel(d.getStoreName(), d.getCount(), rainbow[j]));
                pieList.add(d);
            }
            j++;
            tableList.add(d);

        }

        pieAdapter pa = new pieAdapter(getContext(), pieList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        parent.setLayoutManager(linearLayoutManager);
        parent.setAdapter(pa);

        RecyclerView table = getView().findViewById(R.id.fbData);
        tableAnalyticsAdapter tableAnalyticsAdapter = new tableAnalyticsAdapter(getContext(), tableList);
        LinearLayoutManager linearLayoutManager1 = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        table.setLayoutManager(linearLayoutManager1);
        table.setAdapter(tableAnalyticsAdapter);

    }

    class datafetch extends AsyncTask<Void, Void, List<firebaseModel>>{
        @Override
        protected List<firebaseModel> doInBackground(Void... vars) {
            FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
            Log.d("TAG", "doInBackground: in do in background ");
            CollectionReference collectionRef = firebaseFirestore.collection("mba");
            List<firebaseModel> map = new ArrayList<>();
            collectionRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            // Access the document data
//                            System.out.println("in do in back "+document.getId()+" "+document.getData().get("count"));
                            map.add(new firebaseModel(document.getId(), Integer.parseInt(document.getData().get("count").toString())));
                        }
                        Log.d("TAG", "onComplete: map in doin back: "+map);
                        processData(map);
                    } else {
                        Log.d("TAG", "Error getting documents: ", task.getException());
                    }
                }
            });
            Log.d("TAG", "doInBackground: after doinback "+map);
            return map;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = ProgressDialog.show(getContext(),
                    "ProgressDialog",
                    "Fetching data");
        }

        @Override
        protected void onPostExecute(List<firebaseModel> stringObjectHashMap) {
            super.onPostExecute(stringObjectHashMap);
            progressDialog.dismiss();
        }
    }

}
