package com.example.myapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class DataController
{
    public static final String barcode="barcode";
    public static final String productName="productName";
    public static final String TABLE_NAME="favorite";
    public static final String DATABASE_NAME="barcodes.db";
    public static final int DATABASE_VERSION=4;

    DataBaseHelper dbHelper;
    Context context;
    SQLiteDatabase db;

    TextView total;
    public DataController(Context context)
    {
        this.context=context;
        dbHelper = new DataBaseHelper(context);
    }

    public DataController(Context context, TextView total)
    {
        this.total = total;
        this.context=context;
        dbHelper = new DataBaseHelper(context);
    }

    public DataController open()
    {

        return this;
    }

    public void close()
    {
        dbHelper.close();
    }

    public void deleteCart(String barcode, String storeName){
        db = dbHelper.getWritableDatabase();
        db.delete("cart", "barcode=? and seller = ?",new String[]{barcode, storeName});
        db.close();
    }

    public void deleteFav(String barcode){
        db = dbHelper.getWritableDatabase();
        db.delete("favorite", "barcode=?",new String[]{barcode});
        db.close();
    }

    public long updateCart(String barcode, String productName, String cost, int quantity, String seller, String url,
                       String imageUrl, int newQty){
        db = dbHelper.getWritableDatabase();
        ContentValues content=new ContentValues();
        content.put("barcode", barcode);
        content.put("productName",productName);
        content.put("cost",cost);
        content.put("quantity", newQty);
        content.put("seller",seller);
        content.put("url", url);
        content.put("imageUrl",imageUrl);
        db.update("cart",content,"barcode=? and seller=?",new String[]{String.valueOf(barcode), seller});
        return 0;
    }

    public boolean checkFav(String barcode){
        db = dbHelper.getReadableDatabase();
        Cursor c = db.rawQuery("select * from favorite where barcode='"+barcode+"'", null);
        if(c!=null)
            return true;
        return false;
    }

    public long insertCart(String barcode, String productName, String cost, int quantity, String seller, String url,
                           String imageUrl, String from){
        db = dbHelper.getWritableDatabase();
        //--- check if same product and same store is already in the cart ---
//        Cursor cur = db.rawQuery("select * from cart where productName="+productName+" and seller="+seller,null);
//        if(cur!=null){
//
//        }
        Log.d("TAG", "insert: coming here in insert db "+barcode);
        ContentValues content=new ContentValues();
        content.put("barcode", barcode);
        content.put("productName",productName);
        content.put("cost",cost);
        content.put("quantity", quantity);
        content.put("seller",seller);
        content.put("url", url);
        content.put("imageUrl",imageUrl);
        long res = 0;
        Cursor c = db.rawQuery("select * from cart where productName='"+productName+"' and seller='"+seller+"'",null);
        if(c!=null && c.getCount()>0){
            c.moveToNext();
            Log.d("TAG", "insertCart: quantioty before adding: "+c.getString(3));
            quantity = Integer.parseInt(c.getString(3).toString())+quantity;
            Log.d("TAG", "insertCart: quantity after adding: "+quantity);
            content.put("quantity", quantity);
            //db.rawQuery("update cart set quantity="+quantity+" where barcode="+barcode, null);
            db.update("cart",content,"productName=? and seller=?",new String[]{productName, seller});
            c = db.rawQuery("select * from cart where barcode="+barcode, null);
            while(c.moveToNext()){
                Log.d("TAG", "insertCart: "+c.getString(0)+" "+c.getString(1)+" "
                        +c.getString(2)+" "+c.getString(3)+" "+c.getString(4)+ " "
                        +c.getString(5)+" "+c.getString(6));
            }
        }
        else {
            try {
                res = db.insertOrThrow("cart", null, content);
            } catch (SQLiteConstraintException e) {
                Log.e("TAG", "insert: error in db");
                res = -1;
                e.printStackTrace();
            }
        }
        if(from.equals("undo")) {
//            FragmentTransaction fragmentManager =  ((AppCompatActivity)context).getSupportFragmentManager().beginTransaction();
//            if (Build.VERSION.SDK_INT >= 26) {
//                fragmentManager.setReorderingAllowed(false);
//            }
//            fragmentManager.detach(CartFragment.instance).attach(CartFragment.instance).commit();
//            CartFragment cobj = new CartFragment();
//            cobj.updateTotal(context, cobj.view);
        }
        return res;
    }

    public long insertFav(String barcode, String productName, String imageUrl)
    {
        db = dbHelper.getWritableDatabase();
//        db.execSQL("DROP TABLE IF EXISTS favorite");
//        db.execSQL("create table favorite(barcode text primary key, productName text not null, " +
//                "imageUrl text not null);");
        Log.d("TAG", "insert: coming here in insert db "+barcode);
        ContentValues content=new ContentValues();
        content.put("barcode", barcode);
        content.put("productName",productName);
        content.put("imageUrl", imageUrl);
        long res = 0;
        try {
            res = db.insertOrThrow(TABLE_NAME, null, content);
        }catch (SQLiteConstraintException e){
            Log.e("TAG", "insert: error in db");
            res = -1;
            e.printStackTrace();
        }
        return res;
    }

    public Cursor retrieve()
    {
        SQLiteDatabase read = dbHelper.getReadableDatabase();
        return read.query(TABLE_NAME, new String[]{barcode, "productName", "imageUrl"},
                null, null, null, null, null);
    }

    public Cursor retrieveCart()
    {
        SQLiteDatabase read = dbHelper.getReadableDatabase();
        return read.query("cart", new String[]{barcode, "productName", "cost", "quantity", "seller",
                "url", "imageUrl"}, null, null, null, null, null);
    }

    private static class DataBaseHelper extends SQLiteOpenHelper
    {

        public DataBaseHelper(Context context)
        {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            // TODO Auto-generated method stub
            try
            {
                Log.d("TAG", "onCreate: before table creation");
                db.execSQL("create table if NOT EXISTS favorite(barcode text primary key, productName text not null, " +
                        "imageUrl text not null)");
                Log.d("TAG", "onCreate: after fav table creation");
                db.execSQL("create table if NOT EXISTS cart(barcode text not null, productName text not null," +
                        "cost real not null, quantity integer not null, seller text not null, " +
                        "url text not null, imageUrl text not null, primary key (barcode, seller))");
                Log.d("TAG", "onCreate: after DB creation");
            }
            catch(SQLiteException e)
            {
                e.printStackTrace();
            }
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            // TODO Auto-generated method stub
            db.execSQL("DROP TABLE IF EXISTS favorite");
            db.execSQL("DROP TABLE IF EXISTS cart");
            onCreate(db);
        }

    }

}