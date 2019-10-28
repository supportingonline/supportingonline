package com.egypt.supporting.supportingonline.Custom;

import android.content.Context;

public class MySizes {


    public static int getwedith(Context context){
        int width= context.getResources().getDisplayMetrics().widthPixels;
        return width;

    }

    public static int gethight(Context context){
        int height= context.getResources().getDisplayMetrics().heightPixels;
        return height;

    }
}
