package com.egypt.supporting.supportingonline.Custom;

import android.content.Context;
import android.content.SharedPreferences;

import com.egypt.supporting.supportingonline.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MySharedPref {


    public static void setdata(Context context,String key,String value){
        SharedPreferences preferences=  context.getSharedPreferences(context.getResources().getString(R.string.app_name),Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=preferences.edit();
        editor.putString(key,value);
        editor.commit();

    }

    public static String getdata(Context context,String key){
        SharedPreferences preferences=context.getSharedPreferences(context.getResources().getString(R.string.app_name),0);
        String restoredstring=preferences.getString(key,"");
        if (restoredstring.equals("null")){
            return "";
        }else {
            return restoredstring;
        }
    }

    public static boolean isWorkWithUs(Context context){
        boolean iswork=false;
        SharedPreferences preferences=context.getSharedPreferences(context.getResources().getString(R.string.app_name),0);
        iswork=preferences.getBoolean("work_with",false);

        return iswork;

    }

    public static void setWorkWith(Context context,boolean value){
        SharedPreferences preferences=  context.getSharedPreferences(context.getResources().getString(R.string.app_name),Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=preferences.edit();
        editor.putBoolean("work_with",value);
        editor.commit();

    }

    public static void addLsit(Context context,String id){
        JSONArray jsonArray=null;
        try {
             jsonArray=new JSONArray();
            JSONObject object=new JSONObject();
            object.put("id",id);
            jsonArray.put(object);
            setdata(context,"groups",jsonArray.toString());

        } catch (JSONException e) {
            e.printStackTrace();
        }




    }

    public static ArrayList<String> getAllList(Context context){

        ArrayList<String> list=new ArrayList<>();
        SharedPreferences preferences=context.getSharedPreferences(context.getResources().getString(R.string.app_name),0);
        String restoredstring=preferences.getString("noti","[]");
        if (!restoredstring.equals("[]")){
            try {
                JSONArray  jsonArray=new JSONArray(restoredstring);
                for (int i =0; i<jsonArray.length();i++){
                    JSONObject object=jsonArray.getJSONObject(i);
                    list.add(object.getString("id"));
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return list;
    }




}
