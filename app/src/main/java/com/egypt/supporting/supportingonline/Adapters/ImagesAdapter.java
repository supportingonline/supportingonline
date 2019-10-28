package com.egypt.supporting.supportingonline.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.egypt.supporting.supportingonline.Calls.OnPress;
import com.egypt.supporting.supportingonline.R;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class ImagesAdapter extends RecyclerView.Adapter<ImagesAdapter.ImagesHolder> {

    private ArrayList<String> arrayList;
    private Context context;
    private OnPress onPress;

    public ImagesAdapter(ArrayList<String> arrayList, Context context,OnPress onPress) {
        this.arrayList = arrayList;
        this.context = context;
        this.onPress = onPress;
    }

    @NonNull
    @Override
    public ImagesHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_image,parent,false);
        ImagesHolder vh=new ImagesHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull ImagesHolder holder, int position) {


        Glide.with(context).load(context.getResources().getString(R.string.domain) +"imgs/posts/"+arrayList.get(position))
                .error(R.drawable.ic_user_profile)
                .into(holder.imageView);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onPress.onClick(v,position);
            }
        });

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    protected class ImagesHolder extends RecyclerView.ViewHolder{

        private ImageView imageView;

        public ImagesHolder(@NonNull View itemView) {
            super(itemView);
            imageView=(ImageView)itemView.findViewById(R.id.r_image_image);
        }
    }
}
