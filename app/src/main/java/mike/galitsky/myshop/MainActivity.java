package mike.galitsky.myshop;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.transition.Fade;
import android.transition.Slide;
import android.transition.Visibility;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Random;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<ArrayList<Items>>{


    private static final int REQUEST_CODE = 1212;
    private BackUp backUp;
    private UpLoad upLoad;
    private BuyItem buyItem;
    private List<AuthUI.IdpConfig> providers;

    public static final   String FileName = "Backup.txt";
    final DataBaseHelper itemsHelper = new DataBaseHelper(this);



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            setupWindowAnimations();
        }
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

       // requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        this.getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        this.getWindow().setNavigationBarColor(Color.GRAY);
        Boolean nmod = preferences.getBoolean("switch_preference_1", false);
        if(nmod == true){
            setContentView(R.layout.activity_main_nm);
            getWindow().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.NightModeBackground)));
        }else { setTheme(R.style.AppTheme1);
            getWindow().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.backgroundColor)));
            setContentView(R.layout.activity_main);}
        itemsHelper.insertData();
        getSupportLoaderManager().initLoader(0, null,this);

        providers = Arrays.asList(
                new AuthUI.IdpConfig.GoogleBuilder().build(),
                new AuthUI.IdpConfig.EmailBuilder().build(),
                new AuthUI.IdpConfig.PhoneBuilder().build()
        );
        if(FirebaseAuth.getInstance().getCurrentUser() == null) {
            showSighInOptions();
        }
    }

    private void showSighInOptions() {
        startActivityForResult(AuthUI.getInstance().createSignInIntentBuilder().setAvailableProviders(providers).setTheme(R.style.AppTheme).build(),
                REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_CODE){

            IdpResponse response =IdpResponse.fromResultIntent(data);

            if(resultCode == RESULT_OK){
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
                SharedPreferences.Editor editor = preferences.edit();

                editor.putString("edit_text_name", user.getDisplayName());
                editor.putString("edit_text_email", user.getEmail());
                editor.putString("edit_text_phone", user.getPhoneNumber());

                editor.commit();
            }else {Toast.makeText(this, ""+response.getError().getMessage(), Toast.LENGTH_LONG).show();}
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(buyItem != null){
            buyItem.cancel(true);
        }
        if(backUp != null){
            backUp.cancel(true);
        }
        if(upLoad != null){
            upLoad.cancel(true);
        }


    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){

            case R.id.info: {
                Intent intent = new Intent(MainActivity.this, Info.class);
                startActivity(intent);
            }
        break;

            case R.id.settings:{

                Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
                startActivity(intent);

            }
                break;

            case  R.id.backup:{
                if(hasPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)){
                    if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){

                        backUp = new BackUp();
                        backUp.execute();

                        Toast.makeText(this, Environment.getExternalStorageDirectory().getAbsolutePath()+"/BackUP", Toast.LENGTH_LONG).show();
                    }else {
                        Toast.makeText(this, "No External Storage", Toast.LENGTH_LONG).show();
                    }
                }else {
                    requestPermissions();

                }
            }

            break;

            case  R.id.upload:{
                if(hasPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)){
                    if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){

                        upLoad = new UpLoad();
                        upLoad.execute();

                        Toast.makeText(this, "DONE",Toast.LENGTH_LONG).show();


                    }else {
                        Toast.makeText(this, "No STORAGE", Toast.LENGTH_LONG).show();
                    }
                }else {
                    requestPermissions();
                }
            }

        }

        return super.onOptionsItemSelected(item);
    }

    private void requestPermissions(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
        String[] permissions = new String[]{
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE
            };

                requestPermissions(permissions, 0);

            }
    }


    private boolean hasPermission(String permissions){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            return ContextCompat.checkSelfPermission(this, permissions) == PackageManager.PERMISSION_GRANTED;
        }
        return true;
    }


    @NonNull
    @Override
    public Loader<ArrayList<Items>> onCreateLoader(int i, @Nullable Bundle bundle) {
        return new ItemsLoader(this);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<ArrayList<Items>> loader, ArrayList<Items> adding) {
      MainActivityFragment mainActivityFragment = MainActivityFragment.newInstance(adding);
      mainActivityFragment.setActionListener(new MainActivityFragment.ActionListener() {
          @Override
          public void onClick(final Items item, View view) {
              PopupMenu popupMenu = new PopupMenu(MainActivity.this, view);
              popupMenu.inflate(R.menu.popup);
              popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                  @Override
                  public boolean onMenuItemClick(MenuItem menuItem) {

                      switch (menuItem.getItemId()) {
                          case R.id.ac1: {

                              buyItem = new BuyItem();
                              buyItem.execute(item);
                              Toast.makeText(MainActivity.this, "Added to Basket" ,Toast.LENGTH_SHORT).show();
                              break;
                          }
                          case R.id.ac2: {

                              editItem(item);

                              break;
                          }

                      }
                      return false;
                  }

              });
              popupMenu.show();
          }
      });

      mainActivityFragment.setActionListener2(new MainActivityFragment.ActionListener2() {
          @Override
          public void onClick2(final Items item, View view) {
              view.findViewById(R.id.button2).setOnClickListener(new View.OnClickListener() {
                  @Override
                  public void onClick(View view) {
                      buyItem = new BuyItem();
                      buyItem.execute(item);
                      Toast.makeText(MainActivity.this, "Added to Basket" ,Toast.LENGTH_SHORT).show();
                  }
              });
              editItem(item);

          }
      });
      getSupportFragmentManager().beginTransaction().replace(R.id.tableRow, mainActivityFragment).commit();
    }

    @Override
    public void onLoaderReset(@NonNull Loader<ArrayList<Items>> loader) {
    }


    public class BackUp extends AsyncTask<File, Void, Void >{

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
        protected Void doInBackground(File... files) {
            File folder = new File(Environment.getExternalStorageDirectory().getAbsolutePath(),"/BackUp");
            return itemsHelper.backUp(folder, FileName);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            getSupportLoaderManager().restartLoader(0, null, MainActivity.this);
            if(mDialog !=null && mDialog.isShowing()){
                mDialog.dismiss();
            }
        }
    }
    public class UpLoad extends AsyncTask<File, Void, Void >{

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
        protected Void doInBackground(File... files) {
            File folder = new File(Environment.getExternalStorageDirectory().getAbsolutePath(),"/BackUp");
            return itemsHelper.InsertBackUpItems(folder, FileName);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            getSupportLoaderManager().restartLoader(0, null, MainActivity.this);
            if(mDialog !=null && mDialog.isShowing()){
                mDialog.dismiss();
            }
        }
    }
    public class BuyItem extends AsyncTask<Items, Void, Void >{

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
        protected Void doInBackground(Items... items) {
            itemsHelper.addItem(items[0]);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            //getSupportLoaderManager().restartLoader(0, null, MainActivity.this);
            if(mDialog !=null && mDialog.isShowing()){
                mDialog.dismiss();
            }
        }
    }


        private Void editItem(Items item){

        EditionFragment editionFragment = EditionFragment.newInstance(item);
        editionFragment.setActionListener(new EditionFragment.ActionListener() {
            @Override
            public void save(Items item) {
                buyItem = new BuyItem();
                buyItem.execute(item);
                Toast.makeText(MainActivity.this,"Added to basket!" ,Toast.LENGTH_SHORT).show();
            }
        });

            editionFragment.show(getSupportFragmentManager(), "dialog");
            return null;
    }

    private void setupWindowAnimations() {

        Slide  slide = new Slide();
        slide.setDuration(1000);
        slide.setSlideEdge(Gravity.RIGHT);
        this.getWindow().setEnterTransition(slide);
        this.getWindow().setExitTransition(slide);

    }

}
