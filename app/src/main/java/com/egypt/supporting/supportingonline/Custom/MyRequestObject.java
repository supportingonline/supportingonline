package com.egypt.supporting.supportingonline.Custom;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MyRequestObject extends JsonObjectRequest {
    private String token;

    public MyRequestObject(String token,int method, String url, JSONObject jsonRequest, Response.Listener<JSONObject> listener, Response.ErrorListener errorListener) {
        super(method, url, jsonRequest, listener, errorListener);
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
