package com.bosqueprotector.espol.bosqueprotectorservicios.utils;

import android.os.Environment;
import android.os.StatFs;
import android.util.Log;
import java.io.File;
import java.io.IOException;
import java.util.regex.Pattern;
import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import static com.bosqueprotector.espol.bosqueprotectorservicios.utils.Identifiers.threadRunning;
import static com.bosqueprotector.espol.bosqueprotectorservicios.utils.Identifiers.call;

public class Utils {

    //MÉTODO QUE SUBE UN ARCHIVO AL SERVIDOR
    public static boolean uploadFile(String TAG, String url, File file ,OkHttpClient okHttpClient){
        String[] splitter = file.toString().split(Pattern.quote(File.separator));
        String filename = splitter[splitter.length-1];
        Log.d("ID_PHONE", String.valueOf(Identifiers.ID_PHONE));
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("filename", filename)
                .addFormDataPart("deviceId", Identifiers.ID_PHONE)
                .addFormDataPart("file", filename, RequestBody.create(MediaType.parse("application/octet-stream"), file))
                .build();
        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();

        call = okHttpClient.newCall(request);
        Response response = null;
        try {
            if(!threadRunning){
                return false;
            }
            response = call.execute();
            Log.i(TAG, "id del dispositivo :" + String.valueOf(Identifiers.ID_PHONE));
            Log.i(TAG, "id_applicacion :" + Identifiers.ID_APPLICATION);
            Log.i(TAG, "filename :" + filename);
            Log.i(TAG, "este es mensaje: " + response.body().string());
            return true;
        } catch(IOException e){
            e.printStackTrace();
            return false;
        }
    }

    //VERIFICAR SI HAY ESPACIO EN LA MEMORIA EXTERNA
    public static boolean getAvailableExternalMemorySize() {
        if (externalMemoryAvailable()) {
            File path = Environment.getExternalStorageDirectory();
            StatFs stat = new StatFs(path.getPath());
            long blockSize = stat.getBlockSize();
            long availableBlocks = stat.getAvailableBlocks();
            long size = availableBlocks * blockSize;
            Log.i("utils", "Available size in bytes: " + size);
            if (size < 10485760){ //10MB
                return false;
            }else{
                return true;
            }
        } else {
            return false;
        }
    }

    //RETORNAR LA MEMORIA EXTERNA
    private static boolean externalMemoryAvailable() {
        return android.os.Environment.getExternalStorageState().equals(
                android.os.Environment.MEDIA_MOUNTED);
    }

}