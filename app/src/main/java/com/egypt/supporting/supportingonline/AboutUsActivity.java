package com.egypt.supporting.supportingonline;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.egypt.supporting.supportingonline.Adapters.AboutUsAdapter;
import com.egypt.supporting.supportingonline.Calls.OnPress;
import com.egypt.supporting.supportingonline.Custom.MySizes;
import com.egypt.supporting.supportingonline.Custom.SpaceRecycler_V;
import com.egypt.supporting.supportingonline.Models.AboutUsModel;

import java.util.ArrayList;


public class AboutUsActivity  extends AppCompatActivity {


    private int myPosition=0;
    private RecyclerView recyclerView;
    private AboutUsAdapter adapter;
    private ArrayList<AboutUsModel> arrayList=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);


        // position
        myPosition=getIntent().getIntExtra("position",0);

        //recycler
        arrayList.clear();
        recyclerView=(RecyclerView)findViewById(R.id.recycler_aboutUs);
        recyclerView.setLayoutManager(new LinearLayoutManager(this,RecyclerView.VERTICAL,false));
        recyclerView.addItemDecoration(new SpaceRecycler_V(MySizes.gethight(this)/30));
        adapter=new AboutUsAdapter(arrayList, new OnPress() {
            @Override
            public void onClick(View view, int position) {
                startActivity(new Intent(AboutUsActivity.this,VideoViewActivity.class)
                        .putExtra("url",arrayList.get(position).getVideoUrl()));

                overridePendingTransition(R.anim.slide_up,R.anim.fadout);
            }
        });
        recyclerView.setAdapter(adapter);

        loadUs();

        recyclerView.scrollToPosition(myPosition);






    }

    private void loadUs(){
        String[] titles=getResources().getStringArray(R.array.model_names);
        String[] detalises=getResources().getStringArray(R.array.models_about);
        //  only first 3 is real
        String[] urlsVideos={"Qosp2WBSjZc","yV0Bq7eqr4g","r586jYp8HIo","Cm6uvfMNnY0","tZ89uI-UIbU","vjSjEJnItW4"};

        for (int i =0 ;i<titles.length;i++){
            AboutUsModel model=new AboutUsModel();
            model.setTitle(titles[i]);
            model.setDes(detalises[i]);
            model.setVideoUrl(urlsVideos[i]);
            arrayList.add(model);
        }
        adapter.notifyDataSetChanged();
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.fadin,R.anim.slide_down);

    }

}
