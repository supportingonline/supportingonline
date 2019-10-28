package com.egypt.supporting.supportingonline.Services;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.SystemClock;

import com.egypt.supporting.supportingonline.Custom.MySharedPref;
import com.egypt.supporting.supportingonline.Custom.NotificationMessage;
import com.egypt.supporting.supportingonline.R;
import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;

public class OnNewEmmitService extends Service {

    private Socket mSocket;
    private String inchact;

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.


        return null;
    }

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
    }

    @Override
    public void onCreate() {
        super.onCreate();

        Handler handler=new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {


            }
        });
        try {
            mSocket = IO.socket(getApplicationContext().getResources().getString(R.string.socket));
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        mSocket.connect();




    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

       inchact= MySharedPref.getdata(getApplicationContext(),"inchat");

        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {

            @Override
            public void run() {
             // Toast.makeText(OnNewEmmitService.this.getApplicationContext(),"start",Toast.LENGTH_SHORT).show();


            }
        });

       mSocket.on("new message", on_new_message);
       mSocket.on("supportingonline", on_new_change);
        return START_STICKY;
    }



    @Override
    public void onDestroy() {
        super.onDestroy();
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {

            @Override
            public void run() {
               // Toast.makeText(OnNewEmmitService.this.getApplicationContext(),"destroy",Toast.LENGTH_SHORT).show();
                RestartMyService.CheckService(getApplicationContext());

            }
        });

    }

    Emitter.Listener on_new_message = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {

                    JSONObject data = (JSONObject) args[0];


                 //   Log.i("emit","listen");
                   // Log.i("emit",data.toString());
                    try {
                        String name=data.getString("user");
                        String fromId=data.getString("from");
                        String toId=data.getString("to");
                        String msg=data.getString("message");
                        String image=data.getString("image");
                        if (toId.equals(MySharedPref.getdata(OnNewEmmitService.this.getApplicationContext(),"id"))){

                            Handler handler = new Handler(Looper.getMainLooper());
                            handler.post(new Runnable() {

                                @Override
                                public void run() {


                                    if (!inchact.equals("in")) {

                                        //  Toast.makeText(OnNewEmmitService.this.getApplicationContext(),"emmit",Toast.LENGTH_SHORT).show();
                                        NotificationMessage.onNewMessage(OnNewEmmitService.this.getApplicationContext(),
                                                name, msg, fromId, image);
                                    }
                                }
                            });
                        }
                        //  Log.i("emit",msg);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


        }
    };
    Emitter.Listener on_new_change = args -> {

        JSONObject data = (JSONObject) args[0];



        try {
            String theType=data.getString("type");
            if (theType.equals("group")) {
                String thegroupid = data.getString("group_id");
                String theuserId = data.getString("user_id");

            }else if (theType.equals("friend")){
                String status=data.getString("status");
                String toId=data.getString("to");
                String fromId=data.getString("from");
                String image=data.getString("img");
                String name=data.getString("name");
                if (toId.equals(MySharedPref.getdata(OnNewEmmitService.this.getApplicationContext(),"id")) && status.equals("friend")){
                    Handler handler = new Handler(Looper.getMainLooper());
                    handler.post(new Runnable() {

                        @Override
                        public void run() {
                            NotificationMessage.onNewMessage(OnNewEmmitService.this.getApplicationContext(),
                                    OnNewEmmitService.this.getApplicationContext().getResources().getString(R.string.friendrequset)
                                    , name, fromId, image);

                        }
                    });


                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }




    };




    @Override
    public void onTaskRemoved(Intent rootIntent) {
        Intent restartServiceIntent = new Intent(getApplicationContext(), this.getClass());
        restartServiceIntent.setPackage(getPackageName());

        PendingIntent restartServicePendingIntent =
                PendingIntent.getService(getApplicationContext(), 1, restartServiceIntent, PendingIntent.FLAG_ONE_SHOT);
        AlarmManager alarmService = (AlarmManager) getApplicationContext().getSystemService(Context.ALARM_SERVICE);
        alarmService.set(
                AlarmManager.ELAPSED_REALTIME,
                SystemClock.elapsedRealtime() + 1000,
                restartServicePendingIntent);

        super.onTaskRemoved(rootIntent);
    }




}
