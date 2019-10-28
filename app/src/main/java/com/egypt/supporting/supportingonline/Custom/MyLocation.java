package com.egypt.supporting.supportingonline.Custom;

import android.content.Context;

public class MyLocation {

    private static String getLocation(Context context){
        String locale = context.getResources().getConfiguration().locale.getCountry();
        return locale;
    }

    public static boolean samLocation(Context context){

        String oldLocation=MySharedPref.getdata(context,"locat");
        String currentLocation=getLocation(context);
        if (oldLocation.equals("") || !oldLocation.equals(currentLocation)){
            MySharedPref.setdata(context,"locat",currentLocation);
            return false;
        }else {
            return true;
        }
    }
}
