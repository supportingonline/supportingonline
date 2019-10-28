package com.egypt.supporting.supportingonline;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.egypt.supporting.supportingonline.Calls.ErrorCall;
import com.egypt.supporting.supportingonline.Custom.MyRequest;
import com.egypt.supporting.supportingonline.Custom.MySharedPref;
import com.egypt.supporting.supportingonline.Custom.Myvollysinglton;
import com.egypt.supporting.supportingonline.Custom.OnErrorRequest;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphRequestAsyncTask;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.DefaultLogger;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterAuthToken;
import com.twitter.sdk.android.core.TwitterConfig;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterAuthClient;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;
import com.twitter.sdk.android.core.models.User;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    private String domain,p="";

    private ProgressDialog prog;

    private GoogleSignInClient mGoogleSignInClient;
    private SignInButton signInButton;

    private TwitterLoginButton logintwitter;

    private CallbackManager callbackManager = CallbackManager.Factory.create();
    private LoginButton loginfacebook;
    private static final String EMAIL = "email";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // twitter
        TwitterConfig config = new TwitterConfig.Builder(this)
                .logger(new DefaultLogger(Log.DEBUG))
                .twitterAuthConfig(new TwitterAuthConfig
                        (getResources().getString(R.string.twitter_consumer_key)
                                , getResources().getString(R.string.twitter_consumer_secret)))
                .debug(true)
                .build();
        Twitter.initialize(config);

        setContentView(R.layout.activity_login);

        // domain
        domain=getResources().getString(R.string.domain);

        // progress dialog
        prog=new ProgressDialog(this);
        prog.setMessage(getResources().getString(R.string.loading));

        // key hash
     DialogKeyHash();



        // google

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.google_client_id))
                .requestEmail()
                .build();
        signInButton = findViewById(R.id.sign_in_button);
        signInButton.setSize(SignInButton.SIZE_STANDARD);
        TextView textView = (TextView) signInButton.getChildAt(0);
        textView.setText(getResources().getString(R.string.signin_google));

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);



        findViewById(R.id.sign_in_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                p="g";
                signIn();
            }
        });



        // twitter
        final String[] twname = new String[1];
        final String[] tid = new String[1];
        final String[] temail = new String[1];
        final String[] tavatar = new String[1];
        logintwitter = (TwitterLoginButton) findViewById(R.id.login_twitter);
        logintwitter.setCallback(new Callback<TwitterSession>() {

            @Override
            public void success(Result<TwitterSession> result) {

                TwitterSession session = TwitterCore.getInstance().getSessionManager().getActiveSession();
                TwitterAuthToken authToken = session.getAuthToken();
                /*String token = authToken.token;
                String secret = authToken.secret;*/
                twname[0] =session.getUserName();
                tid[0] =String.valueOf(session.getUserId());


                TwitterAuthClient authClient = new TwitterAuthClient();
                authClient.requestEmail(session, new Callback<String>() {
                    @Override
                    public void success(Result<String> result) {

                        temail[0] =result.data;
                        // Do something with the result, which provides the email address
                        p="t";
                        TwitterCore.getInstance().getApiClient().getAccountService().verifyCredentials(true, true, false).enqueue(new Callback<User>() {
                            @Override
                            public void success(Result<User> result) {
                                User user=result.data;
                                tavatar[0] = user.profileImageUrl;
                                // Log.i("pp",profileImage);
                                Log.i("email",temail[0]);
                                Log.i("email",tid[0]);
                                Log.i("email",twname[0]);
                                Log.i("email",tavatar[0]);

                                login_server(temail[0],tid[0],twname[0],tavatar[0],"twitter");

                            }

                            @Override
                            public void failure(TwitterException exception) {

                            }
                        });

                    }

                    @Override
                    public void failure(TwitterException exception) {
                        // Do something on failure
                    }
                });




                // Do something with result, which provides a TwitterSession for making API calls


            }

            @Override
            public void failure(TwitterException exception) {
                // Do something on failure
            }
        });



        // facebook
        loginfacebook = (LoginButton) findViewById(R.id.login_facebook);
        loginfacebook.setReadPermissions(Arrays.asList(EMAIL));
        // If you are using in a fragment, call loginButton.setFragment(this);

        // Callback registration
        loginfacebook.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                // App code
                final AccessToken accessToken = loginResult.getAccessToken();

                GraphRequestAsyncTask request = GraphRequest.newMeRequest(accessToken, new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject user, GraphResponse graphResponse) {

                        // Log.d("fname", user.optString("name"));
                        // Log.d("fid", user.optString("id"));
                        tid[0]=user.optString("id");
                        twname[0]=user.optString("name");



                        Bundle params = new Bundle();
                        params.putString("fields", "id,email,picture.width(150).height(150)");

                        new GraphRequest(AccessToken.getCurrentAccessToken(), "me", params, HttpMethod.GET,
                                new GraphRequest.Callback() {
                                    @Override
                                    public void onCompleted(GraphResponse response) {
                                        if (response != null) {
                                            try {
                                                JSONObject data = response.getJSONObject();
                                                Log.i("data",data.toString());
                                                // https://graph.facebook.com/"+id of user+"/picture?type=large
                                                String email=data.getString("email");
                                                tavatar[0]="https://graph.facebook.com/"+tid[0]+"/picture?type=large";
                                                temail[0]=data.getString("email");
                                                if (data.has("picture")) {
                                                    String profilePicUrl = data.getJSONObject("picture").getJSONObject("data").getString("url");
                                                }
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }
                                            login_server(temail[0],tid[0],twname[0],tavatar[0],"facebook");
                                        }
                                    }
                                }).executeAsync();
                    }
                }).executeAsync();
            }

            @Override
            public void onCancel() {
                // App code
            }

            @Override
            public void onError(FacebookException exception) {
                // App code
            }
        });


    }
    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, 101);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // facebook
        callbackManager.onActivityResult(requestCode, resultCode, data);


        // twitter
        logintwitter.onActivityResult(requestCode, resultCode, data);

        if (p.equals("g")) {

            // google
            if (resultCode == Activity.RESULT_OK)
                switch (requestCode) {
                    case 101:
                        try {
                            // The Task returned from this call is always completed, no need to attach
                            // a listener.
                            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
                            GoogleSignInAccount account = task.getResult(ApiException.class);
                            //Toast.makeText(this, "Logged In", Toast.LENGTH_SHORT).show();
                            String email = account.getEmail();
                            String name = account.getDisplayName();
                            String avatar = account.getPhotoUrl().toString();
                            String id = account.getId();

                            login_server(email, id, name, avatar, "google");

                            //onLoggedIn(account);
                        } catch (ApiException e) {

                        }
                        break;
                }
        }
    }


    private void login_server(final String email, final String id,
                              final String name, final String avatar, final String provider) {

        prog.show();
        String url=domain+"api/login";
        StringRequest request=new MyRequest(null, Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("res",response);

                prog.dismiss();
                try {
                    JSONObject object=new JSONObject(response);
                    if (object.has("success")){
                        JSONObject jsonObject=object.getJSONObject("success");
                        String token=jsonObject.getString("token");

                        JSONObject userobject=jsonObject.getJSONObject("user");

                        String id=userobject.getString("id");
                        String name=userobject.getString("name");
                        String email=userobject.getString("email");
                        String image=userobject.getString("image");
                        String phone=userobject.getString("phone");
                        String wallet=userobject.getString("wallet");
                        String im_profile=userobject.getString("profile_img");
                        String work_with=userobject.getString("work_with");
                        String education = userobject.getString("education");
                        String location = userobject.getString("location");
                        String about = userobject.getString("about");

                        if (work_with.equals("1")){
                            MySharedPref.setWorkWith(LoginActivity.this,true);
                        }else {
                            MySharedPref.setWorkWith(LoginActivity.this,false);

                        }

                        MySharedPref.setdata(LoginActivity.this,"provider",provider);
                        MySharedPref.setdata(LoginActivity.this,"token", token);
                        MySharedPref.setdata(LoginActivity.this,"email",email);
                        MySharedPref.setdata(LoginActivity.this,"name", name);
                        MySharedPref.setdata(LoginActivity.this,"image", image);
                        MySharedPref.setdata(LoginActivity.this,"profile_img",im_profile);
                        MySharedPref.setdata(LoginActivity.this,"phone", phone);
                        MySharedPref.setdata(LoginActivity.this,"wallet", wallet);
                        MySharedPref.setdata(LoginActivity.this,"id", id);
                        MySharedPref.setdata(LoginActivity.this,"provider",provider);
                        MySharedPref.setdata(LoginActivity.this,"education",education);
                        MySharedPref.setdata(LoginActivity.this,"location",location);
                        MySharedPref.setdata(LoginActivity.this,"about",about);
                        nextactivity(new Intent(LoginActivity.this, HomeActivity.class));

                    }else {

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        },new OnErrorRequest(LoginActivity.this, new ErrorCall() {
            @Override
            public void OnBack() {
                prog.dismiss();
            }
        })){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> map=new HashMap<>();
                map.put("email",email);
                map.put("name",name);
                map.put("id",id);
                map.put("avatar",avatar);
                map.put("provider",provider);
                return map;
            }
        };

        Myvollysinglton.getInstance(this).addtorequst(request);
    }

    private void nextactivity(Intent intent){
        startActivity(intent);
        overridePendingTransition(R.anim.fadin,R.anim.fadout);
        finish();
    }

    private void DialogKeyHash(){ // Add code to print out the key hash
        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "com.ahmed.supporting.supportingonline",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());

                AlertDialog.Builder d=new AlertDialog.Builder(this);
                d.setMessage(Base64.encodeToString(md.digest(), Base64.DEFAULT));
                d.show();
            }
        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }
    }
}
