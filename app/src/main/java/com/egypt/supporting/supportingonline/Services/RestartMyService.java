package com.egypt.supporting.supportingonline.Services;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;

import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;

public class RestartMyService {

    public static void CheckService(Context context){

        OnNewEmmitService onNewEmmi1 = new OnNewEmmitService();
       if (!isMyServiceRunning(context, onNewEmmi1.getClass())) {
          //  Toast.makeText(context, "not running", Toast.LENGTH_SHORT).show();
            Handler handler=new Handler(Looper.getMainLooper());
            handler.post(new Runnable() {
                @RequiresApi(api = Build.VERSION_CODES.O)
                @Override
                public void run() {
                 ContextCompat.startForegroundService( context,new Intent(context, OnNewEmmitService.class));
                   // context.startService(new Intent(context, OnNewEmmitService.class));
                }
            });


        }


    }

    private static boolean isMyServiceRunning(Context context,Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {

                return true;
            }
        }

        return false;
    }
}
