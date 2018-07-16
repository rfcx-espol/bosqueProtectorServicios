package com.bosqueprotector.espol.bosqueprotectorservicios.Services;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Binder;
import android.os.Environment;
import android.os.IBinder;
import android.os.PowerManager;
import android.support.annotation.Nullable;
import android.util.Log;
import com.bosqueprotector.espol.bosqueprotectorservicios.Utils.FolderIterator;
import com.bosqueprotector.espol.bosqueprotectorservicios.Utils.Utils;

import java.io.File;
import java.net.URL;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;

import static com.bosqueprotector.espol.bosqueprotectorservicios.Utils.Identifiers.NUMBER_OF_INTENTS;
import static com.bosqueprotector.espol.bosqueprotectorservicios.Utils.Identifiers.TIME_BETWEEN_INTENTS;
import static com.bosqueprotector.espol.bosqueprotectorservicios.Utils.Identifiers.setAPIKey;

import static com.bosqueprotector.espol.bosqueprotectorservicios.Utils.Identifiers.setPreferencesApplications;

public class SendingAudiosService extends Service {
    private final IBinder mBinder = new LocalBinder();
    public static PowerManager.WakeLock wakeLock;
    public static Thread thread;

    public class LocalBinder extends Binder {
        public SendingAudiosService getService() {
            return SendingAudiosService.this;
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    //CREAR EL SERVICIO DE ENVÍO DE AUDIOS
    @Override
    public void onCreate(){
        super.onCreate();
        setAPIKey(getApplicationContext());
        setPreferencesApplications(getApplicationContext());

        //MANTENER ENCENDIDO EL CPU DEL CELULAR AL APAGAR LA PANTALLA
        PowerManager powerManager = (PowerManager) getSystemService(POWER_SERVICE);
        wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "MyWakelockTag");
        wakeLock.acquire();
    }

    //INICIAR EL SERVICIO DE ENVÍO DE AUDIOS
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        startSendingAudiosHandler();
        return Service.START_NOT_STICKY;
    }

    //DESTRUIR EL SERVICIO DE ENVÍO DE AUDIOS
    @Override
    public void onDestroy() {
        super.onDestroy();
        wakeLock.release();
    }

    //INICIO DEL SERVICIO DE ENVÍO DE AUDIOS
    public void startSendingAudiosHandler(){
        Log.i("SERVICIO", "SERVICIO INICIADO");
        Log.i("HILO", "HILO SENDING: " + Thread.currentThread().getId());
        final Runnable r = new Runnable() {
            @Override
            public void run() {
                boolean res = false;
                int intents = NUMBER_OF_INTENTS;
                while(!res && intents > 0) {
                    try {
                        Log.i("INTENTOS RESTANTES: ", String.valueOf(intents));
                        File dir = new File(Environment.getExternalStorageDirectory().getPath() + "/audios/");
                        ConnectivityManager cm = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
                        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
                        if (activeNetwork != null && activeNetwork.isConnected()) {
                            FolderIterator folderIterator = new FolderIterator();
                            res = folderIterator.iteratingFolders(dir);
                        } else {
                            Log.e("CONEXIÓN", "NO HAY CONEXIÓN A INTERNET");
                        }
                        if(!res)
                            Thread.sleep(TIME_BETWEEN_INTENTS);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    intents--;
                }
            }
        };
        thread = new Thread(r);
        thread.start();
    }

}