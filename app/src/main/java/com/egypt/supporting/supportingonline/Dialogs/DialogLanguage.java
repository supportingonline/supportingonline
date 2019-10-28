package com.egypt.supporting.supportingonline.Dialogs;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.egypt.supporting.supportingonline.Adapters.LanguageAdapter;
import com.egypt.supporting.supportingonline.Calls.OnPress;
import com.egypt.supporting.supportingonline.Custom.MySharedPref;
import com.egypt.supporting.supportingonline.Custom.MySizes;
import com.egypt.supporting.supportingonline.Custom.SpaceRecycler_V;
import com.egypt.supporting.supportingonline.HomeActivity;
import com.egypt.supporting.supportingonline.MainActivity;
import com.egypt.supporting.supportingonline.Models.LanguageModel;
import com.egypt.supporting.supportingonline.R;

import java.util.ArrayList;

public class DialogLanguage extends Dialog {

    private Context context;
    public DialogLanguage(@NonNull Context context) {
        super(context);
        this.context=context;
    }

    private RecyclerView recyclerView;
    private LinearLayoutManager manager;
    private ArrayList<LanguageModel> arrayList;
    private LanguageAdapter adapter;

    private String[] titles={"ar","en"};
    private int[] images={R.drawable.ic_egypt,R.drawable.ic_united_states};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_language);
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);

        // recycler
        arrayList=new ArrayList<>();
        recyclerView=(RecyclerView)findViewById(R.id.recycler_lang);
        manager=new LinearLayoutManager(context);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        adapter=new LanguageAdapter(arrayList, new OnPress() {
            @Override
            public void onClick(View view, int position) {

                String value="";

                switch (position){
                    case 0:
                        value="ar";
                        break;
                    case 1:
                        value="en";
                        break;
                }
                String myCurrentLang=MySharedPref.getdata(context,"lang");
                if (!myCurrentLang.equals(value)) {
                    MySharedPref.setdata(context, "lang", value);
                    DialogLanguage.this.dismiss();
                    context.startActivity(new Intent(context, MainActivity.class));
                    ((HomeActivity) context).overridePendingTransition(R.anim.fadin, R.anim.fadout);
                    ((HomeActivity) context).finish();
                }

            }
        });
        recyclerView.setLayoutManager(manager);
        recyclerView.addItemDecoration(new SpaceRecycler_V(MySizes.gethight(context)/80));
        recyclerView.setAdapter(adapter);

        loadrecycler();

    }

    private void loadrecycler() {
        for (int i=0;i<titles.length;i++){

            LanguageModel model=new LanguageModel();
            model.setTitle(titles[i]);
            model.setImage(images[i]);
            arrayList.add(model);
        }
        adapter.notifyDataSetChanged();
    }
}
