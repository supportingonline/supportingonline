package com.egypt.supporting.supportingonline.Custom;

import android.content.Context;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

public class MyAnimation {

    public static void setAnimation(Context context,View view, int animResourse) {
            Animation animation = AnimationUtils.loadAnimation(context, animResourse);
            view.startAnimation(animation);

    }
}
