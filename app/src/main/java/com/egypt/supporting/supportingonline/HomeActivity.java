package com.egypt.supporting.supportingonline;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.app.ActivityOptions;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


import com.egypt.supporting.supportingonline.Adapters.MenuAdapter;
import com.egypt.supporting.supportingonline.Adapters.ViewPagerAdapter;
import com.egypt.supporting.supportingonline.Calls.ErrorCall;
import com.egypt.supporting.supportingonline.Calls.OnPress;
import com.egypt.supporting.supportingonline.Custom.MyRequest;
import com.egypt.supporting.supportingonline.Custom.MyShare;
import com.egypt.supporting.supportingonline.Custom.MySharedPref;
import com.egypt.supporting.supportingonline.Custom.MySizes;
import com.egypt.supporting.supportingonline.Custom.Myvollysinglton;
import com.egypt.supporting.supportingonline.Custom.OnErrorRequest;
import com.egypt.supporting.supportingonline.Models.ChatHistroyModel;
import com.egypt.supporting.supportingonline.Services.RestartMyService;
import com.egypt.supporting.supportingonline.Custom.SpaceRecycler_V;
import com.egypt.supporting.supportingonline.Dialogs.DialogLanguage;
import com.egypt.supporting.supportingonline.Models.FriendRequestModel;
import com.egypt.supporting.supportingonline.Models.GroupHeaderModel;
import com.egypt.supporting.supportingonline.Models.MenuModel;
import com.egypt.supporting.supportingonline.Models.PeopleYouMayKnowModel;
import com.egypt.supporting.supportingonline.Models.PostModel;
import com.egypt.supporting.supportingonline.Views.TabLayoutViews;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.facebook.login.LoginManager;
import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;
import com.twitter.sdk.android.core.DefaultLogger;
import com.twitter.sdk.android.core.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterConfig;
import com.twitter.sdk.android.core.TwitterCore;

import net.skoumal.fragmentback.BackFragmentHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity {

    public static String domain,token,myname,myimage,myimgprofile,lang,wallet,myid;

    private TabLayout tableLayout;
    private ViewPager viewPager;
    private ViewPagerAdapter adapter;
    public static ArrayList<Fragment> fragments=new ArrayList<>();

    private DrawerLayout drawer;
    private Button btn_menu;

    private RecyclerView recyclerView;
    private MenuAdapter menuadapter;
    private LinearLayoutManager manager;
    private ArrayList<MenuModel> arrayList;

    private EditText editText;
    private TextView textversion;

    private static Socket mSocket;

    public static ArrayList<PostModel> postsList=new ArrayList<>();
    public static ProgressDialog progressDialog;
    public static ArrayList<FriendRequestModel> requests=new ArrayList<>();
    public static ArrayList<GroupHeaderModel> groupshistory=new ArrayList<>();
    public static ArrayList<ChatHistroyModel> chatHistoryList=new ArrayList<>();
    public static ArrayList<PeopleYouMayKnowModel> peopleYouMayKnowList=new ArrayList<>();

    @Override
    protected void onStart() {
        super.onStart();
       RestartMyService.CheckService(this);

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        RestartMyService.CheckService(this);

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_home);



        // keyboard hedin
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        postsList.clear();
        requests.clear();
        groupshistory.clear();
        peopleYouMayKnowList.clear();
        chatHistoryList.clear();
        fragments.clear();

        // progress dialog
        progressDialog=new ProgressDialog(this);
        progressDialog.setMessage(getResources().getString(R.string.loading));




        // values static
        domain=getResources().getString(R.string.domain);
        token= MySharedPref.getdata(this,"token");
        myname=MySharedPref.getdata(this,"name");
        myimage=MySharedPref.getdata(this,"image");
        myimgprofile=MySharedPref.getdata(this,"profile_img");
        lang=MySharedPref.getdata(this,"lang");
        wallet=MySharedPref.getdata(this,"wallet");
        myid=MySharedPref.getdata(this,"id");
        //Log.i("imageeee",myimgprofile);


        // fragments


        fragments.add(new TimeLineFragment());
        fragments.add(new ChatHistoryFragment());
        fragments.add(new FriendsRequestFragment());
        fragments.add(new SupportingFragment());



        // tablayout and viewpager
        viewPager=(ViewPager)findViewById(R.id.home_viewpager);
        tableLayout=(TabLayout) findViewById(R.id.home_tablayout);
        adapter=new ViewPagerAdapter(HomeActivity.this,getSupportFragmentManager(),fragments);
        viewPager.setAdapter(adapter);
        tableLayout.setupWithViewPager(viewPager);

        // drawer
        getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
       drawer=(DrawerLayout)findViewById(R.id.home_drawer);


        // btn menu
        btn_menu=(Button)findViewById(R.id.home_menu_btn);
        btn_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              drawer.openDrawer(Gravity.LEFT);
            }
        });






        // icons


        tableLayout.getTabAt(0).setCustomView(TabLayoutViews.getTabView(this,R.drawable.ic_noti,0));


        tableLayout.getTabAt(1).setCustomView(TabLayoutViews.getTabView(this,R.drawable.ic_msg,0));


        tableLayout.getTabAt(2).setCustomView(TabLayoutViews.getTabView(this,R.drawable.ic_friend,0));

        tableLayout.getTabAt(0).select();
        TabLayoutViews.selectTab(HomeActivity.this,tableLayout.getTabAt(0).getCustomView());


        tableLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
               // Log.i("pppos", String.valueOf(tab.getPosition()));
                RestartMyService.CheckService(HomeActivity.this);

               if (tab.getPosition()==3){

                }
                else if (tab.getPosition()==0|| tab.getPosition()==1 || tab.getPosition()==2){

                  TabLayoutViews.selectTab(HomeActivity.this,tab.getCustomView());
                }


            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                if (tab.getPosition()==3){

                }else if (tab.getPosition()==0 || tab.getPosition()==1 || tab.getPosition()==2){
                    TabLayoutViews.unselectedTab(HomeActivity.this,tab.getCustomView());
                }



            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                if (tab.getPosition()==0){

                   TimeLineFragment fragment= (TimeLineFragment) fragments.get(0);
                    fragment.scrolltoabove();
                }

            }
        });


        // load request
        loadRequests();

        // load chat history
        loadchatHistory();



        // menu
        initmenu();



        // clear notification
        NotificationManager notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        assert notificationManager != null;
        notificationManager.cancelAll();


        // socket
        socketReady();

    }




    private void initmenu(){
        // list of menu model
        arrayList=new ArrayList<>();
        setmodelReady();
        // recycler
        recyclerView=(RecyclerView)findViewById(R.id.recycler_menu);
        manager=new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        menuadapter=new MenuAdapter(arrayList, new OnPress() {
            @Override
            public void onClick(View view, int position) {


                switch (position){
                    case 0:

                        // my profile
                        startActivity(new Intent(HomeActivity.this,MyprofileActivity.class));
                        overridePendingTransition(R.anim.slide_from_left,R.anim.slide_to_right);
                        break;
                    case 1:

                        // my friends
                        startActivity(new Intent(HomeActivity.this,MyfriendsActivity.class));
                       overridePendingTransition(R.anim.slide_from_left,R.anim.slide_to_right);

                        break;
                    case 2:

                        // friends chat
                        startActivity(new Intent(HomeActivity.this,ChatFriendsActivity.class));
                        overridePendingTransition(R.anim.slide_from_left,R.anim.slide_to_right);

                        break;
                    case 3:

                        // language
                        DialogLanguage dialog=new DialogLanguage(HomeActivity.this);
                        dialog.getWindow().getAttributes().windowAnimations=R.style.dialogzoominzommout;
                        dialog.show();

                        break;
                    case 4:
                        // about us
                        startActivity(new Intent(HomeActivity.this,AboutUsActivity.class));
                        overridePendingTransition(R.anim.slide_up,R.anim.fadout);
                        break;

                    case 5:
                        // contact us
                        startActivity(new Intent(HomeActivity.this,ContactUsActivity.class));
                        overridePendingTransition(R.anim.slide_up,R.anim.fadout);

                        break;

                    case 6:
                        // logout
                        logout();
                        break;

                    case 7:

                        // share app
                        MyShare.shareApp(HomeActivity.this);

                }
            }
        });

        recyclerView.setLayoutManager(manager);
        recyclerView.addItemDecoration(new SpaceRecycler_V(MySizes.gethight(this)/90));
        recyclerView.setAdapter(menuadapter);

        // edit
        editText=(EditText)findViewById(R.id.home_edit_search);
        editText.setFocusable(false);
        editText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent=new Intent(HomeActivity.this,SearchActivity.class);
                ActivityOptions options = ActivityOptions
                        .makeSceneTransitionAnimation(HomeActivity.this,
                                view, "trans");
                startActivity(intent,options.toBundle());
            }
        });


        // text version
        textversion=(TextView)findViewById(R.id.home_vrsionname);
        textversion.append("  "+getVesrsionName());

    }

    private void setmodelReady() {
        String[] titles={
                getResources().getString(R.string.my_profile),
                getResources().getString(R.string.my_friends),
                getResources().getString(R.string.chatfriends),
                getResources().getString(R.string.languages),
                getResources().getString(R.string.aboutus),
                getResources().getString(R.string.contactus),
                getResources().getString(R.string.logout),
                getResources().getString(R.string.sharetheapp)
                };

        int[] icons={
                R.drawable.ic_myprofile,
                R.drawable.ic_friends,
                R.drawable.ic_msg,
                R.drawable.ic_language,
                R.drawable.ic_aboutus,
                R.drawable.ic_contact_us,
                R.drawable.ic_logout,
                R.drawable.ic_share

        };

        for (int i=0;i<titles.length;i++){
            MenuModel model=new MenuModel();
            model.setTitle(titles[i]);
            model.setIcon(icons[i]);
            arrayList.add(model);
        }
    }

    private void logout(){

        AlertDialog.Builder dialog=new AlertDialog.Builder(this);
        dialog.setPositiveButton(getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onClick(DialogInterface dialog, int which) {

                // removealldata();
                logoutfromserver();

            }
        }).setNegativeButton(getResources().getString(R.string.no), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }).setTitle(getResources().getString(R.string.logout))
                .setMessage(getResources().getString(R.string.surelogout));

        dialog.create();
        dialog.show();
    }

    private void logoutfromserver(){
        progressDialog.show();
        String url=HomeActivity.domain+"api/logout";
        StringRequest request=new MyRequest(HomeActivity.token, Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                try {
                    JSONObject jsonObject=new JSONObject(response);
                    if (jsonObject.has("success")){

                        removealldata();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        },new OnErrorRequest(HomeActivity.this, new ErrorCall() {
            @Override
            public void OnBack() {

            }
        }));
        Myvollysinglton.getInstance(HomeActivity.this).addtorequst(request);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private void removealldata(){
        MySharedPref.setdata(this,"token","");
        MySharedPref.setdata(this,"name","");
        MySharedPref.setdata(this,"id","");
        MySharedPref.setdata(this,"image","");
        MySharedPref.setdata(this,"im_profile","");
        String provider=MySharedPref.getdata(this,"provider");

        if (provider.equals("google")){
            GoogleSignInOptions gso = new GoogleSignInOptions.Builder
                    (GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestIdToken(getString(R.string.google_client_id))
                    .requestEmail()
                    .build();
            GoogleSignInClient googleSignInClient = GoogleSignIn.getClient(this, gso);
            googleSignInClient.signOut().addOnCompleteListener(this, new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    goback();

                }
            });
        }else if (provider.equals("facebook")){
            LoginManager.getInstance().logOut();
            goback();


        }else if (provider.equals("twitter")){
            // twitter
            TwitterConfig config = new TwitterConfig.Builder(HomeActivity.this)
                    .logger(new DefaultLogger(Log.DEBUG))
                    .twitterAuthConfig(new TwitterAuthConfig
                            (getResources().getString(R.string.twitter_consumer_key)
                                    , getResources().getString(R.string.twitter_consumer_secret)))
                    .debug(true)
                    .build();
            Twitter.initialize(config);
            TwitterCore.getInstance().getSessionManager().clearActiveSession();
            goback();

        }

    }

    private void goback(){
        startActivity(new Intent(HomeActivity.this,LoginActivity.class));
        overridePendingTransition(R.anim.fadin,R.anim.fadout);
        finish();
    }

    public void addCountToTabIcon(int position){
        TabLayoutViews.addToCountNotification(HomeActivity.this,tableLayout.getTabAt(position).getCustomView());
    }

    private void loadRequests() {
        String url=HomeActivity.domain+"api/get_friend_requests";
        StringRequest request=new MyRequest(HomeActivity.token, 1, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
              //  Log.i("requset",response);
                requests.clear();

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

                       requests.add(model);
                       addCountToTabIcon(2);

                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        },new OnErrorRequest(HomeActivity.this, new ErrorCall() {
            @Override
            public void OnBack() {

            }
        }));

        Myvollysinglton.getInstance(this).addtorequst(request);
    }

    private void loadchatHistory() {
        String url=HomeActivity.domain+"api/messages";
        StringRequest request=new MyRequest(HomeActivity.token, 1, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("messages",response);
                chatHistoryList.clear();
                TabLayoutViews.removeCount(HomeActivity.this,tableLayout.getTabAt(1).getCustomView());

                try {
                    JSONObject sucobject=new JSONObject(response);
                    JSONArray array=sucobject.getJSONArray("success");
                    for (int i =0;i<array.length();i++){
                        JSONObject object=array.getJSONObject(i);
                        JSONObject userObject=object.getJSONObject("user");
                        JSONObject messageObject=object.getJSONObject("message");

                        String id=userObject.getString("id");
                        String name=userObject.getString("name");
                        String image=userObject.getString("image");
                        String message=messageObject.getString("message");
                        String date=messageObject.getString("date");
                        String readAt=messageObject.getString("read_at");


                        ChatHistroyModel model=new ChatHistroyModel();
                        model.setUserId(id);
                        model.setUserName(name);
                        model.setUserImage(image);
                        model.setContent(message.replace("<br/>","."));
                        model.setTime(date);
                        if (!readAt.equals("null")){
                            model.setReade(true);
                        }else {
                            addCountToTabIcon(1);
                        }
                        chatHistoryList.add(model);

                    }
                    ChatHistoryFragment.adapter.notifyDataSetChanged();


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        },new OnErrorRequest(HomeActivity.this, new ErrorCall() {
            @Override
            public void OnBack() {

            }
        }));

        Myvollysinglton.getInstance(this).addtorequst(request);
    }

    public void updateUserInfo(){

        String url=domain+"api/user";
        StringRequest request=new MyRequest(token, Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                // Log.i("login",response);
                try {
                    JSONObject object = new JSONObject(response);
                    if (object.has("success")) {
                        JSONObject jsonObject = object.getJSONObject("success");
                        String id = jsonObject.getString("id");
                        String name = jsonObject.getString("name");
                        String email = jsonObject.getString("email");
                        String image = jsonObject.getString("image");
                        String phone = jsonObject.getString("phone");
                        String thewallet = jsonObject.getString("wallet");
                        String im_profile = jsonObject.getString("profile_img");

                        String education = jsonObject.getString("education");
                        String location = jsonObject.getString("location");
                        String about = jsonObject.getString("about");

                      /*  if (work_with.equals("1")){
                            MySharedPref.setWorkWith(MainActivity.this,true);
                        }else {
                            MySharedPref.setWorkWith(MainActivity.this,false);

                        }*/


                        MySharedPref.setdata(HomeActivity.this,"id",id);
                        MySharedPref.setdata(HomeActivity.this,"name",name);
                        MySharedPref.setdata(HomeActivity.this,"email",email);
                        MySharedPref.setdata(HomeActivity.this,"image",image);
                        MySharedPref.setdata(HomeActivity.this,"phone",phone);
                        MySharedPref.setdata(HomeActivity.this,"wallet",thewallet);
                        wallet=thewallet;
                        MySharedPref.setdata(HomeActivity.this,"profile_img",im_profile);
                        MySharedPref.setdata(HomeActivity.this,"education",education);
                        MySharedPref.setdata(HomeActivity.this,"location",location);
                        MySharedPref.setdata(HomeActivity.this,"about",about);

                        SupportingFragment f= (SupportingFragment) fragments.get(3);
                        f.reloadWallet();



                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new OnErrorRequest(HomeActivity.this, new ErrorCall() {
            @Override
            public void OnBack() {

            }
        }));
        Myvollysinglton.getInstance(this).addtorequst(request);
    }

    private void socketReady() {
        try {
            mSocket = IO.socket(getResources().getString(R.string.socket));
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        mSocket.connect();
        mSocket.on("supportingonline",on_new_Change);
        mSocket.on("new message",on_new_message);
    }

    Emitter.Listener on_new_Change = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject data = (JSONObject) args[0];

                    // Log.i("emit","listen");
                 Log.i("emit",data.toString());
                    try {
                        String theType=data.getString("type");
                        if (theType.equals("group")) {
                            String thegroupid = data.getString("group_id");
                            String theuserId = data.getString("user_id");

                        }else if (theType.equals("friend")){
                            String toId=data.getString("to");
                            if (toId.equals(myid)){
                                loadRequests();
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                }
            });


        }
    };

    Emitter.Listener on_new_message = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject data = (JSONObject) args[0];

                    // Log.i("emit","listen");
                    Log.i("emit",data.toString());
                    try {
                        String fromId=data.getString("from");
                        String toId=data.getString("to");
                        String msg=data.getString("message");
                        if ( toId.equals(myid)){
                         loadchatHistory();
                        }
                        //  Log.i("emit",msg);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });


        }
    };

    public static void sendsocketEmmit(Object object) {

        mSocket.emit("supportingonline",object);
    }


    @Override
    public void onBackPressed() {
        RestartMyService.CheckService(this);
        int pos=tableLayout.getSelectedTabPosition();
        if (drawer.isDrawerOpen(Gravity.LEFT)){
            drawer.closeDrawer(Gravity.LEFT);
        }else {

            if(!BackFragmentHelper.fireOnBackPressedEvent(this)) {
                if (pos == 0) {
                    TimeLineFragment fragment= (TimeLineFragment) fragments.get(0);

                    if (fragment.nestedScrollView.canScrollVertically(-1)){
                        fragment.scrolltoabove();
                    }else {
                        finish();
                    }

                } else {
                    tableLayout.getTabAt(0).select();
                }

            }

        }
    }

    private String getVesrsionName(){
        String version="0.0";
        try {
            PackageInfo pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            version=pInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return version;
    }

    @Override
    protected void onResume() {
        super.onResume();
        RestartMyService.CheckService(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
       RestartMyService.CheckService(HomeActivity.this);
    }

}
