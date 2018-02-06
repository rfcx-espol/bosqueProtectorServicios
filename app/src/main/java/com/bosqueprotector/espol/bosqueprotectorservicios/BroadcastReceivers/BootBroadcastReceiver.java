package com.bosqueprotector.espol.bosqueprotectorservicios.BroadcastReceivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.bosqueprotector.espol.bosqueprotectorservicios.Services.SendingAudiosService;

/**
 * Created by joset on 04/02/2018.
 */

public class BootBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equalsIgnoreCase(Intent.ACTION_BOOT_COMPLETED)) {
            Intent serviceIntent = new Intent(context, SendingAudiosService.class);
            context.startService(serviceIntent);
        }
    }
}
