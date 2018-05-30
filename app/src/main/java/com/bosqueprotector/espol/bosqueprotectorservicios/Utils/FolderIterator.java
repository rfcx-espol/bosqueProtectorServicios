package com.bosqueprotector.espol.bosqueprotectorservicios.Utils;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.bosqueprotector.espol.bosqueprotectorservicios.Activities.MainActivity;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

import okhttp3.OkHttpClient;
import static com.bosqueprotector.espol.bosqueprotectorservicios.Utils.Identifiers.NUMBER_OF_INTENTS;
import static com.bosqueprotector.espol.bosqueprotectorservicios.Utils.Identifiers.ON_DESTROY_AUDIO;
import static com.bosqueprotector.espol.bosqueprotectorservicios.Utils.Identifiers.SENDING_AUDIO_TIME;
import static com.bosqueprotector.espol.bosqueprotectorservicios.Utils.Identifiers.threadRunning;

public class FolderIterator {
    private ArrayList<String> routesRecurred;

    public FolderIterator() {
        this.routesRecurred = new ArrayList<>();
    }

    public FolderIterator(int counter, ArrayList<String> routesRecurred) {
        this.routesRecurred = routesRecurred;
    }

    //ITERA EN LAS CARPETAS DE LA CARPETA AUDIOS
    public void iteratingFolders(String TAG, String url, File dir, OkHttpClient okHttpClient) {
        long captureTimeStamp = System.currentTimeMillis();
        if (dir.exists()) {
            File[] files = dir.listFiles();
            Arrays.sort(files);
            for (int i = 0; i < files.length; i++) {
                File file = files[i];
                Log.i(TAG, "este es el archivo : " + file.toString());
                if (file.isDirectory()) {
                    iteratingFolders(TAG, url, file, okHttpClient);
                } else {
                    if(!routesRecurred.contains(file.toString())){
                        int intentsOfUpload = 0;
                        while (intentsOfUpload < NUMBER_OF_INTENTS ){
                            boolean respuesta = Utils.uploadFile(TAG, url, file, okHttpClient);
                            if(!threadRunning) {
                                Log.d("HILO", "HILO: " + Thread.currentThread().getId() + " ELIMINADO");
                                return;
                            }
                            intentsOfUpload++;
                            Log.i(TAG, "INTENTANDO SUBIR ARCHIVO "  + file.toString() + " ...");
                            if (respuesta) {
                                Log.i(TAG, "ARCHIVO SUBIDO EN INTENTO " + intentsOfUpload+ ": " + file.toString());
                                this.routesRecurred.add(file.toString());
                                if (ON_DESTROY_AUDIO ){
                                    boolean isDeleted = file.delete();
                                    if (isDeleted){
                                        Log.i(TAG, "ARCHIVO BORRADO EXITOSAMENTE, HILO: " + Thread.currentThread().getId());
                                    }else{
                                        Log.e(TAG, "ARCHIVO NO BORRADO, HILO: " + Thread.currentThread().getId());
                                    }
                                }
                                break;
                            } else {
                                Log.e(TAG, "ERROR AL SUBIR EL ARCHIVO " + file.toString());
                                try {
                                    Thread.sleep(5000);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    public void directorios(File dir){
        File[] files = dir.listFiles();
        Arrays.sort(files);
        for(int i = 0; i < files.length; i++){
            File file = files[i];
            Log.d("DIRECTORIOS: ", file.toString());
        }
    }

}