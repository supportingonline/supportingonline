package com.egypt.supporting.supportingonline;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.egypt.supporting.supportingonline.Adapters.EmojiAdapter;
import com.egypt.supporting.supportingonline.Calls.ErrorCall;
import com.egypt.supporting.supportingonline.Calls.OnPress;
import com.egypt.supporting.supportingonline.Custom.MyRequest;
import com.egypt.supporting.supportingonline.Custom.MySharedPref;
import com.egypt.supporting.supportingonline.Custom.MySizes;
import com.egypt.supporting.supportingonline.Custom.Myvollysinglton;
import com.egypt.supporting.supportingonline.Custom.OnErrorRequest;
import com.egypt.supporting.supportingonline.Custom.SpaceRecycler_V;
import com.egypt.supporting.supportingonline.Custom.URLImageParser;
import com.egypt.supporting.supportingonline.Models.EmojiModel;
import com.egypt.supporting.supportingonline.Value.Image;
import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.github.aakira.expandablelayout.ExpandableRelativeLayout;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xml.sax.XMLReader;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class WritePostActivity extends AppCompatActivity {

    private ImageView imagecapture,imageEmoji,imageview,cancelimage;
    private ExpandableRelativeLayout exp;
    private EditText editText;
    private String type="text";
    private Bitmap bitmap;
    private RelativeLayout layoutimage;
    private Button button;
    private String domain,token;
    private ProgressDialog prog;
    private RecyclerView recyclerView;
    private GridLayoutManager manager;
    private ArrayList<EmojiModel> arrayList;
    private EmojiAdapter adapter;
    private ArrayList<String> myempjelist=new ArrayList<>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_post);


        // domain and token
        domain=getResources().getString(R.string.domain);
        token= MySharedPref.getdata(this,"token");

        // progress dialog
        prog=new ProgressDialog(this);
        prog.setTitle(getResources().getString(R.string.loading));

        // btn share
        button=(Button)findViewById(R.id.write_btnshare);

        // edit
        editText=(EditText)findViewById(R.id.write_edit);
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String ss= charSequence.toString().trim();
                if (ss.length()>0){
                  button.setAlpha(1f);
                }else {
                    button.setAlpha(0.5f);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        // capture
        imagecapture=(ImageView) findViewById(R.id.write_capture);
        imagecapture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CropImage.activity()
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .start(WritePostActivity.this);
            }
        });

        // expandle
        exp=(ExpandableRelativeLayout)findViewById(R.id.write_exp);
       // exp.collapse();

        // image emoji
        imageEmoji=(ImageView)findViewById(R.id.write_emoji);
        imageEmoji.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                exp.toggle();
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String s=editText.getText().toString().trim();

                StringBuilder builder=new StringBuilder();
                int count=0;
                for (String word : s.split(" ")) {
                    //Log.i("word",word);
                    if (word.equals("￼")){
                        builder.append(myempjelist.get(count));
                        count++;
                    }else {

                        builder.append(word);
                        builder.append(" ");
                    }
                }


                if (type.equals("text")){

                    if (s.length()>0){

                        sharePost(builder.toString().replace("\n","<br/>"));
                    }

                }else if (type.equals("image")){
                    sharePost(builder.toString().replace("\n","<br/>"));
                }
            }
        });

        // recycler emoji
        arrayList=new ArrayList<>();
        recyclerView=(RecyclerView)findViewById(R.id.recycler_emoji);
        manager=new GridLayoutManager(this,8);
        adapter=new EmojiAdapter(arrayList, this, new OnPress() {
            @Override
            public void onClick(View view, int position) {

                editText.append(" ");

                editText.append(Html.fromHtml(arrayList.get(position).getCompUrl(), new URLImageParser(editText, WritePostActivity.this), new Html.TagHandler() {
                    @Override
                    public void handleTag(boolean b, String s, Editable editable, XMLReader xmlReader) {

                     }
                }));
                editText.append(" ");

                myempjelist.add(arrayList.get(position).getCompUrl());

            }
        });
        recyclerView.setLayoutManager(manager);
        recyclerView.addItemDecoration(new SpaceRecycler_V(MySizes.gethight(this)/90));
        recyclerView.setAdapter(adapter);


        // image view
        imageview=(ImageView)findViewById(R.id.write_image);



        // layout image
        layoutimage=(RelativeLayout)findViewById(R.id.write_imagelayout);





        // canecl image
        cancelimage=(ImageView)findViewById(R.id.write_imagecancel);
        cancelimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                layoutimage.setVisibility(View.GONE);
                type="text";
            }
        });

        loadEmoji();
    }





    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri path = result.getUri();


                try {
                    bitmap = MediaStore.Images.Media.getBitmap(WritePostActivity.this.getContentResolver(), path);

                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);


                    layoutimage.setVisibility(View.VISIBLE);
                    button.setAlpha(1f);
                    imageview.setImageBitmap(bitmap);
                    type = "image";




                } catch (IOException e) {

                }
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();

            }
        }

    }



    private  void sharePost(String s){
       // Log.i("image",imagetobinary(bitmap));
        prog.show();
        String url=domain+"api/post";
        StringRequest request=new MyRequest(token, 1, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                prog.dismiss();
                Log.i("share",response);
                try {
                    JSONObject object=new JSONObject(response);
                    if (object.has("success")){
                        String succ=object.getString("success");
                        if (succ.equals("done")){

                            TimeLineFragment fragment = (TimeLineFragment) HomeActivity.fragments.get(0);
                            fragment.reload();

                            onBackPressed();
                        }else {

                            Toast.makeText(WritePostActivity.this, "ERRor("+succ+")", Toast.LENGTH_SHORT).show();
                        }
                    }
                } catch (JSONException e) {
                    prog.dismiss();
                    Toast.makeText(WritePostActivity.this, response, Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }

            }
        }, new OnErrorRequest(WritePostActivity.this, new ErrorCall() {
            @Override
            public void OnBack() {
                prog.dismiss();

            }
        })){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String,String> map=new HashMap<>();
                map.put("type",type);
                map.put("text",s);
                if (type.equals("image")){
                    map.put("image", Image.imagetobinary(bitmap));
                }
                map.put("color","");
                return map;
            }
        };
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

    private void loadEmoji(){
        String url=domain+"api/emoji";
        StringRequest request=new MyRequest(token, 1, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject object=new JSONObject(response);
                    JSONArray array=object.getJSONArray("success");
                    for (int i= 0; i<100;i++){
                       String s=array.getString(i);
                       String [] sp=s.split("src=");
                       String ur=sp[1].replace("/>", "");
                       String myurl=ur.replace("\"","");

                       EmojiModel model=new EmojiModel();
                       model.setCompUrl(s);
                       model.setUrl(myurl);
                       arrayList.add(model);
                       adapter.notifyDataSetChanged();

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        },new OnErrorRequest(WritePostActivity.this, new ErrorCall() {
            @Override
            public void OnBack() {

            }
        }));
        Myvollysinglton.getInstance(this).addtorequst(request);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }






}
