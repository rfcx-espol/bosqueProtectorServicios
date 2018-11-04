package com.bosqueprotector.espol.bosqueprotectorservicios.Activities;

import android.app.AlarmManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.SystemClock;
import android.preference.EditTextPreference;
import android.preference.PreferenceActivity;
import android.util.Log;
import com.bosqueprotector.espol.bosqueprotectorservicios.R;
import com.bosqueprotector.espol.bosqueprotectorservicios.Services.SendingAudiosService;
import com.bosqueprotector.espol.bosqueprotectorservicios.Utils.FolderIterator;
import com.bosqueprotector.espol.bosqueprotectorservicios.Utils.Identifiers;
import static com.bosqueprotector.espol.bosqueprotectorservicios.Utils.Identifiers.SLEEP_TIME;
import static com.bosqueprotector.espol.bosqueprotectorservicios.Utils.Identifiers.call;
import static com.bosqueprotector.espol.bosqueprotectorservicios.Utils.Identifiers.setPreferencesApplications;
import static com.bosqueprotector.espol.bosqueprotectorservicios.Utils.Identifiers.alarmManager;
import static com.bosqueprotector.espol.bosqueprotectorservicios.Utils.Identifiers.pendingIntent;

public class SettingsActivity extends PreferenceActivity implements SharedPreferences.OnSharedPreferenceChangeListener {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.prefs);
        EditTextPreference APIKey_preference = new EditTextPreference(SettingsActivity.this);
        APIKey_preference.setKey("APIKey");
        APIKey_preference.setTitle("APIKey");
        APIKey_preference.setSummary(Identifiers.APIKey);
        APIKey_preference.setDefaultValue(Identifiers.APIKey);
        APIKey_preference.setOrder(0);
        APIKey_preference.setEnabled(false);
        getPreferenceScreen().addPreference(APIKey_preference);
        getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
    }

    //LISTENER QUE DETECTA CAMBIOS EN LAS PREFERENCIAS
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals("sleepTime")) {
            FolderIterator.threadRunning = false;
            call.cancel();
            SendingAudiosService.thread.interrupt();
            alarmManager.cancel(pendingIntent);
            call.cancel();
            SendingAudiosService.thread.interrupt();
            stopService(new Intent(this, SendingAudiosService.class));
            setPreferencesApplications(getApplicationContext());
            alarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime() + 1000,
                    SLEEP_TIME, pendingIntent);
        }
    }

}