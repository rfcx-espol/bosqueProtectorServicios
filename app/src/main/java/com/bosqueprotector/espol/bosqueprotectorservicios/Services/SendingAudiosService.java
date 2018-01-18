package com.bosqueprotector.espol.bosqueprotectorservicios.Services;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Environment;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;


import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.File;
import java.util.Random;

/**
 * Created by joset on 16/01/2018.
 */

public class SendingAudiosService extends Service {

    /*
    * Tags
     */
    private  final String url ="http://200.126.1.156:5000/gzip/UploadFile";

    private static final String TAG = SendingAudiosService.class.getSimpleName();
    @Nullable
    //
    /**
     Binder given to clients, to bind the service to the
     */
    private final IBinder mBinder = new LocalBinder();

    /**
     * Class used for the client Binder.  Because we know this service always
     * runs in the same process as its clients, we don't need to deal with IPC.
     */
    public class LocalBinder extends Binder {
        public SendingAudiosService getService() {
            // Return this instance of LocalService so clients can call public methods
            return SendingAudiosService.this;
        }
    }

    @Nullable
    @Override
    /*
     * return my binder class
     */
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(TAG, "Received start id " + startId + ": " + intent);
        new Thread(new Runnable() {
            @Override
            public void run() {
                startSendingAudios();
            }
        }).start();
        return START_STICKY;
    }

    /**
     * Aqui estan implementados los metodos del cliente, aquellas cosas
     * Implemented client's methods
     */

    public void printImConnected(){
        Log.i(TAG,"esto es un print de estoy conectado" );
    }

    public void startSendingAudios(){
        iteratingFolders( new File(Environment.getExternalStorageDirectory().getPath()+ "/rfcx/audio"));

    }

    public void iteratingFolders (File dir) {
        if (dir.exists()) {
            File[] files = dir.listFiles();
            for (int i = 0; i < files.length; ++i) {
                File file = files[i];
                if (file.isDirectory()) {
                    iteratingFolders(file);
                } else {
                    Log.i(TAG, "este es el archivo : " +  file.toString());
                    boolean respuesta = sendHttpRequestIntentoEnvio(url, file);
                    if (respuesta){
                        Log.i(TAG,"funciono amiguito para: " + file.toString());
                    }else {
                        Log.i(TAG,"no funciono amiguito para: " + file.toString() + " :(");
                    }
                }
            }
        }
    }

    private boolean sendHttpRequestIntentoEnvio(String url, File file) {
        try {
            HttpClient httpclient = new DefaultHttpClient();

            HttpPost httppost = new HttpPost(url);

            MultipartEntity multipartEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
            multipartEntity.addPart("file", new FileBody(file));
            httppost.setEntity(multipartEntity);

            //InputStreamEntity reqEntity = new InputStreamEntity(
            //new FileInputStream(file), -1);
            //httppost.addHeader("Content-Encoding", "gzip");
            //Content-Type: multipart/form-data
            //reqEntity.setContentType("undefined");
            //httppost.addHeader("Content-Encoding", "gzip");
            //reqEntity.setChunked(true); // Send in multiple parts if needed
            //httppost.setEntity(reqEntity);
            HttpResponse response = httpclient.execute(httppost);
            Log.i(TAG, "look if its working: " + file.toString());

            return true;
            //Do something with response...

        } catch (Exception e) {

            Log.i(TAG, e.toString());

            return false;
            // show error
        }
    }




}
