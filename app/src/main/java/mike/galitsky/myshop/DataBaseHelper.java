package mike.galitsky.myshop;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class DataBaseHelper  extends SQLiteOpenHelper {
    public DataBaseHelper(@Nullable Context context) {
        super(context, "MyShop3.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(" CREATE TABLE " + Items.TABLE_NAME + "("
                + Items.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + Categories.COLUMN_CATEGORY_ID +  " INTEGER NOT NULL,"
                + Items.COLUMN_NAME +  " TEXT NOT NULL,"
                + Items.COLUMN_DESCRIPTION +  " TEXT NOT NULL,"
                + Items.COLUMN_PRICE +  " INTEGER NOT NULL)");


        db.execSQL(" CREATE TABLE " + Categories.TABLE_NAME + "("
                + Categories.COLUMN_CATEGORY_ID +  " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + Categories.CATEGORY_NAME + " TEXT NOT NULL)");


        db.execSQL(" CREATE TABLE "+ Basket.TABLE_NAME + "("
                + Basket.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + Basket.COLUMN_ID_ITEM + " INTEGER NOT NULL,"
                + Basket.COLUMN_NAME +  " TEXT NOT NULL,"
                + Categories.COLUMN_CATEGORY_ID +  " INTEGER NOT NULL,"
                + Basket.COLUMN_DESCRIPTION +  " TEXT NOT NULL,"
                + Basket.COLUMN_PRICE +  " INTEGER NOT NULL)");
    }



    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public boolean save(Items item){
        if(item.id > 0){
            return update(item);
        }else {
            return  insert(item) > 0 ;
        }
    }

    public long insert (Items item){
        long id = 0;

        SQLiteDatabase db = getWritableDatabase();

        try{
            ContentValues values = new ContentValues();
            values.put(Items.COLUMN_NAME, item.name);
            values.put(Items.COLUMN_DESCRIPTION, item.description);
            values.put(Items.COLUMN_PRICE, item.price);
            values.put(Categories.COLUMN_CATEGORY_ID, item.category);

            id = db.insert(Items.TABLE_NAME,null, values);


        }catch (Exception e){
            e.printStackTrace();
        }

        return id;
    }

    public boolean update (Items item){
        boolean updated = false;
        SQLiteDatabase db = getWritableDatabase();
        try{
            ContentValues values = new ContentValues();
            values.put(Items.COLUMN_NAME, item.name);
            values.put(Items.COLUMN_DESCRIPTION, item.description);
            values.put(Items.COLUMN_PRICE, item.price);
            values.put(Categories.COLUMN_CATEGORY_ID, item.category);


            updated = db.update(Items.TABLE_NAME, values, Items.COLUMN_ID + "=" + item.id, null) == 1 ;


        }catch (Exception e){
            e.printStackTrace();
        }

        return updated;
    }

    public ArrayList<Items> getItems(){
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = null;
        ArrayList<Items> items = new ArrayList<>();
        try {
            cursor = db.query(Items.TABLE_NAME, null,null,null, null,null,null);
            if(cursor.moveToFirst()){
                while (!cursor.isAfterLast()){

                    Items item = new Items();
                    item.id = cursor.getLong(cursor.getColumnIndex(Items.COLUMN_ID));
                    item.name = cursor.getString(cursor.getColumnIndex(Items.COLUMN_NAME));
                    item.description = cursor.getString(cursor.getColumnIndex(Items.COLUMN_DESCRIPTION));
                    item.price = cursor.getInt(cursor.getColumnIndex(Items.COLUMN_PRICE));
                    item.category = cursor.getInt(cursor.getColumnIndex(Categories.COLUMN_CATEGORY_ID));
                    items.add(0, item);
                    cursor.moveToNext();
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if(cursor !=null){
                cursor.close();
            }
        }
        return items;

    }




    public ArrayList<Categories> getCategories(){
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = null;
        ArrayList<Categories> categories = new ArrayList<>();

        try {
            cursor = db.query(Categories.TABLE_NAME, null,null,null, null,null,null);
            if(cursor.moveToFirst()){
                while (!cursor.isAfterLast()){

                    Categories category = new Categories();
                    category.id = cursor.getInt(cursor.getColumnIndex(Categories.COLUMN_CATEGORY_ID));
                    category.name = cursor.getString(cursor.getColumnIndex(Categories.CATEGORY_NAME));

                    categories.add( category);
                    cursor.moveToNext();
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if(cursor !=null){
                cursor.close();
            }
        }
        return categories;

    }



    public boolean delete (Items item){
        boolean deleted = false;
        SQLiteDatabase db = getWritableDatabase();

        try {
            db.delete(Items.TABLE_NAME, Items.COLUMN_ID + "=" + item.id, null);
        }catch (Exception e){
            e.printStackTrace();
        }

        return deleted;
    }



    public void BackUpItems(File folder, String FileName){
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = null;

        try {
            if(!folder.exists()){
                folder.mkdirs();
            }

            File file = new File(folder, FileName);

            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), "UTF8"));


        try {
            cursor = db.query(Items.TABLE_NAME, null,null,null, null,null,null);
            if(cursor.moveToFirst()){
                while (!cursor.isAfterLast()){

                    Items item = new Items();
                    Categories category = new Categories();
                    item.id = cursor.getLong(cursor.getColumnIndex(Items.COLUMN_ID));
                    item.name = cursor.getString(cursor.getColumnIndex(Items.COLUMN_NAME));
                    item.description = cursor.getString(cursor.getColumnIndex(Items.COLUMN_DESCRIPTION));
                    item.price = cursor.getInt(cursor.getColumnIndex(Items.COLUMN_PRICE));
                    item.category = cursor.getInt(cursor.getColumnIndex(Categories.COLUMN_CATEGORY_ID));

                    Gson gson = new GsonBuilder().create();
                    String jSon = gson.toJson(item);
                        writer.write(jSon);
                        writer.flush();
                    cursor.moveToNext();
                    writer.newLine();
                }
            }

        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if(cursor !=null){
                cursor.close();
                writer.close();
            }
        }
        }catch (Exception e){
            e.printStackTrace();
        }

    }
/*
    public ArrayList<Items> ReadBackUpItems(File folder, String FileName){
        ArrayList<Items> items = new ArrayList<>();
                    try {

                        File file = new File(folder, FileName);
                        if(file.exists()){

                            BufferedReader reader = new BufferedReader(new FileReader(file));
                            String line;

                            while ((line = reader.readLine()) != null){
                                Gson gson = new GsonBuilder().create();
                                Items item = gson.fromJson(line, Items.class);
                                items.add(item);

                            }


                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                            return items;
                }

    public void uploadNewItems(ArrayList<Items> data){

        SQLiteDatabase db = getWritableDatabase();
        db.delete(Items.TABLE_NAME,null,null);
        for (int i=0; i < data.size(); i++) {

            Items item = data.get(i);

            try {
                ContentValues values = new ContentValues();
                values.put(Items.COLUMN_NAME, item.name);
                values.put(Items.COLUMN_DESCRIPTION, item.description);
                values.put(Items.COLUMN_PRICE, item.price);
                values.put(Categories.COLUMN_CATEGORY_ID, item.category);
                values.put(Categories.CATEGORY_NAME, "Category "+item.category);


                db.insert(Items.TABLE_NAME, null, values);


            } catch (Exception e) {
                e.printStackTrace();
            }
        }


    }*/

    public Void backUp(File folder, String FileName){
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = null;
        Gson gson = new GsonBuilder().create();
        ArrayList<Items> items = new ArrayList<>();

        try {
            if(!folder.exists()){
                folder.mkdirs();
            }

            File file = new File(folder, FileName);

            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), "UTF8"));


            try {
                cursor = db.query(Items.TABLE_NAME, null,null,null, null,null,null);
                if(cursor.moveToFirst()){
                    while (!cursor.isAfterLast()){

                        Items item = new Items();
                        Categories category = new Categories();
                        item.id = cursor.getLong(cursor.getColumnIndex(Items.COLUMN_ID));
                        item.name = cursor.getString(cursor.getColumnIndex(Items.COLUMN_NAME));
                        item.description = cursor.getString(cursor.getColumnIndex(Items.COLUMN_DESCRIPTION));
                        item.price = cursor.getInt(cursor.getColumnIndex(Items.COLUMN_PRICE));
                        item.category = cursor.getInt(cursor.getColumnIndex(Categories.COLUMN_CATEGORY_ID));
                        items.add(item);
                   /* String jSon = gson.toJson(item);
                        writer.write(jSon);
                        writer.flush();*/
                        cursor.moveToNext();
                        /*writer.newLine();*/
                    }
                }
                String jSon = gson.toJson(items);
                writer.write(jSon);
                writer.flush();
                writer.close();
            }catch (Exception e){
                e.printStackTrace();
            }finally {
                if(cursor !=null){
                    cursor.close();
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        return null;
    }



    public Void InsertBackUpItems(File folder, String FileName) {
        ArrayList<Items> items = new ArrayList<>();
        SQLiteDatabase db = getWritableDatabase();
        db.delete(Items.TABLE_NAME,null,null);

        try {

            File file = new File(folder, FileName);
            if (file.exists()) {

                BufferedReader reader = new BufferedReader(new FileReader(file));
                String line;

                while ((line = reader.readLine()) != null) {
                    Gson gson = new GsonBuilder().create();
                    TypeToken<List<Items>> token = new TypeToken<List<Items>>() {};
                    items = gson.fromJson(line, token.getType());
                }


            }
        } catch (Exception e) {
            e.printStackTrace();
        }

       for (int i = 0; i < items.size(); i++) {

            Items item = items.get(i);

            try {
                ContentValues values = new ContentValues();
                values.put(Items.COLUMN_NAME, item.name);
                values.put(Items.COLUMN_DESCRIPTION, item.description);
                values.put(Items.COLUMN_PRICE, item.price);
                values.put(Categories.COLUMN_CATEGORY_ID, item.category);

                db.insert(Items.TABLE_NAME, null, values);


            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        return null;
    }

    public long insertCategories (Categories category){
        long id = 0;

        SQLiteDatabase db = getWritableDatabase();

        try{
            ContentValues values = new ContentValues();
            values.put(Categories.CATEGORY_NAME, category.name );
            values.put(Categories.COLUMN_CATEGORY_ID, category.id);

            id = db.insert(Categories.TABLE_NAME,null, values);


        }catch (Exception e){
            e.printStackTrace();
        }

        return id;
    }

    public void insertData (){
        SQLiteDatabase db = getWritableDatabase();
        Random random = new Random();
        Cursor cursor = db.query(Items.TABLE_NAME, null, null, null, null, null, null);
        if (!cursor.moveToFirst()) {

            for (int j=0 ; j<4 ; j++){
               insertCategories(new Categories("Category "+ j, j));
            }
            for(int i=0; i<20; i++){
                insert(new Items("Item " + i , "Description", i,  random.nextInt(4)));
            }
            cursor.close();
        }
    }

    public ArrayList<Items> getCategoryItems(Categories category){
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = null;
        ArrayList<Items> items = new ArrayList<>();
        String selection = Categories.COLUMN_CATEGORY_ID + "=" +String.valueOf(category.id);
        try {
            cursor = db.query(Items.TABLE_NAME, null, selection,null, null, null,null);
            if(cursor.moveToFirst()){
                while (!cursor.isAfterLast()){

                    Items item = new Items();
                    item.id = cursor.getLong(cursor.getColumnIndex(Items.COLUMN_ID));
                    item.name = cursor.getString(cursor.getColumnIndex(Items.COLUMN_NAME));
                    item.description = cursor.getString(cursor.getColumnIndex(Items.COLUMN_DESCRIPTION));
                    item.price = cursor.getInt(cursor.getColumnIndex(Items.COLUMN_PRICE));
                    item.category = cursor.getInt(cursor.getColumnIndex(Categories.COLUMN_CATEGORY_ID));
                    items.add(0, item);
                    cursor.moveToNext();
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if(cursor !=null){
                cursor.close();
            }
        }
        return items;

    }


    public Items getItem(long id){
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = null ;
        Items item = null;

        try {
            cursor = db.query(Items.TABLE_NAME, null,Items.COLUMN_ID+ "=" +id,null, null,null,null);
            if(cursor.moveToFirst()){

                item = new Items();
                item.id = cursor.getLong(cursor.getColumnIndex(Items.COLUMN_ID));
                item.name = cursor.getString(cursor.getColumnIndex(Items.COLUMN_NAME));
                item.description = cursor.getString(cursor.getColumnIndex(Items.COLUMN_DESCRIPTION));
                item.price = cursor.getInt(cursor.getColumnIndex(Items.COLUMN_PRICE));
                item.category = cursor.getInt(cursor.getColumnIndex(Categories.COLUMN_CATEGORY_ID));


            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if(cursor !=null){
                cursor.close();
            }
        }
        return item;
    }


    public long addItem (Items item){
        long id =0;
        SQLiteDatabase db = getWritableDatabase();
        try{
            ContentValues values = new ContentValues();
            values.put(Basket.COLUMN_NAME, item.name);
            values.put(Basket.COLUMN_DESCRIPTION, item.description);
            values.put(Basket.COLUMN_PRICE, item.price);
            values.put(Categories.COLUMN_CATEGORY_ID, item.category);
            values.put(Basket.COLUMN_ID_ITEM, item.id);

            id = db.insert(Basket.TABLE_NAME,null, values);

        }catch (Exception e){
            e.printStackTrace();
        }

        return id;
    }



    public ArrayList<Items> getBasketItems(){
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = null;
        ArrayList<Items> items = new ArrayList<>();

        try {
            cursor = db.query(Basket.TABLE_NAME, null, null, null, null,null,null);
            if(cursor.moveToFirst()){
                while (!cursor.isAfterLast()){
                    Items item = new Items();
                    item.id = cursor.getLong(cursor.getColumnIndex(Basket.COLUMN_ID));
                    item.name = cursor.getString(cursor.getColumnIndex(Basket.COLUMN_NAME));
                    item.description = cursor.getString(cursor.getColumnIndex(Basket.COLUMN_DESCRIPTION));
                    item.price = cursor.getInt(cursor.getColumnIndex(Basket.COLUMN_PRICE));
                    item.category = cursor.getInt(cursor.getColumnIndex(Categories.COLUMN_CATEGORY_ID));
                    items.add(0, item);
                    cursor.moveToNext();
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if(cursor !=null){
                cursor.close();
            }
        }
        return items;

    }

    public boolean clearBasket (){
        boolean deleted = false;
        SQLiteDatabase db = getWritableDatabase();

        try {
            db.delete(Basket.TABLE_NAME, null, null);
        }catch (Exception e){
            e.printStackTrace();
        }
        return deleted;
    }

}








