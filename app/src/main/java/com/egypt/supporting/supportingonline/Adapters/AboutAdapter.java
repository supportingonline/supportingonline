package com.egypt.supporting.supportingonline.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.egypt.supporting.supportingonline.Models.AboutModel;
import com.egypt.supporting.supportingonline.R;

import java.util.ArrayList;

public class AboutAdapter extends RecyclerView.Adapter<AboutAdapter.AboutHolder> {

    private ArrayList<AboutModel> arrayList;

    public AboutAdapter(ArrayList<AboutModel> arrayList) {
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public AboutHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_about,parent,false);

        AboutHolder vh=new AboutHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull AboutHolder holder, int position) {

        holder.imageView.setBackgroundResource(arrayList.get(position).getImage());
        holder.tite.setText(arrayList.get(position).getTitle());
        holder.details.setText(arrayList.get(position).getDetalis());

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    protected class AboutHolder extends RecyclerView.ViewHolder{

        private ImageView imageView;
        private TextView tite,details;

        public AboutHolder(@NonNull View itemView) {
            super(itemView);
            imageView=(ImageView)itemView.findViewById(R.id.r_about_image);
            tite=(TextView) itemView.findViewById(R.id.r_about_title);
            details=(TextView) itemView.findViewById(R.id.r_about_details);
        }
    }
}
