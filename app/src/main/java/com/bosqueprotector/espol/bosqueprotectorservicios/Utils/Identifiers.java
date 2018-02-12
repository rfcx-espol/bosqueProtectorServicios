package com.bosqueprotector.espol.bosqueprotectorservicios.Utils;

/**
 * Created by joset on 23/01/2018.
 */

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import java.util.Map;

public class Identifiers {

    //id of the apllication
    public static String ID_APPLICATION;
    // number of times an audio is intent to send to the server
    public static int NUMBER_OF_INTENTS;
    //time in seconds in which is intended to send the audios
    public static int SENDING_AUDIO_TIME;
    //boolean to determine if send or not send the audios (switch on or off)
    public static boolean IS_ON_SERVICE;
    //boolean to determine if to erase the audios when they are send
    public static boolean ON_DESTROY_AUDIO;
    // get if the sending audio service is bounded with the main activity
    public static boolean IS_BOUNDED_AUDIO_SERVICE = false;

   //these is the intent of service audio service, initilized  in the boot receiver
    public static Intent SERVICE_INTENT_AUDIO_SENDER;
    //these are the prefs of the sending audio service
    public static SharedPreferences PREFS_SETTINGS;

    //ip of the server
    //private  final String url ="http://200.126.1.156/gzip/UploadFile";
    //private  final String url ="http://10.0.2.2:3000/upload";
    //private  final String url ="http://10.10.1.126:3000/upload";
    public static  final String URL_SERVER ="http://192.168.100.91:3000/upload";


    public static void setIdApplication(Context context){
        ID_APPLICATION = Installation.id(context);
    }

    public static void setPreferencesApplications(Context context){
        PREFS_SETTINGS = PreferenceManager.getDefaultSharedPreferences(context);
        NUMBER_OF_INTENTS = Integer.parseInt(PREFS_SETTINGS.getString("numberOfIntents","3"));
        SENDING_AUDIO_TIME = Integer.parseInt(PREFS_SETTINGS.getString("sendingAudioTime", "600"));
        IS_ON_SERVICE = PREFS_SETTINGS.getBoolean("isOnService", true); //listo (a medias)
        ON_DESTROY_AUDIO = PREFS_SETTINGS.getBoolean("onDestroyAudio", true); //listo

        for ( Map.Entry<String,?> pref : PREFS_SETTINGS.getAll().entrySet() ) {
            Log.d("Utils inizialization" , pref.getKey() + ": " +pref.getValue().toString());
        }

    }






}
