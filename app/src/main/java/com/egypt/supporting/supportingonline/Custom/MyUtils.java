package com.egypt.supporting.supportingonline.Custom;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.egypt.supporting.supportingonline.R;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.twitter.sdk.android.core.DefaultLogger;
import com.twitter.sdk.android.core.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterConfig;
import com.twitter.sdk.android.core.TwitterCore;

public class MyUtils {

    public static void logoutprovider(Context context){
        String provider=MySharedPref.getdata(context,"provider");
        if (provider.equals("google")){
            GoogleSignInOptions gso = new GoogleSignInOptions.Builder
                    (GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestIdToken(context.getResources().getString(R.string.google_client_id))
                    .requestEmail()
                    .build();
            GoogleSignInClient googleSignInClient = GoogleSignIn.getClient(context, gso);
            googleSignInClient.signOut().addOnCompleteListener((Activity) context, new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    MySharedPref.setdata(context,"token","");
                    ((Activity)context).finishAffinity();

                }
            });
        }else if (provider.equals("facebook")){
            LoginManager.getInstance().logOut();
            MySharedPref.setdata(context,"token","");
            ((Activity)context).finishAffinity();

        }else if (provider.equals("twitter")){
            // twitter
            TwitterConfig config = new TwitterConfig.Builder(context)
                    .logger(new DefaultLogger(Log.DEBUG))
                    .twitterAuthConfig(new TwitterAuthConfig
                            (context.getResources().getString(R.string.twitter_consumer_key)
                                    , context.getResources().getString(R.string.twitter_consumer_secret)))
                    .debug(true)
                    .build();
            Twitter.initialize(config);
            TwitterCore.getInstance().getSessionManager().clearActiveSession();
            MySharedPref.setdata(context,"token","");
            ((Activity)context).finishAffinity();

        }
    }


}
