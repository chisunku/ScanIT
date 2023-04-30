package com.example.myapplication;

import android.app.Activity;
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

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class GoogleApiAdapter extends RecyclerView.Adapter<GoogleApiAdapter.handler> {

    private final Context context;
    private final ArrayList<GoogleApiModel> googleApiModelArrayList;

    // Constructor
    public GoogleApiAdapter(Context context, ArrayList<GoogleApiModel> googleApiModelArrayList) {
        this.context = context;
        this.googleApiModelArrayList = googleApiModelArrayList;
    }

    @NonNull
    @Override
    public GoogleApiAdapter.handler onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // to inflate the layout for each item of recycler view.
        View view = LayoutInflater.from(context).inflate(R.layout.google_api_card_layout, parent, false);
        return new handler(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GoogleApiAdapter.handler holder, int position) {
        // to set data to textview and imageview of each card layout
        GoogleApiModel model = googleApiModelArrayList.get(position);
        Log.d("TAG", "onBindViewHolder: size : "+googleApiModelArrayList.size());
        Log.d("TAG", "onBindViewHolder: position : "+position);
        try {
            holder.title.setText(model.getTitle());
//        try {
            new DownloadImageTask(holder.img).execute(model.getImageUrl()).get();
//        } catch (ExecutionException e) {
//            throw new RuntimeException(e);
//        }
//        holder.courseIV.setImageResource(model.getCourse_image());
            holder.card.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //implement onClick
//                try {
//                    new ProductDetails(context).execute(model.getBarcode(), "main").get();
//                } catch (ExecutionException e) {
//                    throw new RuntimeException(e);
//                } catch (InterruptedException e) {
//                    throw new RuntimeException(e);
//                }
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(model.getURL()));
                    (context).startActivity(i);
                }
            });
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        // this method is used for showing number of card items in recycler view
        return googleApiModelArrayList.size();
    }

    // View holder class for initializing of your views such as TextView and Imageview
    //hereeee!!
    public static class handler extends RecyclerView.ViewHolder {
        //        private final ImageView courseIV;
        private final TextView title;
        private final TextView URL;
        private ImageView img;
        MaterialCardView card;
//        final private FloatingActionButton URL;

        public handler(View itemView) {
            super(itemView);
//            courseIV = itemView.findViewById(R.id.idIVCourseImage);
            title = itemView.findViewById(R.id.title);
//            cardView = itemView.findViewById(R.id.base_cardview);
            img = itemView.findViewById(R.id.icon);
            URL = itemView.findViewById(R.id.Url);
            card = itemView.findViewById(R.id.card);
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
