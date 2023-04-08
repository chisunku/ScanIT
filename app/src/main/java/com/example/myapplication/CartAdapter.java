package com.example.myapplication;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.carthandler> {
    CartFragment cartObj = new CartFragment();
    private final Context context;
    public final ArrayList<CartModel> cartModelArrayList;
    boolean isEnable=false;
    ArrayList<CartModel> selectList=new ArrayList<CartModel>();
    boolean isSelectAll=false;
    MainViewModel mainViewModel;
    TextView total;

    // Constructor
    public CartAdapter(Context context, ArrayList<CartModel> cartModelArrayList, TextView total) {
        this.context = context;
        this.cartModelArrayList = cartModelArrayList;
        this.total = total;
    }

    @NonNull
    @Override
    public CartAdapter.carthandler onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // to inflate the layout for each item of recycler view.
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_cart_card, parent, false);
        mainViewModel= ViewModelProviders.of((FragmentActivity) context)
                .get(MainViewModel.class);
        return new carthandler(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartAdapter.carthandler holder, int position) {
        // to set data to textview and imageview of each card layout
        Log.d("TAG", "onBindViewHolder: "+position);
        CartModel model = cartModelArrayList.get(position);
        holder.productName.setText(model.getProductName());
        holder.store.setText(model.getSeller());
        holder.cost.setText(model.getCost()+"");
        holder.quantity.setText(model.getQuantity()+"");
        Log.d("TAG", "onBindViewHolder: qty : "+model.getQuantity());
//        holder.quantity.setText(qty+"");
        Log.d("TAG", "onBindViewHolder: image url : "+model.getImageUrl());
        try {
            new CartAdapter.DownloadImageTask(holder.productImage).execute(model.getImageUrl()).get();
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        holder.clickableLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if(model.getUrl()==null)
                        Toast.makeText(context, "Something went wrong, please try again later!!",Toast.LENGTH_LONG).show();
                    else {
                        Intent i = new Intent(Intent.ACTION_VIEW);
                        i.setData(Uri.parse(model.getUrl()));
                        ((Activity)v.getContext()).startActivity(i);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });



        holder.priceComp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    ProductFragment pf = new ProductFragment();
                    FragmentManager fragmentManager =  ((AppCompatActivity)context).getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//                    fragmentTransaction.replace(R.id.content, pf);
//                    fragmentTransaction.commit();
                    new ProductDetails(context).execute(model.getBarcode(), "main").get();
                } catch (ExecutionException e) {
                    throw new RuntimeException(e);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        holder.plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int qty = model.getQuantity()+1;
                if(qty>15)
                    Toast.makeText(context.getApplicationContext(), "Too many items added", Toast.LENGTH_LONG).show();
                else {
                    model.setQuantity(qty);
                    holder.quantity.setText(qty+"");
                    DataController db = new DataController(context);
                    db.updateCart(model.getBarcode(), model.getProductName(), model.getCost(),
                            model.getQuantity(), model.getSeller(), model.getUrl(), model.getImageUrl(), qty);
//                    cartObj.total.setText(qty+"");
                    cartObj.updateTotal(total, context);
                    notifyDataSetChanged();
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
                    total.setText("in minus");
                    DataController db = new DataController(context);
                    db.updateCart(model.getBarcode(), model.getProductName(), model.getCost(),
                            model.getQuantity(), model.getSeller(), model.getUrl(), model.getImageUrl(), qty);
                    notifyDataSetChanged();
                    cartObj.updateTotal(total, context);
                }
                else{
                    Toast.makeText(context.getApplicationContext(), "No items added", Toast.LENGTH_LONG).show();
                }
            }
        });

        holder.favorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DataController db = new DataController(context);
                long l = db.insertFav(model.getBarcode(), model.getProductName(),model.getImageUrl());
                Log.d("TAG", "favorite db res val : "+l );
                if(l == -1){
                    Toast.makeText(context.getApplicationContext(),"Already in favs",Toast.LENGTH_LONG).show();
                }
                else
                    Toast.makeText(context.getApplicationContext(), "product added to fav",Toast.LENGTH_LONG).show();
                Cursor c = db.retrieve();
                while(c.moveToNext()){
                    Log.d("TAG", "favorite: db data -> "+c.getString(0));
                }
            }
        });

        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage("Are you sure you want to delete "+model.getProductName()+" from the cart?");
                builder.setTitle("DELETE");
                builder.setCancelable(false);
                builder.setPositiveButton("Yes", (dialog, which) -> {
                    DataController db = new DataController(context);
                    db.deleteCart(model.getBarcode(), model.getSeller());
                    Toast.makeText(context, "Item "+model.getProductName()+" deleted!!",Toast.LENGTH_LONG).show();
                    cartModelArrayList.remove(position);
                    cartObj.updateTotal(total, context);
                    notifyDataSetChanged();

                });
                builder.setNegativeButton("No", (dialog, which) -> {
                    dialog.cancel();
                });

                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });

//        holder.itemView.setOnLongClickListener();
//        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
//
//            @Override
//
//            public boolean onLongClick(View view) {
//                cartModelArrayList.remove(position);
//
//                notifyItemRemoved(position);
//
//                return true;
//
//            }
//
//        });

//        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
//            @Override
//            public boolean onLongClick(View v) {
//                // check condition
//                if (!isEnable)
//                {
//                    // when action mode is not enable
//                    // initialize action mode
//                    ActionMode.Callback callback=new ActionMode.Callback() {
//                        @Override
//                        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
//                            // initialize menu inflater
//                            MenuInflater menuInflater= mode.getMenuInflater();
//                            // inflate menu
//                            menuInflater.inflate(R.menu.menu,menu);
//                            // return true
//                            return true;
//                        }
//
//                        @Override
//                        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
//                            // when action mode is prepare
//                            // set isEnable true
//                            isEnable=true;
//                            // create method
//                            ClickItem(holder);
//                            // set observer on getText method
//                            mainViewModel.getText().observe((LifecycleOwner) context
//                                    , new Observer<String>() {
//                                        @Override
//                                        public void onChanged(String s) {
//                                            // when text change
//                                            // set text on action mode title
//                                            mode.setTitle(String.format("%s Selected",s));
//                                        }
//                                    });
//                            // return true
//                            return true;
//                        }
//
//                        @Override
//                        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
//                            // when click on action mode item
//                            // get item  id
//                            int id=item.getItemId();
//                            // use switch condition
//                            switch(id)
//                            {
//                                case R.id.menu_delete:
//                                    // when click on delete
//                                    // use for loop
//                                    for(CartModel s:selectList)
//                                    {
//                                        // remove selected item list
//                                        cartModelArrayList.remove(s);
//
//                                    }
//                                    // check condition
//                                    if(cartModelArrayList.size()==0)
//                                    {
//                                        // when array list is empty
//                                        // visible text view
////                                        tvEmpty.setVisibility(View.VISIBLE);
//                                    }
//                                    // finish action mode
//                                    mode.finish();
//                                    break;
//
//                                case R.id.menu_select_all:
//                                    // when click on select all
//                                    // check condition
//                                    if(selectList.size()==cartModelArrayList.size())
//                                    {
//                                        // when all item selected
//                                        // set isselectall false
//                                        isSelectAll=false;
//                                        // create select array list
//                                        selectList.clear();
//                                    }
//                                    else
//                                    {
//                                        // when  all item unselected
//                                        // set isSelectALL true
//                                        isSelectAll=true;
//                                        // clear select array list
//                                        selectList.clear();
//                                        // add value in select array list
//                                        selectList.addAll(cartModelArrayList);
//                                    }
//                                    // set text on view model
//                                    mainViewModel.setText(String .valueOf(selectList.size()));
//                                    // notify adapter
//                                    notifyDataSetChanged();
//                                    break;
//                            }
//                            // return true
//                            return true;
//                        }
//
//                        @Override
//                        public void onDestroyActionMode(ActionMode mode) {
//                            // when action mode is destroy
//                            // set isEnable false
//                            isEnable=false;
//                            // set isSelectAll false
//                            isSelectAll=false;
//                            // clear select array list
//                            selectList.clear();
//                            // notify adapter
//                            notifyDataSetChanged();
//                        }
//                    };
//                    // start action mode
//                    ((AppCompatActivity) v.getContext()).startActionMode(callback);
//                }
//                else
//                {
//                    // when action mode is already enable
//                    // call method
//                    ClickItem(holder);
//                }
//                // return true
//                return true;
//            }
//        });


    }

//    private void ClickItem(CartAdapter.carthandler holder) {
//
//        // get selected item value
//        CartModel s=cartModelArrayList.get(holder.getAdapterPosition());
//        // check condition
//        if(holder.checkbox.getVisibility()==View.GONE)
//        {
//            // when item not selected
//            // visible check box image
//            holder.checkbox.setVisibility(View.VISIBLE);
//            // set background color
//            holder.itemView.setBackgroundColor(Color.GRAY);
//            // add value in select array list
//            selectList.add(s);
//        }
//        else
//        {
//            // when item selected
//            // hide check box image
//            holder.checkbox.setVisibility(View.GONE);
//            // set background color
//            holder.itemView.setBackgroundColor(Color.WHITE);
//            // remove value from select arrayList
//            selectList.remove(s);
//
//        }
//        // set text on view model
//        mainViewModel.setText(String.valueOf(selectList.size()));
//    }

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
//        private CardView cardView;
        private final Button priceComp;
        private final ImageView plus;
        private final ImageView minus;
        private final TextView quantity;
        private final ImageView delete;
        private final ImageView favorite;
        private final LinearLayout clickableLayout;
//        private final ImageView checkbox;

        public carthandler(View itemView) {
            super(itemView);
            productName = itemView.findViewById(R.id.productName);
            store = itemView.findViewById(R.id.store);
//            cardView = itemView.findViewById(R.id.base_cardview);
            cost = itemView.findViewById(R.id.cost);
            productImage = itemView.findViewById(R.id.productImage);
            priceComp = itemView.findViewById(R.id.priceComp);
            plus = itemView.findViewById(R.id.plus);
            minus = itemView.findViewById(R.id.minus);
            quantity = itemView.findViewById(R.id.qty);
            delete = itemView.findViewById(R.id.deleteBtn);
            favorite = itemView.findViewById(R.id.favorite);
            clickableLayout = itemView.findViewById(R.id.clickableLayout);
//            checkbox = itemView.findViewById(R.id.check_box);
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
