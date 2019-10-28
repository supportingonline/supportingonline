package com.egypt.supporting.supportingonline.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.egypt.supporting.supportingonline.Calls.OnPress;
import com.egypt.supporting.supportingonline.Models.FriendRequestModel;
import com.egypt.supporting.supportingonline.R;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class FriendRequestAdpater extends RecyclerView.Adapter<FriendRequestAdpater.FriendRequestHolder> {

    private ArrayList<FriendRequestModel> arrayList;
    private Context context;
    private OnPress onaccept,oncancel,onPressLayout;

    private int lastPosition = -1;

    public FriendRequestAdpater(Context context,ArrayList<FriendRequestModel> arrayList, OnPress onaccept, OnPress oncancel,OnPress onPressLayout) {
        this.arrayList = arrayList;
        this.onaccept = onaccept;
        this.oncancel = oncancel;
        this.context=context;
        this.onPressLayout=onPressLayout;
    }

    @NonNull
    @Override
    public FriendRequestHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_friends_request,parent,false);

        FriendRequestHolder vh=new FriendRequestHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull FriendRequestHolder holder, int position) {


        //image
        Glide.with(context).load(arrayList.get(position).getImage()).error(context.getResources().getDrawable(R.drawable.ic_user_profile))
                .into(holder.imageView);

        // name
        holder.textView.setText(arrayList.get(position).getName());

        // date
      holder.date.setText(arrayList.get(position).getDate());

        holder.accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onaccept.onClick(view,position);
            }
        });

        holder.cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                oncancel.onClick(view,position);
            }
        });

        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onPressLayout.onClick(v,position);
            }
        });

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }






    protected class FriendRequestHolder extends RecyclerView.ViewHolder{

        private TextView textView,date;
        private ImageView imageView;
        private RelativeLayout accept,cancel;
        private LinearLayout layout;

        public FriendRequestHolder(@NonNull View itemView) {
            super(itemView);
            textView=(TextView)itemView.findViewById(R.id.r_request_name);
            date=(TextView)itemView.findViewById(R.id.r_request_date);
            imageView=(ImageView)itemView.findViewById(R.id.r_request_image);
            accept=(RelativeLayout)itemView.findViewById(R.id.r_request_accept);
            cancel=(RelativeLayout)itemView.findViewById(R.id.r_request_cancel);
            layout=(LinearLayout)itemView.findViewById(R.id.r_request_layout);
        }
    }




}
