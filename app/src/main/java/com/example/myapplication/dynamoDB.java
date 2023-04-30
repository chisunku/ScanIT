package com.example.myapplication;


import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;


import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClients;

import org.bson.Document;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.mongodb.App;
import io.realm.mongodb.AppConfiguration;
import io.realm.mongodb.Credentials;
import io.realm.mongodb.User;
import io.realm.mongodb.mongo.MongoClient;
import io.realm.mongodb.mongo.MongoCollection;
import io.realm.mongodb.mongo.MongoDatabase;


public class dynamoDB extends AsyncTask<String , Void, Void> {
    Context ctx = null;
    String TAG = "firestore";
//    AmazonDynamoDBClient client = new AmazonDynamoDBClient(new BasicAWSCredentials("AKIAZNFJQDK2IKAELOJQ", "UE9J4FPG6TSY/8nz721h5ISysdI+RI76XDIn/f29"));
    public dynamoDB(Context ctx){
        this.ctx = ctx;
    }
    public void addItem(){
        FirebaseApp.initializeApp(ctx);
        //firebase
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("mba");
//        myRef.child("Walmart").setValue(1);
        myRef.orderByValue().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<Map.Entry<String, Long>> list = new ArrayList<>();
                for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                    String storeName = childSnapshot.getKey();
                    Long count = (Long) childSnapshot.getValue();
                    Map.Entry<String, Long> entry = new AbstractMap.SimpleEntry<>(storeName, count);
                    list.add(entry);
                }
                Collections.sort(list, new Comparator<Map.Entry<String, Long>>() {
                    @Override
                    public int compare(Map.Entry<String, Long> o1, Map.Entry<String, Long> o2) {
                        return (int) (o2.getValue() - o1.getValue());
                    }
                });
                // Now the list contains the stores ordered by count
                System.out.println("list in FB : "+list);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("TAG", "Error reading stores", databaseError.toException());
            }
        });
    }

    @Override
    protected Void doInBackground(String... vars) {
        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setPersistenceEnabled(false)
                .build();
        firebaseFirestore.setFirestoreSettings(settings);
        HashMap<String , Object> map = new HashMap<>();
        map.put("count", 1);

        //---- adding document ----
//        firebaseFirestore.collection("mba").document("walmart")
//                .set(map)
//                .addOnSuccessListener(new OnSuccessListener<Void>() {
//                    @Override
//                    public void onSuccess(Void aVoid) {
//                        Log.d(TAG, "Document added with ID: " + "walmart");
//                    }
//                })
//                .addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        Log.w(TAG, "Error adding document", e);
//                    }
//                });

        CollectionReference mbaRef = firebaseFirestore.collection("mba");

        //---- where clause -----
//        mbaRef.whereEqualTo("storeName", "walmart")
//                .get()
//                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                        if (task.isSuccessful()) {
//                            for (QueryDocumentSnapshot document : task.getResult()) {
//                                Log.d("TAG", document.getId() + " => " + document.getData());
//                            }
//                        } else {
//                            Log.d("TAG", "Error getting documents: ", task.getException());
//                        }
//                    }
//                });

        String storename = vars[0].toLowerCase();
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

//        DatabaseReference myRef = database.getReference("mba");
//        Log.d("TAG", "doInBackground: in doinback dynamodb");
//        myRef.equalTo("walmart").addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                // Get the result of the query
//                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
//                    // Process the result
//                    Log.d("in walmart FB", "onDataChange: "+snapshot.getKey());
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//                // Handle any errors
//            }
//        });

//        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                if (dataSnapshot.exists()) {
//                    DataSnapshot oldValue = dataSnapshot.child(vars[0]);
//                    if (oldValue != null) {
//                        Long newValue = oldValue + 1;
//                        myRef.setValue(newValue);
//                    }
//                }
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//                Log.e(TAG, "Error reading value", databaseError.toException());
//            }
//        });
        return null;
    }
}
