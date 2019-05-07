package mike.galitsky.adminpannel;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.PopupMenu;


import java.util.ArrayList;
import java.util.Random;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<ArrayList<Items>> {



    private ListView listView;
    private ArrayAdapter adapter;
    private ArrayList<Items> items;
    private InsertItem insertItem;
    private DeleteItem deleteItem;
    private UpdateItem updateItem;

    Random random = new Random();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        items = new ArrayList<>();
        listView = findViewById(R.id.List_view);
        adapter = new ArrayAdapter(
                this,
                android.R.layout.simple_list_item_1,
                android.R.id.text1,
                items
        );
        listView.setAdapter(adapter);

        getSupportLoaderManager().initLoader(0, null,this);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, final int i, long l) {

                PopupMenu popupMenu = new PopupMenu(MainActivity.this, view);
                popupMenu.inflate(R.menu.popup);
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {

                        switch (menuItem.getItemId()) {
                            case R.id.ac1: {

                                insertItem = new InsertItem();
                                insertItem.execute(new Items("Admin Item ", "Admin Description", 60,  random.nextInt(4)));


                                break;
                            }
                            case R.id.ac2: {

                                updateItem = new UpdateItem();
                                updateItem.execute(items.get(i));

                                break;
                            }
                            case R.id.ac3: {

                                deleteItem = new DeleteItem();
                                deleteItem.execute(items.get(i));
                                break;
                            }
                        }
                        return false;
                    }

                });
                popupMenu.show();
            }
        });

    }

    protected void onDestroy() {
        super.onDestroy();
        if(insertItem != null){
            insertItem.cancel(true);
        }
        if(deleteItem != null){
            deleteItem.cancel(true);
        }
        if(updateItem != null){
            updateItem.cancel(true);
        }


    }



    @NonNull
    @Override
    public Loader<ArrayList<Items>> onCreateLoader(int i, @Nullable Bundle bundle) {
        return new ItemsLoader(this);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<ArrayList<Items>> loader, ArrayList<Items> adding) {

        items.clear();
        items.addAll(adding);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onLoaderReset(@NonNull Loader<ArrayList<Items>> loader) {
    }

    public class InsertItem extends AsyncTask<Items, Void, Boolean> {

        private ProgressDialog mDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mDialog = new ProgressDialog(MainActivity.this);
            mDialog.setMessage("Please wait");
            mDialog.setCancelable(false);
            mDialog.show();
        }

        @Override
        protected Boolean doInBackground(Items... items) {

            Items item = items[0];
            ContentValues values = new ContentValues();
            values.put(Items.COLUMN_NAME, item.name);
            values.put(Items.COLUMN_DESCRIPTION, item.description);
            values.put(Items.COLUMN_PRICE, item.price);
            values.put("_idCategory", item.category);

            try{

                Uri uri = Uri.parse("content://mike.galitsky.myshop/items");
                Uri res = getContentResolver().insert(uri, values);

                return res != null;
            }catch (Exception e){
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Boolean suc) {
            super.onPostExecute(suc);

            if (suc) {
                getSupportLoaderManager().restartLoader(0, null, MainActivity.this);
            }
            if(mDialog !=null && mDialog.isShowing()){
                mDialog.dismiss();
            }
        }
    }

    public class DeleteItem extends  AsyncTask<Items, Void, Boolean>{
        private ProgressDialog mDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mDialog = new ProgressDialog(MainActivity.this);
            mDialog.setMessage("Please wait");
            mDialog.setCancelable(false);
            mDialog.show();
        }

        @Override
        protected Boolean doInBackground(Items... items) {
            try {
                Items item = items[0];
                Uri uri = Uri.parse("content://mike.galitsky.myshop/items"+"/"+ item.id);
                getContentResolver().delete(uri,null ,null);

            }catch (Exception e){
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);

            getSupportLoaderManager().restartLoader(0, null, MainActivity.this);
            if(mDialog !=null && mDialog.isShowing()){
                mDialog.dismiss();
            }
        }
    }


    public class UpdateItem extends  AsyncTask<Items, Void, Boolean>{
        private ProgressDialog mDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mDialog = new ProgressDialog(MainActivity.this);
            mDialog.setMessage("Please wait");
            mDialog.setCancelable(false);
            mDialog.show();
        }

        @Override
        protected Boolean doInBackground(Items... items) {
            try {

                Items item = items[0];
                ContentValues values = new ContentValues();
                values.put(Items.COLUMN_NAME, "Updated item");
                values.put(Items.COLUMN_DESCRIPTION, "Updated Description");
                values.put(Items.COLUMN_PRICE, 77);
                values.put("_idCategory", item.category);

                Uri uri = Uri.parse("content://mike.galitsky.myshop/items"+"/"+item.id);
                int res = getContentResolver().update(uri,  values, null, null);

            }catch (Exception e){
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);

            getSupportLoaderManager().restartLoader(0, null, MainActivity.this);
            if(mDialog !=null && mDialog.isShowing()){
                mDialog.dismiss();
            }
        }
    }
}
