package com.example.myapplication;

import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;

public class ProductFragment extends Fragment {
    String barcode;
    JSONObject product = null;
    JSONArray images = null;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        return super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.product_fragment, container,false);
        String info = getArguments().getString("json obj");
        barcode = getArguments().getString("barcode");

        Log.d("TAG", "onCreateView: "+info);
        Log.d("TAG", "onCreateView: "+barcode);
        RecyclerView courseRV = view.findViewById(R.id.recycler_view);
        ArrayList<ProductModel> productModelArrayList = new ArrayList<ProductModel>();
        try {
//            ProgressBar pb = findViewById(R.id.progressBar_cyclic);
//            pb.setVisibility(View.GONE);
            JSONObject json = new JSONObject(info);
            System.out.println("no product json"+json);
            TextView productName = view.findViewById(R.id.productName);

            if(json.toString().contains("\"results\":[]")){
                Toast.makeText(getActivity().getApplicationContext(),"Product not available",Toast.LENGTH_LONG).show();
                return view;
//                Intent it = new Intent(Product.this, MainActivity.class);
//                it.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                getApplicationContext().startActivity(it);
            }
            else {
                product = json.getJSONObject("product");
                System.out.println(product.get("title"));

                productName.setText(product.get("title").toString());
                TextView category = view.findViewById(R.id.category);
                category.setText(product.get("manufacturer").toString());
                TextView manufacturer = view.findViewById(R.id.manufacturer);
                manufacturer.setText(product.get("description").toString());
                try {
                    ImageView img = (ImageView) view.findViewById(R.id.image);
                    images = product.getJSONArray("images");
                    new ProductFragment.DownloadImageTask(img).execute(images.get(0).toString()).get();
////                JSONArray imgs = product.get("imageUrl").toString();
//                System.out.println("image url : "+images.get(0));
////                System.out.println("image url : "+product.get("imageUrl").toString());
//                Bitmap bitmap = BitmapFactory.decodeStream((InputStream)new URL(images.get(0).toString()).getContent());
//                img.setImageBitmap(bitmap);
                } catch (Exception e) {
                    Log.e("TAG", "onCreate: ", e);
                    e.printStackTrace();
                }
                JSONArray stores = product.getJSONArray("online_stores");
                int k = 0;
                for (k = 0; k < stores.length(); k++) {
//                    Log.d("TAG", "onCreate: " + stores.get(k).toString());
//                    if (!stores.get(k).toString().contains("Walmart")) {
//                        Log.d("TAG", "onCreate: Not available at Walmart");
//                    } else {
//                        val = k;
//                        break;
//                    }
                    JSONObject store = stores.getJSONObject(k);
                    if(store.get("price").toString().startsWith("$")) {
                        ProductModel obj = new ProductModel(barcode, store.get("name").toString(), store.get("price").toString(), store.get("url").toString(), product.get("title").toString(), images.get(0).toString());
                        productModelArrayList.add(obj);
                    }
                }
//            JSONArray target = stores.(k);
//            System.out.println("target values: "+stores.getJSONObject(0).get("price"));
//                TextView cost = findViewById(R.id.cost);
//                cost.setText(stores.getJSONObject(val).get("price").toString());
            }
            ProductAdapter productAdapter = new ProductAdapter(getActivity().getApplicationContext(), productModelArrayList);

            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext(),
                    LinearLayoutManager.VERTICAL, false);

            // in below two lines we are setting layoutmanager and adapter to our recycler view.
            courseRV.setLayoutManager(linearLayoutManager);
            courseRV.setAdapter(productAdapter);
            Button fav = view.findViewById(R.id.favorite);
            fav.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DataController db = new DataController(getContext());
                    long l = 0;
                    try {
                        l = db.insertFav(barcode, product.get("title").toString(),images.get(0).toString());
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                    Log.d("TAG", "favorite db res val : "+l );
                    if(l == -1){
                        Toast.makeText(getActivity().getApplicationContext(),"Already in favs",Toast.LENGTH_LONG).show();
                    }
                    else
                        Toast.makeText(getActivity().getApplicationContext(), "product added to fav",Toast.LENGTH_LONG).show();
                    Cursor c = db.retrieve();
                    while(c.moveToNext()){
                        Log.d("TAG", "favorite: db data -> "+c.getString(0));
                    }
                }
            });
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
//        return inflater.inflate(R.layout.fragment, container, false);
        return view;
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

    public void favorite(View v) throws JSONException {
        DataController db = new DataController(getContext());
        long l = db.insertFav(barcode, product.get("title").toString(),images.get(0).toString());
        Log.d("TAG", "favorite db res val : "+l );
        if(l == -1){
            Toast.makeText(getActivity().getApplicationContext(),"Already in favs",Toast.LENGTH_LONG).show();
        }
        else
            Toast.makeText(getActivity().getApplicationContext(), "product added to fav",Toast.LENGTH_LONG).show();
        Cursor c = db.retrieve();
        while(c.moveToNext()){
            Log.d("TAG", "favorite: db data -> "+c.getString(0));
        }
    }
}
