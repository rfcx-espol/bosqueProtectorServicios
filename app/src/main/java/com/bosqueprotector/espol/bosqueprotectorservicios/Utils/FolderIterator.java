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
    /* RETURN 0 SI EL ARCHIVO NO SE ENVIÓ CORRECTAMENTE
    *  RETURN 1 SI EL ARCHIVO SE ENVIÓ CORRECTAMENTE
    *  RETURN 2 SI EL SERVICIO FUE REINICIADO
    *  RETURN 3 SI NO HAY ARCHIVOS LISTOS PARA ENVIAR
    *  RETURN 4 SI EL DIRECTORIO DE ARCHIVOS NO EXISTE */
    public int iteratingFolders(File dir) {
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
                int respuesta = Utils.uploadFile(URL_SERVER, file, okHttpClient);
                if(respuesta == 200) {
                    //SI SE ENVIÓ EL AUDIO CORRECTAMENTE
                    Log.i("INFO", "ARCHIVO ENVIADO EXITOSAMENTE: " + file.toString());
                    Utils.escribirEnLog("INFO - ARCHIVO ENVIADO EXITOSAMENTE: " + file.toString());
                    if (ON_DESTROY_AUDIO) {
                        boolean isDeleted = file.delete();
                        if (isDeleted) {
                            Log.i("INFO", "ARCHIVO BORRADO DEL DISPOSITIVO: " + file.toString());
                            Utils.escribirEnLog("INFO - ARCHIVO BORRADO DEL DISPOSITIVO: " + file.toString());
                        } else {
                            Log.i("INFO", "ARCHIVO NO BORRADO: " + file.toString());
                            Utils.escribirEnLog("INFO - ARCHIVO NO BORRADO: " + file.toString());
                        }
                    }
                    return 1;
                } else if(respuesta == -1) {
                    //SI SE REINICIÓ EL SERVICIO Y EL ARCHIVO NO SE ENVIÓ SE ENVÍA 2 PARA HACER
                    //QUE EL HILO SE CIERRE SOLO
                    return 2;
                }
                return 0;
            } else {
                Log.e("ERROR", "NO HAY ARCHIVOS LISTOS PARA ENVIAR. SE ESPERARÁ AL SIGUIENTE ENVÍO");
                Utils.escribirEnLog("ERROR - NO HAY ARCHIVOS LISTOS PARA ENVIAR. SE ESPERARÁ AL SIGUIENTE ENVÍO");
                return 3;
            }
        }
        Log.e("ERROR", "EL DIRECTORIO DE ARCHIVOS NO EXISTE");
        Utils.escribirEnLog("ERROR - EL DIRECTORIO DE ARCHIVOS NO EXISTE");
        return 4;
    }

}