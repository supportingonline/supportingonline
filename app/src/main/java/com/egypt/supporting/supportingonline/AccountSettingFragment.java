package com.egypt.supporting.supportingonline;


import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.egypt.supporting.supportingonline.Calls.ErrorCall;
import com.egypt.supporting.supportingonline.Custom.MyRequest;
import com.egypt.supporting.supportingonline.Custom.MySharedPref;
import com.egypt.supporting.supportingonline.Custom.Myvollysinglton;
import com.egypt.supporting.supportingonline.Custom.OnErrorRequest;
import com.egypt.supporting.supportingonline.R;
import com.egypt.supporting.supportingonline.Value.MyData;
import com.egypt.supporting.supportingonline.Views.MyEditText;
import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class AccountSettingFragment extends Fragment {

    private EditText ename,ephone,eemail,eeducation,elocation,eabout;
    private Button btn_confirm;
    private ProgressDialog  progressDialog;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_account_setting, container, false);


        progressDialog=new ProgressDialog(getActivity());
        progressDialog.setMessage(getActivity().getResources().getString(R.string.loading));

        ename=(EditText)view.findViewById(R.id.setting_edit_name);
        ephone=(EditText)view.findViewById(R.id.setting_edit_phone);
        eemail=(EditText)view.findViewById(R.id.setting_edit_email);
        MyEditText.enableDisableEditText(false,eemail);
        eeducation=(EditText)view.findViewById(R.id.setting_edit_education);
        elocation=(EditText)view.findViewById(R.id.setting_edit_location);
        eabout=(EditText)view.findViewById(R.id.setting_edit_about);
        btn_confirm=(Button)view.findViewById(R.id.setting_btn);

        ename.setText(MySharedPref.getdata(getActivity(),"name"));
        ephone.setText(MySharedPref.getdata(getActivity(),"phone"));
        eemail.setText(MySharedPref.getdata(getActivity(),"email"));
        eeducation.setText(MySharedPref.getdata(getActivity(),"education"));
        elocation.setText(MySharedPref.getdata(getActivity(),"location"));
        eabout.setText(MySharedPref.getdata(getActivity(),"about"));


        btn_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name= MyData.getStringFromEdit(ename);
                String phone=MyData.getStringFromEdit(ephone);
                String education=MyData.getStringFromEdit(eeducation);
                String location=MyData.getStringFromEdit(elocation);
                String about=MyData.getStringFromEdit(eabout);

                ArrayList<String> keyList=new ArrayList<>();
                ArrayList<String> valueList=new ArrayList<>();

                if (name.length()<5 || phone.length()<5){


                }else {

                    if (!name.equals(MySharedPref.getdata(getActivity(), "name"))) {
                        keyList.add("name");
                        valueList.add(name);
                    }
                    if (!education.equals(MySharedPref.getdata(getActivity(), "phone"))) {
                        keyList.add("phone");
                        valueList.add(phone);
                    }
                    if (!phone.equals(MySharedPref.getdata(getActivity(), "education"))) {
                        keyList.add("education");
                        valueList.add(education);
                    }
                    if (!phone.equals(MySharedPref.getdata(getActivity(), "about"))) {
                        keyList.add("about");
                        valueList.add(about);
                    }
                    if (!phone.equals(MySharedPref.getdata(getActivity(), "location"))) {
                        keyList.add("location");
                        valueList.add(location);
                    }

                    update(keyList, valueList);

                }

            }
        });

        return view;
    }




    private void update(ArrayList<String> keyList,ArrayList<String> valueList){
       progressDialog.show();
        String url=HomeActivity.domain+"api/update_user";
        StringRequest request=new MyRequest(HomeActivity.token, 1, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                Log.i("update",response);

                try {
                    JSONObject object=new JSONObject(response);
                    JSONObject sucObject=object.getJSONObject("success");
                    String name=sucObject.getString("name");
                    String phone=sucObject.getString("phone");
                    String education = sucObject.getString("education");
                    String location = sucObject.getString("location");
                    String about = sucObject.getString("about");

                    MySharedPref.setdata(getActivity(),"name", name);
                    MyprofileActivity.textname.setText(name);
                    MySharedPref.setdata(getActivity(),"phone", phone);
                    MySharedPref.setdata(getActivity(),"education",education);
                    MySharedPref.setdata(getActivity(),"location",location);
                    MySharedPref.setdata(getActivity(),"about",about);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        },new OnErrorRequest(getActivity(), new ErrorCall() {
            @Override
            public void OnBack() {
               progressDialog.dismiss();

            }
        })){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return getTheMap(keyList,valueList);
            }
        };
        Myvollysinglton.getInstance(getActivity()).addtorequst(request);

    }

    private Map<String,String> getTheMap(ArrayList<String> keyList,ArrayList<String> valueList){
        Map<String,String> map=new HashMap<>();
        for (int i=0;i<keyList.size();i++){
            map.put(keyList.get(i),valueList.get(i));
        }
        return map;

    }

}
