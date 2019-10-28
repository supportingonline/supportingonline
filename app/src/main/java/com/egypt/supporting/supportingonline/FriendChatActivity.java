package com.egypt.supporting.supportingonline;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.egypt.supporting.supportingonline.Adapters.ChatAdapter;
import com.egypt.supporting.supportingonline.Calls.ErrorCall;
import com.egypt.supporting.supportingonline.Custom.MyRequest;
import com.egypt.supporting.supportingonline.Custom.MySharedPref;
import com.egypt.supporting.supportingonline.Custom.MySizes;
import com.egypt.supporting.supportingonline.Custom.Myvollysinglton;
import com.egypt.supporting.supportingonline.Custom.OnErrorRequest;
import com.egypt.supporting.supportingonline.Custom.SpaceRecycler_V;
import com.egypt.supporting.supportingonline.Models.ChatModel;
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

public class FriendChatActivity extends AppCompatActivity {

    private String imageUrl,name,us_id,domain,token;
    
    private RecyclerView recyclerView;
    private LinearLayoutManager manager;
    private ArrayList<ChatModel> arrayList;
    private ChatAdapter adapter;
    
    private EditText editText;
    private RelativeLayout layoutchat;
    
    private ImageView backImage;
    private CircleImageView imageView;
    private TextView textView;

    private Socket mSocket;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_chat);

        socketReady();

        // imageUrl , name and id
        imageUrl=getIntent().getStringExtra("image");
        name=getIntent().getStringExtra("name");
        us_id=getIntent().getStringExtra("id");
       // Log.i("iiiiddd",us_id);


        // domain and token
        domain=getResources().getString(R.string.domain);
        token= MySharedPref.getdata(this,"token");
        
        // back
        backImage=(ImageView)findViewById(R.id.friend_chat_back);
        backImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        
        // imageView
        imageView=(CircleImageView)findViewById(R.id.friend_chat_image);
        Glide.with(this).load(imageUrl).into(imageView);
        
        // text name
        textView=(TextView)findViewById(R.id.friend_chat_name);
        textView.setText(name);
        
        // recycler
        arrayList=new ArrayList<>();
        recyclerView=(RecyclerView)findViewById(R.id.recycler_chat);
        manager=new LinearLayoutManager(this);
        manager.setOrientation(RecyclerView.VERTICAL);
        adapter=new ChatAdapter(arrayList,this);
        recyclerView.addItemDecoration(new SpaceRecycler_V(MySizes.gethight(this)/80));
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);

        // load old message
          loadOldMessages();
        
        
        // edit text and layout
        editText=(EditText)findViewById(R.id.friend_chat_edit);
        layoutchat=(RelativeLayout)findViewById(R.id.friend_chat_layout);
        
        layoutchat.setClickable(false);
        
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String ss=charSequence.toString().trim();
                if (ss.length()>0){
                    layoutchat.setAlpha(1f);
                    layoutchat.setClickable(true);
                }else {
                    layoutchat.setAlpha(0.5f);
                    layoutchat.setClickable(false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        
        layoutchat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String message=editText.getText().toString().trim();
                sendMessage(message);
                editText.setText("");
                layoutchat.setAlpha(0.5f);
                layoutchat.setClickable(false);
                recyclerView.scrollToPosition(arrayList.size()-1);

            }
        });


        // reade_message
        readeMessage();
        
        
    }

    private void socketReady() {
        try {
            mSocket = IO.socket(getResources().getString(R.string.socket));
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        mSocket.connect();
        mSocket.on("new message",on_new_message);
    }

    private void sendMessage(String message) {


        send_messge_toserver(message.replace("\n","<br/>"));
       addTorecycler(message.replace("\n","<br/>"));
       sendsocketEmmit(message.replace("\n","<br/>"));



    }



    private void addTorecycler(String msg){
        ChatModel model=new ChatModel();
        model.setMe(true);
        model.setText(msg);
        arrayList.add(model);
        adapter.notifyItemChanged(arrayList.size()-1);
    }


    private void loadOldMessages(){
        String url=domain+"api/conversations";
        StringRequest request=new MyRequest(token, 1, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject object=new JSONObject(response);
                    if (object.has("success")){
                        JSONObject sucoObject=object.getJSONObject("success");
                        JSONObject converobObject=sucoObject.getJSONObject("conversation");
                        JSONArray array=converobObject.getJSONArray("data");
                        for (int i=0;i<array.length();i++){

                            JSONObject dataobObject=array.getJSONObject(i);
                          //  Log.i("chat",dataobObject.toString());

                            String id=dataobObject.getString("id");
                            String body=dataobObject.getString("body");
                            String date=dataobObject.getString("created_at");
                            JSONObject senderObject=dataobObject.getJSONObject("sender");
                            String user_id=senderObject.getString("id");

                            ChatModel model=new ChatModel();
                            model.setText(body);
                            if (user_id.equals(MySharedPref.getdata(FriendChatActivity.this,"id"))) {
                                model.setMe(true);
                            }

                            arrayList.add(0,model);
                            adapter.notifyDataSetChanged();

                        }
                        recyclerView.scrollToPosition(arrayList.size()-1);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        },new OnErrorRequest(FriendChatActivity.this, new ErrorCall() {
            @Override
            public void OnBack() {

            }
        })){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String>  map=new HashMap<>();
                map.put("user_id",us_id);

                return map;
            }
        };
        Myvollysinglton.getInstance(this).addtorequst(request);
    }

    private void sendsocketEmmit(String message) {
        JSONObject object=new JSONObject();
        try {
            object.put("user",MySharedPref.getdata(FriendChatActivity.this,"name"));
            object.put("from",MySharedPref.getdata(FriendChatActivity.this,"id"));
            object.put("to",us_id);
            object.put("message",message);
            object.put("image",MySharedPref.getdata(FriendChatActivity.this,"image"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        mSocket.emit("new message",object);
    }


    Emitter.Listener on_new_message = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject data = (JSONObject) args[0];

                   // Log.i("emit","listen");
                   // Log.i("emit",data.toString());
                    try {
                        String fromId=data.getString("from");
                        String toId=data.getString("to");
                        String msg=data.getString("message");
                        if (fromId.equals(us_id) && toId.equals(MySharedPref.getdata(FriendChatActivity.this,"id"))){
                            ChatModel model=new ChatModel();
                            model.setText(msg);
                            arrayList.add(model);
                            adapter.notifyItemChanged(arrayList.size()-1);
                            recyclerView.scrollToPosition(arrayList.size()-1);

                            // reade message
                            readeMessage();
                        }
                      //  Log.i("emit",msg);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });


        }
    };


    private void send_messge_toserver(String msg){
        String url=domain+"api/send_message";

        StringRequest request=new MyRequest(token, 1, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
              //  Log.i("send",response);

            }
        },new OnErrorRequest(FriendChatActivity.this, new ErrorCall() {
            @Override
            public void OnBack() {

            }
        })){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> map=new HashMap<>();
                map.put("message",msg);
                map.put("to",us_id);
                return map;
            }
        };

        Myvollysinglton.getInstance(this).addtorequst(request);
    }

    private void readeMessage(){

        String url=domain+"api/read_message";
        StringRequest request=new MyRequest(token, 1, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                  Log.i("send",response);

            }
        },new OnErrorRequest(FriendChatActivity.this, new ErrorCall() {
            @Override
            public void OnBack() {

            }
        })){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> map=new HashMap<>();
                map.put("user_id",us_id);
                return map;
            }
        };

        Myvollysinglton.getInstance(this).addtorequst(request);

    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        mSocket.disconnect();
        mSocket.close();

        MySharedPref.setdata(this,"inchat","out");

        overridePendingTransition(R.anim.slide_from_righ,R.anim.slide_to_left);

    }




    @Override
    protected void onStart() {
        super.onStart();
        MySharedPref.setdata(this,"inchat","in");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        MySharedPref.setdata(this,"inchat","in");
    }
}
