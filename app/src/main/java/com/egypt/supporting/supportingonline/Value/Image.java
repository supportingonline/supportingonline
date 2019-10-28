package com.egypt.supporting.supportingonline.Value;

import android.graphics.Bitmap;
import android.os.Build;

import java.io.ByteArrayOutputStream;

public class Image {

    public static String imagetobinary(Bitmap bitmap){
        int qu=50;
        if (sizeOf(bitmap)<800){
            qu=100;
        }
        ByteArrayOutputStream b=new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG,qu,b);
        byte[] imagebite=b.toByteArray();
        return android.util.Base64.encodeToString(imagebite, android.util.Base64.DEFAULT);
    }

    public static int sizeOf(Bitmap data) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB_MR1) {
            return returnKB(data.getRowBytes() * data.getHeight());
        } else {
            return returnKB(data.getByteCount());
        }
    }

    private static int returnKB(int byit){
        return byit/1024;
    }
}
