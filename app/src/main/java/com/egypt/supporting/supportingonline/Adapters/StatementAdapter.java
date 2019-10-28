package com.egypt.supporting.supportingonline.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.egypt.supporting.supportingonline.Custom.ConfirmMoneyToLocal;
import com.egypt.supporting.supportingonline.Models.StatementModel;
import com.egypt.supporting.supportingonline.R;

import java.util.ArrayList;

public class StatementAdapter extends RecyclerView.Adapter<StatementAdapter.StatementHolder> {

    private ArrayList<StatementModel> arrayList;
    private Context context;

    public StatementAdapter(ArrayList<StatementModel> arrayList,Context context) {
        this.arrayList = arrayList;
        this.context=context;
    }

    @NonNull
    @Override
    public StatementHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_statement,parent,false);
        StatementHolder vh=new StatementHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull StatementHolder holder, int position) {

        holder.money.setText(ConfirmMoneyToLocal.transform(context,arrayList.get(position).getMoney()));

        holder.type.setText(arrayList.get(position).getType());

        holder.date.setText(arrayList.get(position).getDate());

        if (arrayList.get(position).getType().equals("Add To Fees Wallet")){
            holder.money.setTextColor(context.getResources().getColor(R.color.green));
        }else {
            holder.money.setTextColor(context.getResources().getColor(R.color.colorAccent));
        }

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    protected class StatementHolder extends RecyclerView.ViewHolder{

        private TextView money,type,date;

        public StatementHolder(@NonNull View itemView) {
            super(itemView);
            money=(TextView)itemView.findViewById(R.id.r_stat_money);
            type=(TextView)itemView.findViewById(R.id.r_stat_type);
            date=(TextView)itemView.findViewById(R.id.r_stat_date);
        }
    }
}
