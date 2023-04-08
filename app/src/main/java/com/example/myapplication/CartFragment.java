package com.example.myapplication;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.snackbar.Snackbar;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class CartFragment extends Fragment {
    public static CartFragment instance = null;
    public TextView total;
    CartAdapter favAdapter;
    View view;
    Context ctx;
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        // Defines the xml file for the fragment
//        return inflater.inflate(R.layout.home_fragment, parent, false);
        instance = new CartFragment();
        view = inflater.inflate(R.layout.cart_fragment, parent,false);
        RecyclerView courseRV = view.findViewById(R.id.items);
        ctx = getActivity().getBaseContext();

        DataController dataController = new DataController(getActivity().getBaseContext());
        Cursor favs = dataController.retrieveCart();
        ArrayList<CartModel> favslist = new ArrayList<>();
        float cartTotal = 0;
        try {
            while(favs.moveToNext()){
                int quantity = favs.getInt(3);
                cartTotal += quantity*Float.parseFloat(favs.getString(2).replaceAll("[^.0-9]",""));
                Log.d("TAG", "onCreate: qty n tot "+quantity+" "+cartTotal);
                CartModel fv = new CartModel(favs.getString(0) ,
                        favs.getString(1), favs.getString(2),
                        Integer.parseInt(favs.getString(3)), favs.getString(4),
                        favs.getString(5), favs.getString(6));
                favslist.add(fv);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        total = view.findViewById(R.id.total);
        favAdapter = new CartAdapter(getContext(), favslist, total);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);

        // in below two lines we are setting layoutmanager and adapter to our recycler view.
        courseRV.setLayoutManager(linearLayoutManager);
        courseRV.setAdapter(favAdapter);

        DecimalFormat df = new DecimalFormat("0.00");
        String cTotal = df.format(cartTotal);
        total.setText(cTotal);



        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                // this method is called
                // when the item is moved.
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                CartModel deletedCourse = favslist.get(viewHolder.getAdapterPosition());
                int position = viewHolder.getAdapterPosition();
                favslist.remove(viewHolder.getAdapterPosition());
                favAdapter.notifyItemRemoved(viewHolder.getAdapterPosition());
                DataController db = new DataController(getContext().getApplicationContext());
                db.deleteCart(deletedCourse.getBarcode(), deletedCourse.getSeller());
                updateTotal(total, getContext());
                Snackbar.make(courseRV, deletedCourse.getProductName(), Snackbar.LENGTH_LONG).setAction("Undo", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        favslist.add(position, deletedCourse);
                        db.insertCart(deletedCourse.getBarcode(), deletedCourse.getProductName(), deletedCourse.getCost(),
                                deletedCourse.getQuantity(), deletedCourse.getSeller(), deletedCourse.getUrl(),
                                deletedCourse.getImageUrl(), "undo");
                        updateTotal(total, getContext());
                        favAdapter.notifyItemInserted(position);

                    }
                }).show();
            }
        }).attachToRecyclerView(courseRV);

        return view;
    }

//    @Override
//    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
//        super.onViewCreated(view, savedInstanceState);
//        total = view.findViewById(R.id.total);
//        DataController dataController = new DataController(ctx);
//        Cursor favs = dataController.retrieveCart();
//        float t = 0;
//        while(favs.moveToNext()){
//            t += favs.getInt(3)*Float.parseFloat(favs.getString(2)
//                    .replaceAll("[^.0-9]",""));
//        }
//        Log.d("in updatetotal", "updateTotal: total = "+t);
////        total = view.findViewById(R.id.total);
////    .findViewById(R.id.total);
//        total.setText("hello");
//        total.setText(t+"");
//    }

    public void updateTotal(TextView tot, Context ctx){
        DataController dataController = new DataController(ctx);
        Cursor favs = dataController.retrieveCart();
        float t = 0;
        while(favs.moveToNext()){
            t += favs.getInt(3)*Float.parseFloat(favs.getString(2)
                    .replaceAll("[^.0-9]",""));
        }
        Log.d("in updatetotal", "updateTotal: total = "+t);
        DecimalFormat df = new DecimalFormat("0.00");
        String total = df.format(t);
        Log.d("TAG", "updateTotal: total = "+total);
        tot.setText(total);
    }
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {

        super.setUserVisibleHint(isVisibleToUser);

        // Refresh tab data:

        if (getFragmentManager() != null) {

            getFragmentManager()
                    .beginTransaction()
                    .detach(this)
                    .attach(this)
                    .commit();
        }
    }
}
