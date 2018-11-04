package com.bosqueprotector.espol.bosqueprotectorservicios.Utils;

import android.os.Environment;
import android.os.StatFs;
import android.util.Log;
import android.media.MediaMetadataRetriever;
import org.json.JSONObject;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
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
import static com.bosqueprotector.espol.bosqueprotectorservicios.Utils.Identifiers.call;

public class Utils {

    //MÉTODO QUE SUBE UN ARCHIVO AL SERVIDOR
    public static int uploadFile(String url, File file ,OkHttpClient okHttpClient){
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
            return 0;
        }
    }

    //MÉTODO QUE ENVÍA EL APIKEY Y RECIBE EL ID DE LA ESTACIÓN EN LA BASE DE DATOS
    private static boolean getStationID(OkHttpClient okHttpClient){
        HttpUrl.Builder httpBuilder = HttpUrl.parse("http://200.126.14.250/api/Station").newBuilder();
        httpBuilder.addQueryParameter("APIKey", Identifiers.APIKey);
        Request request = new Request.Builder().url(httpBuilder.build()).build();
        call = okHttpClient.newCall(request);
        try {
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
            Utils.escribirEnLog("ERROR - " + e.getMessage());
            e.printStackTrace();
            return false;
        } catch(org.json.JSONException je){
            Utils.escribirEnLog("ERROR - " + je.getMessage());
            je.printStackTrace();
            return false;
        }
    }

    //MÉTODO QUE ENVÍA EL ID DE LA ESTACIÓN, EL APIKEY Y EL AUDIO CON SUS DATOS
    private static int sendAudio(OkHttpClient okHttpClient, String url, String filename, String fecha_grabacion, int duracion, File file){
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
            response = call.execute();
            Log.i("CÓDIGO DE RESPUESTA: ", String.valueOf(response.code()));
            Utils.escribirEnLog("INFO - CÓDIGO DE RESPUESTA: " + String.valueOf(response.code()));
            return response.code();
        } catch (IOException e) {
            Utils.escribirEnLog("ERROR - " + e.getMessage());
            e.printStackTrace();
            return -1;
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
            return size >= 30000000;
        }
        return false;
    }

    //RETORNAR LA MEMORIA EXTERNA
    public static boolean externalMemoryAvailable() {
        return android.os.Environment.getExternalStorageState().equals(
                android.os.Environment.MEDIA_MOUNTED);
    }

    //VERIFICA SI SE PUEDE ESCRIBIR EN LA MEMORIA EXTERNA
    public static boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state);
    }

    public static void escribirEnLog(String mensaje) {
        FileOutputStream fos;
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss.SSS");
        String fecha = sdf.format(new Date());
        String cadena = fecha + ": " + mensaje + "\n";
        try {
            fos = new FileOutputStream(Identifiers.log.getPath(), true);
            if(fos.getChannel().position() > 1000000) {
                fos.close();
                fos = new FileOutputStream(Identifiers.log.getPath(), false);
                fos.close();
                fos = new FileOutputStream(Identifiers.log.getPath(), true);
                fos.write(cadena.getBytes());
                fos.close();
            } else {
                fos.write(cadena.getBytes());
                fos.close();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}