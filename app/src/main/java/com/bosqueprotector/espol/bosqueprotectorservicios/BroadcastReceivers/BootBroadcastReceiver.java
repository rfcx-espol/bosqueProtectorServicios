package com.bosqueprotector.espol.bosqueprotectorservicios.BroadcastReceivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.bosqueprotector.espol.bosqueprotectorservicios.Activities.MainActivity;
import com.bosqueprotector.espol.bosqueprotectorservicios.Services.SendingAudiosService;
import com.bosqueprotector.espol.bosqueprotectorservicios.Utils.Utils;

import java.util.Timer;
import java.util.TimerTask;

import static com.bosqueprotector.espol.bosqueprotectorservicios.Utils.Identifiers.SERVICE_INTENT_AUDIO_SENDER;

/**
 * Created by joset on 04/02/2018.
 */

public class BootBroadcastReceiver extends BroadcastReceiver {

    String TAG = BootBroadcastReceiver.class.getSimpleName();
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i(TAG, "Initializing broadcast...");
        if (intent.getAction().equalsIgnoreCase(Intent.ACTION_BOOT_COMPLETED)) {

            Log.i(TAG, "Initializing sending audio service...");
            SERVICE_INTENT_AUDIO_SENDER = new Intent(context, SendingAudiosService.class);
            context.startService(SERVICE_INTENT_AUDIO_SENDER/*.addFlags((Intent.FLAG_ACTIVITY_NEW_TASK))*/);



            /*
            Intent mainIntent = new Intent(context, MainActivity.class);
            context.startActivity(mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
            */
        }
    }
}
