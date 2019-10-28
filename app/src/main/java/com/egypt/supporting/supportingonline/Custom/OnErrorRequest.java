package com.egypt.supporting.supportingonline.Custom;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.egypt.supporting.supportingonline.Calls.ErrorCall;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;

public class OnErrorRequest implements Response.ErrorListener {

    private Context context;
    private ErrorCall errorCall;

    public OnErrorRequest(Context context,ErrorCall errorCall) {
        this.context = context;
        this.errorCall=errorCall;
    }

    @Override
    public void onErrorResponse(VolleyError error) {
      //  Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show();
        errorCall.OnBack();

        if (error.networkResponse!=null) {
            try {
                String body = new String(error.networkResponse.data, StandardCharsets.UTF_8);
                JSONObject object = new JSONObject(body);

                 Log.i("main_error",body);
                if (object.has("message")) {
                    if (object.getString("message").equals("Unauthenticated.")){
                         MySharedPref.setdata(context, "token", "");
                         MyUtils.logoutprovider(context);
                    }else {
                        Toast.makeText(context, object.getString("message"), Toast.LENGTH_SHORT).show();
                        errorCall.OnBack();
                    }


                } else {
                 errorCall.OnBack();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }else {
            Toast.makeText(context, "Connection Error", Toast.LENGTH_SHORT).show();
        }


    }
}
