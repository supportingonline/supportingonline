package com.egypt.supporting.supportingonline.Services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class StartMyServiceAtBootReciever extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        //Toast.makeText(context, "BoroadCast", Toast.LENGTH_SHORT).show();
        RestartMyService.CheckService(context);
    }
}
