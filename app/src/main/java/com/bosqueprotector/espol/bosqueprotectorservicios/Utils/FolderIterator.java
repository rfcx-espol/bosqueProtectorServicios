package com.bosqueprotector.espol.bosqueprotectorservicios.Utils;

import android.util.Log;
import java.io.File;
import java.util.Arrays;
import okhttp3.OkHttpClient;
import static com.bosqueprotector.espol.bosqueprotectorservicios.Utils.Identifiers.ON_DESTROY_AUDIO;
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
                OkHttpClient okHttpClient = new OkHttpClient();
                File file = files[0];
                Log.i("SUBIENDO ARCHIVO", file.toString());
                Log.i("HILO", "HILO FOLDERS: " + Thread.currentThread().getId());
                int respuesta = Utils.uploadFile(URL_SERVER, file, okHttpClient);
                if(respuesta == 200) {
                    //SI SE ENVIÓ EL AUDIO CORRECTAMENTE
                    if (ON_DESTROY_AUDIO) {
                        boolean isDeleted = file.delete();
                        if (isDeleted) {
                            Log.i("BORRADO", "ARCHIVO BORRADO EXITOSAMENTE");
                        } else {
                            Log.i("BORRADO", "ARCHIVO NO BORRADO");
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