package com.egypt.supporting.supportingonline.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.egypt.supporting.supportingonline.Calls.OnPress;
import com.egypt.supporting.supportingonline.Models.GroupHeaderModel;
import com.egypt.supporting.supportingonline.R;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class GroupHeaderAdapter extends RecyclerView.Adapter<GroupHeaderAdapter.GroupHeaderHolder> {

    private ArrayList<GroupHeaderModel> arrayList;
    private OnPress onPress;
    private Context context;

    public GroupHeaderAdapter(ArrayList<GroupHeaderModel> arrayList,Context context, OnPress onPress) {
        this.arrayList = arrayList;
        this.onPress = onPress;
        this.context=context;
    }

    @NonNull
    @Override
    public GroupHeaderHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_group_header,parent,false);
        GroupHeaderHolder vh=new GroupHeaderHolder(view);

        return vh;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull GroupHeaderHolder holder, int position) {



        // details
        holder.details.setText(arrayList.get(position).getDetails());


        // image
        holder.imageView.setBackgroundResource(arrayList.get(position).getImage());

        // beside title
        String beside="";
        if (arrayList.get(position).getBesideTitle()!=null){
            beside=" "+arrayList.get(position).getBesideTitle();
        }


        String type=arrayList.get(position).getType();

        switch (type){
            case "1":
            case "4":
                holder.layout.setBackground(context.getResources().getDrawable(R.drawable.circel_shape));
                holder.title.setText(context.getResources().getString(R.string.bronze)+beside);
                break;

            case "2":
            case "5":
                holder.layout.setBackground(context.getResources().getDrawable(R.drawable.circel_shape2));
                holder.title.setText(context.getResources().getString(R.string.silver)+beside);
                break;

            case "3":
            case "6":
                holder.layout.setBackground(context.getResources().getDrawable(R.drawable.circel_shape3));
                holder.title.setText(context.getResources().getString(R.string.gold)+beside);
                break;
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onPress.onClick(view,position);
            }
        });


    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    protected class GroupHeaderHolder extends RecyclerView.ViewHolder{

        private RelativeLayout layout;
        private CircleImageView imageView;
        private TextView title,details;

        public GroupHeaderHolder(@NonNull View itemView) {
            super(itemView);

            layout=(RelativeLayout)itemView.findViewById(R.id.r_gh_layout);
            imageView=(CircleImageView)itemView.findViewById(R.id.r_gh_image);
            title=(TextView)itemView.findViewById(R.id.r_gh_title);
            details=(TextView) itemView.findViewById(R.id.r_gh_details);
        }
    }
}
