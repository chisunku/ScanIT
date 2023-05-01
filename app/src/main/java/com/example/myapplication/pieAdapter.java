package com.example.myapplication;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class pieAdapter extends RecyclerView.Adapter<pieAdapter.pieHandler> {
    CartFragment cartObj = new CartFragment();
    private final Context context;

    int[] rainbow;

    MainViewModel mainViewModel;
    public final ArrayList<firebaseModel> pieList;


    // Constructor
    public pieAdapter(Context context, ArrayList<firebaseModel> pieList) {
        this.context = context;
        this.pieList = pieList;
        rainbow = context.getResources().getIntArray(R.array.pie);
    }

    @NonNull
    @Override
    public pieAdapter.pieHandler onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // to inflate the layout for each item of recycler view.
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chart_card, parent, false);
        mainViewModel= ViewModelProviders.of((FragmentActivity) context)
                .get(MainViewModel.class);
        return new pieHandler(view);
    }

    @Override
    public void onBindViewHolder(@NonNull pieAdapter.pieHandler holder, int position) {
        // to set data to textview and imageview of each card layout
        Log.d("TAG", "onBindViewHolder: "+position);
        Log.d("TAG", "onBindViewHolder: "+pieList.size());
        firebaseModel model = pieList.get(position);
        Log.d("TAG", "onBindViewHolder: storename "+model.getStoreName());
        holder.storeName.setText(model.getStoreName());
        holder.colSquare.setBackgroundColor(rainbow[position]);

    }


    @Override
    public int getItemCount() {
        // this method is used for showing number of card items in recycler view
        return pieList.size();
    }

    public static class pieHandler extends RecyclerView.ViewHolder {
        private final TextView storeName;

        private final View colSquare;
//        private final ImageView checkbox;

        public pieHandler(View itemView) {
            super(itemView);
            storeName = itemView.findViewById(R.id.storeName);
            colSquare = itemView.findViewById(R.id.color);
        }
    }
}

