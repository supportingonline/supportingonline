package com.egypt.supporting.supportingonline.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.text.Html;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.egypt.supporting.supportingonline.Calls.OnPress;
import com.egypt.supporting.supportingonline.Custom.URLImageParser;
import com.egypt.supporting.supportingonline.Models.PostModel;
import com.egypt.supporting.supportingonline.R;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class PostAdapter  extends RecyclerView.Adapter<PostAdapter.PostHolder> {

    private ArrayList<PostModel> arrayList;
    private Context context;
    private OnPress onPressProfile,onPressImage,onPressWow,onPressComment,onPressShare;


    public PostAdapter(ArrayList<PostModel> arrayList, Context context, OnPress onPressProfile, OnPress onPressImage, OnPress onPressWow, OnPress onPressComment, OnPress onPressShare) {
        this.arrayList = arrayList;
        this.context = context;
        this.onPressProfile = onPressProfile;
        this.onPressImage = onPressImage;
        this.onPressWow = onPressWow;
        this.onPressComment = onPressComment;
        this.onPressShare = onPressShare;
    }

    @NonNull
    @Override
    public PostHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_post,parent,false);
        PostHolder vh=new PostHolder(view);

        return vh;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull final PostHolder holder, final int position) {

        // animation
        Handler handler=new Handler();
        handler.post(new Runnable() {
            @Override
            public void run() {
                //setAnimation(holder.wow,position);
            }
        });


        // im profile
        Glide.with(context).load(arrayList.get(position).getIm_profile()).error(context.getResources().getDrawable(R.drawable.ic_user_profile))
                .into(holder.im_profileImage);
        holder.im_profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onPressProfile.onClick(view,position);
            }
        });

        // name
        holder.name.setText(arrayList.get(position).getName());

        // date
        holder.date.setText(arrayList.get(position).getDate());

        // text
        if (arrayList.get(position).getText().equals("null")){
            holder.text.setVisibility(View.GONE);
        }else {


            URLImageParser p = new URLImageParser(holder.text, context);
            Spanned htmlSpan = Html.fromHtml(arrayList.get(position).getText()
                    , p, null);
            holder.text.setText(htmlSpan);
           /* holder.text.setText(Html.fromHtml(arrayList.get(position).getText().trim(), new URLImageParser(holder.text, context), new Html.TagHandler() {
                @Override
                public void handleTag(boolean b, String s, Editable editable, XMLReader xmlReader) {

                }
            }));*/

        }

        // type
        //type
        String type=arrayList.get(position).getType();


        if ( type.equals("text")){
            holder.imageView.setVisibility(View.GONE);

        }else {
            // image
            holder.imageView.setVisibility(View.VISIBLE);
            Glide.with(context)
                    .load(context.getResources().getString(R.string.domain)
                            +"imgs/posts/"+arrayList.get(position).getImage())
                    .fitCenter()
                    .into(holder.imageView);
            holder.imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onPressImage.onClick(view,position);
                }
            });
        }






        // comment count
        holder.comment_count.setText(arrayList.get(position).getCount_comment()+" " +context.getResources().getString(R.string.comment));

        // wow count
        String countWow=arrayList.get(position).getCount_wow();
        holder.wow_count.setText(countWow+ " ");

        // share
        holder.share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onPressShare.onClick(view,position);
            }
        });

        // comment
        holder.comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onPressComment.onClick(view,position);
            }
        });


        // wow
        holder.wow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onPressWow.onClick(view,position);
            }
        });


        // is wowed
        if (arrayList.get(position).isIs_wowed()){
            holder.wow_image.setColorFilter(ContextCompat.getColor(context, R.color.colorPrimary), android.graphics.PorterDuff.Mode.SRC_IN);

        }







    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }



    protected class PostHolder extends RecyclerView.ViewHolder{

        private CircleImageView im_profileImage;
        private TextView name,date,text,comment_count,wow_count;
        private ImageView imageView,wow_image;
        private RelativeLayout share,comment,wow;

        public PostHolder(@NonNull View itemView) {
            super(itemView);
            im_profileImage=(CircleImageView)itemView.findViewById(R.id.r_post_improfile);

            name=(TextView)itemView.findViewById(R.id.r_post_name);
            date=(TextView)itemView.findViewById(R.id.r_post_date);
            text=(TextView)itemView.findViewById(R.id.r_post_text);
            comment_count=(TextView)itemView.findViewById(R.id.r_post_comment_count);
            wow_count=(TextView)itemView.findViewById(R.id.r_post_wow_count);

            imageView=(ImageView)itemView.findViewById(R.id.r_post_image);
            wow_image=(ImageView)itemView.findViewById(R.id.r_post_wow_image);

            share=(RelativeLayout)itemView.findViewById(R.id.r_post_share);
            comment=(RelativeLayout)itemView.findViewById(R.id.r_post_comment);
            wow=(RelativeLayout)itemView.findViewById(R.id.r_post_wow);

        }
    }
}
