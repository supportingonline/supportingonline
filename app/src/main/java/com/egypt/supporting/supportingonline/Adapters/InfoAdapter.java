package com.egypt.supporting.supportingonline.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.egypt.supporting.supportingonline.Models.InfoModel;
import com.egypt.supporting.supportingonline.R;

import java.util.ArrayList;

public class InfoAdapter extends RecyclerView.Adapter<InfoAdapter.InfoHolder> {

    private ArrayList<InfoModel> arrayList;

    public InfoAdapter(ArrayList<InfoModel> arrayList) {
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public InfoHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_info,parent,false);

        InfoHolder vh=new InfoHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull InfoHolder holder, int position) {

        holder.title.setText(arrayList.get(position).getTitle());
        holder.count.setText(arrayList.get(position).getCount());
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    protected class InfoHolder extends RecyclerView.ViewHolder{

        private TextView title,count;

        public InfoHolder(@NonNull View itemView) {
            super(itemView);

            title=(TextView)itemView.findViewById(R.id.r_info_title);
            count=(TextView)itemView.findViewById(R.id.r_info_count);
        }
    }
}
