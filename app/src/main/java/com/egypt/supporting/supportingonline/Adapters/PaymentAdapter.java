package com.egypt.supporting.supportingonline.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.egypt.supporting.supportingonline.Models.PaymentModel;
import com.egypt.supporting.supportingonline.R;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class PaymentAdapter  extends RecyclerView.Adapter<PaymentAdapter.PaymentHolder>{

    private ArrayList<PaymentModel> arrayList;
    private Context context;

    public PaymentAdapter(ArrayList<PaymentModel> arrayList,Context context) {
        this.arrayList = arrayList;
        this.context=context;
    }

    @NonNull
    @Override
    public PaymentHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_payment,parent,false);

        PaymentHolder vh=new PaymentHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull PaymentHolder holder, int position) {

        holder.imageView.setBackgroundResource(arrayList.get(position).getLogo());

        holder.name.setText(arrayList.get(position).getBankName());

        holder.currency.setText(arrayList.get(position).getCurrency());

        holder.number.setText(arrayList.get(position).getAccountNumber());

        holder.country.setText(arrayList.get(position).getCountry());

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    protected class PaymentHolder extends RecyclerView.ViewHolder{

        private TextView number,name,country,currency;
        private CircleImageView imageView;

        public PaymentHolder(@NonNull View itemView) {
            super(itemView);
            number=(TextView)itemView.findViewById(R.id.r_payment_accountNumber);
            name=(TextView)itemView.findViewById(R.id.r_payment_bankName);
            country=(TextView)itemView.findViewById(R.id.r_payment_country);
            currency=(TextView)itemView.findViewById(R.id.r_payment_currency);
            imageView=(CircleImageView) itemView.findViewById(R.id.r_payment_logo);
        }
    }
}
