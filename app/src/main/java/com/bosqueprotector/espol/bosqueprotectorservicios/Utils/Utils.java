package com.bosqueprotector.espol.bosqueprotectorservicios.Utils;

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
}
