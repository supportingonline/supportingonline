package com.egypt.supporting.supportingonline;


import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.egypt.supporting.supportingonline.Adapters.GroupHeaderAdapter;
import com.egypt.supporting.supportingonline.Calls.OnPress;
import com.egypt.supporting.supportingonline.Custom.MySizes;
import com.egypt.supporting.supportingonline.Custom.SpaceRecycler_V;
import com.egypt.supporting.supportingonline.Models.GroupHeaderModel;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class SupportingGroupsFragment extends Fragment {

    private int user;
    private RecyclerView recyclerView;
    private LinearLayoutManager manager;
    private ArrayList<GroupHeaderModel> arrayList;
    private GroupHeaderAdapter adapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_supporting_groups, container, false);


         // recycler
        arrayList=new ArrayList<>();
        recyclerView=(RecyclerView)view.findViewById(R.id.recycler_group_header);
        manager=new LinearLayoutManager(getActivity());
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        adapter=new GroupHeaderAdapter(arrayList,getActivity(), new OnPress() {
            @Override
            public void onClick(View view, int position) {
                startActivity(new Intent(getActivity(),CreateGroupActivity.class)
                .putExtra("type",arrayList.get(position).getType()));
                getActivity().overridePendingTransition(R.anim.fadin,R.anim.fadout);
            }
        });
        recyclerView.setLayoutManager(manager);
        recyclerView.addItemDecoration(new SpaceRecycler_V(MySizes.gethight(getActivity())/90));
        recyclerView.setAdapter(adapter);

        getrecyclerReady();



        return view;
    }

    private void getrecyclerReady() {
        arrayList.clear();
        // get user kind
        user=getArguments().getInt("user");



        String[] det2User={getResources().getString(R.string.bronze2_det)
                ,getResources().getString(R.string.silver2_det)
                ,getResources().getString(R.string.gold2_det)};

        String[] det3User={getResources().getString(R.string.bronze3_det)
                ,getResources().getString(R.string.silver3_det)
                ,getResources().getString(R.string.gold3_det)};

        String type2User[]={"1","2","3"};
        String type3User[]={"4","5","6"};

        for (int i=0;i<3;i++) {

            GroupHeaderModel model=new GroupHeaderModel();


            if (user == 2) {
                model.setDetails(det2User[i]);
                model.setType(type2User[i]);
                model.setImage(R.drawable.towusers);

            } else if (user == 3) {
                model.setDetails(det3User[i]);
                model.setType(type3User[i]);
                model.setImage(R.drawable.threeusers);
            }
            arrayList.add(model);
            adapter.notifyDataSetChanged();
        }
    }



}
