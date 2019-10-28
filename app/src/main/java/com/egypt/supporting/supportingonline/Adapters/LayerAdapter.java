package com.egypt.supporting.supportingonline.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.egypt.supporting.supportingonline.Custom.ConfirmMoneyToLocal;
import com.egypt.supporting.supportingonline.Models.LayerModel;
import com.egypt.supporting.supportingonline.R;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class LayerAdapter extends RecyclerView.Adapter<LayerAdapter.LayerHolder>{

    private ArrayList<LayerModel> arrayList;
    private Context context;

    public LayerAdapter(ArrayList<LayerModel> arrayList, Context context) {
        this.arrayList = arrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public LayerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_layer,parent,false);
        LayerHolder vh=new LayerHolder(view);

        return vh;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull LayerHolder holder, int position) {

        Glide.with(context).load(arrayList.get(position).getImage()).into(holder.imageView);



        holder.name.setText(arrayList.get(position).getName());



        // price
        String price=arrayList.get(position).getPrice();
        if (price.equals("out")){

            holder.layout.setBackgroundColor(context.getResources().getColor(R.color.greenLight));
            holder.price.setText("out");
            holder.price.setTextColor(context.getResources().getColor(R.color.colorAccent));
        }else {
            if (position==0){
                holder.layout.setBackgroundColor(context.getResources().getColor(R.color.colorAccentLight));
            }else {
                holder.layout.setBackgroundColor(context.getResources().getColor(R.color.blueLight));
            }

            holder.price.setText(ConfirmMoneyToLocal.transform(context,arrayList.get(position).getPrice()));
        }

        // number
        if (position==arrayList.size()-1){
            holder.number.setText(String.valueOf(position));
        }else {
            holder.number.setText(String.valueOf(position + 1));
        }


    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    protected class LayerHolder extends RecyclerView.ViewHolder{

        private CircleImageView imageView;
        private TextView name,price,number;
        private LinearLayout layout;

        public LayerHolder(@NonNull View itemView) {
            super(itemView);
            imageView=(CircleImageView)itemView.findViewById(R.id.r_layer_image);
            name=(TextView) itemView.findViewById(R.id.r_layer_name);
            price=(TextView) itemView.findViewById(R.id.r_layer_price);
            number=(TextView) itemView.findViewById(R.id.r_layer_number);
            layout=(LinearLayout) itemView.findViewById(R.id.r_layer_layout);
        }
    }
}
