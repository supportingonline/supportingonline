package com.egypt.supporting.supportingonline.Custom;

import android.content.Context;

import com.egypt.supporting.supportingonline.R;
import com.google.android.gms.cast.framework.SessionProvider;

import java.util.List;

public class CastOptionsProvider implements com.google.android.gms.cast.framework.OptionsProvider {
    public com.google.android.gms.cast.framework.CastOptions getCastOptions(Context appContext) {

        // Register you custom receiver on the Google Cast SDK Developer Console to get this ID.
        String receiverId = appContext.getResources().getString(R.string.google_client_id);

        return new com.google.android.gms.cast.framework.CastOptions.Builder()
                .setReceiverApplicationId(receiverId)
                .build();
    }

    public List<SessionProvider> getAdditionalSessionProviders(Context context) {
        return null;
    }
}