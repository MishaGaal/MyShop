package mike.galitsky.myshop;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.transition.Explode;
import android.transition.Slide;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Locale;

public class BasektActivity extends AppCompatActivity {

   private DatabaseReference mDatabaseReference;
    private DataBaseHelper itemsHelper = new DataBaseHelper(this);
    private ArrayList<Items> adding = new ArrayList<>();
    private GetBasket getBasket;
    private ClearBasket clearBasket;
    private NotificationManager mManager ;
    private Notification notification;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        Boolean rus = preferences.getBoolean("switch_preference_2", false);
        if (rus == true) {
            Locale localeRU = new Locale("ru");
            Locale.setDefault(localeRU);
            Configuration configRU = new Configuration();
            configRU.locale = localeRU;
            getResources().updateConfiguration(configRU,
                    getResources().getDisplayMetrics());
        }
        // requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        //getSupportActionBar().hide(); // hide the title bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        this.getWindow().setNavigationBarColor(Color.GRAY);
        setupWindowAnimations();
        Boolean nmod = preferences.getBoolean("switch_preference_1", false);
        if (nmod == true) {
            setContentView(R.layout.activity_basket_nm);
            getWindow().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.NightModeBackground)));
        } else {
            setTheme(R.style.AppTheme1);
            setContentView(R.layout.activity_basekt);
            getWindow().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.backgroundColor)));
        }


        getBasket = new GetBasket();
        getBasket.execute();


        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            Intent intent = new Intent(this, MainActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
            String CHANNEL_ID = "MY_CHANNEL";
            mManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            NotificationChannel foregroundChannel =
                    new NotificationChannel(CHANNEL_ID, "SwiftShop", NotificationManager.IMPORTANCE_HIGH);
            foregroundChannel.enableLights(false);
            foregroundChannel.enableVibration(false);
            foregroundChannel.setShowBadge(false);
            foregroundChannel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
            mManager.createNotificationChannel(foregroundChannel);
             notification = new Notification.Builder(this, CHANNEL_ID)
                    .setAutoCancel(true) //Скрывать по клику
                    .setSmallIcon(R.mipmap.ic_launcher) //Картинка в строке состояния
                    .setTicker("New order!") //Текст в строке состояния
                    .setContentTitle("You made a new order") //Заголовок уведомления
                    .setContentText("Thank you for choosing my app!") //Текст уведомления
                    .setWhen(System.currentTimeMillis()) //Время уведомления
                    .setContentIntent(pendingIntent) //Клик на уведомление
                    .build();
        }else{
            mManager= (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            Intent intent = new Intent(this, MainActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);


                notification = new NotificationCompat.Builder(this)
                    .setAutoCancel(true) //Скрывать по клику
                    .setSmallIcon(R.mipmap.ic_launcher) //Картинка в строке состояния
                    .setDefaults(Notification.DEFAULT_ALL) //Стандартные звуки и т.д.
                    .setTicker("New order!") //Текст в строке состояния
                    .setContentTitle("You made a new order") //Заголовок уведомления
                    .setContentText("Thank you for choosing my app!") //Текст уведомления
                    .setWhen(System.currentTimeMillis()) //Время уведомления
                    .setContentIntent(pendingIntent) //Клик на уведомление
                    .build();
        }
        findViewById(R.id.button3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OrderFragment orderFragment = OrderFragment.newInstance(adding);
                orderFragment.setActionListener(new OrderFragment.ActionListener() {
                    @Override
                    public void save(Order order) {
                        mDatabaseReference = FirebaseDatabase.getInstance().getReference();
                        mDatabaseReference.child("order").push().setValue(order);
                        Toast.makeText(BasektActivity.this,"Thank you for order!" ,Toast.LENGTH_SHORT).show();
                        clearBasket = new ClearBasket();
                        clearBasket.execute();
                        getBasket = new GetBasket();
                        getBasket.execute();
                        mManager.notify(1, notification);
                    }
                });

                orderFragment.show(getSupportFragmentManager(), "dialog");
            }
        });


    }
    @Override
    protected void onDestroy(){
        super.onDestroy();
        if(getBasket!= null){
            getBasket.cancel(true);
        }
        if(clearBasket != null){
            clearBasket.cancel(true);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.basket, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        clearBasket = new ClearBasket();
        clearBasket.execute();
        getBasket = new GetBasket();
        getBasket.execute();
        return true;
    }

    public class GetBasket extends AsyncTask<Void, Void, ArrayList<Items>> {

        private ProgressDialog mDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mDialog = new ProgressDialog(BasektActivity.this);
            mDialog.setMessage("Please wait");
            mDialog.setCancelable(false);
            mDialog.show();
        }

        @Override
        protected ArrayList<Items> doInBackground(Void... voids) {
            return itemsHelper.getBasketItems();
        }

        @Override
        protected void onPostExecute(ArrayList<Items> aVoid) {
            super.onPostExecute(aVoid);
            adding.addAll(aVoid);
            BasketFragment basketFragment = BasketFragment.newInstance(adding);
            basketFragment.setActionListener(new BasketFragment.ActionListener() {
                @Override
                public void onClick(Items item, View view) {
                    EditionFragment editionFragment = EditionFragment.newInstance(item);
                    editionFragment.setActionListener(new EditionFragment.ActionListener() {
                        @Override
                        public void save(Items item) {
                            Toast.makeText(BasektActivity.this, "Already in basket", Toast.LENGTH_LONG).show();
                        }
                    });
                    editionFragment.show(getSupportFragmentManager(), "dialog");
                }
            });
            getSupportFragmentManager().beginTransaction().replace(R.id.basker_fragment_layout, basketFragment).commit();
            if(mDialog !=null && mDialog.isShowing()){
                mDialog.dismiss();
            }
        }
    }

    public class ClearBasket extends AsyncTask<Void, Void, Void>{
        private ProgressDialog mDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mDialog = new ProgressDialog(BasektActivity.this);
            mDialog.setMessage("Please wait");
            mDialog.setCancelable(false);
            mDialog.show();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            itemsHelper.clearBasket();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            adding.clear();
            if(mDialog !=null && mDialog.isShowing()){
                mDialog.dismiss();
            }
        }
    }

    private void setupWindowAnimations() {
        /*Slide slide = new Slide();
        slide.setDuration(1000);
        slide.setSlideEdge(Gravity.RIGHT);
       this.getWindow().setExitTransition(slide);
*/
        Slide slide1 = new Slide();
        slide1.setDuration(500);
        slide1.setSlideEdge(Gravity.RIGHT);
        getWindow().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.backgroundColor)));
        this.getWindow().setEnterTransition(slide1);
        slide1.setDuration(1000);
        this.getWindow().setExitTransition(slide1);
    }

}
