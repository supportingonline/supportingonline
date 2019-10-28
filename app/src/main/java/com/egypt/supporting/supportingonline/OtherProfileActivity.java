package com.egypt.supporting.supportingonline;

import android.annotation.SuppressLint;
import android.app.ActivityOptions;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;

import com.egypt.supporting.supportingonline.Adapters.AboutAdapter;
import com.egypt.supporting.supportingonline.Adapters.InfoAdapter;
import com.egypt.supporting.supportingonline.Adapters.ViewPagerAdapterNormal;
import com.egypt.supporting.supportingonline.Calls.ErrorCall;
import com.egypt.supporting.supportingonline.Custom.MyRequest;
import com.egypt.supporting.supportingonline.Custom.MySharedPref;
import com.egypt.supporting.supportingonline.Custom.MySizes;
import com.egypt.supporting.supportingonline.Custom.Myvollysinglton;
import com.egypt.supporting.supportingonline.Custom.OnErrorRequest;
import com.egypt.supporting.supportingonline.Custom.SpaceRecycler_V;
import com.egypt.supporting.supportingonline.Models.AboutModel;
import com.egypt.supporting.supportingonline.Models.InfoModel;
import com.egypt.supporting.supportingonline.Models.SearchModel;
import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.google.android.material.tabs.TabLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class OtherProfileActivity extends AppCompatActivity {

    private  String id,domain,token,hisimage,hisname,hiscover;
    private RecyclerView inforecycler;
    private LinearLayoutManager infomanager;
    private ArrayList<InfoModel> infolist=new ArrayList<>();
    private InfoAdapter infoAdapter;

    private RecyclerView aboutrecycler;
    private LinearLayoutManager aboutmanager;
    private ArrayList<AboutModel> aboutlist=new ArrayList<>();
    private AboutAdapter aboutAdapter;

    private ImageView coverimage;
    private CircleImageView profileimage;
    private boolean isfollow,isBlock,IsImageDefualt;
    private int isfriend;
    private Button btnFollow,btnMessage,btnFriend,btnBlock;
    private LinearLayout other_layoutBTNS;


    private TabLayout tableLayout;
    private ViewPager viewPager;
    private ViewPagerAdapterNormal adapter;
    public static ArrayList<Fragment> fragments=new ArrayList<>();
    private ArrayList<String> titles=new ArrayList<>();

    private TextView textname;
    public static ArrayList<SearchModel> friends=new ArrayList<>();
    public static ArrayList<String> images=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other_profile);

        // keyboard hedin
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        images.clear();
        friends.clear();
        fragments.clear();

        // id  and name
        id=getIntent().getStringExtra("id");
        hisname=getIntent().getStringExtra("name");

        // domain and token
        domain=getResources().getString(R.string.domain);
        token= MySharedPref.getdata(this,"token");

        // name
        textname=(TextView)findViewById(R.id.other_profile_name);
        textname.setText(hisname);



        // info recycler
        inforecycler=(RecyclerView)findViewById(R.id.recycler_info);
        infomanager=new LinearLayoutManager(this);
        infomanager.setOrientation(LinearLayoutManager.VERTICAL);
        infoAdapter=new InfoAdapter(infolist);
        inforecycler.setLayoutManager(infomanager);
        inforecycler.addItemDecoration(new SpaceRecycler_V(MySizes.gethight(this)/120));
        inforecycler.addItemDecoration(new DividerItemDecoration(inforecycler.getContext(), DividerItemDecoration.VERTICAL));
        inforecycler.setAdapter(infoAdapter);

        // info recycler

        aboutrecycler=(RecyclerView)findViewById(R.id.recycler_about);
        aboutmanager=new LinearLayoutManager(this);
        aboutmanager.setOrientation(LinearLayoutManager.VERTICAL);
        aboutAdapter=new AboutAdapter(aboutlist);
        aboutrecycler.setLayoutManager(aboutmanager);
        aboutrecycler.addItemDecoration(new DividerItemDecoration(aboutrecycler.getContext(), DividerItemDecoration.VERTICAL));
        aboutrecycler.setAdapter(aboutAdapter);

        // images
        coverimage=(ImageView)findViewById(R.id.other_timeline_image);
        coverimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(OtherProfileActivity.this,FullImageActivity.class)
                        .putExtra("is_me",false)
                        .putExtra("type","timeline")
                        .putExtra("image_url",domain+"imgs/profiles/"+hiscover)
                        .putExtra("is_default",IsImageDefualt);

                ActivityOptions options = ActivityOptions
                        .makeSceneTransitionAnimation(OtherProfileActivity.this,
                                coverimage, "trans");
                startActivity(intent,options.toBundle());
            }
        });

        // profile image
        profileimage=(CircleImageView)findViewById(R.id.other_profile_image);
        profileimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(OtherProfileActivity.this,FullImageActivity.class)
                        .putExtra("is_me",false)
                        .putExtra("type","profile")
                        .putExtra("image_url",hisimage);

                ActivityOptions options = ActivityOptions
                        .makeSceneTransitionAnimation(OtherProfileActivity.this,
                                profileimage, "trans");
                startActivity(intent,options.toBundle());
            }
        });




        // buttons
        btnFollow=(Button)findViewById(R.id.other_btn_follow);
        btnMessage=(Button)findViewById(R.id.other_btn_messasge);
        btnFriend=(Button)findViewById(R.id.other_btn_friend);
        btnBlock=(Button)findViewById(R.id.other_btn_block);
        other_layoutBTNS=(LinearLayout)findViewById(R.id.other_layoutBTNS);

        // follow
        btnFollow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isfollow){
                    ViewCompat.setBackgroundTintList(btnFollow, ColorStateList.valueOf(getResources().getColor(R.color.colorPrimary)));
                    btnFollow.setText(getResources().getString(R.string.follow));
                }else {
                    ViewCompat.setBackgroundTintList(btnFollow, ColorStateList.valueOf(getResources().getColor(R.color.colorAccent)));
                    btnFollow.setText(getResources().getString(R.string.unfollow));
                }
                addFriend_Follow_Server("follow");
                isfollow=!isfollow;

            }
        });

        // friend
        btnFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isfriend!=2) {
                    addFriend_Follow_Server("friend");
                    JSONObject object = new JSONObject();
                    try {
                        object.put("type", "friend");
                        object.put("status", "friend");
                        object.put("from", HomeActivity.myid);
                        object.put("to", id);
                        object.put("img", HomeActivity.myimage);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    HomeActivity.sendsocketEmmit(object);
                    if (isfriend == 0) {
                        ViewCompat.setBackgroundTintList(btnFriend, ColorStateList.valueOf(getResources().getColor(R.color.gold)));
                        btnFriend.setText(getResources().getString(R.string.waiting_for_accept));
                        btnFriend.setAlpha(0.5f);
                        btnFriend.setTextColor(getResources().getColor(R.color.colorAccent));

                    } else if (isfriend == 1) {
                        ViewCompat.setBackgroundTintList(btnFriend, ColorStateList.valueOf(getResources().getColor(R.color.colorPrimary)));
                        btnFriend.setText(getResources().getString(R.string.add_friend));
                        btnFriend.setAlpha(1f);
                        btnFriend.setTextColor(getResources().getColor(R.color.white));
                        isfriend = 0;
                    }
                }
            }
        });

        // message
        btnMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(OtherProfileActivity.this,FriendChatActivity.class)
                        .putExtra("image",hisimage)
                        .putExtra("name",hisname)
                        .putExtra("id",id));
                overridePendingTransition(R.anim.slide_from_left,R.anim.slide_to_right);
            }
        });

        // block
        btnBlock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isBlock){
                    btnBlock.setText(getResources().getString(R.string.block));
                    ViewCompat.setBackgroundTintList(btnBlock, ColorStateList.valueOf(getResources().getColor(R.color.colorPrimary)));

                }else {
                    btnBlock.setText(getResources().getString(R.string.unBlock));
                    ViewCompat.setBackgroundTintList(btnBlock, ColorStateList.valueOf(getResources().getColor(R.color.colorAccent)));
                }
                addFriend_Follow_Server("block");
                isBlock=!isBlock;
            }
        });





        //load

        loadInfo();

    }


    private void loadInfo(){
        String url=domain+"api/user_profile";
        StringRequest request=new MyRequest(token, 1, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
           //   Log.i("profile",response);

                try {
                    JSONObject object=new JSONObject(response);
                    JSONObject sucobject=object.getJSONObject("success");
                    JSONObject dataobject=sucobject.getJSONObject("data");
                    JSONObject userobject=dataobject.getJSONObject("user");

                    // images
                    hiscover=userobject.getString("profile_img");
                    hisimage=userobject.getString("image");

                   // Log.i("hisCover",hiscover);
                    if (hiscover.equals("default.png")){
                        IsImageDefualt=true;
                        coverimage.setBackgroundResource(R.drawable.defult);
                    }else {
                        Glide.with(OtherProfileActivity.this).load(domain + "imgs/profiles/" + hiscover).error(R.drawable.ic_error).into(coverimage);
                    }
                    Glide.with(OtherProfileActivity.this).load(hisimage).error(R.drawable.ic_error).into(profileimage);


                    // about

                    String education=userobject.getString("education");
                    String location=userobject.getString("location");
                    String about=userobject.getString("about");

                    String listAboutTitles[]={getResources().getString(R.string.education),getResources().getString(R.string.location),getResources().getString(R.string.about)};
                    String listAbout[]={education,location,about};
                    int iconsAbout[]={
                            R.drawable.ic_education,R.drawable.ic_location,R.drawable.ic_about
                    };
                    for (int i=0;i<listAboutTitles.length;i++){
                        Log.i("hhhhhh",listAbout[i]);
                        AboutModel model=new AboutModel();
                        model.setTitle(listAboutTitles[i]);
                        if (!listAbout[i].equals("null")){
                            model.setDetalis(listAbout[i]);
                        }
                        model.setImage(iconsAbout[i]);
                        aboutlist.add(model);

                    }
                    aboutAdapter.notifyDataSetChanged();


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


                    // buttons
                    other_layoutBTNS.setAlpha(1f);
                    isfriend=dataobject.getInt("isfriend");
                    isfollow=dataobject.getBoolean("isfollow");
                    isBlock=dataobject.getBoolean("block");

                    if (isfriend==1){
                        btnFriend.setText(getResources().getString(R.string.remove_friend));
                        ViewCompat.setBackgroundTintList(btnFriend, ColorStateList.valueOf(getResources().getColor(R.color.colorAccent)));
                    }else if (isfriend==2){
                        btnFriend.setText(getResources().getString(R.string.waiting_for_accept));
                        ViewCompat.setBackgroundTintList(btnFriend, ColorStateList.valueOf(getResources().getColor(R.color.gold)));
                        btnFriend.setAlpha(0.5f);
                        btnFriend.setTextColor(getResources().getColor(R.color.colorAccent));
                    }

                    if (isfollow){
                        btnFollow.setText(getResources().getString(R.string.unfollow));
                        ViewCompat.setBackgroundTintList(btnFollow, ColorStateList.valueOf(getResources().getColor(R.color.colorAccent)));
                    }

                    if (isBlock){
                        btnBlock.setText(getResources().getString(R.string.unBlock));
                        ViewCompat.setBackgroundTintList(btnBlock, ColorStateList.valueOf(getResources().getColor(R.color.colorAccent)));
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
                map.put("user_id",id);

                return map;
            }
        };

        Myvollysinglton.getInstance(this).addtorequst(request);




    }

    @SuppressLint("ClickableViewAccessibility")
    private void getTabReady(){

        Bundle bundle=new Bundle();
        bundle.putString("id",id);
        bundle.putString("name",hisname);
        bundle.putString("domain",domain);
        bundle.putString("token",token);
        bundle.putString("image",hisimage);
        bundle.putBoolean("is_me",false);

        // posts
        PostsFragment postsFragment=new PostsFragment();
        postsFragment.setArguments(bundle);

        // images
        ImagesFragment imagesFragment=new ImagesFragment();
        imagesFragment.setArguments(bundle);

        // setting
        FriendsFragment friendsFragment=new FriendsFragment();
        friendsFragment.setArguments(bundle);


        // fragments
        fragments.clear();
        fragments.add(postsFragment);
        fragments.add(imagesFragment);
        fragments.add(friendsFragment);


        // titles

        titles.add(getResources().getString(R.string.posts));
        titles.add(getResources().getString(R.string.photos));
        titles.add(getResources().getString(R.string.friends));

        // tablayout and viewpager
        viewPager=(ViewPager)findViewById(R.id.other_viewpager);
        tableLayout=(TabLayout) findViewById(R.id.other_tablayout);
        adapter=new ViewPagerAdapterNormal(OtherProfileActivity.this,getSupportFragmentManager(),fragments,titles);
        viewPager.setAdapter(adapter);
        tableLayout.setupWithViewPager(viewPager);

        /*viewPager.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                v.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });

        viewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                viewPager.getParent().requestDisallowInterceptTouchEvent(true);
            }
        });*/
    }


    private void addFriend_Follow_Server(String kind){
        Log.i("id",id);
       String  url=domain+"api/"+kind;
         StringRequest request=new MyRequest(token, 1, url, new Response.Listener<String>() {
             @Override
             public void onResponse(String response) {
                 Log.i("send",response);

             }
         },new OnErrorRequest(OtherProfileActivity.this, new ErrorCall() {
             @Override
             public void OnBack() {

             }
         })){
             @Override
             protected Map<String, String> getParams() throws AuthFailureError {
                 Map<String, String> map=new HashMap<>();
                 map.put("user_id",id);

                 return map;
             }
         };
         Myvollysinglton.getInstance(this).addtorequst(request);
    }




    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_from_righ,R.anim.slide_to_left);

    }
}
