package com.egypt.supporting.supportingonline.Custom;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class MyRequest extends StringRequest {
    private String token;

    public MyRequest(String token,int method, String url, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        super(method, url, listener, errorListener);
        this.token=token;

    }


    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        Map<String,String> map=new HashMap<>();
        map.put("Authorization","Bearer "+token);
        map.put("X-Requested-With","XMLHttpRequst");
        map.put("Accept","application/json");
        return map;
    }
}
