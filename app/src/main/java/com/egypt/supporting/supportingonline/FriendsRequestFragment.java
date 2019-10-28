package com.egypt.supporting.supportingonline;


import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.egypt.supporting.supportingonline.Adapters.FriendRequestAdpater;
import com.egypt.supporting.supportingonline.Calls.ErrorCall;
import com.egypt.supporting.supportingonline.Calls.OnPress;
import com.egypt.supporting.supportingonline.Custom.MyRequest;
import com.egypt.supporting.supportingonline.Custom.MySizes;
import com.egypt.supporting.supportingonline.Custom.Myvollysinglton;
import com.egypt.supporting.supportingonline.Custom.OnErrorRequest;
import com.egypt.supporting.supportingonline.Custom.SpaceRecycler_V;
import com.egypt.supporting.supportingonline.Models.FriendRequestModel;
import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 */
public class FriendsRequestFragment extends Fragment {
    
    private ArrayList<FriendRequestModel> arrayList=new ArrayList<>();
    private RecyclerView recyclerView;
    private LinearLayoutManager manager;
    private FriendRequestAdpater adapter;

    private SwipeRefreshLayout swipe;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_friends_request, container, false);

        // recycler

        recyclerView=(RecyclerView)view.findViewById(R.id.recycler_friends_request);
        manager=new LinearLayoutManager(getActivity());
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        adapter=new FriendRequestAdpater(getActivity(), arrayList, new OnPress() {
            @Override
            public void onClick(View view, int position) {
                // accept
                replyRequest("accept",arrayList.get(position).getId());
                setAnimation(recyclerView.getChildAt(position),position);
            }
        }, new OnPress() {
            @Override
            public void onClick(View view, int position) {
                // cancel
                replyRequest("deny",arrayList.get(position).getId());
                setAnimation(recyclerView.getChildAt(position),position);
            }
        }, new OnPress() {
            @Override
            public void onClick(View view, int position) {
                // layout profile
                startActivity(new Intent(getActivity(),OtherProfileActivity.class)
                        .putExtra("id", arrayList.get(position).getId())
                        .putExtra("name", arrayList.get(position).getName()));
                getActivity().overridePendingTransition(R.anim.slide_from_left,R.anim.slide_to_right);
            }
        });
        recyclerView.setLayoutManager(manager);
        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL));
        recyclerView.addItemDecoration(new SpaceRecycler_V(MySizes.gethight(getActivity())/90));
        recyclerView.setAdapter(adapter);

        if (HomeActivity.requests.size()==0){
            loadRequests();
        }else {
            arrayList.clear();
            arrayList.addAll(HomeActivity.requests);
            adapter.notifyDataSetChanged();
        }

        // swipe refresh
        swipe=(SwipeRefreshLayout)view.findViewById(R.id.f_requests_swipe);
        swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                HomeActivity.requests.clear();
                loadRequests();

            }
        });


        


        return view;
    }

    private void setAnimation(View viewToAnimate, int position)
    {

            Animation animation = AnimationUtils.loadAnimation(getActivity(), R.anim.zoomoutslow);
            animation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    arrayList.remove(position);
                    adapter.notifyDataSetChanged();
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
            viewToAnimate.startAnimation(animation);



    }



    public void loadRequests() {
        String url=HomeActivity.domain+"api/get_friend_requests";
        StringRequest request=new MyRequest(HomeActivity.token, 1, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                arrayList.clear();
                HomeActivity.requests.clear();

                swipe.setRefreshing(false);

                Log.i("rrrrr",response);
                try {
                    JSONObject sucobject=new JSONObject(response);
                    JSONArray array=sucobject.getJSONArray("success");
                    for (int i=0;i<array.length();i++){
                        JSONObject object=array.getJSONObject(i);
                        String id=object.getString("id");
                        String date=object.getString("created_at");
                        String image=object.getString("image");
                        String name=object.getString("name");

                        FriendRequestModel model=new FriendRequestModel();
                        model.setId(id);
                        model.setDate(date);
                        model.setImage(image);
                        model.setName(name);

                        HomeActivity.requests.add(model);

                    }
                    arrayList.addAll(HomeActivity.requests);
                    adapter.notifyDataSetChanged();

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        },new OnErrorRequest(getActivity(), new ErrorCall() {
            @Override
            public void OnBack() {
                swipe.setRefreshing(false);

            }
        }));

        Myvollysinglton.getInstance(getActivity()).addtorequst(request);
    }

    private void replyRequest(String type,String userId){
        String url=HomeActivity.domain+"api/friend_request";
        StringRequest request=new MyRequest(HomeActivity.token, 1, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
               // Log.i("reply",response);

            }
        },new OnErrorRequest(getActivity(), new ErrorCall() {
            @Override
            public void OnBack() {

            }
        })){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map=new HashMap<>();
                map.put("type",type);
                map.put("user_id",userId);
                return map;
            }
        };
        Myvollysinglton.getInstance(getActivity()).addtorequst(request);
    }

}
