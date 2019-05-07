package mike.galitsky.adminpannel;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.AsyncTaskLoader;

import java.util.ArrayList;

public class ItemsLoader  extends AsyncTaskLoader<ArrayList<Items>> {

    private Context mContext;


    public ItemsLoader(@NonNull Context context) {
        super(context);
        mContext = context;
    }

    @Nullable
    @Override
    public ArrayList<Items> loadInBackground() {

        ArrayList<Items> items = new ArrayList<>();

        Cursor cursor = null;

        try {
            Uri uri = Uri.parse("content://mike.galitsky.myshop/items");
            cursor = mContext.getContentResolver().query(uri, null,null,null,null);
            if(cursor.moveToFirst()){
                while (!cursor.isAfterLast()){
                    Items item = new Items();
                    item.id = cursor.getLong(cursor.getColumnIndex(Items.COLUMN_ID));
                    item.name = cursor.getString(cursor.getColumnIndex(Items.COLUMN_NAME));
                    item.description = cursor.getString(cursor.getColumnIndex(Items.COLUMN_DESCRIPTION));
                    item.price = cursor.getInt(cursor.getColumnIndex(Items.COLUMN_PRICE));
                    item.category = cursor.getInt(cursor.getColumnIndex("_idCategory"));
                    items.add(item);
                    cursor.moveToNext();
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if(cursor != null){
                cursor.close();
            }
        }
        return items;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

}
