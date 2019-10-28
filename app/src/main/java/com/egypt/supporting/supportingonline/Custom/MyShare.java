package com.egypt.supporting.supportingonline.Custom;

import android.app.Activity;
import android.content.Context;

import androidx.core.app.ShareCompat;

public class MyShare {

    public static void shareApp(Context context){
        ShareCompat.IntentBuilder.from((Activity) context)
                .setType("text/plain")
                .setChooserTitle("Share This App")
                .setText("http://play.google.com/store/apps/details?id=" + context.getPackageName())
                .startChooser();
    }
}
