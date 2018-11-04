package com.bosqueprotector.espol.bosqueprotectorservicios.Utils;

import android.util.Log;
import java.io.File;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;
import okhttp3.OkHttpClient;
import static com.bosqueprotector.espol.bosqueprotectorservicios.Utils.Identifiers.ON_DESTROY_AUDIO;
import static com.bosqueprotector.espol.bosqueprotectorservicios.Utils.Identifiers.SLEEP_TIME;
import static com.bosqueprotector.espol.bosqueprotectorservicios.Utils.Identifiers.URL_SERVER;

public class FolderIterator {
    public static boolean threadRunning = true;

    public FolderIterator() {}

    //ENVÍA EL ARCHIVO MÁS ANTIGUO
    public boolean iteratingFolders(File dir) {
        if (dir.exists()) {
            File[] files = dir.listFiles();
            if(files.length > 1) {
                Arrays.sort(files);
                OkHttpClient.Builder builder = new OkHttpClient.Builder();
                builder.connectTimeout(SLEEP_TIME, TimeUnit.SECONDS)
                        .writeTimeout(SLEEP_TIME, TimeUnit.SECONDS)
                        .readTimeout(SLEEP_TIME, TimeUnit.SECONDS);
                OkHttpClient okHttpClient = builder.build();
                File file = files[0];
                Log.i("INFO", "INICIO DE ENVÍO DE ARCHIVO: " + file.toString());
                Utils.escribirEnLog("INFO - INICIO DE ENVÍO DE ARCHIVO");
                int respuesta = Utils.uploadFile(URL_SERVER, file, okHttpClient);
                if(respuesta == 200) {
                    //SI SE ENVIÓ EL AUDIO CORRECTAMENTE
                    Utils.escribirEnLog("INFO - ARCHIVO ENVIADO EXITOSAMENTE");
                    if (ON_DESTROY_AUDIO) {
                        boolean isDeleted = file.delete();
                        if (isDeleted) {
                            Log.i("INFO", "ARCHIVO BORRADO DEL DISPOSITIVO");
                            Utils.escribirEnLog("INFO - ARCHIVO BORRADO DEL DISPOSITIVO");
                        } else {
                            Log.i("INFO", "ARCHIVO NO BORRADO");
                            Utils.escribirEnLog("INFO - ARCHIVO NO BORRADO");
                        }
                    }
                    return true;
                } else if(respuesta == -1) {
                    //SI SE REINICIÓ EL SERVICIO Y EL ARCHIVO NO SE ENVIÓ SE ENVÍA TRUE PARA HACER
                    //QUE EL HILO SE CIERRE SOLO
                    return true;
                }
                return false;
            }
            return false;
        }
        return false;
    }

}