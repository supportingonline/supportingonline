package com.egypt.supporting.supportingonline;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.egypt.supporting.supportingonline.Adapters.SearchAdapter;
import com.egypt.supporting.supportingonline.Calls.ErrorCall;
import com.egypt.supporting.supportingonline.Calls.OnPress;
import com.egypt.supporting.supportingonline.Custom.MyRequest;
import com.egypt.supporting.supportingonline.Custom.MySharedPref;
import com.egypt.supporting.supportingonline.Custom.MySizes;
import com.egypt.supporting.supportingonline.Custom.Myvollysinglton;
import com.egypt.supporting.supportingonline.Custom.OnErrorRequest;
import com.egypt.supporting.supportingonline.Custom.SpaceRecycler_V;
import com.egypt.supporting.supportingonline.Models.SearchModel;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MyfriendsActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private LinearLayoutManager manager;
    private SearchAdapter adapter;
    private ArrayList<SearchModel> arrayList,fullList;

    private EditText editText;

    private String domain,token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myfriends);


        // domain and token
        domain=getResources().getString(R.string.domain);
        token= MySharedPref.getdata(this,"token");

        // recycler
        arrayList=new ArrayList<>();
        fullList=new ArrayList<>();
        recyclerView=(RecyclerView)findViewById(R.id.recycler_myfriends);
        manager=new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        adapter=new SearchAdapter(arrayList, this, new OnPress() {
            @Override
            public void onClick(View view, int position) {

                startActivity(new Intent(MyfriendsActivity.this, OtherProfileActivity.class)
                        .putExtra("id",arrayList.get(position).getId())
                        .putExtra("name",arrayList.get(position).getName()));
                overridePendingTransition(R.anim.slide_from_left,R.anim.slide_to_right);
            }
        });
        recyclerView.setLayoutManager(manager);
        recyclerView.addItemDecoration(new SpaceRecycler_V(MySizes.gethight(this)/90));
        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL));
        recyclerView.setAdapter(adapter);

        // edite
        editText=(EditText)findViewById(R.id.myfriends_edit);
        editText.setClickable(false);
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                arrayList.clear();
                String s=charSequence.toString().trim();
                if (s.length()>0){
                    filterList(s);
                }else {
                   arrayList.addAll(fullList);
                   adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        loadFriends();
    }

    private void loadFriends() {
        String url=domain+"api/profile";

        StringRequest request=new MyRequest(token, 1, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject object=new JSONObject(response);
                    JSONObject sucobObject=object.getJSONObject("success");
                    JSONArray array=sucobObject.getJSONArray("my_friends");
                    Log.i("friend",array.toString());
                    for (int i=0;i<array.length();i++){
                        JSONObject ob=array.getJSONObject(i);
                        String id=ob.getString("id");
                        String name=ob.getString("name");
                        String image=ob.getString("image");
                        SearchModel model=new SearchModel();
                        model.setId(id);
                        model.setName(name);
                        model.setImage(image);
                        arrayList.add(model);
                        adapter.notifyDataSetChanged();
                    }
                    fullList.addAll(arrayList);
                    editText.setClickable(true);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        },new OnErrorRequest(MyfriendsActivity.this, new ErrorCall() {
            @Override
            public void OnBack() {

            }
        }));

        Myvollysinglton.getInstance(this).addtorequst(request);
    }

    private void filterList(String key){
        for (int i=0;i<fullList.size();i++){
            SearchModel model=fullList.get(i);
            String name=model.getName();
            if (name.toLowerCase().contains(key.toLowerCase())){
                arrayList.add(model);
            }
        }
        adapter.notifyDataSetChanged();
    }
    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (editText.isFocused() ) {
                Rect outRect = new Rect();
                editText.getGlobalVisibleRect(outRect);

                if (!outRect.contains((int)event.getRawX(), (int)event.getRawY())) {
                    editText.clearFocus();
                    editText.clearFocus();

                    //
                    // Hide keyboard
                    //
                    hidekeyboadr(v);
                }
            }
        }
        return super.dispatchTouchEvent(event);
    }

    public void hidekeyboadr(View v){

        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_from_righ,R.anim.slide_to_left);

    }

}
