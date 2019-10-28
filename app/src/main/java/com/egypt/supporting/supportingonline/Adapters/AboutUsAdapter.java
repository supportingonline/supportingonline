package com.egypt.supporting.supportingonline.Adapters;

import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.egypt.supporting.supportingonline.Calls.OnPress;
import com.egypt.supporting.supportingonline.Models.AboutUsModel;
import com.egypt.supporting.supportingonline.R;

import java.util.ArrayList;

public class AboutUsAdapter extends RecyclerView.Adapter<AboutUsAdapter.AboutUsHolder>{

    private ArrayList<AboutUsModel> arrayList;
    private OnPress onPress;

    public AboutUsAdapter(ArrayList<AboutUsModel> arrayList, OnPress onPress) {
        this.arrayList = arrayList;
        this.onPress = onPress;
    }

    @NonNull
    @Override
    public AboutUsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_aboutus,parent,false);
        AboutUsHolder vh=new AboutUsHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull AboutUsHolder holder, int position) {

        holder.title.setText(arrayList.get(position).getTitle());

        holder.des.setText(arrayList.get(position).getDes());

        holder.watch.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);
        holder.watch.setOnClickListener(new View.OnClickListener() {
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

    protected class AboutUsHolder extends RecyclerView.ViewHolder{

        private TextView title,des,watch;

        public AboutUsHolder(@NonNull View itemView) {
            super(itemView);
            title=(TextView)itemView.findViewById(R.id.r_aboutus_title);
            des=(TextView)itemView.findViewById(R.id.r_aboutus_des);
            watch=(TextView)itemView.findViewById(R.id.r_aboutus_watch);
        }
    }
}
