package com.bosqueprotector.espol.bosqueprotectorservicios.Activities;

import android.os.Bundle;
import android.preference.PreferenceActivity;

import com.bosqueprotector.espol.bosqueprotectorservicios.R;

/**
 * Created by joset on 25/01/2018.
 */

public class SettingsActivity extends PreferenceActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.prefs);
    }


}
