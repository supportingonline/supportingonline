package com.egypt.supporting.supportingonline.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.Editable;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.egypt.supporting.supportingonline.Calls.OnPress;
import com.egypt.supporting.supportingonline.Custom.URLImageParser;
import com.egypt.supporting.supportingonline.R;
import com.egypt.supporting.supportingonline.Models.ChatHistroyModel;
import com.bumptech.glide.Glide;

import org.xml.sax.XMLReader;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatHistoryAdapter  extends RecyclerView.Adapter<ChatHistoryAdapter.ChatHistroyHolder>{

    private ArrayList<ChatHistroyModel> arrayList;
    private Context context;
    private OnPress onPress;

    public ChatHistoryAdapter(ArrayList<ChatHistroyModel> arrayList, Context context, OnPress onPress) {
        this.arrayList = arrayList;
        this.context = context;
        this.onPress = onPress;
    }

    @NonNull
    @Override
    public ChatHistroyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_chat_history,parent,false);
        ChatHistroyHolder vh=new ChatHistroyHolder(view);
        return vh;
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(@NonNull ChatHistroyHolder holder, int position) {

        // name
        holder.name.setText(arrayList.get(position).getUserName());

        // content
      //  holder.content.setText(arrayList.get(position).getContent());

        holder.content.setText(Html.fromHtml(arrayList.get(position).getContent().trim(), new URLImageParser(holder.content, context), new Html.TagHandler() {
            @Override
            public void handleTag(boolean b, String s, Editable editable, XMLReader xmlReader) {

            }
        }));

        // image
        Glide.with(context).load(arrayList.get(position).getUserImage()).error(R.drawable.ic_user_profile)
                .into(holder.imageView);

        // click
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onPress.onClick(v,position);
            }
        });

        // reade
        if (!arrayList.get(position).isReade()){
           holder.layout.setBackgroundColor(context.getResources().getColor(R.color.orange));
        }else {
            holder.layout.setBackgroundColor(android.R.color.transparent);
        }

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    protected  class ChatHistroyHolder extends RecyclerView.ViewHolder{

        private CircleImageView imageView;
        private TextView name,content;
        private LinearLayout layout;

        public ChatHistroyHolder(@NonNull View itemView) {
            super(itemView);
            imageView=(CircleImageView)itemView.findViewById(R.id.r_chathistory_image);
            name=(TextView)itemView.findViewById(R.id.r_chathistory_name);
            content=(TextView)itemView.findViewById(R.id.r_chathistory_content);
            layout=(LinearLayout)itemView.findViewById(R.id.r_chathistory_layout);
        }
    }
}
