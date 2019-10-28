package com.egypt.supporting.supportingonline.Dialogs;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.egypt.supporting.supportingonline.Calls.OnPressView;
import com.egypt.supporting.supportingonline.Custom.URLImageParser;
import com.egypt.supporting.supportingonline.R;
import com.bumptech.glide.Glide;

import org.xml.sax.XMLReader;

public class DialogShare  extends Dialog {

    private OnPressView onpress;
    private Context context;
    private String text,type,image;

    public DialogShare(@NonNull Context context,String text,String image,String type, OnPressView onpress) {
        super(context);
        this.onpress = onpress;
        this.context=context;
        this.text=text;
        this.type=type;
        this.image=image;
    }

    private TextView textView;
    private RelativeLayout share;
    private ImageView imageView;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.diaolg_share);
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);

        textView=(TextView)findViewById(R.id.d_share_text);
        imageView=(ImageView) findViewById(R.id.d_share_image);
        imageView.setVisibility(View.GONE);
        share=(RelativeLayout)findViewById(R.id.d_share_share);

        Log.i("type",type);

        if (type.equals("image")){
            imageView.setVisibility(View.VISIBLE);
            Glide.with(context).load(context.getResources().getString(R.string.domain)
                    +"imgs/posts/"+image).into(imageView);
        }

        if (!text.equals("null")) {
            textView.setText(Html.fromHtml(text, new URLImageParser(textView, context), new Html.TagHandler() {
                @Override
                public void handleTag(boolean b, String s, Editable editable, XMLReader xmlReader) {

                }
            }));
        }else {
            textView.setVisibility(View.GONE);
        }

        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onpress.onclick(view);
                dismiss();
            }
        });

    }
}
