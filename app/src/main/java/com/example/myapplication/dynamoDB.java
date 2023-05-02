package com.example.myapplication;


import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.checkerframework.checker.units.qual.A;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class dynamoDB extends AsyncTask<Void , Void, HashMap<String, Object>> {
    Context ctx = null;

    ProgressDialog progressDialog;
    String TAG = "firestore";
//    AmazonDynamoDBClient client = new AmazonDynamoDBClient(new BasicAWSCredentials("AKIAZNFJQDK2IKAELOJQ", "UE9J4FPG6TSY/8nz721h5ISysdI+RI76XDIn/f29"));
    public dynamoDB(Context ctx){
        this.ctx = ctx;
    }

    public void addToDB(String storeName){
        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
//        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
//                .setPersistenceEnabled(false)
//                .build();
//        firebaseFirestore.setFirestoreSettings(settings);
        HashMap<String , Object> map = new HashMap<>();
        map.put("count", 1);
        Log.d(TAG, "doInBackground: in do in background ");
        CollectionReference mbaRef = firebaseFirestore.collection("mba");
        String storename = storeName.toLowerCase();
        DocumentReference docRef = firebaseFirestore.collection("mba").document(storename);

        // ---- Check if the document exists else add -----
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        int count = Integer.parseInt(document.getData().get("count").toString());
                        Log.d(TAG, "onComplete: before increment count : "+count);
                        count++;
                        Log.d(TAG, "onComplete: count : "+count);
                        // If the document exists, update the other values
                        docRef.update("count", count)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Log.d(TAG, "DocumentSnapshot successfully updated!");
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.w(TAG, "Error updating document", e);
                                    }
                                });
                    } else {
                        Log.d(TAG, "Document does not exist Adding new one ");
                        firebaseFirestore.collection("mba").document(storename)
                                .set(map)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Log.d(TAG, "Document added with ID: " + storename);
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.w(TAG, "Error adding document", e);
                                    }
                                });
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });
    }

    @Override
    protected HashMap<String, Object> doInBackground(Void... vars) {
        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
        Log.d(TAG, "doInBackground: in do in background ");
        CollectionReference collectionRef = firebaseFirestore.collection("mba");
        HashMap<String, Object> map = new HashMap<>();
        collectionRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        // Access the document data
                        System.out.println("in do in back "+document.getId()+" "+document.getData().get("count"));
                        map.put(document.getId(), document.getData().get("count"));
                    }
                } else {
                    Log.d(TAG, "Error getting documents: ", task.getException());
                }
            }
        });
        Log.d(TAG, "doInBackground: after doinback "+map);
        return map;
    }

    protected void onPreExecute() {
        super.onPreExecute();
        progressDialog = ProgressDialog.show(ctx,
                "ProgressDialog",
                "Fetching data");
    }

    @Override
    protected void onPostExecute(HashMap<String, Object> stringObjectHashMap) {
        super.onPostExecute(stringObjectHashMap);
        Log.d(TAG, "onPostExecute: dynamodb "+stringObjectHashMap);
        Bundle bundle = new Bundle();
        bundle.putString("analytics", String.valueOf(stringObjectHashMap));
        Analytics ana = new Analytics();
        ana.setArguments(bundle);
        FragmentManager fragmentManager =  ((AppCompatActivity)ctx).getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.content, ana);
        fragmentTransaction.addToBackStack("prod");
        fragmentTransaction.commit();
        progressDialog.dismiss();
    }
}
