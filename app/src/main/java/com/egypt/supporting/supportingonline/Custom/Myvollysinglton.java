package com.egypt.supporting.supportingonline.Custom;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class Myvollysinglton {
    private static Myvollysinglton instance;
    private static RequestQueue requestQueue;
    private static Context mcontext;


    private Myvollysinglton(Context context){
        mcontext=context;
        requestQueue=getrequstque();
    }

    public RequestQueue getrequstque() {
        if (requestQueue==null){
            requestQueue= Volley.newRequestQueue(mcontext.getApplicationContext());

        }

        return  requestQueue;
    }
    public static synchronized Myvollysinglton getInstance(Context context){
        if (instance==null){
            instance=new Myvollysinglton(context);
        }
        return instance;
    }
    public<T> void addtorequst(Request<T> request){

       // cancel("req");
        //request.setTag("req");
       /* request.setRetryPolicy(new DefaultRetryPolicy(
                5000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));*/
        requestQueue.add(request);
    }
    
    public static void cancel(String tag){
        requestQueue.cancelAll(tag);
    }

}

