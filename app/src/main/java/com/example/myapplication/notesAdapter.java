package com.example.myapplication;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;


public class notesAdapter extends RecyclerView.Adapter<notesAdapter.notesHandler> {
    CartFragment cartObj = new CartFragment();
    private final Context context;

    MainViewModel mainViewModel;

    public ArrayList<NotesModel> notesList;


    // Constructor
    public notesAdapter(Context context, ArrayList<NotesModel> notesList) {
        this.context = context;
        this.notesList = notesList;
    }

    @NonNull
    @Override
    public notesAdapter.notesHandler onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // to inflate the layout for each item of recycler view.
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.notes_card_layout, parent, false);
        mainViewModel= ViewModelProviders.of((FragmentActivity) context)
                .get(MainViewModel.class);
        return new notesHandler(view);
    }

    @Override
    public void onBindViewHolder(@NonNull notesAdapter.notesHandler holder, int position) {
        // to set data to textview and imageview of each card layout
        Log.d("TAG", "onBindViewHolder: "+position);
        Log.d("TAG", "onBindViewHolder: "+notesList.size());
        NotesModel model = notesList.get(position);
        DataController db = new DataController(context);
//        Log.d("TAG", "onBindViewHolder: storename "+model.getStoreName());
        holder.item.setText(model.getTask());
        holder.item.setChecked((model.getChecked()==0)?false:true);
        holder.item.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    db.updateStatus(model.getTask(), 1);
                } else {
                    db.updateStatus(model.getTask(), 0);
                }
            }
        });
        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(holder.item.isChecked()){
                    db.updateStatus(model.getTask(), 0);
                }
                else{
                    db.updateStatus(model.getTask(),1);
                }
            }
        });

    }

    public void setItems(ArrayList<NotesModel> todoList) {
        this.notesList = todoList;
        try {
//            Log.d("TAG", "setItems: in notes adapter", todoList.get(0).getTask());
            System.out.println("task : "+todoList.size());
        }catch(Exception e){
            System.out.println("try error itseems");
            e.printStackTrace();
        }
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        // this method is used for showing number of card items in recycler view
        return notesList.size();
    }

    public static class notesHandler extends RecyclerView.ViewHolder {
        private final CheckBox item;
        RelativeLayout layout;
//        private final ImageView checkbox;

        public notesHandler(View itemView) {
            super(itemView);
            layout = itemView.findViewById(R.id.layout);
            item = itemView.findViewById(R.id.todoCheckBox);

        }
    }
}

