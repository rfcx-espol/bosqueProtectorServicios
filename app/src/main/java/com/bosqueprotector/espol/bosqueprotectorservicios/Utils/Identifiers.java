package com.bosqueprotector.espol.bosqueprotectorservicios.Utils;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.util.Log;
import java.util.Map;
import okhttp3.Call;

public class Identifiers {
    //ID DE LA ESTACIÓN PARA AUTENTICACIÓN EN EL SERVIDOR
    public static String APIKey;
    //ID DE LA APLICACIÓN
    public static String ID_APPLICATION;
    //ID DE LA ESTACIÓN EN LA BASE DE DATOS
    public static String ID_STATION;
    //NÚMERO DE VECES QUE EL AUDIO INTENTA ENVIARSE AL SERVIDOR
    public static int NUMBER_OF_INTENTS;
    //TIEMPO EN SEGUNDOS EN QUE SE INTENTA ENVIAR EL AUDIO ANTES DE REINICIAR
    public static int SENDING_AUDIO_TIME;
    //TIEMPO EN SEGUNDOS EN QUE LA APP NO ESTÁ CORRIENDO
    public static int SLEEP_TIME;
    //DETERMINA SI SE ENVÍAN AUDIOS O NO
    public static boolean IS_ON_SERVICE;
    //DETERMINA SI SE BORRAN LOS ARCHIVOS DEL DISPOSITIVO UNA VEZ ENVIADOS
    public static boolean ON_DESTROY_AUDIO;
    //DETERMINA SI SE UNEN EL SERVICIO CON LA ACTIVIDAD PRINCIPAL
    public static boolean IS_BOUNDED_AUDIO_SERVICE = false;
    //INTENT DEL SERVICIO, INICIALIZADO EN EL BOOT RECEIVER
    public static Intent SERVICE_INTENT_AUDIO_SENDER;
    //PREFERENCIAS DEL SERVICIO
    public static SharedPreferences PREFS_SETTINGS;
    public static boolean onService = false;
    public static AlarmManager alarmManager;
    public static PendingIntent pendingIntent;
    public static boolean threadRunning = true;
    public static Call call;
    //DIRECCIÓN IP DEL SERVIDOR
    public static final String URL_SERVER ="http://200.126.14.250/File/UploadFile";

    //ESTABLECE EL ID DEL DISPOSITIVO
    @SuppressLint("HardwareIds")
    public static void setAPIKey(Context context){
        APIKey = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
    }

    //ESTABLECE EL ID DE LA APLICACIÓN
    public static void setIdApplication(Context context){
        ID_APPLICATION = Installation.id(context);
    }

    //ESTABLECE LAS PREFERENCIAS DEL SERVICIO
    public static void setPreferencesApplications(Context context){
        PREFS_SETTINGS = PreferenceManager.getDefaultSharedPreferences(context);
        NUMBER_OF_INTENTS = Integer.parseInt(PREFS_SETTINGS.getString("numberOfIntents","3"));
        SENDING_AUDIO_TIME = Integer.parseInt(PREFS_SETTINGS.getString("sendingAudioTime", "300")) * 1000;
        SLEEP_TIME = Integer.parseInt(PREFS_SETTINGS.getString("sleepTime", "300")) * 1000;
        IS_ON_SERVICE = PREFS_SETTINGS.getBoolean("isOnService", true);
        ON_DESTROY_AUDIO = PREFS_SETTINGS.getBoolean("onDestroyAudio", true);

        for ( Map.Entry<String,?> pref : PREFS_SETTINGS.getAll().entrySet() ) {
            Log.d("CONFIGURACIÓN ACTUAL" , pref.getKey() + ": " +pref.getValue().toString());
        }
    }
}