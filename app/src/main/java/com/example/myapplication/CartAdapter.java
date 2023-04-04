package com.example.myapplication;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.carthandler> {

    private final Context context;
    private final ArrayList<CartModel> cartModelArrayList;

    // Constructor
    public CartAdapter(Context context, ArrayList<CartModel> cartModelArrayList) {
        this.context = context;
        this.cartModelArrayList = cartModelArrayList;
    }

    @NonNull
    @Override
    public CartAdapter.carthandler onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // to inflate the layout for each item of recycler view.
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_cart_card, parent, false);
        return new carthandler(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartAdapter.carthandler holder, int position) {
        // to set data to textview and imageview of each card layout
        Log.d("TAG", "onBindViewHolder: "+position);
        CartModel model = cartModelArrayList.get(position);
        holder.productName.setText(model.getProductName());
        holder.store.setText(model.getSeller());
        holder.cost.setText(model.getCost());
        Log.d("TAG", "onBindViewHolder: image url : "+model.getImageUrl());
        try {
            new CartAdapter.DownloadImageTask(holder.productImage).execute(model.getImageUrl()).get();
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    new ProductDetails(context).execute(model.getBarcode(), "main").get();
                } catch (ExecutionException e) {
                    throw new RuntimeException(e);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        // this method is used for showing number of card items in recycler view
        return cartModelArrayList.size();
    }


    // View holder class for initializing of your views such as TextView and Imageview
    //hereeee!!
    public static class carthandler extends RecyclerView.ViewHolder {
        private final ImageView productImage;
        private final TextView productName;
        private final TextView store;
        private final TextView cost;
        private CardView cardView;

        public carthandler(View itemView) {
            super(itemView);
            productName = itemView.findViewById(R.id.productName);
            store = itemView.findViewById(R.id.store);
            cardView = itemView.findViewById(R.id.base_cardview);
            cost = itemView.findViewById(R.id.cost);
            productImage = itemView.findViewById(R.id.productImage);
        }
    }
    public class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", "in the doinbkg mtd: "+e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            Log.d("TAG", "onPostExecute: in image postexecute thing");
            bmImage.setImageBitmap(result);
        }
    }
}
