package com.egypt.supporting.supportingonline;


import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.egypt.supporting.supportingonline.Adapters.ImagesAdapter;
import com.egypt.supporting.supportingonline.Calls.ErrorCall;
import com.egypt.supporting.supportingonline.Calls.OnPress;
import com.egypt.supporting.supportingonline.Custom.MyRequest;
import com.egypt.supporting.supportingonline.Custom.Myvollysinglton;
import com.egypt.supporting.supportingonline.Custom.OnErrorRequest;
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
public class ImagesFragment extends Fragment {

    private ArrayList<String> arrayList=new ArrayList<>();
    private RecyclerView recyclerView;
    private ImagesAdapter adapter;

    private boolean isMe;
    private String domain,token,id;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_images, container, false);

        isMe=getArguments().getBoolean("is_me");
        domain=getArguments().getString("domain");
        token=getArguments().getString("token");
        id=getArguments().getString("id");

        arrayList.clear();

        // recycler
        recyclerView=(RecyclerView)view.findViewById(R.id.recycler_photos);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(),3));

        adapter=new ImagesAdapter(arrayList, getActivity(), new OnPress() {
            @Override
            public void onClick(View view, int position) {
                Intent intent=new Intent(getActivity(),FullImageActivity.class)
                        .putExtra("is_me",false)
                        .putExtra("image_url",domain+"imgs/posts/"+arrayList.get(position)).putExtra("type","post");
                ActivityOptions options = ActivityOptions
                        .makeSceneTransitionAnimation(getActivity(),
                                view, "trans");
                startActivity(intent,options.toBundle());
            }
        });
        recyclerView.setAdapter(adapter);

        if (isMe){
            if (MyprofileActivity.images.size()==0){
                reload();
            }else {
                arrayList.addAll(MyprofileActivity.images);
                adapter.notifyDataSetChanged();
            }
        }else {
            if (OtherProfileActivity.images.size()==0){
                reload();
            }else {
                arrayList.addAll(OtherProfileActivity.images);
                adapter.notifyDataSetChanged();
            }
        }

        return view;
    }



    public void reload(){
        String url=domain+"api/posts_images";
        StringRequest request=new MyRequest(token, 1, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
               // Log.i("image",response);
                try {
                    JSONObject object=new JSONObject(response);
                    JSONArray array=object.getJSONArray("success");
                    arrayList.clear();
                    if (isMe){
                        MyprofileActivity.images.clear();


                    }else {
                        OtherProfileActivity.images.clear();
                    }
                    for (int i=0;i<array.length();i++){
                        JSONObject theObject=array.getJSONObject(i);
                        String image=theObject.getString("post_link");

                        if (isMe){
                            MyprofileActivity.images.add(image);
                        }else {
                            OtherProfileActivity.images.add(image);
                        }
                    }

                    if (isMe){
                       arrayList.addAll(MyprofileActivity.images);
                    }else {
                        arrayList.addAll(OtherProfileActivity.images);
                    }

                    adapter.notifyDataSetChanged();

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        },new OnErrorRequest(getActivity(), new ErrorCall() {
            @Override
            public void OnBack() {

            }
        })){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map=new HashMap<>();
                map.put("id",id);
                return map;
            }
        };
        Myvollysinglton.getInstance(getActivity()).addtorequst(request);

    }

}
