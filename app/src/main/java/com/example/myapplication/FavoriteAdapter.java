package com.example.myapplication;

import android.app.AlertDialog;
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

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class FavoriteAdapter extends RecyclerView.Adapter<FavoriteAdapter.handler> {

    private final Context context;
    private final ArrayList<FavoriteModel> favModelArrayList;

    // Constructor
    public FavoriteAdapter(Context context, ArrayList<FavoriteModel> favModelArrayList) {
        this.context = context;
        this.favModelArrayList = favModelArrayList;
    }

    @NonNull
    @Override
    public FavoriteAdapter.handler onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // to inflate the layout for each item of recycler view.
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_fav_model, parent, false);
        return new handler(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FavoriteAdapter.handler holder, int position) {
        // to set data to textview and imageview of each card layout
        FavoriteModel model = favModelArrayList.get(position);
        holder.ProductName.setText(model.getProductName());
        try {
            new DownloadImageTask(holder.img).execute(model.getImageUrl()).get();
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
//        holder.courseIV.setImageResource(model.getCourse_image());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //implement onClick
                try {
                    new ProductDetails(context).execute(model.getBarcode(), "main").get();
                } catch (ExecutionException e) {
                    throw new RuntimeException(e);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage("Are you sure you want to remove "+model.getProductName()+" from the favorites?");
                builder.setTitle("REMOVE");
                builder.setCancelable(true);
                builder.setPositiveButton("Yes", (dialog, which) -> {
                    DataController db = new DataController(context);
                    db.deleteFav(model.getBarcode());
                    Toast.makeText(context, "Item "+model.getProductName()+" deleted!!",Toast.LENGTH_LONG).show();
                    favModelArrayList.remove(position);
                    notifyDataSetChanged();
                });
                builder.setNegativeButton("No", (dialog, which) -> {
                    dialog.cancel();
                });

                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }

        });
    }

    @Override
    public int getItemCount() {
        // this method is used for showing number of card items in recycler view
        return favModelArrayList.size();
    }

    // View holder class for initializing of your views such as TextView and Imageview
    //hereeee!!
    public static class handler extends RecyclerView.ViewHolder {
        //        private final ImageView courseIV;
        private final TextView ProductName;
//        private CardView cardView;

        private ImageView img;
        final private FloatingActionButton delete;

        public handler(View itemView) {
            super(itemView);
//            courseIV = itemView.findViewById(R.id.idIVCourseImage);
            ProductName = itemView.findViewById(R.id.productName);
//            cardView = itemView.findViewById(R.id.base_cardview);
            img = itemView.findViewById(R.id.productImage);
            delete = itemView.findViewById(R.id.deleteBtn);
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
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }
}
