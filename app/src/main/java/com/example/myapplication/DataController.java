package com.example.myapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DataController
{
    public static final String barcode="barcode";
    public static final String productName="productName";
    public static final String TABLE_NAME="favorite";
    public static final String DATABASE_NAME="barcodes.db";
    public static final int DATABASE_VERSION=4;
    //type is that its either a product or store saved
    public static final String TABLE_CREATE="create table favorites (barcode text primary key);";

    DataBaseHelper dbHelper;
    Context context;
    SQLiteDatabase db;

    public DataController(Context context)
    {
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
        db.update("cart",content,"barcode=?",new String[]{String.valueOf(barcode)});
        return 0;
    }
    public long insertCart(String barcode, String productName, String cost, int quantity, String seller, String url,
                           String imageUrl){
        db = dbHelper.getWritableDatabase();
//        db.execSQL("DROP TABLE IF EXISTS cart");
//        db.execSQL("create table cart(barcode text primary key, productName text not null," +
//                "cost real not null, quantity integer not null, seller text not null, " +
//                "url text not null, imageUrl text not null);");
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
        Cursor c = db.rawQuery("select * from cart where barcode="+barcode, null);
        if(c!=null && c.getCount()>0){
            c.moveToNext();
            quantity = Integer.parseInt(c.getString(3).toString())+quantity;
            //db.rawQuery("update cart set quantity="+quantity+" where barcode="+barcode, null);
            db.update("cart",content,"barcode=?",new String[]{barcode});
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
        return read.query(TABLE_NAME, new String[]{barcode, "productName", "imageUrl"}, null, null, null, null, null);
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
                db.execSQL("create table favorite(barcode text primary key, productName text not null, " +
                        "imageUrl text not null);");
                db.execSQL("create table cart(barcode text primary key, productName text not null," +
                        "cost real not null, quantity integer not null, seller text not null, " +
                        "url text not null, imageUrl text not null);");
            }
            catch(SQLiteException e)
            {
                e.printStackTrace();
            }
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            // TODO Auto-generated method stub
            db.execSQL("DROP TABLE IF EXISTS favorites");
            db.execSQL("DROP TABLE IF EXISTS cart");
            onCreate(db);
        }

    }

}