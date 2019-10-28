package com.egypt.supporting.supportingonline.Dialogs;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.egypt.supporting.supportingonline.Calls.OnPressView;
import com.egypt.supporting.supportingonline.R;
import com.bumptech.glide.Glide;

import de.hdodenhof.circleimageview.CircleImageView;

public class DialogGroupItem extends Dialog {
    private Context context;
    private OnPressView onPressremove,onPressshowImage,onpressIReceive;
    private String name,image,receciveImage;
    private boolean receive,delete;

    public DialogGroupItem(@NonNull Context context, OnPressView onPressremove,OnPressView onPressshowImage,OnPressView onpressIReceive, String name, String image,
                           String receciveImage,boolean receive,boolean delete) {
        super(context);
        this.context = context;
        this.onPressremove = onPressremove;
        this.onPressshowImage = onPressshowImage;
        this.onpressIReceive = onpressIReceive;
        this.name = name;
        this.image = image;
        this.receciveImage = receciveImage;

        this.receive=receive;
        this.delete=delete;

    }


    private ImageView join_image;
    private CircleImageView imageView;
    private Button btn_remove,btn_showReceiveImage,btn_showReceive;
    private TextView textname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_group_item);
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);


        join_image=(ImageView)findViewById(R.id.group_my_join);
        imageView=(CircleImageView)findViewById(R.id.group_my_image);
        btn_remove=(Button)findViewById(R.id.group_remove_btn);
        btn_showReceiveImage=(Button)findViewById(R.id.group_showimage_btn);
        btn_showReceive=(Button)findViewById(R.id.group_showrecive_btn);
        textname=(TextView)findViewById(R.id.group_my_name);

        // name
        textname.setText(name);

        // image
        Glide.with(context).load(image).into(imageView);

        // remove btn
        btn_remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogGroupItem.this.dismiss();
                onPressremove.onclick(v);
            }
        });
        if (!delete){
            btn_remove.setVisibility(View.GONE);
        }



        // show i receive
        btn_showReceive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogGroupItem.this.dismiss();
                onpressIReceive.onclick(v);
            }
        });
        if (!receive){
            btn_showReceive.setVisibility(View.GONE);
        }



        // show image receive
        btn_showReceiveImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogGroupItem.this.dismiss();
                onPressshowImage.onclick(v);
            }
        });
        if (receciveImage.equals("null")){
            btn_showReceiveImage.setVisibility(View.GONE);
        }

    }
}
