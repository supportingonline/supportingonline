package com.egypt.supporting.supportingonline.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.egypt.supporting.supportingonline.Calls.OnPress;
import com.egypt.supporting.supportingonline.Models.SearchModel;
import com.egypt.supporting.supportingonline.R;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.SearchHolder> {

    private ArrayList<SearchModel> arrayList;
    private Context context;
    private OnPress onPress;

    public SearchAdapter(ArrayList<SearchModel> arrayList, Context context, OnPress onPress) {
        this.arrayList = arrayList;
        this.context = context;
        this.onPress = onPress;
    }

    @NonNull
    @Override
    public SearchHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_search,parent,false);
        SearchHolder vh=new SearchHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull SearchHolder holder, final int position) {

        // name
        holder.textView.setText(arrayList.get(position).getName());

        // image
        Glide.with(context).load(arrayList.get(position).getImage()).error(context.getResources().getDrawable(R.drawable.ic_user_profile))
                .into(holder.imageView);

        //click
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

    protected class SearchHolder extends RecyclerView.ViewHolder{

      private TextView textView;
      private ImageView imageView;

        public SearchHolder(@NonNull View itemView) {
            super(itemView);
            textView=(TextView)itemView.findViewById(R.id.r_search_name);
            imageView=(ImageView)itemView.findViewById(R.id.r_search_image);

        }
    }
}
