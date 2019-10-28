package com.egypt.supporting.supportingonline.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.egypt.supporting.supportingonline.Calls.OnPress;
import com.egypt.supporting.supportingonline.Models.MenuModel;
import com.egypt.supporting.supportingonline.R;

import java.util.ArrayList;

public class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.MenuHolder> {

    private ArrayList<MenuModel> arrayList;
    private OnPress onPress;

    public MenuAdapter(ArrayList<MenuModel> arrayList, OnPress onPress) {
        this.arrayList = arrayList;
        this.onPress = onPress;
    }

    @NonNull
    @Override
    public MenuHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_menu,parent,false);
        MenuHolder vh=new MenuHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull MenuHolder holder, final int position) {

        // title
        holder.textView.setText(arrayList.get(position).getTitle());

        // icon
        holder.imageView.setBackgroundResource(arrayList.get(position).getIcon());

        // click
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

    protected class MenuHolder extends RecyclerView.ViewHolder{

       private TextView textView;
       private ImageView imageView;

        public MenuHolder(@NonNull View itemView) {
            super(itemView);
            textView=(TextView)itemView.findViewById(R.id.r_menu_title);
            imageView=(ImageView)itemView.findViewById(R.id.r_menu_icon);
        }
    }
}
