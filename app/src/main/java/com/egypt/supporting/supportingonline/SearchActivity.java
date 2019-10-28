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
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ProgressBar;

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
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SearchActivity extends AppCompatActivity {

   private EditText editText;
   private String domain,token;

   private RecyclerView recyclerView;
   private SearchAdapter adapter;
   private ArrayList<SearchModel> arrayList=new ArrayList<>();
   private ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);


        // domain and token
        domain=getResources().getString(R.string.domain);
        token= MySharedPref.getdata(SearchActivity.this,"token");

        // progress
        progressBar=(ProgressBar)findViewById(R.id.search_progress);

        // recycler
        arrayList.clear();
        recyclerView=(RecyclerView)findViewById(R.id.recycler_search);
        adapter=new SearchAdapter(arrayList, this, new OnPress() {
            @Override
            public void onClick(View view, int position) {

                // view profile

                if (arrayList.get(position).getId().equals(HomeActivity.myid)){
                    startActivity(new Intent(SearchActivity.this,MyprofileActivity.class));

                }else {
                    startActivity(new Intent(SearchActivity.this, OtherProfileActivity.class)
                            .putExtra("id", arrayList.get(position).getId())
                            .putExtra("name", arrayList.get(position).getName()));
                }
                overridePendingTransition(R.anim.slide_from_left,R.anim.slide_to_right);

            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(this,RecyclerView.VERTICAL,false));
        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL));
        recyclerView.addItemDecoration(new SpaceRecycler_V(MySizes.gethight(this)/90));

        recyclerView.setAdapter(adapter);

        // edit
        editText=(EditText)findViewById(R.id.search_edit);
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Myvollysinglton.cancel("search");
                arrayList.clear();
                adapter.notifyDataSetChanged();
                String ss=editText.getText().toString().trim();
                if (ss.length()>0){
                    beginsearch(ss);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void beginsearch(final String key){
        progressBar.setVisibility(View.VISIBLE);
        arrayList.clear();
        adapter.notifyDataSetChanged();
        String url=domain+"api/search";
        StringRequest request=new MyRequest(token, Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressBar.setVisibility(View.INVISIBLE);
                try {
                    JSONObject object=new JSONObject(response);
                    if (object.has("success")) {
                        JSONArray array = object.getJSONArray("success");
                       // Log.i("name",String.valueOf(array.length()));
                        for (int i=0 ;i<array.length();i++){
                            JSONObject userobject=array.getJSONObject(i);
                            String id=userobject.getString("id");
                            String name=userobject.getString("name");
                            String image=userobject.getString("image");
                          // Log.i("name",name);
                            SearchModel model=new SearchModel();
                            model.setId(id);
                            model.setName(name);
                            model.setImage(image);
                            arrayList.add(model);

                        }
                        adapter.notifyDataSetChanged();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        },new OnErrorRequest(SearchActivity.this, new ErrorCall() {
            @Override
            public void OnBack() {
                progressBar.setVisibility(View.INVISIBLE);

            }
        })){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> map=new HashMap<>();
                map.put("search",key);
                return map;
            }
        };
        request.setTag("search");
        Myvollysinglton.getInstance(this).addtorequst(request);
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

    }


}
