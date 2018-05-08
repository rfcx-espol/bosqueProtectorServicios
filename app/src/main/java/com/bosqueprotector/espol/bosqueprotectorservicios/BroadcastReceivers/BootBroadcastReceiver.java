package com.bosqueprotector.espol.bosqueprotectorservicios.broadcastReceivers;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.util.Log;
import com.bosqueprotector.espol.bosqueprotectorservicios.services.SendingAudiosService;

import static com.bosqueprotector.espol.bosqueprotectorservicios.utils.Identifiers.SENDING_AUDIO_TIME;
import static com.bosqueprotector.espol.bosqueprotectorservicios.utils.Identifiers.SERVICE_INTENT_AUDIO_SENDER;
import static com.bosqueprotector.espol.bosqueprotectorservicios.utils.Identifiers.SLEEP_TIME;
import static com.bosqueprotector.espol.bosqueprotectorservicios.utils.Identifiers.onService;
import static com.bosqueprotector.espol.bosqueprotectorservicios.utils.Identifiers.setPreferencesApplications;
import static com.bosqueprotector.espol.bosqueprotectorservicios.utils.Identifiers.alarmManager;
import static com.bosqueprotector.espol.bosqueprotectorservicios.utils.Identifiers.pendingIntent;

public class BootBroadcastReceiver extends BroadcastReceiver {
    String TAG = BootBroadcastReceiver.class.getSimpleName();
    @Override
    public void onReceive(Context context, Intent intent) {
        /*if (intent.getAction().equalsIgnoreCase(Intent.ACTION_BOOT_COMPLETED)) {
            SERVICE_INTENT_AUDIO_SENDER = new Intent(context, SendingAudiosService.class);
            context.startService(SERVICE_INTENT_AUDIO_SENDER);
        }*/

        //INICIAR EL SERVICIO
        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
            if (!onService) {
                setPreferencesApplications(context);
                pendingIntent = PendingIntent.getService(context, 0,
                        new Intent(context, SendingAudiosService.class), PendingIntent.FLAG_UPDATE_CURRENT);
                alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
                if (alarmManager != null) {
                    alarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime() + 1000,
                            SLEEP_TIME + SENDING_AUDIO_TIME, pendingIntent);
                    Log.d("ALARMA", "ALARMA CREADA DESPUÉS DE REINICIAR EL DISPOSITIVO");
                }
                onService = true;
            }
        }

    }
}