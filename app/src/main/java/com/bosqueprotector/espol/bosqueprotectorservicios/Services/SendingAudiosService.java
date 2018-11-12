package com.bosqueprotector.espol.bosqueprotectorservicios.Services;

import android.annotation.SuppressLint;
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
    @SuppressLint("InvalidWakeLockTag")
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
        final Runnable r = new Runnable() {
            @Override
            public void run() {
                int res = 0;
                int intents = NUMBER_OF_INTENTS;
                while(res == 0 && intents > 0) {
                    try {
                        File dir = new File(Environment.getExternalStorageDirectory().getPath() + "/audios/");
                        ConnectivityManager cm = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
                        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
                        intents--;
                        if (activeNetwork != null && activeNetwork.isConnected()) {
                            Log.i("INFO", "CONEXIÓN ESTABLECIDA DESPUÉS DE: " + (3 - intents) + " INTENTO(S)");
                            Utils.escribirEnLog("INFO - CONEXIÓN ESTABLECIDA DESPUÉS DE: " + (3 - intents) + " INTENTO(S)");
                            FolderIterator folderIterator = new FolderIterator();
                            res = folderIterator.iteratingFolders(dir);
                            if(res == 0 || res == 3)
                                Thread.sleep(TIME_BETWEEN_INTENTS);
                        } else {
                            if(intents == 0) {
                                Log.e("ERROR", "NO HAY CONEXIÓN A INTERNET. SE ESPERARÁ AL SIGUIENTE ENVÍO");
                                Utils.escribirEnLog("ERROR - NO HAY CONEXIÓN A INTERNET. SE ESPERARÁ AL SIGUIENTE ENVÍO");
                            }
                        }
                    } catch (InterruptedException e) {
                        Utils.escribirEnLog("ERROR - " + e.getMessage());
                        e.printStackTrace();
                    }
                }
            }
        };
        thread = new Thread(r);
        thread.start();
    }

}