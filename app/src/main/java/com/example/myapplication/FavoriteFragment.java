package com.example.myapplication;

import android.database.Cursor;
import android.graphics.Canvas;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

public class FavoriteFragment extends Fragment {
    FavoriteModel fv;
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        // Defines the xml file for the fragment
//        return inflater.inflate(R.layout.home_fragment, parent, false);
        View view = inflater.inflate(R.layout.favorite_fragment, parent,false);
        RecyclerView courseRV = view.findViewById(R.id.recycler_view);
        DataController dataController=new DataController(getActivity().getBaseContext());
        Cursor favs = dataController.retrieve();
        ArrayList<FavoriteModel> favslist = new ArrayList<>();
        try {
            while(favs.moveToNext()){
//                Log.d("TAG", "onCreate: favs from db"+favs.getString(1) +" "+ favs.getString(2)+" "+ favs.getString(0) );
                fv = new FavoriteModel(favs.getString(1) , favs.getString(2), favs.getString(0));
                favslist.add(fv);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        FavoriteAdapter favAdapter = new FavoriteAdapter(getContext(), favslist);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);

        // in below two lines we are setting layoutmanager and adapter to our recycler view.
        courseRV.setLayoutManager(linearLayoutManager);
        courseRV.setAdapter(favAdapter);


        ColorDrawable background = new ColorDrawable(ContextCompat.getColor(getContext(), R.color.delete));

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                // this method is called
                // when the item is moved.
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                // this method is called when we swipe our item to right direction.
                // on below line we are getting the item at a particular position.
                FavoriteModel deletedCourse = favslist.get(viewHolder.getAdapterPosition());

                // below line is to get the position
                // of the item at that position.
                int position = viewHolder.getAdapterPosition();
                // this method is called when item is swiped.
                // below line is to remove item from our array list.
                favslist.remove(viewHolder.getAdapterPosition());

                // below line is to notify our item is removed from adapter.
                favAdapter.notifyItemRemoved(viewHolder.getAdapterPosition());
                DataController db = new DataController(getActivity().getApplicationContext());
                db.deleteFav(deletedCourse.getBarcode());

                // below line is to display our snackbar with action.
                Snackbar.make(courseRV, deletedCourse.getProductName(), Snackbar.LENGTH_LONG).setAction("Undo", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // adding on click listener to our action of snack bar.
                        // below line is to add our item to array list with a position.
                        favslist.add(position, deletedCourse);
                        db.insertFav(fv.getBarcode(), fv.getProductName(),fv.getImageUrl());

                        // below line is to notify item is
                        // added to our adapter class.
                        favAdapter.notifyItemInserted(position);
                    }
                }).show();

            }
            public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView,
                                    @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY,
                                    int actionState, boolean isCurrentlyActive) {
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
                View itemView = viewHolder.itemView;
                int backgroundCornerOffset = 20;

                if (dX > 0) { // Swiping to the right
                    background.setBounds(itemView.getLeft(), itemView.getTop(),
                            itemView.getLeft() + ((int) dX) + backgroundCornerOffset,
                            itemView.getBottom());

                } else if (dX < 0) { // Swiping to the left
                    background.setBounds(itemView.getRight() + ((int) dX) - backgroundCornerOffset,
                            itemView.getTop(), itemView.getRight(), itemView.getBottom());
                } else { // view is unSwiped
                    background.setBounds(0, 0, 0, 0);
                }
                background.draw(c);
            }
            // at last we are adding this
            // to our recycler view.
        }).attachToRecyclerView(courseRV);
        return view;
    }
}
