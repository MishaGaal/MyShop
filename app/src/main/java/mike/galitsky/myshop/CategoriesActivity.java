package mike.galitsky.myshop;
import android.app.ActivityOptions;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.TabItem;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.transition.Fade;
import android.transition.Slide;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Locale;

public class CategoriesActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<ArrayList<Categories>>{


    private ArrayList<Categories> categories;
    private InsertCategoryItems categoryItems;
    private TabLayout tabLayout;
    private ArrayAdapter<Categories> adapter;
    private BottomNavigationView navigationView;

    final DataBaseHelper itemsHelper = new DataBaseHelper(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        Boolean rus = preferences.getBoolean("switch_preference_2", false);
        if(rus == true){
            Locale localeRU = new Locale("ru");
            Locale.setDefault(localeRU);
            Configuration configRU = new Configuration();
            configRU.locale = localeRU;
            getResources().updateConfiguration(configRU,
                    getResources().getDisplayMetrics());
        }

        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        this.getWindow().setNavigationBarColor(Color.GRAY);
        setupWindowAnimations();
        Boolean nmod = preferences.getBoolean("switch_preference_1", false);
        if(nmod == true){
            setContentView(R.layout.activity_categories_nm);
            getWindow().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.NightModeBackground)));
        }else { setTheme(R.style.AppTheme1);
            getWindow().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.backgroundColor)));
            setContentView(R.layout.activity_categoires);}


            navigationView =  findViewById(R.id.bottom_navigation);
        navigationView.setSelectedItemId(R.id.action_category);
        navigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                switch (menuItem.getItemId()){
                    case R.id.action_category: {

                        break;
                    }

                    case R.id.action_home: {

                        startActivity(new Intent(CategoriesActivity.this, MainActivity.class), ActivityOptions.makeSceneTransitionAnimation
                                (CategoriesActivity.this).toBundle());
                        break;
                    }
                    case R.id.action_basket: {

                        Intent intent = new Intent(CategoriesActivity.this, BasektActivity.class);
                        startActivity(intent);
                        break;
                    }

                    case R.id.action_info:
                        startActivity(new Intent(CategoriesActivity.this, Info.class), ActivityOptions.makeSceneTransitionAnimation
                                (CategoriesActivity.this).toBundle());
                        break;

                }

                return false;
            }
        });
            tabLayout = findViewById(R.id.tab_layout);
            int category = tabLayout.getSelectedTabPosition();
            tabLayout.getTabAt(category).setText(getResources().getString(R.string.category) + category);
            tabLayout.setSelectedTabIndicatorColor(getResources().getColor(R.color.logoColor1));
            tabLayout.addOnTabSelectedListener(new TabLayout.BaseOnTabSelectedListener() {
                @Override
                public void onTabSelected(TabLayout.Tab tab) {
                    int category = tab.getPosition();
                    tab.setText(getResources().getString(R.string.category) + category);
                    categoryItems = new InsertCategoryItems();
                    categoryItems.execute(categories.get(category));
                }

                @Override
                public void onTabUnselected(TabLayout.Tab tab) {

                }

                @Override
                public void onTabReselected(TabLayout.Tab tab) {

                }
            });

        categories = new ArrayList<>();
        getSupportLoaderManager().initLoader(0, null,this);

    }


    @NonNull
    @Override
    public Loader<ArrayList<Categories>> onCreateLoader(int i, @Nullable Bundle bundle) {
        return new CategoryLoader(this);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<ArrayList<Categories>> loader, ArrayList<Categories> cat) {
        categories.clear();
        categories.addAll(cat);
        categoryItems = new InsertCategoryItems();
        categoryItems.execute(categories.get(0));
    }

    @Override
    public void onLoaderReset(@NonNull Loader<ArrayList<Categories>> loader) {

    }

    public class InsertCategoryItems extends AsyncTask<Categories, Void, ArrayList<Items>> {

        private ProgressDialog mDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mDialog = new ProgressDialog(CategoriesActivity.this);
            mDialog.setMessage("Please wait");
            mDialog.setCancelable(false);
            mDialog.show();
        }

        @Override
        protected ArrayList<Items> doInBackground(Categories... categories) {

            Categories category = categories[0];

            return itemsHelper.getCategoryItems(category);
        }

        @Override
        protected void onPostExecute(ArrayList<Items> items) {
            super.onPostExecute(items);

            CategoryItemsFragment categoryItemsFragment = CategoryItemsFragment.newInstance(items);
            categoryItemsFragment.setActionListener(new CategoryItemsFragment.ActionListener() {
                @Override
                public void buy(Items item) {
                    itemsHelper.addItem(item);
                }
            });
            getSupportFragmentManager().beginTransaction().replace(R.id.fragmet_catergory, categoryItemsFragment).commit();
            if(mDialog !=null && mDialog.isShowing()){
                mDialog.dismiss();
            }
        }
    }

    private void setupWindowAnimations() {

        Slide slide1 = new Slide();
        slide1.setDuration(1000);
        slide1.setSlideEdge(Gravity.LEFT);
        this.getWindow().setEnterTransition(slide1);
        this.getWindow().setExitTransition(slide1);
    }

}
