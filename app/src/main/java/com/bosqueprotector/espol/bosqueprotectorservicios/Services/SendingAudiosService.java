package com.bosqueprotector.espol.bosqueprotectorservicios.services;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Environment;
import android.os.IBinder;
import android.os.PowerManager;
import android.support.annotation.Nullable;
import android.util.Log;
import com.bosqueprotector.espol.bosqueprotectorservicios.utils.FolderIterator;
import java.io.File;
import okhttp3.OkHttpClient;
import static com.bosqueprotector.espol.bosqueprotectorservicios.utils.Identifiers.URL_SERVER;
import static com.bosqueprotector.espol.bosqueprotectorservicios.utils.Identifiers.setIdApplication;
import static com.bosqueprotector.espol.bosqueprotectorservicios.utils.Identifiers.setIdPhone;
import static com.bosqueprotector.espol.bosqueprotectorservicios.utils.Identifiers.setPreferencesApplications;
import static com.bosqueprotector.espol.bosqueprotectorservicios.utils.Identifiers.SENDING_AUDIO_TIME;
import static com.bosqueprotector.espol.bosqueprotectorservicios.utils.Identifiers.threadRunning;

public class SendingAudiosService extends Service {
    private static final String TAG = SendingAudiosService.class.getSimpleName();
    OkHttpClient okHttpClient = new OkHttpClient();
    private final IBinder mBinder = new LocalBinder();
    public static PowerManager.WakeLock wakeLock;

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
        setIdApplication(getApplicationContext());
        setIdPhone(getApplicationContext());
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
        return Service.START_STICKY;
    }

    //DESTRUIR EL SERVICIO DE ENVÍO DE AUDIOS
    @Override
    public void onDestroy() {
        super.onDestroy();
        wakeLock.release();
    }

    public void printImConnected(){
        Log.i(TAG,"CONEXIÓN ESTABLECIDA" );
    }

    //INICIO DEL SERVICIO DE ENVÍO DE AUDIOS
    public void startSendingAudiosHandler(){
        Log.d("SERVICIO", "SERVICIO INICIADO");
        threadRunning = true;
        final Runnable r = new Runnable() {
            @Override
            public void run() {
                FolderIterator folderIterator = new FolderIterator();
                folderIterator.iteratingFolders(TAG,URL_SERVER, new File(Environment.getExternalStorageDirectory().getPath()+ "/audios/"),okHttpClient);
            }
        };
        Thread newThread = new Thread(r);
        newThread.start();
    }

}