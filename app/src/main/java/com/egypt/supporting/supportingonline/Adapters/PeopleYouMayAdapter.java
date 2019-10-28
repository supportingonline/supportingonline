package com.egypt.supporting.supportingonline.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.egypt.supporting.supportingonline.Calls.OnPress;
import com.egypt.supporting.supportingonline.Models.PeopleYouMayKnowModel;
import com.egypt.supporting.supportingonline.R;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class PeopleYouMayAdapter extends RecyclerView.Adapter<PeopleYouMayAdapter.PeopleYouMayHolder> {
    private ArrayList<PeopleYouMayKnowModel> arrayList;
    private Context context;
    private OnPress onPress;

    public PeopleYouMayAdapter(ArrayList<PeopleYouMayKnowModel> arrayList, Context context, OnPress onPress) {
        this.arrayList = arrayList;
        this.context = context;
        this.onPress = onPress;
    }

    @NonNull
    @Override
    public PeopleYouMayHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_peopleyoumayknow,parent,false);
        PeopleYouMayHolder vh=new PeopleYouMayHolder(view);
        return vh;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull PeopleYouMayHolder holder, int position) {

        // name
        holder.name.setText(arrayList.get(position).getName());

        // mutual
        holder.mutual.setText(arrayList.get(position).getMutual()+" "+context.getResources().getString(R.string.mutualfrirends));

        // image
        Glide.with(context).load(arrayList.get(position).getImage()).error(R.drawable.ic_user_profile).into(holder.imageView);

        // click
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

    protected class PeopleYouMayHolder extends RecyclerView.ViewHolder{
        private ImageView imageView;
        private TextView name,mutual;

        public PeopleYouMayHolder(@NonNull View itemView) {
            super(itemView);
            imageView=(ImageView)itemView.findViewById(R.id.r_people_image);
            name=(TextView) itemView.findViewById(R.id.r_people_name);
            mutual=(TextView) itemView.findViewById(R.id.r_people_mutual);
        }
    }
}
