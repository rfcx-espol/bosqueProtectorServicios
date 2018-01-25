package com.bosqueprotector.espol.bosqueprotectorservicios.Utils;

import android.util.Log;

import java.io.File;
import java.util.ArrayList;

import okhttp3.OkHttpClient;

/**
 * Created by joset on 23/01/2018.
 */

public class FolderIterator {

    private int counter;
    private ArrayList<String> routesRecurred;

    public FolderIterator() {
        this.counter = 0;
        this.routesRecurred = new ArrayList<String>();
    }

    public FolderIterator(int counter, ArrayList<String> routesRecurred) {
        this.counter = counter;
        this.routesRecurred = routesRecurred;
    }

    public void iteratingFolders(String TAG, String url, File dir, OkHttpClient okHttpClient) {

        if (dir.exists()) {
            File[] files = dir.listFiles();
            for (int i = 0; i < files.length; i++) {
                File file = files[i];
                Log.i(TAG, "este es el archivo1 : " + file.toString());
                if (file.isDirectory()) {
                    iteratingFolders(TAG, url, file, okHttpClient);
                } else {
                    //while (this.counter < 7) {
                        //Log.i(TAG, "este es el archivo2 : " + file.toString());
                        //boolean respuesta = sendHttpRequestIntentoEnvio(url, file);
                        if(!routesRecurred.contains(file.toString())){
                            int intentsOfUpload = 0;
                            while (intentsOfUpload < 3){
                                boolean respuesta = Utils.uploadFile(TAG, url, file, okHttpClient);
                                intentsOfUpload++;
                                if (respuesta) {
                                    Log.i(TAG, "file uploaded in intent " + intentsOfUpload+ ": " + file.toString());
                                    this.routesRecurred.add(file.toString());
                                    break;
                                /*
                                boolean isDeleted = file.delete();
                                if (isDeleted){
                                    Log.i(TAG, "se elimino");
                                }else{
                                    Log.i(TAG, "no se elimino");
                                }
                                */

                                } else {

                                    Log.i(TAG, "no funciono amiguito para: " + file.toString() + " :(");
                                    try {
                                        Thread.sleep(5000);
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }


                                }
                                Log.i(TAG, "el contador: " + counter);
                                this.counter++;

                            }


                        }

                    //}
                }
            }
        }

    }

    public void iteratingFolders2(String TAG, String url, File dir, OkHttpClient okHttpClient) {
        if (dir.exists()) {
            File[] files = dir.listFiles(); //give month folders
            for (int i = 0; i <files.length; i++){
                File[] files2 = files[i].listFiles(); //inside it is each file
                for (int j = 0; j <files2.length;j++){
                    File[] files3 = files2[j].listFiles();
                    for (int k = 0; k < files3.length; k++){
                        File specificFile = files3[k];
                        //while (this.counter < 7) {
                            Log.i(TAG, "este es el archivo2 : " + specificFile.toString());
                            //boolean respuesta = sendHttpRequestIntentoEnvio(url, file);
                            if(!routesRecurred.contains(specificFile.toString())){
                                boolean respuesta = Utils.uploadFile(TAG, url, specificFile, okHttpClient);
                                if (respuesta) {
                                    Log.i(TAG, "file uploaded: " + specificFile.toString());
                                    this.routesRecurred.add(specificFile.toString());
                                    /*
                                    boolean isDeleted = specificFile.delete();
                                    if (isDeleted){
                                        Log.i(TAG, "se elimino");
                                    }else{
                                        Log.i(TAG, "no se elimino");
                                    }
                                    */

                                } else {

                                    Log.i(TAG, "no funciono amiguito para: " + specificFile.toString() + " :(");
                                }
                                this.counter++;
                                Log.i(TAG, "el contador: " + counter);
                                //this.counter++;

                            }

                        //}

                    }

                }
            }
        }

    }

}
