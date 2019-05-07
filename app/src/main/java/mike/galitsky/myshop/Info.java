package mike.galitsky.myshop;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.transition.Explode;
import android.transition.Fade;
import android.transition.Slide;
import android.transition.Visibility;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import java.util.ArrayList;
import java.util.Locale;

public class Info extends AppCompatActivity {

private WebView webView;

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

        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        getSupportActionBar().hide(); // hide the title bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        this.getWindow().setNavigationBarColor(Color.GRAY);
        Boolean nmod = preferences.getBoolean("switch_preference_1", false);
        if(nmod == true){
            setContentView(R.layout.activity_info_nm);
            getWindow().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.NightModeBackground)));
        }else { setTheme(R.style.AppTheme1);
            getWindow().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.backgroundColor)));
            setContentView(R.layout.activity_info);}

        setupWindowAnimations();
        webView = findViewById(R.id.webView);
        webView.setWebViewClient(new WebViewClient());

        WebSettings settings = webView.getSettings();
        settings.setDefaultTextEncodingName("utf-8");

        webView.loadUrl("https://www.facebook.com/mykhail.galytskyi");
    }
    private void setupWindowAnimations() {

        Slide slide = new Slide();
        slide.setDuration(1000);
        slide.setSlideEdge(Gravity.TOP);
        this.getWindow().setExitTransition(slide);
        this.getWindow().setEnterTransition(slide);
    }
}
