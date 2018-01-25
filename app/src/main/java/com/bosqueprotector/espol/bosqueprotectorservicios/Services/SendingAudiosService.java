package com.bosqueprotector.espol.bosqueprotectorservicios.Services;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Environment;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;


import com.bosqueprotector.espol.bosqueprotectorservicios.Utils.FolderIterator;
import com.bosqueprotector.espol.bosqueprotectorservicios.Utils.Identifiers;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.regex.Pattern;

import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by joset on 16/01/2018.
 */

public class SendingAudiosService extends Service {

    /*
    * Tags
     */
    private  final String url ="http://200.126.1.156:5000/gzip/UploadFile";
    //private  final String url ="http://10.0.2.2:3000/upload";
    //private  final String url ="http://10.10.1.129:3000/upload";
    private int counter = 0;
    private static final String TAG = SendingAudiosService.class.getSimpleName();
    OkHttpClient okHttpClient = new OkHttpClient();
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
        Log.i(TAG, "esta es carpeta: " + Environment.getExternalStorageDirectory().getPath()+ "/rfcx/audio");
        //iteratingFolders( new File(Environment.getExternalStorageDirectory().getPath()+ "/rfcx/audio"));
        FolderIterator folderIterator = new FolderIterator();
        folderIterator.iteratingFolders(TAG,url, new File(Environment.getExternalStorageDirectory().getPath()+ "/rfcx/audio"),okHttpClient);

    }
    /*
    public void iteratingFolders (File dir) {

        if (dir.exists()) {
            File[] files = dir.listFiles();
            for (int i = 0; i < files.length; ++i) {
                File file = files[i];
                Log.i(TAG, "este es el archivo1 : " + file.toString());
                if (file.isDirectory()) {
                    iteratingFolders(file);
                } else {
                    while (counter < 7) {
                        Log.i(TAG, "este es el archivo2 : " + file.toString());
                        //boolean respuesta = sendHttpRequestIntentoEnvio(url, file);
                        boolean respuesta = uploadFile(url, file);
                        if (respuesta) {
                            Log.i(TAG, "file uploaded: " + file.toString());

                        } else {

                            Log.i(TAG, "no funciono amiguito para: " + file.toString() + " :(");
                        }
                        Log.i(TAG, "el contador: " + counter);
                        counter++;
                    }
                }
            }
        }
    }
    */
    /*
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
    */
    /*
    public boolean uploadFile(String url, File file){

        String[] splitter = file.toString().split(Pattern.quote(File.separator));
        String filename = splitter[splitter.length-1];

        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("nombreArchivo", filename)
                .addFormDataPart("idApplication", Identifiers.ID_APPLICATION)
                .addFormDataPart("file", filename, RequestBody.create(MediaType.parse("application/octet-stream"), file))
                .build();
        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();

        Call call = okHttpClient.newCall(request);
        Response response = null;
        try {
            response = call.execute();
            Log.i(TAG, "este es mensaje: " + response.body().string());
            return true;

        }catch(IOException e){
            e.printStackTrace();
            return false;
        }
    }
    */







}
