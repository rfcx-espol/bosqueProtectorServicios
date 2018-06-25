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
import com.bosqueprotector.espol.bosqueprotectorservicios.Utils.Identifiers;

import static com.bosqueprotector.espol.bosqueprotectorservicios.Utils.Identifiers.SENDING_AUDIO_TIME;
import static com.bosqueprotector.espol.bosqueprotectorservicios.Utils.Identifiers.SLEEP_TIME;
import static com.bosqueprotector.espol.bosqueprotectorservicios.Utils.Identifiers.call;
import static com.bosqueprotector.espol.bosqueprotectorservicios.Utils.Identifiers.setPreferencesApplications;
import static com.bosqueprotector.espol.bosqueprotectorservicios.Utils.Identifiers.alarmManager;
import static com.bosqueprotector.espol.bosqueprotectorservicios.Utils.Identifiers.pendingIntent;
import static com.bosqueprotector.espol.bosqueprotectorservicios.Utils.Identifiers.threadRunning;

public class SettingsActivity extends PreferenceActivity implements SharedPreferences.OnSharedPreferenceChangeListener {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.prefs);
        EditTextPreference APIKey_preference = new EditTextPreference(SettingsActivity.this);
        APIKey_preference.setKey("APIKey");
        APIKey_preference.setTitle("APIKey");
        APIKey_preference.setSummary("Key to authenticate the station in the server");
        APIKey_preference.setDefaultValue(Identifiers.APIKey);
        APIKey_preference.setOrder(0);
        getPreferenceScreen().addPreference(APIKey_preference);
        getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
    }

    //LISTENER QUE DETECTA CAMBIOS EN LAS PREFERENCIAS
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        Log.d("METODO CHANGE", "CAMBIÓ LA CONFIGURACIÓN: " + key);
        if (key.equals("sleepTime") || key.equals("sendingAudioTime")) {
            Log.d("METODO CHANGE", "ENTRÓ");
            threadRunning = false;
            call.cancel();
            stopService(new Intent(this, SendingAudiosService.class));
            setPreferencesApplications(getApplicationContext());
            alarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime() + 1000,
                    SLEEP_TIME + SENDING_AUDIO_TIME , pendingIntent);
            Log.d("ALARMA", "ALARMA CAMBIADA");
        }
    }

}