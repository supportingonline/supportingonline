package com.egypt.supporting.supportingonline.Adapters;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.egypt.supporting.supportingonline.Calls.OnPress;
import com.egypt.supporting.supportingonline.Custom.MySizes;
import com.egypt.supporting.supportingonline.Models.GroupModel;
import com.egypt.supporting.supportingonline.R;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class GroupAdapter extends RecyclerView.Adapter<GroupAdapter.GroupHolder> {

    private ArrayList<GroupModel> arrayList;
    private Context context;
    private OnPress onPress;
    private int screenPosition;

    public GroupAdapter(ArrayList<GroupModel> arrayList, Context context,int screenPosition, OnPress onPress) {
        this.arrayList = arrayList;
        this.context = context;
        this.onPress = onPress;
        this.screenPosition=screenPosition;

    }

    @NonNull
    @Override
    public GroupHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_group,parent,false);

        GroupHolder vh=new GroupHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull GroupHolder holder, int position) {




            // 2 user group

            if (screenPosition == 2 || screenPosition == 4 || screenPosition == 8) {
                holder.v_mdel.setVisibility(View.GONE);
                if (position == 0 || position == 2 || position == 4 || position == 6) {
                    holder.v21.setVisibility(View.INVISIBLE);
                    holder.v22.setVisibility(View.VISIBLE);
                } else if (position == 1 || position == 3 || position == 5 || position == 7) {
                    holder.v21.setVisibility(View.VISIBLE);
                    holder.v22.setVisibility(View.INVISIBLE);
                }
            }

            // 3 user group

            else if (screenPosition == 3 || screenPosition == 9 || screenPosition == 27) {


                if (position == 1 || position == 4 || position == 7 || position == 10 || position == 13 || position == 16 || position == 19 || position == 22 || position == 25) {
                    holder.v_mdel.setVisibility(View.VISIBLE);
                    holder.v21.setVisibility(View.VISIBLE);
                    holder.v22.setVisibility(View.VISIBLE);
                } else if (position == 0 || position == 3 || position == 6 || position == 9 || position == 12 || position == 15 || position == 18 || position == 21 || position == 24) {
                    holder.v_mdel.setVisibility(View.INVISIBLE);
                    holder.v22.setVisibility(View.VISIBLE);
                    holder.v21.setVisibility(View.INVISIBLE);
                } else if (position == 2 || position == 5 || position == 8 || position == 11 || position == 14 || position == 17 || position == 20 || position == 23 || position == 26) {
                    holder.v_mdel.setVisibility(View.INVISIBLE);
                    holder.v21.setVisibility(View.VISIBLE);
                    holder.v22.setVisibility(View.INVISIBLE);

                }


            }

            // if is last
            if (arrayList.get(position).isHasChildern() ) {
                holder.v_bottom.setVisibility(View.VISIBLE);
            } else {

                holder.v_bottom.setVisibility(View.GONE);
            }


            // resizes
            int myWidth = MySizes.getwedith(context) / screenPosition;

            holder.name.setTextSize(myWidth / 40);

            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(myWidth / 2, myWidth / 2);
            layoutParams.gravity = Gravity.CENTER;
            holder.imageView.setLayoutParams(layoutParams);

            holder.layout2.setPadding(0, myWidth / 15, 0, myWidth / 15);

        // visiable
        if (!arrayList.get(position).isVisable()){
            holder.layout.setVisibility(View.INVISIBLE);
        }else {
            holder.layout.setVisibility(View.VISIBLE);


            // click
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onPress.onClick(v, position);
                }
            });

            // has data

            if (!arrayList.get(position).isHasdata()) {

                // waiting

                holder.name.setVisibility(View.GONE);
              
                holder.imageView.setBackground(context.getDrawable(R.drawable.wait));
            } else {
                // data

                // name
                holder.name.setVisibility(View.VISIBLE);
                holder.name.setText(arrayList.get(position).getName());

                // image
                holder.imageView.setBackground(null);
                Glide.with(context).load(arrayList.get(position).getImage()).error(R.drawable.ic_user_profile)
                        .into(holder.imageView);
            }

        }


    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    protected class GroupHolder extends RecyclerView.ViewHolder{

        private View v_mdel,v21,v22,v_bottom;
        private TextView name;
        private CircleImageView imageView;

        private LinearLayout layout,layout2;


        public GroupHolder(@NonNull View itemView) {
            super(itemView);
            v_mdel=(View)itemView.findViewById(R.id.v_mdl);
            v21=(View)itemView.findViewById(R.id.v_2_1);
            v22=(View)itemView.findViewById(R.id.v_2_2);
            v_bottom=(View)itemView.findViewById(R.id.v_bottom);
            name=(TextView)itemView.findViewById(R.id.r_group_name);
            imageView=(CircleImageView) itemView.findViewById(R.id.r_group_image);
            layout=(LinearLayout)itemView.findViewById(R.id.r_group_layout);
            layout2=(LinearLayout)itemView.findViewById(R.id.r_group_layout2);

        }
    }
}
