package com.egypt.supporting.supportingonline.Adapters;

import android.content.Context;
import android.text.Editable;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.egypt.supporting.supportingonline.Custom.URLImageParser;
import com.egypt.supporting.supportingonline.Models.ChatModel;
import com.egypt.supporting.supportingonline.R;

import org.xml.sax.XMLReader;

import java.util.ArrayList;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ChatHolder> {

    private ArrayList<ChatModel> arrayList;
    private Context context;

    public ChatAdapter(ArrayList<ChatModel> arrayList, Context context) {
        this.arrayList = arrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public ChatHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_chat,parent,false);
        ChatHolder vh=new ChatHolder(view);

        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull ChatHolder holder, int position) {

        boolean isMe=arrayList.get(position).isMe();


        if (isMe){
            holder.other.setVisibility(View.GONE);
            holder.me.setVisibility(View.VISIBLE);
            holder.me.setText(Html.fromHtml(arrayList.get(position).getText(), new URLImageParser(holder.me, context), new Html.TagHandler() {
                @Override
                public void handleTag(boolean b, String s, Editable editable, XMLReader xmlReader) {

                }
            }));
           // holder.me.setText(arrayList.get(position).getText());
        }else {
            holder.me.setVisibility(View.GONE);
            holder.other.setVisibility(View.VISIBLE);
           // holder.other.setText(arrayList.get(position).getText());
            holder.other.setText(Html.fromHtml(arrayList.get(position).getText(), new URLImageParser(holder.other, context), new Html.TagHandler() {
                @Override
                public void handleTag(boolean b, String s, Editable editable, XMLReader xmlReader) {

                }
            }));
        }



    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    protected class ChatHolder extends RecyclerView.ViewHolder{

        private TextView other,me;

        public ChatHolder(@NonNull View itemView) {
            super(itemView);

            other=(TextView)itemView.findViewById(R.id.r_chat_other);
            me=(TextView)itemView.findViewById(R.id.r_chat_me);
        }
    }
}
