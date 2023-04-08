package com.example.myapplication;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.handler> {

    private final Context context;
    private final ArrayList<ProductModel> productModelArrayList;
    DataController db;

    // Constructor
    public ProductAdapter(Context context, ArrayList<ProductModel> productModelArrayList) {
        this.context = context;
        this.productModelArrayList = productModelArrayList;
        db = new DataController(context.getApplicationContext());
    }

    @NonNull
    @Override
    public ProductAdapter.handler onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // to inflate the layout for each item of recycler view.
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_card_layout, parent, false);
        return new handler(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductAdapter.handler holder, int position) {
        // to set data to textview and imageview of each card layout
        ProductModel model = productModelArrayList.get(position);
        holder.courseNameTV.setText(model.getStoreName());
        holder.courseRatingTV.setText("" + model.getCost());
//        holder.courseIV.setImageResource(model.getCourse_image());
        holder.plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int qty = model.getQuantity()+1;
                if(qty>15)
                    Toast.makeText(context.getApplicationContext(), "Too many items added", Toast.LENGTH_LONG).show();
                else {
                    model.setQuantity(qty);
                    holder.quantity.setText(qty+"");
                }
            }
        });
        holder.minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int qty = model.getQuantity();
                if(qty != 0){
                    qty-=1;
                    model.setQuantity(qty);
                    holder.quantity.setText(qty+"");
                }
                else{
                    Toast.makeText(context.getApplicationContext(), "No items added", Toast.LENGTH_LONG).show();
                }
            }
        });
        holder.cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("added to cart + "+model.getImageUrl());
                Toast.makeText(context.getApplicationContext(), "adding to cart", Toast.LENGTH_LONG).show();
//                DataController db = new DataController(context.getApplicationContext());
                //qty;
                int qty = Integer.parseInt(holder.quantity.getText().toString());
                long r = db.insertCart(model.getBarcode(), model.getProductName(), model.getCost(), qty, model.getStoreName(), model.getUrl(), model.getImageUrl(), "product");
                if(r == -1){
                    Toast.makeText(context.getApplicationContext(), "Something went wrong :( please try again!!", Toast.LENGTH_LONG).show();
                }
            }
        });
        holder.clickableLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //implement onClick
                System.out.println("Clicked");
                Toast.makeText(context.getApplicationContext(), "clicked "+model.getStoreName(),Toast.LENGTH_LONG).show();
                if(model.getUrl()==null){
                    Toast.makeText(context,"Something went wrong, please try again latar!!",Toast.LENGTH_LONG).show();
                }
                else {
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(model.getUrl()));
                    ((Activity)v.getContext()).startActivity(i);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        // this method is used for showing number of card items in recycler view
        return productModelArrayList.size();
    }

    // View holder class for initializing of your views such as TextView and Imageview
    //hereeee!!
    public static class handler extends RecyclerView.ViewHolder {
//        private final ImageView courseIV;
        private final TextView courseNameTV;
        private final TextView courseRatingTV;
        private final FloatingActionButton cart;
        private final TextView quantity;
//        private CardView cardView;
        private final FloatingActionButton plus;
        private final FloatingActionButton minus;
        private final LinearLayout clickableLayout;
//        private final TextView priceComp;

        public handler(View itemView) {
            super(itemView);
            courseNameTV = itemView.findViewById(R.id.storeName);
            courseRatingTV = itemView.findViewById(R.id.cost);
//            cardView = itemView.findViewById(R.id.base_cardview);
            cart = itemView.findViewById(R.id.cart);
            plus = itemView.findViewById(R.id.plus);
            minus = itemView.findViewById(R.id.minus);
            quantity = itemView.findViewById(R.id.qty);
            clickableLayout = itemView.findViewById(R.id.clickableLayout);
//            priceComp = itemView.findViewById(R.id.priceComp);
        }
    }
}
