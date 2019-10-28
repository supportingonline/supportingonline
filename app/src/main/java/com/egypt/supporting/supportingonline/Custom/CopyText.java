package com.egypt.supporting.supportingonline.Custom;

import android.content.Context;
import android.widget.Toast;

import com.egypt.supporting.supportingonline.R;

public class CopyText {

    public static void copy(Context context, String text) {
        if(android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.HONEYCOMB) {
            android.text.ClipboardManager clipboard = (android.text.ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
            clipboard.setText(text);
        } else {
            android.content.ClipboardManager clipboard = (android.content.ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
            android.content.ClipData clip = android.content.ClipData.newPlainText("Copied Text", text);
            clipboard.setPrimaryClip(clip);
        }

        Toast.makeText(context, context.getResources().getString(R.string.copied), Toast.LENGTH_SHORT).show();


    }
}
