package com.egypt.supporting.supportingonline;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.egypt.supporting.supportingonline.Calls.ErrorCall;
import com.egypt.supporting.supportingonline.Custom.MyLanguage;
import com.egypt.supporting.supportingonline.Custom.MyRequest;
import com.egypt.supporting.supportingonline.Custom.MySharedPref;
import com.egypt.supporting.supportingonline.Custom.Myvollysinglton;
import com.egypt.supporting.supportingonline.Custom.OnErrorRequest;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {


    private String token,domain;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //startActivity(new Intent(this,GroupContentActivity.class));



        // language
        MyLanguage.changeLanguage(this);

        //token
        token= MySharedPref.getdata(this,"token");
       // Log.i("token",token);
        if (token.equals("")){

            nextactivity(new Intent(MainActivity.this,LoginActivity.class));
        }else {

            // domain
            domain=getResources().getString(R.string.domain);
            login();
        }


    }

    private void login(){

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
                        String wallet = jsonObject.getString("wallet");
                        String im_profile = jsonObject.getString("profile_img");
                        String work_with = jsonObject.getString("work_with");
                        String education = jsonObject.getString("education");
                        String location = jsonObject.getString("location");
                        String about = jsonObject.getString("about");

                        if (work_with.equals("1")){
                            MySharedPref.setWorkWith(MainActivity.this,true);
                        }else {
                            MySharedPref.setWorkWith(MainActivity.this,false);

                        }


                        MySharedPref.setdata(MainActivity.this,"id",id);
                        MySharedPref.setdata(MainActivity.this,"name",name);
                        MySharedPref.setdata(MainActivity.this,"email",email);
                        MySharedPref.setdata(MainActivity.this,"image",image);
                        MySharedPref.setdata(MainActivity.this,"phone",phone);
                        MySharedPref.setdata(MainActivity.this,"wallet",wallet);
                        MySharedPref.setdata(MainActivity.this,"profile_img",im_profile);
                        MySharedPref.setdata(MainActivity.this,"education",education);
                        MySharedPref.setdata(MainActivity.this,"location",location);
                        MySharedPref.setdata(MainActivity.this,"about",about);


                        nextactivity(new Intent(MainActivity.this,HomeActivity.class));

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new OnErrorRequest(MainActivity.this, new ErrorCall() {
            @Override
            public void OnBack() {

            }
        }));
        Myvollysinglton.getInstance(this).addtorequst(request);
    }

    private void nextactivity(Intent intent){
        startActivity(intent);
        overridePendingTransition(R.anim.fadin,R.anim.fadout);
        finish();
    }


}
