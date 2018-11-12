package com.bosqueprotector.espol.bosqueprotectorservicios.BroadcastReceivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import com.bosqueprotector.espol.bosqueprotectorservicios.Utils.Utils;

public class BatteryLevelReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        //DETECTAR UN NIVEL DE BATERÍA BAJO
        if (intent.getAction().equals("android.intent.action.BATTERY_LOW")) {
            Utils.escribirEnLog("INFO - NIVEL DE BATERÍA BAJO");
        } else if(intent.getAction().equals("android.intent.action.BATTERY_OKAY")) {
            Utils.escribirEnLog("INFO - NIVEL DE BATERÍA VUELVE A UN NIVEL ACEPTABLE");
        }

    }

}
