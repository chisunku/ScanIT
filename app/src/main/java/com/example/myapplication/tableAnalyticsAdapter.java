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

public class tableAnalyticsAdapter extends RecyclerView.Adapter<tableAnalyticsAdapter.tableHandler> {
    CartFragment cartObj = new CartFragment();
    private final Context context;

    int[] rainbow;

    MainViewModel mainViewModel;
    public final ArrayList<firebaseModel> tableList;


    // Constructor
    public tableAnalyticsAdapter(Context context, ArrayList<firebaseModel> tableList) {
        this.context = context;
        this.tableList = tableList;
        rainbow = context.getResources().getIntArray(R.array.pie);
    }

    @NonNull
    @Override
    public tableAnalyticsAdapter.tableHandler onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.table_view_card_analytics, parent, false);
        mainViewModel= ViewModelProviders.of((FragmentActivity) context)
                .get(MainViewModel.class);
        return new tableHandler(view);
    }

    @Override
    public void onBindViewHolder(@NonNull tableAnalyticsAdapter.tableHandler holder, int position) {
        Log.d("TAG", "onBindViewHolder: "+position);
        Log.d("TAG", "onBindViewHolder: "+tableList.size());
        firebaseModel model = tableList.get(position);
        Log.d("TAG", "onBindViewHolder: storename "+model.getStoreName()+" "+model.getCount());
        holder.storeName.setText(model.getStoreName());
//        holder.storeName.setTextColor(R.color.black);
            holder.countT.setText(model.getCount()+"");
//            holder.countT.setTextColor(rainbow[position]);
    }


    @Override
    public int getItemCount() {
        return tableList.size();
    }

    public static class tableHandler extends RecyclerView.ViewHolder {
        private final TextView storeName;

        private final TextView countT;
//        private final ImageView checkbox;

        public tableHandler(View itemView) {
            super(itemView);
            storeName = itemView.findViewById(R.id.storeName);
            countT = itemView.findViewById(R.id.countT);
        }
    }
}

