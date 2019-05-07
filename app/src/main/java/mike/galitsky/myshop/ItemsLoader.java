package mike.galitsky.myshop;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.AsyncTaskLoader;

import java.util.ArrayList;

public class ItemsLoader extends AsyncTaskLoader<ArrayList<Items>> {

    private  DataBaseHelper mHelper;


    public ItemsLoader(@NonNull Context context) {
        super(context);
        mHelper = new DataBaseHelper(context);
    }

    @Nullable
    @Override
    public ArrayList<Items> loadInBackground() {
        return mHelper.getItems();
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }
}
