package com.bosqueprotector.espol.bosqueprotectorservicios.BroadcastReceivers;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.os.SystemClock;
import android.util.Log;
import com.bosqueprotector.espol.bosqueprotectorservicios.Services.SendingAudiosService;
import com.bosqueprotector.espol.bosqueprotectorservicios.Utils.Identifiers;
import com.bosqueprotector.espol.bosqueprotectorservicios.Utils.Utils;

import java.io.File;

import static com.bosqueprotector.espol.bosqueprotectorservicios.Utils.Identifiers.SLEEP_TIME;
import static com.bosqueprotector.espol.bosqueprotectorservicios.Utils.Identifiers.onService;
import static com.bosqueprotector.espol.bosqueprotectorservicios.Utils.Identifiers.setPreferencesApplications;
import static com.bosqueprotector.espol.bosqueprotectorservicios.Utils.Identifiers.alarmManager;
import static com.bosqueprotector.espol.bosqueprotectorservicios.Utils.Identifiers.pendingIntent;

public class BootBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        //INICIAR EL SERVICIO
        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
            if (!onService) {
                setPreferencesApplications(context);
                pendingIntent = PendingIntent.getService(context, 0,
                        new Intent(context, SendingAudiosService.class), PendingIntent.FLAG_UPDATE_CURRENT);
                alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
                if (alarmManager != null) {
                    alarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime() + 1000,
                            SLEEP_TIME, pendingIntent);
                    Identifiers.log = new File(Environment.getExternalStorageDirectory(), "Log - BosqueProtector.txt");
                    Utils.escribirEnLog("INFO - SERVICIO REINICIADO DESPUÉS DE REINICIAR EL DISPOSITIVO");
                    Log.i("INFO", "SERVICIO REINICIADO DESPUÉS DE REINICIAR EL DISPOSITIVO");
                }
                onService = true;
            }
        }

    }
}