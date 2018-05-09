package com.bosqueprotector.espol.bosqueprotectorservicios.activities;

import android.app.AlarmManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.SystemClock;
import android.preference.PreferenceActivity;
import android.util.Log;
import com.bosqueprotector.espol.bosqueprotectorservicios.R;
import com.bosqueprotector.espol.bosqueprotectorservicios.services.SendingAudiosService;

import static com.bosqueprotector.espol.bosqueprotectorservicios.utils.Identifiers.SENDING_AUDIO_TIME;
import static com.bosqueprotector.espol.bosqueprotectorservicios.utils.Identifiers.SLEEP_TIME;
import static com.bosqueprotector.espol.bosqueprotectorservicios.utils.Identifiers.call;
import static com.bosqueprotector.espol.bosqueprotectorservicios.utils.Identifiers.setPreferencesApplications;
import static com.bosqueprotector.espol.bosqueprotectorservicios.utils.Identifiers.alarmManager;
import static com.bosqueprotector.espol.bosqueprotectorservicios.utils.Identifiers.pendingIntent;
import static com.bosqueprotector.espol.bosqueprotectorservicios.utils.Identifiers.threadRunning;

public class SettingsActivity extends PreferenceActivity implements SharedPreferences.OnSharedPreferenceChangeListener {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.prefs);
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