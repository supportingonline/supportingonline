package com.egypt.supporting.supportingonline;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.egypt.supporting.supportingonline.Calls.ErrorCall;
import com.egypt.supporting.supportingonline.Custom.ConfirmMoneyToLocal;
import com.egypt.supporting.supportingonline.Custom.MyRequest;
import com.egypt.supporting.supportingonline.Custom.MySharedPref;
import com.egypt.supporting.supportingonline.Custom.Myvollysinglton;
import com.egypt.supporting.supportingonline.Custom.OnErrorRequest;
import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ConfirmJoinGroupActivity extends AppCompatActivity {

    private String code,type,price,domain,token;
    private int countUser;

    private Button confirm_btn;
    private EditText ename,ephone;
    private TextView textprice,textType;

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_join_group);

        // data
        code=getIntent().getStringExtra("code");
        type=getIntent().getStringExtra("type");
        price=getIntent().getStringExtra("price");
        domain=getResources().getString(R.string.domain);
        token= MySharedPref.getdata(this,"token");

        // progress dialog
        progressDialog=new ProgressDialog(this);
        progressDialog.setMessage(getResources().getString(R.string.loading));

        // name
        ename=(EditText)findViewById(R.id.confirm_edit_name);
        ename.setText(MySharedPref.getdata(this,"name"));


        // phone
        ephone=(EditText)findViewById(R.id.confirm_edit_phone);
        ephone.setText(MySharedPref.getdata(this,"phone"));


        // text price
        textprice=(TextView)findViewById(R.id.confirm_price);
        textprice.setText(ConfirmMoneyToLocal.transform(this,price));

        // text Type
        textType=(TextView)findViewById(R.id.confirm_type);
        String kind=null;
        switch (type){
            case "1":
                kind=getResources().getString(R.string.bronze)+" 2";
                countUser=2;
                break;
            case "2":
                kind=getResources().getString(R.string.silver)+" 2";
                countUser=2;
                break;
            case "3":
                kind=getResources().getString(R.string.gold)+" 2";
                countUser=2;
                break;
            case "4":
                kind=getResources().getString(R.string.bronze)+" 3";
                countUser=3;
                break;
            case "5":
                kind=getResources().getString(R.string.silver)+" 3";
                countUser=3;
                break;
            case "6":
                kind=getResources().getString(R.string.gold)+" 3";
                countUser=3;
                break;
        }
        textType.setText(kind);


        confirm_btn=(Button)findViewById(R.id.confirm_btn_confirm);
        confirm_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String theName=ename.getText().toString().trim();
                String thePhone=ephone.getText().toString().trim();

                if (theName.length()<5 || thePhone.length()<8){
                    Toast.makeText(ConfirmJoinGroupActivity.this, getResources().getString(R.string.confirm_data), Toast.LENGTH_SHORT).show();
                }else {

                    confirmGroup(theName,thePhone,code);
                }

            }
        });
    }

    private void confirmGroup(String theName, String thePhone, String code) {

        String url=domain+"api/confirm_join";
        StringRequest request=new MyRequest(token, 1, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
               // Log.i("confirm",response);
                try {
                    JSONObject object=new JSONObject(response);
                    if (object.has("success")){
                        String theId=object.getString("success");
                        startActivity(new Intent(ConfirmJoinGroupActivity.this,GroupContentActivity.class)
                        .putExtra("count_user",countUser).putExtra("id",theId)
                        .putExtra("type",type).putExtra("date","Now")
                                .putExtra("first_time",true));
                        overridePendingTransition(R.anim.slide_up,R.anim.fadout);
                        finish();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        },new OnErrorRequest(ConfirmJoinGroupActivity.this, new ErrorCall() {
            @Override
            public void OnBack() {

            }
        })){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map=new HashMap<>();
                map.put("name",theName);
                map.put("phone",thePhone);
                map.put("code",code);
                return map;
            }
        };

        Myvollysinglton.getInstance(this).addtorequst(request);
    }
}
