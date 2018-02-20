package com.bosqueprotector.espol.bosqueprotectorservicios.Utils;

import android.content.Context;
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

/**
 * Created by joset on 23/01/2018.
 */

public class Utils {

        /*
    * These method is the core, is the one that upload the file to the server
     */


    public static boolean uploadFile(String TAG, String url, File file ,OkHttpClient okHttpClient){

        String[] splitter = file.toString().split(Pattern.quote(File.separator));
        String filename = splitter[splitter.length-1];

        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("filename", filename)
                .addFormDataPart("deviceId", Identifiers.ID_APPLICATION)
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
            Log.i(TAG, "este es :" + Identifiers.ID_APPLICATION);
            Log.i(TAG, "este es mensaje: " + response.body().string());
            return true;

        }catch(IOException e){
            e.printStackTrace();
            return false;
        }
    }

    /*
    * These method initialize all the constants at the beggining of the service
     */

    public static void initializingVariables(Context context){
        Identifiers.setIdApplication(context);
        Identifiers.setPreferencesApplications(context);
    }
    /*
    * These methods return available external size
    */

    public static boolean getAvailableExternalMemorySize() {
        if (externalMemoryAvailable()) {
            File path = Environment.getExternalStorageDirectory();
            StatFs stat = new StatFs(path.getPath());
            long blockSize = stat.getBlockSize();
            long availableBlocks = stat.getAvailableBlocks();
            long size = availableBlocks * blockSize;
            Log.i("Utils", "Available size in bytes: " + size);
            if (size < 10485760){ //10MB
                return false;
            }else{
                return true;
            }
        } else {
            return false;
        }
    }

    public static boolean externalMemoryAvailable() {
        return android.os.Environment.getExternalStorageState().equals(
                android.os.Environment.MEDIA_MOUNTED);
    }


}
