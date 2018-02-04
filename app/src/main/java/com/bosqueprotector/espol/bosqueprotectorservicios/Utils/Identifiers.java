package com.bosqueprotector.espol.bosqueprotectorservicios.Utils;

/**
 * Created by joset on 23/01/2018.
 */

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class Identifiers {

    public static String ID_APPLICATION;
    public static int NUMBER_OF_INTENTS;
    public static int SLEEP_TIME;
    public static int SENDING_AUDIO_TIME;
    public static boolean IS_ON_SERVICE;
    public static boolean ON_DESTROY_AUDIO;
    public static boolean IS_BOUNDED_AUDIO_SERVICE = false;
    //public static

    public static void setIdApplication(Context context){
        ID_APPLICATION = Installation.id(context);
    }

    public static void setPreferencesApplications(Context context){
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
        NUMBER_OF_INTENTS = Integer.parseInt(settings.getString("numberOfIntents","3"));
        SLEEP_TIME = Integer.parseInt(settings.getString("sleepTime", "600"));
        SENDING_AUDIO_TIME = Integer.parseInt(settings.getString("sendingAudioTime", "600"));
        IS_ON_SERVICE = settings.getBoolean("isOnService", true); //listo (a medias)
        ON_DESTROY_AUDIO = settings.getBoolean("onDestroyAudio", true); //listo

    }






}
