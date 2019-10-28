package com.egypt.supporting.supportingonline.Value;

import android.widget.EditText;

public class MyData {

    public static String getStringFromEdit(EditText editText){

        return editText.getText().toString().trim();
    }
}
