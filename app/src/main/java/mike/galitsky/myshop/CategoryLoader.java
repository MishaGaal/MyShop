package mike.galitsky.myshop;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.AsyncTaskLoader;

import java.util.ArrayList;

public class CategoryLoader extends AsyncTaskLoader<ArrayList<Categories>> {

    private  DataBaseHelper mHelper;


    public CategoryLoader(@NonNull Context context) {
        super(context);
        mHelper = new DataBaseHelper(context);
    }

    @Nullable
    @Override
    public ArrayList<Categories> loadInBackground() {

        return mHelper.getCategories();
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }
}