package com.egypt.supporting.supportingonline.Custom;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Build;

import androidx.annotation.RequiresApi;

import java.util.Locale;

public class MyLanguage {

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    public static void changeLanguage(Context context) {

        Locale locale = new Locale(getLanguage(context));
        Locale.setDefault(locale);
        Configuration config = context.getResources().getConfiguration();
        config.setLocale(locale);
        context.createConfigurationContext(config);
        context.getResources().updateConfiguration(config, context.getResources().getDisplayMetrics());

    }

    private static String getLanguage(Context context){

        String lang ="en";
        String value=MySharedPref.getdata(context,"lang");
        if (value!=""){
            lang=value;
        }
        return lang;

    }
}
