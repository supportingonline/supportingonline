package com.egypt.supporting.supportingonline.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.egypt.supporting.supportingonline.Calls.OnPress;
import com.egypt.supporting.supportingonline.Models.LanguageModel;
import com.egypt.supporting.supportingonline.R;

import java.util.ArrayList;

public class LanguageAdapter extends RecyclerView.Adapter<LanguageAdapter.LanguageHolder> {

    private ArrayList<LanguageModel> arrayList;
    private OnPress onPress;

    public LanguageAdapter(ArrayList<LanguageModel> arrayList, OnPress onPress) {
        this.arrayList = arrayList;
        this.onPress = onPress;
    }

    @NonNull
    @Override
    public LanguageHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_language,parent,false);

        LanguageHolder vh=new LanguageHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull LanguageHolder holder, int position) {
        holder.imageView.setBackgroundResource(arrayList.get(position).getImage());
        holder.textView.setText(arrayList.get(position).getTitle());
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


    protected class LanguageHolder extends RecyclerView.ViewHolder{

        private TextView textView;
        private ImageView imageView;

        public LanguageHolder(@NonNull View itemView) {
            super(itemView);
            textView=(TextView)itemView.findViewById(R.id.r_lang_name);
            imageView=(ImageView)itemView.findViewById(R.id.r_lang_image);
        }
    }
}
