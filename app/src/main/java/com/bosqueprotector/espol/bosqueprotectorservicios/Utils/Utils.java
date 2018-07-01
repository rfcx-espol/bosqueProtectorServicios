package com.bosqueprotector.espol.bosqueprotectorservicios.Utils;

import android.os.Environment;
import android.os.StatFs;
import android.util.Log;
import android.media.MediaMetadataRetriever;
import org.json.JSONObject;
import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.Date;
import java.text.SimpleDateFormat;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.Callback;
import static com.bosqueprotector.espol.bosqueprotectorservicios.Utils.Identifiers.threadRunning;
import static com.bosqueprotector.espol.bosqueprotectorservicios.Utils.Identifiers.call;

public class Utils {

    //MÉTODO QUE SUBE UN ARCHIVO AL SERVIDOR
    public static boolean uploadFile(String TAG, String url, File file ,OkHttpClient okHttpClient){
        Identifiers.ID_STATION = "";
        String[] splitter = file.toString().split(Pattern.quote(File.separator));
        String filename = splitter[splitter.length-1];
        MediaMetadataRetriever meta = new MediaMetadataRetriever();
        meta.setDataSource(file.toString());
        int duracion = Integer.parseInt(meta.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)) / 1000;
        Date d = new Date(Long.valueOf(filename.substring(0, filename.length() - 4)));
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        String fecha_grabacion = sdf.format(d);
        boolean responseId = getStationID(okHttpClient);
        if(responseId){
            return sendAudio(okHttpClient, url, filename, fecha_grabacion, duracion, file);
        } else {
            return false;
        }
    }

    //MÉTODO QUE ENVÍA EL APIKEY Y RECIBE EL ID DE LA ESTACIÓN EN LA BASE DE DATOS
    private static boolean getStationID(OkHttpClient okHttpClient){
        HttpUrl.Builder httpBuilder = HttpUrl.parse("http://200.126.14.250/api/Station").newBuilder();
        httpBuilder.addQueryParameter("APIKey", Identifiers.APIKey);
        Request request = new Request.Builder().url(httpBuilder.build()).build();
        call = okHttpClient.newCall(request);
        try {
            if(!threadRunning){
                return false;
            }
            Response response = call.execute();
            if(response.code() == 200){
                JSONObject obj = new JSONObject(response.body().string());
                if(obj.getString("APIKey").equals(Identifiers.APIKey)){
                    Identifiers.ID_STATION = obj.getString("Id");
                    response.body().close();
                    return true;
                }
                return false;
            } else {
                response.body().close();
                return false;
            }
        } catch(IOException e){
            e.printStackTrace();
            return false;
        } catch(org.json.JSONException je){
            je.printStackTrace();
            Log.d("ERROR", je.getMessage());
            return false;
        }
    }

    //MÉTODO QUE ENVÍA EL ID DE LA ESTACIÓN, EL APIKEY Y EL AUDIO CON SUS DATOS
    private static boolean sendAudio(OkHttpClient okHttpClient, String url, String filename, String fecha_grabacion, int duracion, File file){
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("Filename", filename)
                .addFormDataPart("Id", Identifiers.ID_STATION)
                .addFormDataPart("APIKey", Identifiers.APIKey)
                .addFormDataPart("RecordingDate", fecha_grabacion)
                .addFormDataPart("Duration", String.valueOf(duracion))
                .addFormDataPart("Format", filename.substring(filename.length() - 3, filename.length()))
                //.addFormDataPart("bitRate", meta.extractMetadata(MediaMetadataRetriever.METADATA_KEY_BITRATE))
                .addFormDataPart("File", filename, RequestBody.create(MediaType.parse("application/octet-stream"), file))
                .build();
        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();
        call = okHttpClient.newCall(request);
        Response response = null;
        try {
            if (!threadRunning) {
                return false;
            }
            response = call.execute();
            return true;
        } catch (IOException e) {
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