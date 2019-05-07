package mike.galitsky.myshop;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.widget.Toast;

public class SettingsActivity  extends PreferenceActivity {



    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.pref);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
         final Boolean nmod = preferences.getBoolean("switch_preference_1", false);
         final Boolean rus = preferences.getBoolean("switch_preference_2", false);
         if(nmod == true || rus == true ){
             Toast.makeText(this,"Restar app or change to land orientation !" ,Toast.LENGTH_SHORT).show();
         }


    }
}
