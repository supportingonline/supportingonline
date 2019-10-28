package com.egypt.supporting.supportingonline;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.egypt.supporting.supportingonline.Calls.ErrorCall;
import com.egypt.supporting.supportingonline.Custom.MyRequest;
import com.egypt.supporting.supportingonline.Custom.MySharedPref;
import com.egypt.supporting.supportingonline.Custom.Myvollysinglton;
import com.egypt.supporting.supportingonline.Custom.OnErrorRequest;
import com.egypt.supporting.supportingonline.Value.Image;
import com.egypt.supporting.supportingonline.Views.TouchImageView;
import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class FullImageActivity extends AppCompatActivity {

    private boolean isMe;
    private String type,imageUrl;

    private TouchImageView imageView;
    private ImageView edit;

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_image);

        // progress dialog
        progressDialog=new ProgressDialog(this);
        progressDialog.setMessage(getResources().getString(R.string.loading));

        imageView = (TouchImageView) findViewById(R.id.full_image);
        isMe=getIntent().getBooleanExtra("is_me",false);
        if (isMe){
            type=getIntent().getStringExtra("type");
        }
        boolean isdefult=false;
        imageUrl=getIntent().getStringExtra("image_url");
        type=getIntent().getStringExtra("type");
        if (type.equals("timeline")){
             isdefult=getIntent().getBooleanExtra("is_default",false);
            if (isdefult){
                imageView.setBackground(getResources().getDrawable(R.drawable.defult));

            }else {



                Glide.with(this).load(imageUrl).error(R.drawable.ic_user_profile)
                        .into(imageView);
            }

        }else {

            Glide.with(this).load(imageUrl).error(R.drawable.ic_user_profile)
                    .into(imageView);
        }



        // edit
        edit=(ImageView)findViewById(R.id.full_edit);
        if (isMe){
            edit.setVisibility(View.VISIBLE);
            edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    CropImage.activity()
                            .setGuidelines(CropImageView.Guidelines.ON)
                            .start(FullImageActivity.this);
                }
            });
        }else {
            edit.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri path = result.getUri();


                try {
                   Bitmap bitmap = MediaStore.Images.Media.getBitmap(FullImageActivity.this.getContentResolver(), path);

                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                    //Log.i("size", String.valueOf(Image.sizeOf(bitmap)));


                   imageView.setBackgroundResource(0);
                    imageView.setImageBitmap(bitmap);

                    String key="image";
                    if (type.equals("timeline")){
                        key="profile_image";
                    }
                   updatePhoto(key,Image.imagetobinary(bitmap));


                } catch (IOException e) {

                }
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();

            }
        }
    }


    private void updatePhoto(String key,String value){
        progressDialog.show();

        String url=getResources().getString(R.string.domain)+"api/update_user";
        StringRequest request=new MyRequest(MySharedPref.getdata(FullImageActivity.this, "token"), 1, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                Log.i("image",response);
                try {
                    JSONObject object=new JSONObject(response);
                    JSONObject sucObject=object.getJSONObject("success");
                    String image=sucObject.getString("image");
                    String profile_img=sucObject.getString("profile_img");


                    MySharedPref.setdata(FullImageActivity.this,"image", image);
                    MySharedPref.setdata(FullImageActivity.this,"profile_img", profile_img);



                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        },new OnErrorRequest(FullImageActivity.this, new ErrorCall() {
            @Override
            public void OnBack() {
                progressDialog.dismiss();
            }
        })){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map=new HashMap<>();
                map.put(key,value);
                return map;
            }
        };
        Myvollysinglton.getInstance(this).addtorequst(request);

    }
}
