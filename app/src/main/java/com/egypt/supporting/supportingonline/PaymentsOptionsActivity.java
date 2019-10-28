package com.egypt.supporting.supportingonline;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.egypt.supporting.supportingonline.Adapters.PaymentAdapter;
import com.egypt.supporting.supportingonline.Calls.ErrorCall;
import com.egypt.supporting.supportingonline.Custom.MyRequest;
import com.egypt.supporting.supportingonline.Custom.MySharedPref;
import com.egypt.supporting.supportingonline.Custom.Myvollysinglton;
import com.egypt.supporting.supportingonline.Custom.OnErrorRequest;
import com.egypt.supporting.supportingonline.Models.PaymentModel;
import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class PaymentsOptionsActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private LinearLayoutManager manager;
    private PaymentAdapter adapter;
    private ArrayList<PaymentModel> arrayList=new ArrayList<>();

    private EditText editText;
    private Spinner spinner;
    private Button btn_confirm;
    private String[] numbers;

    private String domain,token;

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payments_options);

        // keyboard hedin
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        // domain and token
        domain=getResources().getString(R.string.domain);
        token= MySharedPref.getdata(this,"token");


        // progress dialog
        progressDialog=new ProgressDialog(this);
        progressDialog.setMessage(getResources().getString(R.string.loading));
        
        // recycler
        recyclerView=(RecyclerView)findViewById(R.id.recycler_paymentOption);
        manager=new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        adapter=new PaymentAdapter(arrayList,this);
        recyclerView.setLayoutManager(manager);
        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL));
        recyclerView.setAdapter(adapter);

       // edit  ,  spinner , btn
        editText=(EditText)findViewById(R.id.payment_edit);
        spinner=(Spinner)findViewById(R.id.payment_spinner);
        btn_confirm=(Button)findViewById(R.id.payment_confirm);


        numbers=new String[]{getResources().getString(R.string.account_number),"3708341715301"};
        ArrayAdapter<String> spinnerAdapter=new ArrayAdapter<String>(this,R.layout.textspinner2,numbers);
        spinner.setAdapter(spinnerAdapter);

        btn_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (spinner.getSelectedItemPosition()==0 || editText.getText().toString().trim().length()<5){

                    Toast.makeText(PaymentsOptionsActivity.this, getResources().getString(R.string.confirm_data), Toast.LENGTH_SHORT).show();

                }else {
                    confermToServer(editText.getText().toString().trim(),numbers[spinner.getSelectedItemPosition()]);

                }
            }
        });



        
        // load recycler
        loadrecyler();
    }


    private void loadrecyler() {

        String[] names=getResources().getStringArray(R.array.banks_name);
        String[] countryes={"Emirate"};
        int logos[]={R.drawable.emiratesislamic};

        for (int i=0;i<names.length;i++){
            PaymentModel model=new PaymentModel();
            model.setBankName(names[i]);
            model.setAccountNumber(numbers[i+1]);
            model.setCountry(countryes[i]);
            model.setLogo(logos[i]);
            model.setCurrency("AED");
            arrayList.add(model);
            adapter.notifyDataSetChanged();
        }
    }

    private void confermToServer(String code,String number) {

        progressDialog.show();
        String url=domain+"api/payment_code";
        StringRequest request=new MyRequest(token, 1, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("res",response);
                progressDialog.dismiss();

                try {
                    JSONObject object=new JSONObject(response);
                    if (object.has("success")){
                        Toast.makeText(PaymentsOptionsActivity.this, getResources().getString(R.string.successsend), Toast.LENGTH_SHORT).show();
                        onBackPressed();

                    }else if (object.has("error")){
                        Toast.makeText(PaymentsOptionsActivity.this, object.getString("error"), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new OnErrorRequest(PaymentsOptionsActivity.this, new ErrorCall() {
            @Override
            public void OnBack() {
                progressDialog.dismiss();

            }
        })){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map=new HashMap<>();
                map.put("code",code);
                map.put("visa_num",number);
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

}
