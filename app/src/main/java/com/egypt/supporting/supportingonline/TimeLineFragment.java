package com.egypt.supporting.supportingonline;


import android.app.ActivityOptions;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.egypt.supporting.supportingonline.Adapters.PeopleYouMayAdapter;
import com.egypt.supporting.supportingonline.Adapters.PostAdapter;
import com.egypt.supporting.supportingonline.Calls.ErrorCall;
import com.egypt.supporting.supportingonline.Calls.OnPress;
import com.egypt.supporting.supportingonline.Calls.OnPressView;
import com.egypt.supporting.supportingonline.Custom.MyRequest;
import com.egypt.supporting.supportingonline.Custom.MySizes;
import com.egypt.supporting.supportingonline.Custom.Myvollysinglton;
import com.egypt.supporting.supportingonline.Custom.OnErrorRequest;
import com.egypt.supporting.supportingonline.Custom.SpaceRecycler_V;
import com.egypt.supporting.supportingonline.Dialogs.DialogShare;
import com.egypt.supporting.supportingonline.Models.PeopleYouMayKnowModel;
import com.egypt.supporting.supportingonline.Models.PostModel;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;


/**
 * A simple {@link Fragment} subclass.
 */
public class TimeLineFragment extends Fragment {


    private EditText editText;
    private String nexturl,lasturl;
    private int c=0;


    private RecyclerView recyclerView;
    private PostAdapter adapter;

    private RecyclerView recyclerViewPeople;
    private PeopleYouMayAdapter peopleAdapter;
    private TextView textpeople;

    public NestedScrollView nestedScrollView;
    private ProgressBar progressBar;

    private SwipeRefreshLayout swipe;

    private CircleImageView imageView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_time_line, container, false);




        // image
        imageView=(CircleImageView)view.findViewById(R.id.f_timeline_image);
        Glide.with(getActivity()).load(HomeActivity.myimage).into(imageView);


        // edit
        editText=(EditText)view.findViewById(R.id.f_timeline_edit);
        editText.setFocusable(false);
        editText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getActivity(),WritePostActivity.class);
                ActivityOptions options = ActivityOptions
                        .makeSceneTransitionAnimation(getActivity(),
                                view, "trans");
                startActivity(intent,options.toBundle());

            }
        });

        // progress
        progressBar=(ProgressBar)view.findViewById(R.id.f_timeline_progress);
        progressBar.setVisibility(View.GONE);

        // recycler posts

        recyclerView=(RecyclerView)view.findViewById(R.id.recycler_timeline);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(),RecyclerView.VERTICAL,false));

        adapter=new PostAdapter(HomeActivity.postsList, getActivity(), new OnPress() {
            @Override
            public void onClick(View view, int position) {
                // profile
                if (HomeActivity.postsList.get(position).getUser_id().equals(HomeActivity.myid)){
                    startActivity(new Intent(getActivity(),MyprofileActivity.class));
                }else {
                    startActivity(new Intent(getActivity(), OtherProfileActivity.class)
                            .putExtra("id", HomeActivity.postsList.get(position).getUser_id())
                    .putExtra("name",HomeActivity.postsList.get(position).getName()));
                }
                getActivity().overridePendingTransition(R.anim.slide_from_left,R.anim.slide_to_right);
            }

        }, new OnPress() {
            @Override
            public void onClick(View view, int position) {
                // image

                Intent intent=new Intent(getActivity(),FullImageActivity.class)
                        .putExtra("is_me",false)
                        .putExtra("image_url",HomeActivity.domain+"imgs/posts/"+HomeActivity.postsList.get(position).getImage())
                        .putExtra("type","post");
                ActivityOptions options = ActivityOptions
                        .makeSceneTransitionAnimation(getActivity(),
                                view, "trans");
                startActivity(intent,options.toBundle());
            }
        }, new OnPress() {
            @Override
            public void onClick(View view, int position) {
                Myvollysinglton.cancel("wow");
                // wow
               boolean isWawed=HomeActivity.postsList.get(position).isIs_wowed();
               // count wow
                int c=Integer.parseInt(HomeActivity.postsList.get(position).getCount_wow());
                int newC=0;
                if (isWawed){
                    newC=c-1;
                }else {
                    newC=c+1;
                }

                HomeActivity.postsList.get(position).setCount_wow(String.valueOf(newC));
                HomeActivity.postsList.get(position).setIs_wowed(!isWawed);
                adapter.notifyItemChanged(position);

                // wow server
                wowToServer(HomeActivity.postsList.get(position).getId());
            }
        }, new OnPress() {
            @Override
            public void onClick(View view, int position) {
                //comment
                upcommentactivety(HomeActivity.postsList.get(position).getId(),position);
            }
        }, new OnPress() {
            @Override
            public void onClick(View view, int position) {

                //share


                openshare(HomeActivity.postsList.get(position).getId(),HomeActivity.postsList.get(position).getText(),
                        HomeActivity.postsList.get(position).getImage(),
                        HomeActivity.postsList.get(position).getType());

            }
        });

        recyclerView.addItemDecoration(new SpaceRecycler_V(MySizes.gethight(getActivity())/80));
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();


       // nested
        nestedScrollView=(NestedScrollView)view.findViewById(R.id.f_timeline_nested);
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

        // swipe refresh
        swipe=(SwipeRefreshLayout)view.findViewById(R.id.f_timeline_swipe);
        swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                HomeActivity.postsList.clear();
                loadPosts(HomeActivity.domain + "api/posts");

            }
        });



        // load posts
        if (HomeActivity.postsList.size()==0) {
            loadPosts(HomeActivity.domain + "api/posts");
        }



        // text people
        textpeople=(TextView)view.findViewById(R.id.f_timeline_textpeople);
        textpeople.setVisibility(View.GONE);

        // recycler people
        recyclerViewPeople=(RecyclerView)view.findViewById(R.id.recycler_people_may_know);
        peopleAdapter=new PeopleYouMayAdapter(HomeActivity.peopleYouMayKnowList, getActivity(), new OnPress() {
            @Override
            public void onClick(View view, int position) {
                startActivity(new Intent(getActivity(),OtherProfileActivity.class)
                        .putExtra("id",HomeActivity.peopleYouMayKnowList.get(position).getId())
                .putExtra("name",HomeActivity.peopleYouMayKnowList.get(position).getName()));
                getActivity().overridePendingTransition(R.anim.slide_from_left,R.anim.slide_to_right);
            }
        });
        recyclerViewPeople.setLayoutManager(new LinearLayoutManager(getActivity(),RecyclerView.HORIZONTAL,false));
        recyclerViewPeople.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.HORIZONTAL));
        recyclerViewPeople.setAdapter(peopleAdapter);
        if (HomeActivity.peopleYouMayKnowList.size()==0){
            loadPeopleYouMayKnow();
        }else {
            textpeople.setVisibility(View.VISIBLE);
        }

        return view;
    }

    private void loadPeopleYouMayKnow() {
        String url=HomeActivity.domain+"api/peoples";
        StringRequest request=new MyRequest(HomeActivity.token, 1, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("people",response);
                try {
                    JSONObject object=new JSONObject(response);
                    JSONArray array=object.getJSONArray("success");
                    HomeActivity.peopleYouMayKnowList.clear();
                    for (int i= 0;i<array.length();i++){
                        JSONObject userObject=array.getJSONObject(i);
                        String id=userObject.getString("id");
                        String image=userObject.getString("image");
                        String name=userObject.getString("name");
                        String mutual=userObject.getString("mutual_friends");

                        PeopleYouMayKnowModel model=new PeopleYouMayKnowModel();
                        model.setId(id);
                        model.setName(name);
                        model.setImage(image);
                        model.setMutual(mutual);
                        HomeActivity.peopleYouMayKnowList.add(model);
                        peopleAdapter.notifyDataSetChanged();
                        textpeople.setVisibility(View.VISIBLE);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        },new OnErrorRequest(getActivity(), new ErrorCall() {
            @Override
            public void OnBack() {

            }
        }));
        Myvollysinglton.getInstance(getActivity()).addtorequst(request);
    }

    private void openshare(String id, String text,String image,String type) {

        DialogShare dialog=new DialogShare(getActivity(),text,image,type, new OnPressView() {
            @Override
            public void onclick(View view) {
                share(id);

            }
        });
        Window window = dialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();

        wlp.gravity = Gravity.BOTTOM;
        window.setAttributes(wlp);

        dialog.getWindow().getAttributes().windowAnimations=R.style.dialogshareanimation;
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.show();
    }

    private void share(String id){
        HomeActivity.progressDialog.show();

        String url=HomeActivity.domain+"api/share";
        StringRequest request=new MyRequest(HomeActivity.token, 1, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject object=new JSONObject(response);
                    if (object.has("success")){
                        HomeActivity.progressDialog.dismiss();
                        HomeActivity.postsList.clear();
                        loadPosts(HomeActivity.domain + "api/posts");
                        scrolltoabove();

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

    private void loadPosts(String url){
        // progress
        progressBar.setVisibility(View.VISIBLE);

        StringRequest request=new MyRequest(HomeActivity.token, Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
               // Log.i("rr",response.toString());
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

                            // Log.i("rr","hhhhhhhhhhhhhhh");
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


                            // user object
                            JSONObject userobject = postoObject.getJSONObject("user");

                            String username = userobject.getString("name");
                            String userimage = userobject.getString("image");
                          //  Log.i("image",userimage);

                            PostModel model = new PostModel();
                            model.setId(id);
                            model.setUser_id(user_id);
                            model.setName(username);
                            model.setDate(date);
                            model.setCount_comment(countcomment);
                            model.setText(text);
                            model.setCount_wow(countwow);
                            model.setType(type);
                            model.setImage(postlink);
                            model.setIm_profile(userimage);
                            if (!countmylike.equals("0")){
                                model.setIs_wowed(true);
                            }

                            HomeActivity.postsList.add(model);
                            adapter.notifyDataSetChanged();
                            // progress
                            progressBar.setVisibility(View.GONE);
                            swipe.setRefreshing(false);

                        }
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
                swipe.setRefreshing(false);
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String>  map=new HashMap<>();
                map.put("note","timeline");
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
        intent.putExtra("is_timeline",true);
        intent.putExtra("position",position);
        startActivity(intent);
        getActivity().overridePendingTransition(R.anim.slide_up,R.anim.fadout);
    }

    public void addOneToComment(int position){
        PostModel model=HomeActivity.postsList.get(position);
        int peviros=Integer.parseInt(model.getCount_comment());
        int now=peviros+1;
        model.setCount_comment(String.valueOf(now));
        adapter.notifyItemChanged(position);
    }


    public void reload(){
        nexturl=null;
        lasturl=null;
        HomeActivity.postsList.clear();
        adapter.notifyDataSetChanged();
        loadPosts(HomeActivity.domain+"api/posts");
    }


    public void scrolltoabove(){

        nestedScrollView.fullScroll(View.FOCUS_UP);
      //  nestedScrollView.smoothScrollTo(0,0);

    }

}
