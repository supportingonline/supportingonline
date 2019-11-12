package com.egypt.supporting.supportingonline;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.egypt.supporting.supportingonline.Adapters.GroupHeaderAdapter;
import com.egypt.supporting.supportingonline.Calls.ErrorCall;
import com.egypt.supporting.supportingonline.Calls.OnPress;
import com.egypt.supporting.supportingonline.Custom.ConfirmMoneyToLocal;
import com.egypt.supporting.supportingonline.Custom.KeyBoardHiding;
import com.egypt.supporting.supportingonline.Custom.MyLocation;
import com.egypt.supporting.supportingonline.Custom.MyRequest;
import com.egypt.supporting.supportingonline.Custom.MySharedPref;
import com.egypt.supporting.supportingonline.Custom.Myvollysinglton;
import com.egypt.supporting.supportingonline.Custom.OnErrorRequest;
import com.egypt.supporting.supportingonline.Models.GroupHeaderModel;
import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 */
public class SupportingMainFragment extends Fragment {

    private static TextView  textwallet,statofaccount,paymentsOptions,supportingtext;
    private EditText ecode,egroup;
    private Button btn_code,btn_group,btn_work;

    private RecyclerView recyclerView;
    private LinearLayoutManager manager;
    public static ArrayList<GroupHeaderModel> arrayList=new ArrayList<>();
    public static GroupHeaderAdapter adapter;
    private static ProgressBar progressBar;
    private static View view;



    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
         view= inflater.inflate(R.layout.fragment_supporting_main, container, false);


        // text supporting
        supportingtext=(TextView)view.findViewById(R.id.sup_supportingText);


        // text wallet
        textwallet=(TextView)view.findViewById(R.id.sup_wallet);

        refreshTextWallet();

        // progress
        progressBar=(ProgressBar)view.findViewById(R.id.sup_btn_progress);



        // stat of account
        statofaccount=(TextView)view.findViewById(R.id.sup_statofaccount);
        statofaccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(),StatementActivity.class));
                getActivity().overridePendingTransition(R.anim.slide_up,R.anim.fadout);
            }
        });

        // payments options
        paymentsOptions=(TextView)view.findViewById(R.id.sup_payment);
        paymentsOptions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(),PaymentsOptionsActivity.class));
                getActivity().overridePendingTransition(R.anim.slide_up,R.anim.fadout);
            }
        });


        // code
        ecode=(EditText)view.findViewById(R.id.sup_edite_code);
        btn_code=(Button)view.findViewById(R.id.sup_btn_code);
        ecode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String code=s.toString().trim();
                if (code.length()>4){
                    btn_code.setAlpha(1f);
                }else {
                    btn_code.setAlpha(0.5f);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        btn_code.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String code=ecode.getText().toString().trim();
                if (code.length()>4){
                    KeyBoardHiding.hide(v,getActivity());
                    chargeMoneyByCode(code);
                }


            }
        });

        // btn work
        btn_work=(Button)view.findViewById(R.id.sup_work_btn);
        if (MySharedPref.isWorkWithUs(getActivity())){
           // btn_work.setVisibility(View.VISIBLE);
            btn_work.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(getActivity(),WorkWithActivity.class));
                    getActivity().overridePendingTransition(R.anim.slide_from_righ,R.anim.slide_to_left);
                }
            });
        }else {
           // btn_work.setVisibility(View.GONE);
        }


        // group join
        egroup=(EditText)view.findViewById(R.id.sup_edite_group);
        btn_group=(Button)view.findViewById(R.id.sup_btn_group);
        egroup.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String code=s.toString().trim();
                if (code.length()>4){
                    btn_group.setAlpha(1f);
                }else {
                    btn_group.setAlpha(0.5f);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        btn_group.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String code=egroup.getText().toString().trim();
                if (code.length()>4){
                    KeyBoardHiding.hide(v,getActivity());
                    joinGroupByCode(code);
                }

            }
        });

        // recycler
        arrayList.clear();
        arrayList.addAll(HomeActivity.groupshistory);
        recyclerView=(RecyclerView)view.findViewById(R.id.recycler_group_history);
        manager=new LinearLayoutManager(getActivity(),RecyclerView.VERTICAL,false);
        adapter=new GroupHeaderAdapter(arrayList, getActivity(), new OnPress() {
            @Override
            public void onClick(View view, int position) {
                int countUser=0;
                if (arrayList.get(position).getType().equals("1") || arrayList.get(position).getType().equals("2") || arrayList.get(position).getType().equals("3")){
                    countUser=2;

                }else if (arrayList.get(position).getType().equals("4") || arrayList.get(position).getType().equals("5") || arrayList.get(position).getType().equals("6")){
                    countUser=3;
                }
                startActivity(new Intent(getActivity(),GroupContentActivity.class).putExtra("count_user",countUser)
                .putExtra("id",arrayList.get(position).getId()).putExtra("date",arrayList.get(position).getDetails())
                .putExtra("type",arrayList.get(position).getType())
                .putExtra("position",position));
                getActivity().overridePendingTransition(R.anim.slide_up,R.anim.fadout);

            }
        });
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);

        if (arrayList.size()==0){
            // load
            supportingtext.setText(getActivity().getResources().getString(R.string.loading_you_groups));

            loadGroups();
        }else {
            progressBar.setVisibility(View.GONE);
        }

        // currency
        if (!MyLocation.samLocation(getActivity())) {
            getCurrency();
        }





        return view;
    }

    @SuppressLint("SetTextI18n")
    public void refreshTextWallet() {

        textwallet.setText(getResources().getString(R.string.fees_wallet)+"     "+ ConfirmMoneyToLocal.transform(getActivity(),HomeActivity.wallet));
    }


    public static void loadGroups() {
        progressBar.setVisibility(View.VISIBLE);
        arrayList.clear();
        HomeActivity.groupshistory.clear();
        String url=HomeActivity.domain+"api/get_groups";
        StringRequest request=new MyRequest(HomeActivity.token, 1, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
          //   Log.i("groups",response);
                adapter.notifyDataSetChanged();
                progressBar.setVisibility(View.GONE);
                try {
                    JSONObject object=new JSONObject(response);
                    JSONArray array=object.getJSONArray("success");
                    if (array.length()==0){
                        supportingtext.setText(view.getResources().getString(R.string.no_data));
                    }else {
                        supportingtext.setText(view.getResources().getString(R.string.this_is_your_support_groups));
                    }
                    for (int i=0;i<array.length();i++){
                        JSONObject theobject=array.getJSONObject(i);
                        boolean isFinished= Boolean.parseBoolean(theobject.getString("is_finished"));
                        String date=theobject.getString("created_at");
                        String id=theobject.getString("id");
                        JSONObject groupObject=theobject.getJSONObject("group");


                        String type=groupObject.getString("type");
                        String price=groupObject.getString("price");




                        GroupHeaderModel model=new GroupHeaderModel();
                        model.setId(id);
                        model.setType(type);
                        model.setDetails(date);
                        if (type.equals("1") || type.equals("2") || type.equals("3")){
                            model.setImage(R.drawable.towusers);
                        }else  if (type.equals("4") || type.equals("5") || type.equals("6")){
                            model.setImage(R.drawable.threeusers);
                        }
                        if (!isFinished){
                            model.setBesideTitle("("+view.getResources().getString(R.string.stillgoing)+")");
                        }
                        HomeActivity.groupshistory.add(model);
                        arrayList.add(model);
                        adapter.notifyDataSetChanged();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        },new OnErrorRequest(view.getContext(), new ErrorCall() {
            @Override
            public void OnBack() {
                progressBar.setVisibility(View.GONE);

            }
        }));
        Myvollysinglton.getInstance(view.getContext()).addtorequst(request);
    }


    private void chargeMoneyByCode(String code) {

        HomeActivity.progressDialog.show();

        String url=HomeActivity.domain+"api/get_money";

        StringRequest request=new MyRequest(HomeActivity.token, 1, url, new Response.Listener<String>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(String response) {

                HomeActivity.progressDialog.dismiss();
               // Log.i("getmoney",response);

                try {
                    JSONObject object=new JSONObject(response);
                    String s=object.getString("success");
                    if (s.equals("not valid")){

                        // wrong code
                        Toast.makeText(getActivity(), getResources().getString(R.string.notvaildcode), Toast.LENGTH_SHORT).show();
                    }else {

                        // success code
                        MySharedPref.setdata(getActivity(),"wallet",s);
                        textwallet.setText(view.getContext().getResources().getString(R.string.fees_wallet)+"     "+ConfirmMoneyToLocal.transform(view.getContext(),s));
                        Toast.makeText(getActivity(), textwallet.getText(), Toast.LENGTH_SHORT).show();
                        ecode.setText("");
                        btn_code.setAlpha(0.5f);


                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        },new OnErrorRequest(getActivity(), new ErrorCall() {
            @Override
            public void OnBack() {
                HomeActivity.progressDialog.dismiss();

            }
        })){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> map=new HashMap<>();
                map.put("code",code);
                return map;
            }
        };

        Myvollysinglton.getInstance(getActivity()).addtorequst(request);
    }

    private void joinGroupByCode(String code) {
        HomeActivity.progressDialog.show();
        String url=HomeActivity.domain+"api/join_data";
        StringRequest request=new MyRequest(HomeActivity.token, 1, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                HomeActivity.progressDialog.dismiss();
                Log.i("join group",response);

                try {
                    JSONObject object=new JSONObject(response);

                    if (object.has("success")) {
                        JSONObject sucObject = object.getJSONObject("success");
                        String type=sucObject.getString("type");
                        String price=sucObject.getString("price");

                        startActivity(new Intent(getActivity(),ConfirmJoinGroupActivity.class)
                        .putExtra("code",code).putExtra("type",type).putExtra("price",price));
                        getActivity().overridePendingTransition(R.anim.slide_up,R.anim.fadout);
                        egroup.setText("");

                    }else if (object.has("error")){
                        String errorMessage=object.getString("error");
                        Toast.makeText(getActivity(), errorMessage, Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        },new OnErrorRequest(getActivity(), new ErrorCall() {
            @Override
            public void OnBack() {
                HomeActivity.progressDialog.dismiss();

            }
        })){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> map=new HashMap<>();
                map.put("code",code);
                return map;
            }
        };
        Myvollysinglton.getInstance(getActivity()).addtorequst(request);
    }

    private void getCurrency() {

        String url=HomeActivity.domain+"api/get_currency";
        StringRequest request=new MyRequest(HomeActivity.token, 1, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
             //   Log.i("res",response);
                try {
                    JSONObject object=new JSONObject(response);
                    JSONObject sucObject=object.getJSONObject("success");
                    String currency=sucObject.getString("currency");
                    String value=sucObject.getString("value");
                    MySharedPref.setdata(view.getContext(),"currency",currency);
                    MySharedPref.setdata(view.getContext(),"value",value);

                    refreshTextWallet();


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



}
