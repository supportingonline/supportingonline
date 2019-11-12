package com.egypt.supporting.supportingonline;


import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;

import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.egypt.supporting.supportingonline.Adapters.PostAdapter;
import com.egypt.supporting.supportingonline.Calls.ErrorCall;
import com.egypt.supporting.supportingonline.Calls.OnPress;
import com.egypt.supporting.supportingonline.Calls.OnPressView;
import com.egypt.supporting.supportingonline.Custom.MyRequest;
import com.egypt.supporting.supportingonline.Custom.MySizes;
import com.egypt.supporting.supportingonline.Custom.Myvollysinglton;
import com.egypt.supporting.supportingonline.Custom.OnErrorRequest;
import com.egypt.supporting.supportingonline.Custom.SpaceRecycler_V;
import com.egypt.supporting.supportingonline.Dialogs.DialogShareBottomSheet;
import com.egypt.supporting.supportingonline.Models.PostModel;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
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
public class PostsFragment extends Fragment {


    private ArrayList<PostModel> arrayList;
    private RecyclerView recyclerView;
    private LinearLayoutManager manager;
    private PostAdapter adapter;
    private ProgressBar progressBar;
    private String token,domain,id,image,name,nexturl,lasturl;
    private NestedScrollView nestedScrollView;
    private int c=0;
    private boolean isMe=false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_posts, container, false);

        token=getArguments().getString("token");
        domain=getArguments().getString("domain");
        id=getArguments().getString("id");
        name=getArguments().getString("name");
        image=getArguments().getString("image");
        isMe=getArguments().getBoolean("is_me");

        // recycler
        arrayList=new ArrayList<>();
        recyclerView=(RecyclerView)view.findViewById(R.id.recycler_posts);
        manager=new LinearLayoutManager(getActivity());
        manager.setOrientation(RecyclerView.VERTICAL);
        adapter=new PostAdapter(arrayList, getActivity(), new OnPress() {
            @Override
            public void onClick(View view, int position) {
                // profile

            }
        }, new OnPress() {
            @Override
            public void onClick(View view, int position) {
                //image
                Intent intent=new Intent(getActivity(),FullImageActivity.class)
                        .putExtra("is_me",false)
                        .putExtra("image_url",domain+"imgs/posts/"+arrayList.get(position).getImage()).putExtra("type","post");
                ActivityOptions options = ActivityOptions
                        .makeSceneTransitionAnimation(getActivity(),
                                view, "trans");
                startActivity(intent,options.toBundle());
            }
        }, new OnPress() {
            @Override
            public void onClick(View view, int position) {
                // wow
                boolean isWawed=arrayList.get(position).isIs_wowed();
                // count wow
                int c=Integer.parseInt(arrayList.get(position).getCount_wow());
                int newC=0;
                if (isWawed){
                    newC=c-1;
                }else {
                    newC=c+1;
                }

                arrayList.get(position).setCount_wow(String.valueOf(newC));
                arrayList.get(position).setIs_wowed(!isWawed);
                adapter.notifyItemChanged(position);

                // wow server
                Myvollysinglton.cancel("wow");
                wowToServer(arrayList.get(position).getId());
            }
        }, new OnPress() {
            @Override
            public void onClick(View view, int position) {
                //comment
                upcommentactivety(arrayList.get(position).getId(),position);
            }
        }, new OnPress() {
            @Override
            public void onClick(View view, int position) {
                //share
                openshare(arrayList.get(position).getId(),arrayList.get(position).getText(),arrayList.get(position).getImage(),
                        arrayList.get(position).getType());
            }
        });
        recyclerView.setLayoutManager(manager);
        recyclerView.addItemDecoration(new SpaceRecycler_V(MySizes.gethight(getActivity())/80));
        recyclerView.setAdapter(adapter);

        // progress
        progressBar=(ProgressBar)view.findViewById(R.id.posts_progress);
        progressBar.setVisibility(View.GONE);

        // nested
        nestedScrollView=(NestedScrollView)view.findViewById(R.id.posts_scroll);
        nestedScrollView.setOnScrollChangeListener((NestedScrollView.OnScrollChangeListener) (v, scrollX, scrollY, oldScrollX, oldScrollY) -> {
            if(v.getChildAt(v.getChildCount() - 1) != null) {
                if ((scrollY >= (v.getChildAt(v.getChildCount() - 1).getMeasuredHeight() - v.getMeasuredHeight())) &&
                        scrollY > oldScrollY) {
                    //code to fetch more data for endless scrolling
                    //Toast.makeText(getActivity(), "End", Toast.LENGTH_SHORT).show();
                    if (c!=1) {
                        loadPosts(nexturl);
                    }

                }
            }
        });


        loadPosts(domain+"api/posts");

        return view;
    }

    private void loadPosts(String url){

        // progress
        progressBar.setVisibility(View.VISIBLE);

        StringRequest request=new MyRequest(token, Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressBar.setVisibility(View.GONE);
          //  Log.i("rr",response);
                try {
                    JSONObject object = new JSONObject(response);
                    if (object.has("success")) {
                        JSONObject jsonObject = object.getJSONObject("success");
                       // Log.i("rr",jsonObject.toString());
                        // next and last url
                        nexturl = jsonObject.getString("next_page_url");
                        lasturl = jsonObject.getString("last_page_url");
                        if (nexturl.equals(lasturl)) {
                            c = 1;
                        }
                        //  Log.i("rr",nexturl);

                        // array of data
                        JSONArray array = jsonObject.getJSONArray("data");
                        for (int i = 0; i < array.length(); i++) {

                           //Log.i("rr","hhhhhhhhhhhhhhh");
                            // inner object data
                            // Log.i("post",array.get(i).toString());
                            JSONObject postoObject = array.getJSONObject(i);
                            String id = postoObject.getString("id");
                            String user_id = postoObject.getString("user_id");
                            String text = postoObject.getString("text");
                            String postlink = postoObject.getString("post_link");
                            String type = postoObject.getString("type");

                            String date = postoObject.getString("updated_at");
                              String countwow = postoObject.getString("likes_count");
                             String countcomment = postoObject.getString("comments_count");
                             String countmylike = postoObject.getString("my_like_count");




                            PostModel model = new PostModel();
                            model.setId(id);
                            model.setUser_id(user_id);
                            model.setName(name);
                            model.setDate(date);
                             model.setCount_comment(countcomment);
                            model.setText(text);
                             model.setCount_wow(countwow);
                            model.setType(type);
                            model.setImage(postlink);
                            model.setIm_profile(image);
                            if (!countmylike.equals("0")) {
                                model.setIs_wowed(true);
                            }


                            arrayList.add(model);
                            adapter.notifyDataSetChanged();


                        }


                        // progress
                      //  progressBar.setVisibility(View.GONE);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // progress
                progressBar.setVisibility(View.GONE);
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String>  map=new HashMap<>();
                map.put("note","profile");
                map.put("user_id",id);

                return map;
            }
        };
        Myvollysinglton.getInstance(getActivity()).addtorequst(request);
    }

    private void wowToServer(String post_id){
        String url=HomeActivity.domain+"api/post_like";
        StringRequest request=new MyRequest(HomeActivity.token, 1, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

            }
        },new OnErrorRequest(getActivity(), new ErrorCall() {
            @Override
            public void OnBack() {

            }
        })){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String>  map=new HashMap<>();
                map.put("post_id",post_id);

                return map;
            }
        };
        request.setTag("wow");
        Myvollysinglton.getInstance(getActivity()).addtorequst(request);
    }

    private void upcommentactivety(String post_id,int position){
        Intent intent=new Intent(getActivity(),CommentActivity.class);
        intent.putExtra("post_id",post_id);
        intent.putExtra("is_timeline",false);
        intent.putExtra("is_me",isMe);
        intent.putExtra("position",position);
        startActivity(intent);
        getActivity().overridePendingTransition(R.anim.slide_up,R.anim.fadout);
    }

    public void addOneToComment(int position){
        PostModel model=arrayList.get(position);
        int peviros=Integer.parseInt(model.getCount_comment());
        int now=peviros+1;
        model.setCount_comment(String.valueOf(now));
        adapter.notifyItemChanged(position);
    }

    private void openshare(String id, String text,String image,String type) {



        DialogShareBottomSheet addPhotoBottomDialogFragment =
                new DialogShareBottomSheet(new OnPressView() {
                    @Override
                    public void onclick(View view) {
                        share(id);
                    }
                },getActivity(),text,type,image);
        assert getFragmentManager() != null;
        addPhotoBottomDialogFragment.show(getFragmentManager(),"");
    }

    private void share(String id){

        String url=HomeActivity.domain+"api/share";
        StringRequest request=new MyRequest(HomeActivity.token, 1, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject object=new JSONObject(response);
                    if (object.has("success")){

                        Toast.makeText(getActivity(), "success", Toast.LENGTH_SHORT).show();
                       getActivity().onBackPressed();


                    }
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
                Map<String,String>  map=new HashMap<>();
                map.put("post_id",id);

                return map;
            }
        };
        Myvollysinglton.getInstance(getActivity()).addtorequst(request);


    }


}
