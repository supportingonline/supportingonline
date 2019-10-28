package com.egypt.supporting.supportingonline.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.egypt.supporting.supportingonline.Calls.OnPress;
import com.egypt.supporting.supportingonline.Models.EmojiModel;
import com.egypt.supporting.supportingonline.R;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class EmojiAdapter extends RecyclerView.Adapter<EmojiAdapter.EmojiHolder> {

    private ArrayList<EmojiModel> arrayList;
    private Context context;
    private OnPress onPress;

    public EmojiAdapter(ArrayList<EmojiModel> arrayList, Context context, OnPress onPress) {
        this.arrayList = arrayList;
        this.context = context;
        this.onPress = onPress;
    }

    @NonNull
    @Override
    public EmojiHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.recyler_emoji,parent,false);

        EmojiHolder vh=new EmojiHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull EmojiHolder holder, int position) {

        Glide.with(context).load(arrayList.get(position).getUrl()).into(holder.imageView);

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

    protected class EmojiHolder extends RecyclerView.ViewHolder{

        private ImageView imageView;

        public EmojiHolder(@NonNull View itemView) {
            super(itemView);

            imageView=(ImageView)itemView.findViewById(R.id.r_emoji_image);
        }
    }
}
