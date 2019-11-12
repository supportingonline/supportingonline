package com.egypt.supporting.supportingonline.Dialogs;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.egypt.supporting.supportingonline.Calls.OnPressView;

import com.egypt.supporting.supportingonline.Custom.URLImageParser;
import com.egypt.supporting.supportingonline.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import org.xml.sax.XMLReader;

public class DialogShareBottomSheet extends BottomSheetDialogFragment {

    private OnPressView onpress;
    private Context context;
    private String text,type,image;

    public DialogShareBottomSheet(OnPressView onpress, Context context, String text, String type, String image) {
        this.onpress = onpress;
        this.context = context;
        this.text = text;
        this.type = type;
        this.image = image;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.diaolg_share, container, false);
    }

    private TextView textView;
    private RelativeLayout share;
    private ImageView imageView;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        textView=(TextView)view.findViewById(R.id.d_share_text);
        imageView=(ImageView)view.findViewById(R.id.d_share_image);
        imageView.setVisibility(View.GONE);
        share=(RelativeLayout)view.findViewById(R.id.d_share_share);

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
