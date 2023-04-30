package com.example.myapplication;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;

public class googleApiFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        return super.onCreateView(inflater, container, savedInstanceState);
        View v = inflater.inflate(R.layout.google_api_layout, container,false);
        Toast.makeText(getContext(), "in google API", Toast.LENGTH_SHORT).show();
        TextView title = v.findViewById(R.id.title);
        String info = getArguments().getString("json obj");
        RecyclerView courseRV = v.findViewById(R.id.recyclerView);
        ArrayList<GoogleApiModel> googleApiModelArrayList = new ArrayList<>();
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonData = objectMapper.readTree(info);
            if(Integer.parseInt(jsonData.get("searchInformation").get("totalResults").toString().replaceAll("\"","")) == 0){
                Toast.makeText(getActivity().getApplicationContext(), "Invalid barcode scanned", Toast.LENGTH_SHORT).show();
                return v;
            }
            title.setText(jsonData.get("items").get(0).get("title").toString());

            int i = Integer.parseInt(jsonData.get("searchInformation").get("totalResults").toString().replaceAll("\"",""));
            while(i>0) {
                i--;
                String titles = jsonData.get("items").get(i).get("displayLink").toString();
                String URL = jsonData.get("items").get(i).get("link").toString();
                String snippet = jsonData.get("items").get(i).get("snippet").toString();
                JsonNode imageUrl = jsonData.get("items").get(i).get("pagemap");
                String image = "";
                if(imageUrl.get("cse_thumbnail").get("src")!=null){
                    image = imageUrl.get("cse_thumbnail").get("src").toString();
                }
                else if(imageUrl.get("cse_image").get("src")!=null){
                    image = imageUrl.get("cse_image").get("src").toString();
                }
                Log.d("TAG", "onCreateView: in google api fragment loop : "+jsonData.get("items").get(0).get("title").toString());
                GoogleApiModel gpi = new GoogleApiModel(jsonData.get("items").get(0).get("title").toString(), URL, titles, image);
                googleApiModelArrayList.add(gpi);
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        Log.d("TAG", "onCreateView: checking the arraylist : "+googleApiModelArrayList.get(0).getTitle());
        GoogleApiAdapter gpiAdapter = new GoogleApiAdapter(getContext(), googleApiModelArrayList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        courseRV.setLayoutManager(linearLayoutManager);
        courseRV.setAdapter(gpiAdapter);
        return v;
    }
}
