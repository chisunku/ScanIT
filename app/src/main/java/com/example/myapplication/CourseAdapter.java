package com.example.myapplication;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.ImageView;
        import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
        import java.util.ArrayList;

public class CourseAdapter extends RecyclerView.Adapter<CourseAdapter.handler> {

    private final Context context;
    private final ArrayList<CourseModel> courseModelArrayList;

    // Constructor
    public CourseAdapter(Context context, ArrayList<CourseModel> courseModelArrayList) {
        this.context = context;
        this.courseModelArrayList = courseModelArrayList;
    }

    @NonNull
    @Override
    public CourseAdapter.handler onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // to inflate the layout for each item of recycler view.
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_card_layout, parent, false);
        return new handler(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CourseAdapter.handler holder, int position) {
        // to set data to textview and imageview of each card layout
        CourseModel model = courseModelArrayList.get(position);
        holder.courseNameTV.setText(model.getStoreName());
        holder.courseRatingTV.setText("" + model.getCost());
//        holder.courseIV.setImageResource(model.getCourse_image());
        holder.cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("added to cart + "+model.getImageUrl());
                Toast.makeText(context.getApplicationContext(), "adding to cart", Toast.LENGTH_LONG).show();
                DataController db = new DataController(context.getApplicationContext());
                //qty;
                int qty = 0;
                long r = db.insertCart(model.getBarcode(), model.getProductName(), model.getCost(), qty, model.getStoreName(), model.getUrl(), model.getImageUrl());
                if(r == -1){
                    Toast.makeText(context.getApplicationContext(), "Something went wrong :( please try again!!", Toast.LENGTH_LONG).show();
                }
            }
        });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //implement onClick
                System.out.println("Clicked");
                Toast.makeText(context.getApplicationContext(), "clicked "+model.getStoreName(),Toast.LENGTH_LONG).show();
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(model.getUrl()));
                context.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        // this method is used for showing number of card items in recycler view
        return courseModelArrayList.size();
    }

    // View holder class for initializing of your views such as TextView and Imageview
    //hereeee!!
    public static class handler extends RecyclerView.ViewHolder {
//        private final ImageView courseIV;
        private final TextView courseNameTV;
        private final TextView courseRatingTV;
        private final ImageView cart;
        private CardView cardView;

        public handler(View itemView) {
            super(itemView);
//            courseIV = itemView.findViewById(R.id.idIVCourseImage);
            courseNameTV = itemView.findViewById(R.id.storeName);
            courseRatingTV = itemView.findViewById(R.id.cost);
            cardView = itemView.findViewById(R.id.base_cardview);
            cart = itemView.findViewById(R.id.cart);
        }
    }
}
