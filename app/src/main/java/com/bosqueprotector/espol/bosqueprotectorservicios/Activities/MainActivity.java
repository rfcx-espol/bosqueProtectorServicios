package com.bosqueprotector.espol.bosqueprotectorservicios.Activities;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import com.bosqueprotector.espol.bosqueprotectorservicios.R;
import com.bosqueprotector.espol.bosqueprotectorservicios.Services.SendingAudiosService;
import static com.bosqueprotector.espol.bosqueprotectorservicios.Utils.Identifiers.IS_BOUNDED_AUDIO_SERVICE;
import static com.bosqueprotector.espol.bosqueprotectorservicios.Utils.Identifiers.SLEEP_TIME;
import static com.bosqueprotector.espol.bosqueprotectorservicios.Utils.Identifiers.onService;
import static com.bosqueprotector.espol.bosqueprotectorservicios.Utils.Identifiers.SENDING_AUDIO_TIME;
import static com.bosqueprotector.espol.bosqueprotectorservicios.Utils.Identifiers.setPreferencesApplications;
import static com.bosqueprotector.espol.bosqueprotectorservicios.Utils.Identifiers.alarmManager;
import static com.bosqueprotector.espol.bosqueprotectorservicios.Utils.Identifiers.pendingIntent;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();
    SendingAudiosService senderAudio;
    private boolean CONNECTION = true;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intentServiceAudio = new Intent(this, SendingAudiosService.class);
        //startService(intentServiceAudio);

        //INICIAR EL SERVICIO
        if(!onService) {
            PreferenceManager.setDefaultValues(this, R.xml.prefs, false);
            setPreferencesApplications(getApplicationContext());

            pendingIntent = PendingIntent.getService(getApplicationContext(), 0,
                    new Intent(this, SendingAudiosService.class), PendingIntent.FLAG_UPDATE_CURRENT);
            alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            if (alarmManager != null) {
                alarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime() + 1000,
                        SLEEP_TIME + SENDING_AUDIO_TIME, pendingIntent);
                Log.d("ALARMA", "ALARMA CREADA");
            }
            onService = true;
        }
        //bindService(intentServiceAudio, mConnection, Context.BIND_ABOVE_CLIENT);
        setContentView(com.bosqueprotector.espol.bosqueprotectorservicios.R.layout.activity_main);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.prefs:
                Log.i(TAG, "este es audio");
                 if (IS_BOUNDED_AUDIO_SERVICE){
                     senderAudio.printImConnected();
                 }else{
                     Log.i(TAG, "no se pudo hace conexion");
                 }
                startActivity(new Intent(this, SettingsActivity.class));
                break;
            case R.id.photos:
                break;
            case R.id.sensors:
                break;
        }
        return true;
    }

    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName className, IBinder service) {
            SendingAudiosService.LocalBinder binder = (SendingAudiosService.LocalBinder) service;
            senderAudio = binder.getService();
            IS_BOUNDED_AUDIO_SERVICE = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            IS_BOUNDED_AUDIO_SERVICE= false;
        }
    };

}