package com.example.android1.loginmedia;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterAuthToken;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.models.User;

import retrofit2.Call;

public class TwitterHomePage extends AppCompatActivity {


    /////for shared preferences
    public static final String LoginPREFERENCES = "LoginPrefs";
    public static final String idKey = "nameKey";
    public static final String emailKey = "emailKey";
    public static final String urlKey = "urlKey";
    public static final String nameKey = "nameKey";
    public static final String loginTypeKey = "loginTypeKey";
    SharedPreferences sharedpreferences;
    SharedPreferences.Editor editor;


    String name = "";
    String email = "";
    String url = "";
    TextView textViewname, textViewemail;
    ImageView profileimageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_twitter);

        sharedpreferences = getSharedPreferences(LoginPREFERENCES, Context.MODE_PRIVATE);
        editor = sharedpreferences.edit();
        editor.clear().apply();

        textViewemail = (TextView) findViewById(R.id.email);
        textViewname = (TextView) findViewById(R.id.name);
        profileimageView = (ImageView) findViewById(R.id.profilepic);


        TwitterSession session = TwitterCore.getInstance().getSessionManager().getActiveSession();
        TwitterAuthToken authToken = session.getAuthToken();

        final Call<User> user = TwitterCore.getInstance().getApiClient().getAccountService().verifyCredentials(false, false, true);
        user.enqueue(new Callback<User>() {
            @Override
            public void success(Result<User> userResult) {
                name = userResult.data.name;
                email = userResult.data.email;


                // _normal (48x48px) | _bigger (73x73px) | _mini (24x24px)
                String photoUrlNormalSize = userResult.data.profileImageUrl;
                String photoUrlBiggerSize = userResult.data.profileImageUrl.replace("_normal", "_bigger");
                url = userResult.data.profileImageUrl.replace("_normal", "_mini");
                String photoUrlOriginalSize = userResult.data.profileImageUrl.replace("_normal", "");


                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {


                        editor.putString(nameKey, name);
                        editor.putString(emailKey, email);
                        editor.putString(idKey, "0");
                        editor.putString(urlKey, url);
                        editor.putString(loginTypeKey, "Twitter");
                        editor.apply();


                        textViewname.setText(name);
                        if (email != null)
                            textViewemail.setText(email);
                        Glide.with(getApplicationContext()).load(url)
                                .thumbnail(0.5f)
                                .crossFade()
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .into(profileimageView);
                    }
                });


            }

            @Override
            public void failure(TwitterException exc) {
                Log.d("TwitterKit", "Verify Credentials Failure", exc);
            }
        });


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);//Menu Resource, Menu
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.logout:
                Toast.makeText(getApplicationContext(), "logout", Toast.LENGTH_LONG).show();
                editor.clear().apply();
                Intent intent = new Intent(TwitterHomePage.this, LoginActivity.class);
                startActivity(intent);
                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }


}
