package com.egypt.supporting.supportingonline;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.annotation.SuppressLint;
import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.egypt.supporting.supportingonline.Adapters.InfoAdapter;
import com.egypt.supporting.supportingonline.Adapters.ViewPagerAdapterNormal;
import com.egypt.supporting.supportingonline.Custom.MyRequest;
import com.egypt.supporting.supportingonline.Custom.MySharedPref;
import com.egypt.supporting.supportingonline.Custom.MySizes;
import com.egypt.supporting.supportingonline.Custom.Myvollysinglton;
import com.egypt.supporting.supportingonline.Custom.SpaceRecycler_V;
import com.egypt.supporting.supportingonline.Models.InfoModel;
import com.egypt.supporting.supportingonline.Models.SearchModel;
import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.google.android.material.tabs.TabLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class MyprofileActivity extends AppCompatActivity {

    private  String domain,token;
    private RecyclerView inforecycler;
    private LinearLayoutManager infomanager;
    private ArrayList<InfoModel> infolist=new ArrayList<>();
    private InfoAdapter infoAdapter;
    private ImageView coverimage;
    private CircleImageView profileimage;
    public static TextView textname;
    private boolean IsImageDefualt;

    private TabLayout tableLayout;
    private ViewPager viewPager;
    private ViewPagerAdapterNormal adapter;
    public static ArrayList<Fragment> fragments=new ArrayList<>();
    private ArrayList<String> titles=new ArrayList<>();

    public static ArrayList<SearchModel> friends=new ArrayList<>();
    public static ArrayList<String> images=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myprofile);



        // keyboard hedin
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        images.clear();
        friends.clear();
        fragments.clear();

        // domain and token
        domain=getResources().getString(R.string.domain);
        token= MySharedPref.getdata(this,"token");

        // name
        textname=(TextView)findViewById(R.id.my_profile_name);
        textname.setText(MySharedPref.getdata(this,"name"));

        // info recycler
        inforecycler=(RecyclerView)findViewById(R.id.recycler_info2);
        infomanager=new LinearLayoutManager(this);
        infomanager.setOrientation(LinearLayoutManager.VERTICAL);
        infoAdapter=new InfoAdapter(infolist);
        inforecycler.setLayoutManager(infomanager);
        inforecycler.addItemDecoration(new SpaceRecycler_V(MySizes.gethight(this)/120));
        inforecycler.addItemDecoration(new DividerItemDecoration(inforecycler.getContext(), DividerItemDecoration.VERTICAL));
        inforecycler.setAdapter(infoAdapter);

        // images

        coverimage=(ImageView)findViewById(R.id.my_timeline_image);
        if (MySharedPref.getdata(MyprofileActivity.this,"profile_img").equals("default.png")){
            IsImageDefualt=true;
            coverimage.setBackgroundResource(R.drawable.defult);
        }else {

            Glide.with(this).load(domain+"imgs/profiles/"+MySharedPref.getdata(MyprofileActivity.this,"profile_img"))
                   .fitCenter().into(coverimage);
        }

        coverimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MyprofileActivity.this,FullImageActivity.class)
                        .putExtra("is_me",true)
                        .putExtra("type","timeline")
                        .putExtra("image_url",domain+"imgs/profiles/"+MySharedPref.getdata(MyprofileActivity.this,"profile_img"))
                        .putExtra("is_default",IsImageDefualt);

                ActivityOptions options = ActivityOptions
                        .makeSceneTransitionAnimation(MyprofileActivity.this,
                                coverimage, "trans");
                startActivity(intent,options.toBundle());
            }
        });
        profileimage=(CircleImageView)findViewById(R.id.my_profile_image);
        Glide.with(this).load(MySharedPref.getdata(MyprofileActivity.this,"image"))
                .into(profileimage);
        profileimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MyprofileActivity.this,FullImageActivity.class)
                        .putExtra("is_me",true)
                        .putExtra("type","profile")
                        .putExtra("image_url",MySharedPref.getdata(MyprofileActivity.this,"image"));

                ActivityOptions options = ActivityOptions
                        .makeSceneTransitionAnimation(MyprofileActivity.this,
                                profileimage, "trans");
                startActivity(intent,options.toBundle());
            }
        });

        loadInfo();
    }

    private void loadInfo(){
        String url=domain+"api/user_profile";
        StringRequest request=new MyRequest(token, 1, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
               //   Log.i("my",response);

                try {
                    JSONObject object=new JSONObject(response);
                    JSONObject sucobject=object.getJSONObject("success");
                    JSONObject dataobject=sucobject.getJSONObject("data");
                    JSONObject userobject=dataobject.getJSONObject("user");

                    // friends
                    JSONArray friendsArray=sucobject.getJSONArray("my_friends");
                    for (int i=0;i<friendsArray.length();i++){
                        JSONObject obj=friendsArray.getJSONObject(i);
                        String id=obj.getString("id");
                        String name=obj.getString("name");
                        String image=obj.getString("image");
                        SearchModel model=new SearchModel();
                        model.setId(id);
                        model.setImage(image);
                        model.setName(name);
                        friends.add(model);

                    }



                    // info

                    String countfoloowers=dataobject.getString("followers");
                    String countfriend=dataobject.getString("friends");
                    String countviwers=dataobject.getString("viewers");

                    InfoModel model1=new InfoModel();
                    model1.setTitle("followers");
                    model1.setCount(countfoloowers);

                    InfoModel model2=new InfoModel();
                    model2.setTitle("friends");
                    model2.setCount(countfriend);

                    InfoModel model3=new InfoModel();
                    model3.setTitle("viewers");
                    model3.setCount(countviwers);

                    infolist.add(model1);
                    infolist.add(model2);
                    infolist.add(model3);
                    infoAdapter.notifyDataSetChanged();

                    // load posts
                    // loadPosts(domain+"api/posts");
                    getTabReady();


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {


            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String>  map=new HashMap<>();
                map.put("user_id",MySharedPref.getdata(MyprofileActivity.this,"id"));

                return map;
            }
        };

        Myvollysinglton.getInstance(this).addtorequst(request);




    }

    @SuppressLint("ClickableViewAccessibility")
    private void getTabReady(){

        Bundle bundle=new Bundle();
        bundle.putString("id",MySharedPref.getdata(MyprofileActivity.this,"id"));
        bundle.putString("name",MySharedPref.getdata(MyprofileActivity.this,"name"));
        bundle.putString("domain",domain);
        bundle.putString("token",token);
        bundle.putString("image",MySharedPref.getdata(MyprofileActivity.this,"image"));
        bundle.putBoolean("is_me",true);

        // posts
        PostsFragment postsFragment=new PostsFragment();
        postsFragment.setArguments(bundle);

        // images
        ImagesFragment imagesFragment=new ImagesFragment();
        imagesFragment.setArguments(bundle);

        // setting
        FriendsFragment friendsFragment=new FriendsFragment();
        friendsFragment.setArguments(bundle);

        AccountSettingFragment accountSettingFragment=new AccountSettingFragment();



        // fragments
        fragments.clear();
        fragments.add(postsFragment);
        fragments.add(imagesFragment);
        fragments.add(friendsFragment);
        fragments.add(accountSettingFragment);


        // titles

        titles.add(getResources().getString(R.string.posts));
        titles.add(getResources().getString(R.string.photos));
        titles.add(getResources().getString(R.string.friends));
        titles.add(getResources().getString(R.string.setting));

        // tablayout and viewpager
        viewPager=(ViewPager)findViewById(R.id.my_viewpager);
        tableLayout=(TabLayout) findViewById(R.id.my_tablayout);
        adapter=new ViewPagerAdapterNormal(MyprofileActivity.this,getSupportFragmentManager(),fragments,titles);
        viewPager.setAdapter(adapter);
        tableLayout.setupWithViewPager(viewPager);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_from_righ,R.anim.slide_to_left);

    }
}
