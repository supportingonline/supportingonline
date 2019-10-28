package com.egypt.supporting.supportingonline;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.egypt.supporting.supportingonline.Adapters.SearchAdapter;
import com.egypt.supporting.supportingonline.Calls.OnPress;
import com.egypt.supporting.supportingonline.Custom.MySizes;
import com.egypt.supporting.supportingonline.Custom.SpaceRecycler_V;
import com.egypt.supporting.supportingonline.Models.SearchModel;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class FriendsFragment extends Fragment {

    private boolean is_me;
    private String id,name,domain,token;
    private ArrayList<SearchModel> arrayList=new ArrayList<>();
    private RecyclerView recyclerView;
    private LinearLayoutManager manager;
    private SearchAdapter adapter;
    private TextView textView;

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_friends, container, false);





        token=getArguments().getString("token");
        domain=getArguments().getString("domain");
        id=getArguments().getString("id");
        name=getArguments().getString("name");
        is_me=getArguments().getBoolean("is_me");



        // recycler
        arrayList.clear();
        recyclerView=(RecyclerView)view.findViewById(R.id.recycler_friends);
        manager=new LinearLayoutManager(getActivity());
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        adapter=new SearchAdapter(arrayList, getActivity(), new OnPress() {
            @Override
            public void onClick(View view, int position) {

                if (!arrayList.get(position).getId().equals(HomeActivity.myid)){

                    startActivity(new Intent(getActivity(), OtherProfileActivity.class)
                            .putExtra("id", arrayList.get(position).getId())
                            .putExtra("name", arrayList.get(position).getName()));
                    getActivity().overridePendingTransition(R.anim.slide_from_left,R.anim.slide_to_right);
                }

            }
        });
        recyclerView.setLayoutManager(manager);
        recyclerView.addItemDecoration(new SpaceRecycler_V(MySizes.gethight(getActivity())/90));
        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL));
        recyclerView.setAdapter(adapter);

        if (is_me){
            arrayList.clear();
            arrayList.addAll(MyprofileActivity.friends);
            adapter.notifyDataSetChanged();

        }else {
            arrayList.clear();
            arrayList.addAll(OtherProfileActivity.friends);
            adapter.notifyDataSetChanged();
        }


        // textview
        textView=(TextView)view.findViewById(R.id.friends_text);
        textView.setText(String.valueOf(arrayList.size())+ " "+getActivity().getResources().getString(R.string.friends));

        return view;
    }

}
