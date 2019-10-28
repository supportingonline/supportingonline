package com.egypt.supporting.supportingonline;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.egypt.supporting.supportingonline.Adapters.CommentAdapter;
import com.egypt.supporting.supportingonline.Calls.ErrorCall;
import com.egypt.supporting.supportingonline.Calls.OnPress;
import com.egypt.supporting.supportingonline.Custom.MyRequest;
import com.egypt.supporting.supportingonline.Custom.MySharedPref;
import com.egypt.supporting.supportingonline.Custom.MySizes;
import com.egypt.supporting.supportingonline.Custom.Myvollysinglton;
import com.egypt.supporting.supportingonline.Custom.OnErrorRequest;
import com.egypt.supporting.supportingonline.Custom.SpaceRecycler_V;
import com.egypt.supporting.supportingonline.Models.CommentModel;
import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CommentActivity extends AppCompatActivity {

    private String post_id,domain,token,nextPage;
    private EditText editText;
    private RelativeLayout sendlayout;

    private RecyclerView recyclerView;
    private LinearLayoutManager manager;
    private ArrayList<CommentModel> arrayList=new ArrayList<>();
    private CommentAdapter adapter;
    private boolean isTimline;
    private int position;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);

        // domain and token
        domain=getResources().getString(R.string.domain);
        token= MySharedPref.getdata(this,"token");

        // post id
        post_id=getIntent().getStringExtra("post_id");
        isTimline=getIntent().getBooleanExtra("is_timeline",false);
        position=getIntent().getIntExtra("position",0);

        // send layout and edit
        editText=(EditText)findViewById(R.id.comment_edit);
        sendlayout=(RelativeLayout)findViewById(R.id.comment_layout);
        sendlayout.setClickable(false);


        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String ss=charSequence.toString().trim();
                if (ss.length()>0){
                    sendlayout.setAlpha(1f);

                }else {
                    sendlayout.setAlpha(0.5f);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        // click send
        sendlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String text=editText.getText().toString().trim();
                editText.setText("");
                sendlayout.setAlpha(0.5f);

                if (text.trim().length()>0) {
                    // add to recycler
                    CommentModel model = new CommentModel();
                    model.setDate("now");
                    model.setId("");
                    model.setDetails(text);
                    model.setImage(MySharedPref.getdata(CommentActivity.this, "image"));
                    model.setUser_id(MySharedPref.getdata(CommentActivity.this, "id"));
                    model.setUser_name(MySharedPref.getdata(CommentActivity.this, "name"));
                    arrayList.add(model);
                    adapter.notifyDataSetChanged();
                    recyclerView.scrollToPosition(arrayList.size() - 1);


                    if (isTimline){
                        TimeLineFragment fragment= (TimeLineFragment) HomeActivity.fragments.get(0);
                        fragment.addOneToComment(position);
                    }else {
                        boolean isMe=getIntent().getBooleanExtra("is_me",false);
                        if (isMe){
                            PostsFragment fragment=(PostsFragment)MyprofileActivity.fragments.get(0);
                            fragment.addOneToComment(position);
                        }else {
                            PostsFragment fragment=(PostsFragment)OtherProfileActivity.fragments.get(0);
                            fragment.addOneToComment(position);
                        }

                    }
                    // server comment

                    comment(text);
                }
            }
        });


        // recycler

        recyclerView=(RecyclerView)findViewById(R.id.recycler_comments);
        manager=new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        adapter=new CommentAdapter(arrayList, this, new OnPress() {
            @Override
            public void onClick(View view, int position) {


                if (!arrayList.get(position).getId().equals(HomeActivity.myid)){

                    startActivity(new Intent(CommentActivity.this, OtherProfileActivity.class)
                            .putExtra("id", arrayList.get(position).getId())
                            .putExtra("name", arrayList.get(position).getUser_name()));
                   overridePendingTransition(R.anim.slide_from_left,R.anim.slide_to_right);
                }
            }
        });
        recyclerView.setLayoutManager(manager);
        recyclerView.addItemDecoration(new SpaceRecycler_V(MySizes.gethight(this)/90));
        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL));
        recyclerView.setAdapter(adapter);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (!recyclerView.canScrollVertically(-1)) {
                  //  Toast.makeText(CommentActivity.this, "first", Toast.LENGTH_LONG).show();
                    if (!nextPage.equals("null")) {
                        loadComments(nextPage);
                    }

                }
            }
        });

        // load
        loadComments(domain+"api/comments");
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.fadin,R.anim.slide_down);
    }

    private void comment(String text){
        String url=domain+"api/comment";
        StringRequest request=new MyRequest(token, 1, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
               // Log.i("resComment",response);
                try {
                    JSONObject object=new JSONObject(response);
                    if (object.has("success")){

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        },new OnErrorRequest(CommentActivity.this, new ErrorCall() {
            @Override
            public void OnBack() {

            }
        })){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String>  map=new HashMap<>();
                map.put("post_id",post_id);
                map.put("comment",text.replace("\n","<br/>"));

                return map;
            }
        };
        Myvollysinglton.getInstance(this).addtorequst(request);

    }

    private void loadComments(String url){

        StringRequest request=new MyRequest(token, 1, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
             //Log.i("tttttt",response);
                try {
                    JSONObject object=new JSONObject(response);
                    if (object.has("success")) {

                        JSONObject jsonObject = object.getJSONObject("success");
                        nextPage=jsonObject.getString("next_page_url");
                       // Log.i("nextPage",nextPage);
                        JSONArray array = jsonObject.getJSONArray("data");
                        if (array.length()>0){
                            for (int i=0;i<array.length();i++){
                                JSONObject commentobObject=array.getJSONObject(i);
                               // Log.i("commment",commentobObject.toString()+"\n");
                                String text=commentobObject.getString("comment");
                                String id=commentobObject.getString("id");
                                String date=commentobObject.getString("created_at");
                                JSONObject useroObject=commentobObject.getJSONObject("user");
                                String usre_id=useroObject.getString("id");
                                String user_name=useroObject.getString("name");
                                String user_image=useroObject.getString("image");
                             // Log.i("comment",text);

                                CommentModel model=new CommentModel();
                                model.setId(id);
                                model.setDate(date);
                                model.setDetails(text);
                                model.setImage(user_image);
                                model.setUser_id(usre_id);
                                model.setUser_name(user_name);

                                arrayList.add(0,model);
                                adapter.notifyDataSetChanged();
                            }
                            recyclerView.scrollToPosition(arrayList.size()-1);

                        }

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        },new OnErrorRequest(CommentActivity.this, new ErrorCall() {
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

        Myvollysinglton.getInstance(this).addtorequst(request);

    }
}
