package com.egypt.supporting.supportingonline;


import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import net.skoumal.fragmentback.BackFragment;


/**
 * A simple {@link Fragment} subclass.
 */
public class SupportingFragment extends Fragment implements BackFragment {

    private RelativeLayout main,tow,three;
    private static int pos;
    private SwipeRefreshLayout swipe;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_supporting, container, false);

        main=(RelativeLayout)view.findViewById(R.id.sup_main);
        tow=(RelativeLayout)view.findViewById(R.id.sup_tow);
        three=(RelativeLayout)view.findViewById(R.id.sup_three);

        // main

        main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (pos!=0){
                    main.setBackgroundColor(getResources().getColor(R.color.dark));
                    tow.setBackgroundColor(getResources().getColor(R.color.dark2));
                    three.setBackgroundColor(getResources().getColor(R.color.dark2));
                    changefragment(new SupportingMainFragment());
                }
                pos=0;
            }
        });

        // tow

        tow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (pos!=1){
                    main.setBackgroundColor(getResources().getColor(R.color.dark2));
                    tow.setBackgroundColor(getResources().getColor(R.color.dark));
                    three.setBackgroundColor(getResources().getColor(R.color.dark2));
                    SupportingGroupsFragment fragment=new SupportingGroupsFragment();
                    Bundle bundle=new Bundle();
                    bundle.putInt("user",2);
                    fragment.setArguments(bundle);
                    changefragment(fragment);
                }
                pos=1;
            }
        });

        // three

        three.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (pos!=2){
                    main.setBackgroundColor(getResources().getColor(R.color.dark2));
                    tow.setBackgroundColor(getResources().getColor(R.color.dark2));
                    three.setBackgroundColor(getResources().getColor(R.color.dark));
                    SupportingGroupsFragment fragment=new SupportingGroupsFragment();
                    Bundle bundle=new Bundle();
                    bundle.putInt("user",3);
                    fragment.setArguments(bundle);
                    changefragment(fragment);
                }
                pos=2;
            }
        });

        // main fragment
        main.setBackgroundColor(getResources().getColor(R.color.dark));
        changefragment(new SupportingMainFragment());
        pos=0;


        // swipe
        swipe=(SwipeRefreshLayout)view.findViewById(R.id.sup_swipe);
        swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (pos==0){
                    reloadData();
                    reloadInfo();
                    swipe.setRefreshing(false);
                }
            }
        });

        return view;
    }

    public void changefragment(Fragment fragment) {
        FragmentTransaction ft = getChildFragmentManager().beginTransaction();
        ft.setCustomAnimations(R.anim.fadin,R.anim.fadout);
        ft.replace(R.id.sup_container, fragment, "fragment");
        ft.commit();

    }

    @Override
    public boolean onBackPressed() {
        if (pos==0){
            return false;
        }else {
            changefragment(new SupportingMainFragment());
            main.setBackgroundColor(getResources().getColor(R.color.dark));
            tow.setBackgroundColor(getResources().getColor(R.color.dark2));
            three.setBackgroundColor(getResources().getColor(R.color.dark2));
            changefragment(new SupportingMainFragment());
            pos=0;
            return true;
        }
    }

    public void reloadData(){
        FragmentManager fm = getChildFragmentManager();
        SupportingMainFragment fragm = (SupportingMainFragment) fm.findFragmentById(R.id.sup_container);
        fragm.loadGroups();
    }
    public void reloadWallet(){
        FragmentManager fm = getChildFragmentManager();
        SupportingMainFragment fragm = (SupportingMainFragment) fm.findFragmentById(R.id.sup_container);
        fragm.refreshTextWallet();
    }

    public void reloadInfo(){
        ((HomeActivity)getActivity()).updateUserInfo();
    }



    @Override
    public int getBackPriority() {
        return 0;
    }
}
