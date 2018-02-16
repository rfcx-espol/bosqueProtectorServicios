package com.bosqueprotector.espol.bosqueprotectorservicios.Utils;

import android.util.Log;

import java.io.File;
import java.util.ArrayList;

import okhttp3.OkHttpClient;

import static com.bosqueprotector.espol.bosqueprotectorservicios.Utils.Identifiers.NUMBER_OF_INTENTS;
import static com.bosqueprotector.espol.bosqueprotectorservicios.Utils.Identifiers.ON_DESTROY_AUDIO;

/**
 * Created by joset on 23/01/2018.
 */


/*
*Class to implement an iterator over the folders
*
*
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
                //Log.i(TAG, "este es el archivo1 : " + file.toString());
                if (file.isDirectory()) {
                    iteratingFolders(TAG, url, file, okHttpClient);
                } else {
                        if(!routesRecurred.contains(file.toString())){
                            int intentsOfUpload = 0;
                            while (intentsOfUpload < NUMBER_OF_INTENTS ){
                                boolean respuesta = Utils.uploadFile(TAG, url, file, okHttpClient);
                                intentsOfUpload++;
                                Log.i(TAG, "trying to upload file "  + file.toString() + " ...");
                                if (respuesta) {
                                    Log.i(TAG, "file uploaded in intent " + intentsOfUpload+ ": " + file.toString());
                                    this.routesRecurred.add(file.toString());
                                    if (ON_DESTROY_AUDIO ){
                                        boolean isDeleted = file.delete();
                                        if (isDeleted){
                                            Log.i(TAG, "file deleted succesfully!");
                                        }else{
                                            Log.e(TAG, "file couldn't be deleted :(");
                                        }

                                    }
                                    break;

                                } else {

                                    Log.e(TAG, "something failed when trying to upload : " + file.toString() + " :(");
                                    try {
                                        Thread.sleep(5000);
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }


                                }
                                this.counter++;

                            }


                        }

                }
            }
        }

    }


}
