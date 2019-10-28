package com.egypt.supporting.supportingonline;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.egypt.supporting.supportingonline.Adapters.GroupAdapter;
import com.egypt.supporting.supportingonline.Adapters.LayerAdapter;
import com.egypt.supporting.supportingonline.Calls.ErrorCall;
import com.egypt.supporting.supportingonline.Calls.OnPress;
import com.egypt.supporting.supportingonline.Calls.OnPressView;
import com.egypt.supporting.supportingonline.Custom.CopyText;
import com.egypt.supporting.supportingonline.Custom.MyRequest;
import com.egypt.supporting.supportingonline.Custom.MySharedPref;
import com.egypt.supporting.supportingonline.Custom.Myvollysinglton;
import com.egypt.supporting.supportingonline.Custom.OnErrorRequest;
import com.egypt.supporting.supportingonline.Dialogs.DialogGroupItem;
import com.egypt.supporting.supportingonline.Models.GroupModel;
import com.egypt.supporting.supportingonline.Models.LayerModel;
import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class GroupContentActivity extends AppCompatActivity  {


    private LinearLayout groupLayout;

    private TextView alarmtext,copytext,myName,texttitle;
    private int countUser,position;
    private boolean firstTime;
    private String id,token,domain,date,type;
    private RecyclerView recyclerLayer;
    private LinearLayoutManager managerLayer;
    private LayerAdapter adapterLayer;
    private ArrayList<LayerModel> arrayListLayer=new ArrayList<>();

    private ImageView joinImage;
    private CircleImageView myImage;
    private Button btn_remove,btn_showImage,btn_payFess,btn_sendMoney;
    private ProgressDialog progressDialog;

    private Socket mSocket;

    private String groupIdUpdate;
    ArrayList<GroupModel> listGroup2=new ArrayList<>();
    ArrayList<GroupModel> listGroup=new ArrayList<>();
    ArrayList<GroupModel> listGroup3=new ArrayList<>();
    RecyclerView recyclerGroup,recyclerGroup2,recyclerGroup3;
    GroupAdapter groupAdapter,groupAdapter2,groupAdapter3;



    @SuppressLint({"ClickableViewAccessibility", "SetTextI18n"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_content);



        // params
        countUser=getIntent().getIntExtra("count_user",0);
        position=getIntent().getIntExtra("position",0);
        id=getIntent().getStringExtra("id");
        firstTime=getIntent().getBooleanExtra("first_time",false);
        Log.i("iiiiiiii",String.valueOf(firstTime));
        domain=getResources().getString(R.string.domain);
        token= MySharedPref.getdata(this,"token");
        type=getIntent().getStringExtra("type");
        date=getIntent().getStringExtra("date");

        // socket
        socketReady();

        // progress dialog
        progressDialog=new ProgressDialog(this);
        progressDialog.setMessage(getResources().getString(R.string.loading));

        // title text
        texttitle=(TextView)findViewById(R.id.group_text);
        String nameofgroup=null;
        if (type.equals("1") || type.equals("4")){
            nameofgroup=getResources().getString(R.string.bronze);
        }else if (type.equals("2") || type.equals("5")){
            nameofgroup=getResources().getString(R.string.silver);
        }else if (type.equals("3") || type.equals("6")){
            nameofgroup=getResources().getString(R.string.gold);
        }
        texttitle.setText(nameofgroup+"(" +getResources().getString(R.string.joined_at)+" "+date+")");

        // alarm text
        alarmtext=(TextView)findViewById(R.id.group_alarmtext);

        // btn pay fess
        btn_payFess=(Button)findViewById(R.id.group_payfess_btn);
        btn_payFess.setVisibility(View.GONE);
        btn_payFess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                payToserver("fees");
            }
        });

        // btn send money
        btn_sendMoney=(Button)findViewById(R.id.group_sendmoney_btn);
        btn_sendMoney.setVisibility(View.GONE);
        btn_sendMoney.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (arrayListLayer.get(0).getName().equals("System")) {
                    payToserver("payment");
                }else {
                    startActivity(new Intent(GroupContentActivity.this,AddRecevieMessageActivity.class)
                    .putExtra("id",id).putExtra("date",date)
                            .putExtra("type",type)
                            .putExtra("position",position).putExtra("count_user",countUser));
                    overridePendingTransition(R.anim.slide_up,R.anim.fadout);
                    finish();
                }

            }
        });

        // copy text
        copytext=(TextView)findViewById(R.id.group_copy);
        copytext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CopyText.copy(GroupContentActivity.this,alarmtext.getText().toString());
            }
        });

        // text my name
        myName=(TextView)findViewById(R.id.group_my_name);
        myName.setText(MySharedPref.getdata(this,"name"));

        // remove btn
        btn_remove=(Button)findViewById(R.id.group_remove_btn);

        // show image receive
        btn_showImage=(Button)findViewById(R.id.group_showimage_btn);

        // join image
        joinImage=(ImageView)findViewById(R.id.group_my_join);

        // my image
        myImage=(CircleImageView)findViewById(R.id.group_my_image);
        Glide.with(this).load(MySharedPref.getdata(this,"image")).error(R.drawable.ic_user_profile)
                .into(myImage);

        // layout contanier
        groupLayout=(LinearLayout)findViewById(R.id.group_layout);

        // recycler
        recyclerLayer=(RecyclerView) findViewById(R.id.recycler_layer);
        managerLayer=new LinearLayoutManager(this,RecyclerView.VERTICAL,false);
        adapterLayer=new LayerAdapter(arrayListLayer,this);
        recyclerLayer.setLayoutManager(managerLayer);
        recyclerLayer.addItemDecoration(new DividerItemDecoration(recyclerLayer.getContext(), DividerItemDecoration.VERTICAL));
        recyclerLayer.setAdapter(adapterLayer);


        // init
        init1();
        init2();
        init3();

       loadTreeLayers(true);



    }


    private void socketReady() {
        try {
            mSocket = IO.socket(getResources().getString(R.string.socket));
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        mSocket.connect();
        mSocket.on("supportingonline",on_new_message);
    }

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
                        String theType=data.getString("type");
                        if (theType.equals("group")) {
                            String thegroupid = data.getString("group_id");
                            String theuserId = data.getString("user_id");
                         //   Toast.makeText(GroupContentActivity.this, theuserId, Toast.LENGTH_SHORT).show();
                            if (thegroupid.equals(groupIdUpdate) || theuserId.equals(groupIdUpdate)){


                                loadTreeLayers(false);
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                }
            });


        }
    };


    private void sendsocketEmmit() {
        Toast.makeText(this, groupIdUpdate, Toast.LENGTH_SHORT).show();
        JSONObject object=new JSONObject();
        try {
            object.put("type","group");
            object.put("group_id",groupIdUpdate);
            object.put("device","nagy");
            object.put("user_id",id);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.i("send",object.toString());

        mSocket.emit("supportingonline",object);
    }

    private void init1() {
        listGroup.clear();
        for (int m=0;m<countUser;m++){
            GroupModel model=new GroupModel();
           // model.setHasChildern(true);
            model.setVisable(true);
            listGroup.add(model);
        }
        recyclerGroup=new RecyclerView(this);
        recyclerGroup.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
        recyclerGroup.setLayoutManager(new GridLayoutManager(GroupContentActivity.this,countUser));

        groupAdapter=new GroupAdapter(listGroup, GroupContentActivity.this, countUser, new OnPress() {
            @Override
            public void onClick(View view, int position) {
                if (listGroup.get(position).isHasdata()) {
                    popUpDialog(listGroup.get(position).getName(),listGroup.get(position).getImage()
                    ,listGroup.get(position).getReceiveImage(),listGroup.get(position).getPayment(),
                            listGroup.get(position).isReceive(),listGroup.get(position).isDelete()
                            ,listGroup.get(position).getUserGroup());
                }
            }
        });
        recyclerGroup.setAdapter(groupAdapter);
        groupAdapter.notifyDataSetChanged();

    }

    private void init2() {
        listGroup2.clear();
        for (int t=0;t<(countUser*countUser);t++){
            GroupModel model2=new GroupModel();
           // model2.setVisable(true);
           // model2.setHasChildern(true);
            listGroup2.add(model2);
        }
        recyclerGroup2=new RecyclerView(this);
        recyclerGroup2.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
        recyclerGroup2.setLayoutManager(new GridLayoutManager(GroupContentActivity.this,countUser*countUser));
        groupAdapter2=new GroupAdapter(listGroup2, GroupContentActivity.this, (countUser*countUser), new OnPress() {
            @Override
            public void onClick(View view, int position) {

                // pop up
                if (listGroup2.get(position).isHasdata()) {
                    popUpDialog(listGroup2.get(position).getName(),listGroup2.get(position).getImage()
                            ,listGroup2.get(position).getReceiveImage(),listGroup2.get(position).getPayment(),
                            listGroup2.get(position).isReceive(),listGroup2.get(position).isDelete()
                            ,listGroup2.get(position).getUserGroup());
                }
            }
        });
        recyclerGroup2.setAdapter(groupAdapter2);
        groupAdapter2.notifyDataSetChanged();

    }

    private void init3() {
        listGroup3.clear();
        for (int t=0;t<(countUser*countUser*countUser);t++){
            GroupModel model3=new GroupModel();
         //  model3.setVisable(true);
          //  model3.setHasChildern(true);
            listGroup3.add(model3);
        }
        recyclerGroup3=new RecyclerView(this);
        recyclerGroup3.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
        recyclerGroup3.setLayoutManager(new GridLayoutManager(GroupContentActivity.this,countUser*countUser*countUser));
        groupAdapter3=new GroupAdapter(listGroup3, GroupContentActivity.this, (countUser*countUser*countUser), new OnPress() {
            @Override
            public void onClick(View view, int position) {

                // pop up
                if (listGroup3.get(position).isHasdata()) {
                    popUpDialog(listGroup3.get(position).getName(),listGroup3.get(position).getImage()
                            ,listGroup3.get(position).getReceiveImage(),listGroup3.get(position).getPayment(),
                            listGroup3.get(position).isReceive(),listGroup3.get(position).isDelete()
                            ,listGroup3.get(position).getUserGroup());
                }
            }
        });
        recyclerGroup3.setAdapter(groupAdapter3);
        groupAdapter3.notifyDataSetChanged();




    }


    private void loadTreeLayers(boolean isFirst){
        if (isFirst){
            progressDialog.show();
            groupLayout.addView(recyclerGroup);
            groupLayout.addView(recyclerGroup2);
            groupLayout.addView(recyclerGroup3);
        }

        String url=domain+"api/get_group";
        StringRequest request=new MyRequest(token, 1, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                Log.i("group",response);

                    init1();
                    init2();
                    init3();
                    arrayListLayer.clear();
                    adapterLayer.notifyDataSetChanged();

                try {
                    JSONObject object=new JSONObject(response);
                    JSONObject sucobject=object.getJSONObject("success");

                    JSONObject userGroupObject=sucobject.getJSONObject("user_group");
                    JSONObject groupObject=userGroupObject.getJSONObject("group");
                    String groupPrice=groupObject.getString("price");
                    groupIdUpdate=userGroupObject.getString("group_id");
                    String payment=userGroupObject.getString("payment");
                    String link=userGroupObject.getString("link");
                    String receiveImage=userGroupObject.getString("receive_image");
                    String workWith=userGroupObject.getString("work_with");
                    String layer=userGroupObject.getString("layer");
                    //  JSONArray childern=userGroupObject.getJSONArray("children");


                    // remove


                    // data on top
                    userUpdatetop(payment,link);
                    // tree
                    userUpdate(payment,receiveImage);

                    JSONArray headArray=sucobject.getJSONArray("header");
                    loadLayers(headArray,groupPrice);


                    JSONArray treeArray=sucobject.getJSONArray("tree");
                    if (treeArray.length()==0 && layer.equals("1") && workWith.equals("null")){
                        btn_remove.setVisibility(View.VISIBLE);
                        btn_remove.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                removerFrom();
                            }
                        });

                    }else {
                        btn_remove.setVisibility(View.GONE);
                    }
                    updateFirstTree(treeArray);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                // if first time
                if (firstTime){
                    firstTime=false;
                    sendsocketEmmit();


                }

            }
        },new OnErrorRequest(GroupContentActivity.this, new ErrorCall() {
            @Override
            public void OnBack() {
                Handler handler=new Handler(Looper.getMainLooper());
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (!isFirst){
                          SupportingMainFragment.loadGroups();
                            onBackPressed();

                        }

                    }
                });


            }
        })){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map=new HashMap<>();
                map.put("group_id",id);

                return map;
            }
        };
        Myvollysinglton.getInstance(this).addtorequst(request);
    }

    private void loadLayers(JSONArray array,String groupPrice) {


                    for (int i =0 ;i<array.length();i++){
                        JSONObject groupObject= null;
                        try {
                            groupObject = array.getJSONObject(i);

                        String name=groupObject.getString("name");
                        String image=groupObject.getString("image");
                        String price=groupObject.getString("price");

                        LayerModel  model=new LayerModel();
                        model.setName(name);
                        model.setImage(image);
                        if (i==0){

                            String paymentGroup=groupObject.getString("payment_group");
                            if (paymentGroup.equals("0")||paymentGroup.equals("2")){
                                model.setPrice(groupPrice);
                            }else  {
                                //Log.i("oooou","deddddddd");
                                model.setPrice("out");
                            }
                        }else {
                            String pp=null;
                            if (i==1 && countUser==2){
                                pp = String.valueOf(Double.parseDouble(groupPrice)*2);
                            }else if (i==1 && countUser==3){
                                pp = String.valueOf(Double.parseDouble(groupPrice)*3);
                            }else if (i==2 && countUser==2){
                                pp = String.valueOf(Double.parseDouble(groupPrice)*4);
                            }
                            else if (i==2 && countUser==3){
                                pp = String.valueOf(Double.parseDouble(groupPrice)*9);
                            }else if (i==3 && countUser==2){
                                pp = String.valueOf(Double.parseDouble(groupPrice)*8);
                            }else if (i==3 && countUser==3){
                                pp = String.valueOf(Double.parseDouble(groupPrice)*27);
                            }

                            model.setPrice(pp);
                        }
                        arrayListLayer.add(model);
                        adapterLayer.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

    }


    private void userUpdatetop(String payment, String link) {

        if (payment.equals("0")){
            btn_sendMoney.setVisibility(View.VISIBLE);
            alarmtext.setText(getResources().getString(R.string.to_show_yor_link));
            alarmtext.setTextColor(getResources().getColor(R.color.colorAccent));
            copytext.setVisibility(View.INVISIBLE);
        }

        if (payment.equals("1")){
            alarmtext.setText(link);
            copytext.setVisibility(View.VISIBLE);
        }

        if (payment.equals("2")){
            alarmtext.setText(getResources().getString(R.string.to_show_yor_link));
            alarmtext.setTextColor(getResources().getColor(R.color.colorAccent));
            copytext.setVisibility(View.INVISIBLE);
        }

        if (link.equals("null")){
            btn_payFess.setVisibility(View.VISIBLE);
            alarmtext.setText(getResources().getString(R.string.to_show_yor_link));
            alarmtext.setTextColor(getResources().getColor(R.color.colorAccent));
            copytext.setVisibility(View.INVISIBLE);
        }
    }

    private void updateFirstTree(JSONArray treeArray) {

        for (int i=0;i<treeArray.length();i++){
            try {
                JSONObject object=treeArray.getJSONObject(i);

                String payment=object.getString("payment");
                String userGroup=object.getString("user_group");
                String receive_image=object.getString("receive_image");
                boolean receive=object.getBoolean("receive");
                boolean delete=object.getBoolean("delete");

                JSONObject userObject=object.getJSONObject("user");
                String id=userObject.getString("id");
                String name=userObject.getString("name");
                String image=userObject.getString("image");

                int whereIam=0;
                if (i==0){
                    whereIam=0;
                }else if (i==1){
                    whereIam=countUser;
                }else if (i==3){
                    whereIam=6;
                }


                if (object.has("children")){
                    JSONArray childrenArray=object.getJSONArray("children");
                    if (childrenArray.length()>0){

                        secondTree(childrenArray,whereIam);
                    }
                }

                // first layer
                GroupModel model=listGroup.get(i);
                model.setHasdata(true);
                model.setId(id);
                model.setImage(image);
                model.setName(name);
                model.setPayment(payment);
                model.setUserGroup(userGroup);
                model.setReceive(receive);
                model.setDelete(delete);
                model.setReceiveImage(receive_image);
                model.setHasChildern(true);
                groupAdapter.notifyDataSetChanged();

                setWatit(whereIam,listGroup2,groupAdapter2);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void secondTree(JSONArray childrenArray,int whereIam) {


        for (int i=0;i<childrenArray.length();i++){
            try {
                JSONObject  object = childrenArray.getJSONObject(i);
                // Log.i("user2",object.toString());
                String payment=object.getString("payment");
                String receive_image=object.getString("receive_image");
                boolean receive=object.getBoolean("receive");
                boolean delete=object.getBoolean("delete");

                JSONObject userObject=object.getJSONObject("user");
                String id=userObject.getString("id");
                String name=userObject.getString("name");
                String image=userObject.getString("image");

                int whereIam2=0;
                if (whereIam==0){
                    whereIam2=0;
                }else if (whereIam==1){
                    whereIam2=countUser;
                }else if (whereIam==2 || whereIam==3){
                    whereIam2=(i+whereIam)*countUser;
                }else if (whereIam==4){
                    whereIam2=12;
                }else if (whereIam==5){
                    whereIam2=15;
                }else if (whereIam==6){
                    whereIam2=18;
                }else if (whereIam==7){
                    whereIam2=21;
                }



                GroupModel model2=listGroup2.get(i+whereIam);
                model2.setVisable(true);
                model2.setHasdata(true);
                model2.setId(id);
                model2.setImage(image);
                model2.setName(name);
                model2.setPayment(payment);
                model2.setReceive(receive);
                model2.setDelete(delete);
                model2.setReceiveImage(receive_image);
                if (!(type.equals("1") || type.equals("2") || type.equals("4")|| type.equals("5"))) {
                    model2.setHasChildern(true);
                }
                groupAdapter2.notifyDataSetChanged();

                if (!(type.equals("1") || type.equals("2") || type.equals("4")|| type.equals("5"))) {
                    setWatit(whereIam2, listGroup3, groupAdapter3);
                }

                if (object.has("children")){
                    JSONArray childrenArrayT=object.getJSONArray("children");
                    if (childrenArrayT.length()>0){
                        therdTree(childrenArrayT,whereIam2);
                    }
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }


        }

    }

    private void therdTree(JSONArray childrenArray, int whereIam2) {

        for (int t=0;t<countUser;t++){
            GroupModel model2=listGroup3.get(t+whereIam2);
            model2.setVisable(true);
            groupAdapter3.notifyDataSetChanged();
        }

        for (int i=0;i<childrenArray.length();i++){
            try {
                JSONObject  object = childrenArray.getJSONObject(i);
              //  Log.i("therd",object.toString());
                //Log.i("where2", String.valueOf(whereIam2));
                String payment=object.getString("payment");
                String receive_image=object.getString("receive_image");
                boolean receive=object.getBoolean("receive");
                boolean delete=object.getBoolean("delete");

                JSONObject userObject=object.getJSONObject("user");
                String id=userObject.getString("id");
                String name=userObject.getString("name");
                String image=userObject.getString("image");

                GroupModel model2=listGroup3.get(i+whereIam2);
                model2.setVisable(true);
                model2.setHasdata(true);
                model2.setId(id);
                model2.setImage(image);
                model2.setName(name);
                model2.setPayment(payment);
                model2.setReceive(receive);
                model2.setDelete(delete);
                model2.setReceiveImage(receive_image);
                model2.setHasChildern(false);
                groupAdapter3.notifyDataSetChanged();


            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

    private void userUpdate(String payment, String receiveImage) {
        // join
        if (payment.equals("1")){
            joinImage.setVisibility(View.VISIBLE);
        }else {
            joinImage.setVisibility(View.GONE);
        }

        if (receiveImage.equals("null")){
            btn_showImage.setVisibility(View.GONE);
        }else {
            btn_showImage.setVisibility(View.VISIBLE);
            btn_showImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(GroupContentActivity.this,FullImageActivity.class)
                    .putExtra("is_me",false).putExtra("image_url",domain+"imgs/receive/"+receiveImage)
                    .putExtra("type","receive"));
                }
            });
        }

    }

    private void setWatit(int whereIam,ArrayList<GroupModel> thelist,GroupAdapter theadapter){
        for (int t=0;t<countUser;t++){
            GroupModel model=thelist.get(t+whereIam);
            model.setVisable(true);
            theadapter.notifyDataSetChanged();
        }
    }

    private void payToserver(String kind){
        progressDialog.show();
        String url=domain+"api/send_money";
        StringRequest request=new MyRequest(token, 1, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                Log.i(kind,response);
                try {
                    JSONObject object=new JSONObject(response);
                    if (object.has("success")){

                        sendsocketEmmit();

                        restart();

                    }else if (object.has("error")){
                        Toast.makeText(GroupContentActivity.this, object.getString("error"), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        },new OnErrorRequest(GroupContentActivity.this, new ErrorCall() {
            @Override
            public void OnBack() {
                progressDialog.dismiss();

            }
        })){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> map=new HashMap<>();
                map.put("group",id);
                map.put("type",kind);
                return map;
            }
        };
        Myvollysinglton.getInstance(this).addtorequst(request);
    }

    public void restart() {
        startActivity(new Intent(GroupContentActivity.this,GroupContentActivity.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                .putExtra("type",type).putExtra("date",date).putExtra("count_user",countUser).putExtra("id",id));
        finish();
    }


    private void removerFrom(){
        progressDialog.show();
        String url=domain+"api/remove_model";
        StringRequest request=new MyRequest(token, 1, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
               // Log.i("remove",response);
                try {
                    JSONObject object=new JSONObject(response);
                    if (object.has("success")){
                        SupportingFragment fragment=(SupportingFragment) HomeActivity.fragments.get(3);
                        fragment.reloadData();
                        fragment.reloadInfo();
                        onBackPressed();
                    }else if (object.has("error")){
                        Toast.makeText(GroupContentActivity.this, object.getString("error"), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        },new OnErrorRequest(GroupContentActivity.this, new ErrorCall() {
            @Override
            public void OnBack() {
                progressDialog.dismiss();
                onBackPressed();

            }
        })){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map=new HashMap<>();
                map.put("group_id",id);
                return map;
            }
        };

        Myvollysinglton.getInstance(this).addtorequst(request);
    }

    private void iReceivedUserFrom(String modelId){
        progressDialog.show();
        String url=domain+"api/ireceived";
        StringRequest request=new MyRequest(token, 1, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                progressDialog.dismiss();
              //  Log.i("ireceived",response);
                try {
                    JSONObject object=new JSONObject(response);
                    if (object.has("success")){
                        sendsocketEmmit();

                        restart();

                    }else if (object.has("error")){
                        Toast.makeText(GroupContentActivity.this, object.getString("error"), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        },new OnErrorRequest(GroupContentActivity.this, new ErrorCall() {
            @Override
            public void OnBack() {
                progressDialog.dismiss();

            }
        })){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map=new HashMap<>();
                map.put("group_id",modelId);
                return map;
            }
        };

        Myvollysinglton.getInstance(this).addtorequst(request);
    }

    private void deleteUserModel(String modelId){

        progressDialog.show();
        String url=domain+"api/delete_model";
        StringRequest request=new MyRequest(token, 1, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
              // Log.i("delete",response);
                try {
                    JSONObject object=new JSONObject(response);
                    if (object.has("success")){
                        sendsocketEmmit();

                        restart();
                    }else if (object.has("error")){
                        Toast.makeText(GroupContentActivity.this, object.getString("error"), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        },new OnErrorRequest(GroupContentActivity.this, new ErrorCall() {
            @Override
            public void OnBack() {
                progressDialog.dismiss();

            }
        })){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map=new HashMap<>();
                map.put("group_id",modelId);
                return map;
            }
        };

        Myvollysinglton.getInstance(this).addtorequst(request);

    }

    private void popUpDialog(String name,String image,String receiveImage,String payment,boolean receive,boolean delete,String userGroup){
        DialogGroupItem dialog=new DialogGroupItem(GroupContentActivity.this, new OnPressView() {
            @Override
            public void onclick(View view) {

                // btn remove
                deleteUserModel(userGroup);

            }
        }, new OnPressView() {
            @Override
            public void onclick(View view) {
                // btn show image
                startActivity(new Intent(GroupContentActivity.this,FullImageActivity.class)
                        .putExtra("is_me",false).putExtra("image_url",domain+"imgs/receive/"+receiveImage)
                        .putExtra("type","receive"));

            }
        }, new OnPressView() {
            @Override
            public void onclick(View view) {
                // btn i receive
                iReceivedUserFrom(userGroup);
            }
        }
                , name, image, receiveImage, receive, delete);
        dialog.getWindow().getAttributes().windowAnimations=R.style.dialogzoominzommout;
        dialog.show();
    }




    @Override
    public void onBackPressed() {

        //super.onBackPressed();
        finish();
        overridePendingTransition(R.anim.fadin,R.anim.slide_down);
    }



}
