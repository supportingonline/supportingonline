package com.egypt.supporting.supportingonline;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.egypt.supporting.supportingonline.Adapters.StatementAdapter;
import com.egypt.supporting.supportingonline.Calls.ErrorCall;
import com.egypt.supporting.supportingonline.Custom.MyRequest;
import com.egypt.supporting.supportingonline.Custom.MySizes;
import com.egypt.supporting.supportingonline.Custom.Myvollysinglton;
import com.egypt.supporting.supportingonline.Custom.OnErrorRequest;
import com.egypt.supporting.supportingonline.Custom.SpaceRecycler_V;
import com.egypt.supporting.supportingonline.Models.StatementModel;
import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class StatementActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private LinearLayoutManager manager;
    private StatementAdapter adapter;
    private ArrayList<StatementModel> arrayList=new ArrayList<>();

    private ProgressBar progressBar;
    private TextView textNoData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statement);

        // keyboard hedin
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        // progress
        progressBar=(ProgressBar)findViewById(R.id.statement_progress);

        // text no data
        textNoData=(TextView)findViewById(R.id.statement_noData);
        textNoData.setVisibility(View.GONE);

        // recycler
        recyclerView=(RecyclerView)findViewById(R.id.recycler_statement);
        manager=new LinearLayoutManager(this,RecyclerView.VERTICAL,false);
        recyclerView.setLayoutManager(manager);
        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL));
        recyclerView.addItemDecoration(new SpaceRecycler_V(MySizes.gethight(this)/90));
        adapter=new StatementAdapter(arrayList,this);
        recyclerView.setAdapter(adapter);

        // load
        loadStatement();
    }

    private void loadStatement() {
        String url=HomeActivity.domain+"api/fees_wallet";
        StringRequest request=new MyRequest(HomeActivity.token, 1, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
               // Log.i("statment",response);
                try {
                    JSONObject object=new JSONObject(response);
                    JSONArray array=object.getJSONArray("success");
                    if (array.length()==0){
                        textNoData.setVisibility(View.VISIBLE);

                    }
                    progressBar.setVisibility(View.INVISIBLE);
                    for (int i =0;i<array.length();i++){
                        JSONObject theObject=array.getJSONObject(i);
                        StatementModel model=new StatementModel();
                        model.setDate(theObject.getString("created_at"));
                        model.setMoney(theObject.getString("price"));
                        model.setType(returnTypeName(theObject.getString("type")));
                        arrayList.add(model);
                        adapter.notifyDataSetChanged();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        },new OnErrorRequest(StatementActivity.this, new ErrorCall() {
            @Override
            public void OnBack() {

            }
        })){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> map=new HashMap<>();


                return map;
            }
        };

        Myvollysinglton.getInstance(this).addtorequst(request);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.fadin,R.anim.slide_down);
    }


    private String returnTypeName(String type){
        String t=null;
        if (type.equals("fees")){
            t="Pay Fees";
        }else if (type.equals("model")){
            t="Buying a Model";
        }else if (type.equals("wallet")){
            t="Add To Fees Wallet";
        }

        return t;
    }
}
