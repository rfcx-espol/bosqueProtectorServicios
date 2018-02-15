package com.bosqueprotector.espol.bosqueprotectorservicios.Activities;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.bosqueprotector.espol.bosqueprotectorservicios.R;
import com.bosqueprotector.espol.bosqueprotectorservicios.Services.SendingAudiosService;
import com.bosqueprotector.espol.bosqueprotectorservicios.Utils.Identifiers;
import com.bosqueprotector.espol.bosqueprotectorservicios.Utils.Utils;

import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import static com.bosqueprotector.espol.bosqueprotectorservicios.Utils.Identifiers.IS_BOUNDED_AUDIO_SERVICE;
import static com.bosqueprotector.espol.bosqueprotectorservicios.Utils.Identifiers.IS_ON_SERVICE;
import static com.bosqueprotector.espol.bosqueprotectorservicios.Utils.Identifiers.PREFS_SETTINGS;
import static com.bosqueprotector.espol.bosqueprotectorservicios.Utils.Identifiers.SENDING_AUDIO_TIME;
import static com.bosqueprotector.espol.bosqueprotectorservicios.Utils.Identifiers.SERVICE_INTENT_AUDIO_SENDER;

public class MainActivity extends AppCompatActivity implements SharedPreferences.OnSharedPreferenceChangeListener {
    /*
    * Tags ce la clase
     */
    private static final String TAG = MainActivity.class.getSimpleName();
    /*
    * Servicios implementados
    *
    */
    SendingAudiosService senderAudio;
    private boolean CONNECTION =true;
    /**/

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Utils.initializingVariables(getApplicationContext());
        Log.i(TAG, "binding service with main activity");

        Intent intentServiceAudio = new Intent(this, SendingAudiosService.class);
        startService(intentServiceAudio);
        bindService(intentServiceAudio, mConnection, Context.BIND_ABOVE_CLIENT);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onStart() {
        super.onStart();
        // Bind to LocalService
        /*
        if (CONNECTION) {
            if (IS_ON_SERVICE){
                Intent intentServiceAudio = new Intent(this, SendingAudiosService.class);
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                startService(intentServiceAudio);
                bindService(intentServiceAudio, mConnection, Context.BIND_AUTO_CREATE);
            }

        }else{
            Timer t = new Timer();

            t.scheduleAtFixedRate(new TimerTask() {

                @Override
                public void run() {

                    if (IS_ON_SERVICE){
                        Intent intentServiceAudio = new Intent(getApplicationContext(), SendingAudiosService.class);
                            try {
                                Thread.sleep(500);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        startService(intentServiceAudio);
                        bindService(intentServiceAudio, mConnection, Context.BIND_AUTO_CREATE);
                    }




                                      }

                                  },
                    //Set how long before to start calling the TimerTask (in milliseconds)
                    1500,
                    //Set the amount of time between each execution (in milliseconds)
                    SENDING_AUDIO_TIME*1000);

        }
        */


    }




    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        switch (item.getItemId()) {

            case R.id.audio:

                Log.i(TAG, "este es audio");
                 if (IS_BOUNDED_AUDIO_SERVICE){
                     senderAudio.printImConnected();
                 }else{
                     Log.i(TAG, "no se pudo hace conexion");
                 }

                Intent prefsIntent = new Intent(this, SettingsActivity.class);
                startActivity(prefsIntent);
                break;

            case R.id.photos:
                Log.i(TAG, "este es photos");
                break;

            case R.id.sensors:
                Log.i(TAG, "este son sensores");
                break;


        }

        return true;
    }




    /** Defines callbacks for service binding, passed to bindService() */
    private ServiceConnection mConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className,
                                       IBinder service) {
            // We've bound to LocalService, cast the IBinder and get LocalService instance
            SendingAudiosService.LocalBinder binder = (SendingAudiosService.LocalBinder) service;
            senderAudio = binder.getService();
            IS_BOUNDED_AUDIO_SERVICE = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            IS_BOUNDED_AUDIO_SERVICE= false;
        }
    };

    @Override
    public synchronized void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String prefKey) {
        Log.d(TAG, "Pref changed: "+prefKey+" = "+PREFS_SETTINGS.getString(prefKey, null));

    }






}



