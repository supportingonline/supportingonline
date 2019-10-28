package com.egypt.supporting.supportingonline.Custom;

import android.content.Context;

public class ConfirmMoneyToLocal {

    public static String transform(Context context, String value){
        double doubleValue=Double.parseDouble(value);
        String newValue=null;
        String theXValue=MySharedPref.getdata(context,"value");
        if (theXValue.equals("")){
            newValue=String.valueOf(doubleValue*1);
        }else {
            double xvalue=Double.parseDouble(theXValue);
            newValue=String.valueOf(RoundDouble.round(doubleValue*xvalue,2));
        }
        return newValue + " " + MySharedPref.getdata(context, "currency");
    }
}
