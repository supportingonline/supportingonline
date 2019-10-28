package com.egypt.supporting.supportingonline.Adapters;

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.egypt.supporting.supportingonline.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class WorkLayerAdapter extends RecyclerView.Adapter<WorkLayerAdapter.WorkLayerolder> {

    @NonNull
    @Override
    public WorkLayerolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull WorkLayerolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    protected class WorkLayerolder extends RecyclerView.ViewHolder {

        private CircleImageView imageView;
        private TextView name;

        public WorkLayerolder(@NonNull View itemView) {
            super(itemView);
            imageView=(CircleImageView)itemView.findViewById(R.id.r_work_layer_image);
            name=(TextView) itemView.findViewById(R.id.r_work_layer_name);
        }
    }
}
