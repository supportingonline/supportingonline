package com.egypt.supporting.supportingonline.Views;

import android.content.Context;
import android.graphics.PorterDuff;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.egypt.supporting.supportingonline.Custom.MyAnimation;
import com.egypt.supporting.supportingonline.R;

public class TabLayoutViews {

    public static View getTabView(Context context ,int icon,int tabCountNotification){
        View v = LayoutInflater.from(context).inflate(R.layout.tab_view, null);
        TextView tv = (TextView) v.findViewById(R.id.tabnoti);


        RelativeLayout layout=(RelativeLayout)v.findViewById(R.id.tabcircle);

        tv.setText(String.valueOf(tabCountNotification));
        if (tabCountNotification==0){
            layout.setVisibility(View.INVISIBLE);
        }
        ImageView img = (ImageView) v.findViewById(R.id.tabicon);
        img.setImageResource(icon);
        img.setColorFilter(context.getResources().getColor(R.color.white), PorterDuff.Mode.SRC_IN);


        return v;
    }

    public static void selectTab(Context context,View view){
        TextView t=view.findViewById(R.id.tabnoti);
        t.setText("0");

        ImageView im=view.findViewById(R.id.tabicon);
        im.setColorFilter(context.getResources().getColor(R.color.gray), PorterDuff.Mode.SRC_IN);

        RelativeLayout layout=(RelativeLayout)view.findViewById(R.id.tabcircle);
        layout.setVisibility(View.INVISIBLE);
    }


    public static void unselectedTab(Context context,View view){

        ImageView im=view.findViewById(R.id.tabicon);
        im.setColorFilter(context.getResources().getColor(R.color.white), PorterDuff.Mode.SRC_IN);
    }

    public static void addToCountNotification(Context context,View view){
        TextView t=view.findViewById(R.id.tabnoti);
        int s= Integer.parseInt(t.getText().toString());
        int newValue=s+1;
        t.setText(String.valueOf(newValue));

        RelativeLayout layout=(RelativeLayout)view.findViewById(R.id.tabcircle);
        layout.setVisibility(View.VISIBLE);
        MyAnimation.setAnimation(context,layout,R.anim.zominzomout);
        MyAnimation.setAnimation(context,t,R.anim.zominzomout);

    }

    public static void removeCount(Context context,View view){
        TextView t=view.findViewById(R.id.tabnoti);
        t.setText("0");
        RelativeLayout layout=(RelativeLayout)view.findViewById(R.id.tabcircle);
        layout.setVisibility(View.INVISIBLE);

    }
}
