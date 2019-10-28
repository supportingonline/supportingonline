package com.egypt.supporting.supportingonline;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CreateGroupActivity extends AppCompatActivity {

    private String type,domain,token,name,id,phone;

    private String[] prices;

    private EditText ename,ephone,epromo;
    private Spinner spinner;
    private Button confirm_btn;
    private ImageView imageView;
    private TextView textType,textmore;


    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_group);



        // keyboard hedin
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        // type , domain , token , name , id , phone , email
        type=getIntent().getStringExtra("type");
        domain=getResources().getString(R.string.domain);
        token= MySharedPref.getdata(this,"token");
        name= MySharedPref.getdata(this,"name");
        id= MySharedPref.getdata(this,"id");
        phone= MySharedPref.getdata(this,"phone");


        // progress dialog
        progressDialog=new ProgressDialog(this);
        progressDialog.setMessage(getResources().getString(R.string.loading));

        // kind text to type
        String kind=null;

        // image
        imageView=(ImageView)findViewById(R.id.create_image);
        int im=0;
        switch (type){
            case "1":
                im=R.drawable.bronze2;
                kind=getResources().getString(R.string.bronze)+" 2";
                break;
            case "2":
                im=R.drawable.silver2;
                kind=getResources().getString(R.string.silver)+" 2";
                break;
            case "3":
                im=R.drawable.gold2;
                kind=getResources().getString(R.string.gold)+" 2";
                break;
            case "4":
                im=R.drawable.bronze3;
                kind=getResources().getString(R.string.bronze)+" 3";
                break;
            case "5":
                im=R.drawable.silver3;
                kind=getResources().getString(R.string.silver)+" 3";
                break;
            case "6":
                im=R.drawable.gold3;
                kind=getResources().getString(R.string.gold)+" 3";
                break;
        }

        imageView.setBackgroundResource(im);

        // text Type
        textType=(TextView)findViewById(R.id.create_type);
        textType.setText(kind);

        // text more
        textmore=(TextView)findViewById(R.id.create_more);
        textmore.append(" "+kind);
        textmore.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);
        textmore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CreateGroupActivity.this,AboutUsActivity.class)
                        .putExtra("position",(Integer.parseInt(type)-1)));
                overridePendingTransition(R.anim.slide_up,R.anim.fadout);
            }
        });

        // name
        ename=(EditText)findViewById(R.id.create_edit_name);
        ename.setText(name);


        // phone
        ephone=(EditText)findViewById(R.id.create_edit_phone);
        ephone.setText(phone);

        // promo
        epromo=(EditText)findViewById(R.id.create_edit_promo);

        // spinner
        spinner=(Spinner)findViewById(R.id.create_spinner);
         prices=getResources().getStringArray(R.array.prices);
        ArrayList<String> newPrices=new ArrayList<>();
        for (int i =0; i<prices.length;i++){
            newPrices.add(ConfirmMoneyToLocal.transform(this,prices[i]));
        }
         ArrayAdapter<String> adapter=new ArrayAdapter<String>(this,R.layout.textspinner,newPrices);
         spinner.setAdapter(adapter);



        // btn
        confirm_btn=(Button)findViewById(R.id.create_btn_confirm);
        confirm_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String theName=ename.getText().toString().trim();
                String thePhone=ephone.getText().toString().trim();
                String thePrice=prices[spinner.getSelectedItemPosition()];
                String thePromo=epromo.getText().toString().trim();

                if (theName.length()<5 || thePhone.length()<8){
                    Toast.makeText(CreateGroupActivity.this, getResources().getString(R.string.confirm_data), Toast.LENGTH_SHORT).show();
                }else {
                    if (thePromo.length()==0){
                        thePromo=null;
                    }
                    createGroup(theName,thePhone,type,thePrice,thePromo);
                }

            }
        });




    }

    private void createGroup(String thename,String thephone,String thetype,String theprice,String thepromo){
        progressDialog.show();
        String url=domain+"api/create_group";
        StringRequest request=new MyRequest(token, 1, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
               //Log.i("creat",response);
                try {
                    JSONObject object=new JSONObject(response);
                    if (object.has("success")){

                        String gId=object.getString("success");
                        int countUser=0;
                        if (type.equals("1") || type.equals("2") || type.equals("3")){
                            countUser=2;

                        }else if (type.equals("4") || type.equals("5") || type.equals("6")){
                            countUser=3;
                        }
                        startActivity(new Intent(CreateGroupActivity.this,GroupContentActivity.class).putExtra("count_user",countUser)
                                .putExtra("id",gId).putExtra("type",type));
                        overridePendingTransition(R.anim.slide_up,R.anim.fadout);
                        SupportingMainFragment.loadGroups();
                        finish();

                    }else if (object.has("error")){
                        Toast.makeText(CreateGroupActivity.this, object.getString("error"), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        },new OnErrorRequest(CreateGroupActivity.this, new ErrorCall() {
            @Override
            public void OnBack() {

            }
        })){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map=new HashMap<>();
                map.put("name",thename);
                map.put("phone",thephone);
                map.put("group",thetype);
                map.put("price",theprice);
                if (thepromo!=null){
                    map.put("promo",thepromo);
                }
                return map;
            }
        };

        Myvollysinglton.getInstance(this).addtorequst(request);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.fadin,R.anim.fadout);
    }

}
