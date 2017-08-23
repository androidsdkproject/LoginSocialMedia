package com.example.android1.loginmedia;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

public class SplashScreen extends Activity {
    public static final String LoginPREFERENCES = "LoginPrefs";
    public static final String idKey = "nameKey";
    public static final String emailKey = "emailKey";
    public static final String urlKey = "urlKey";
    public static final String nameKey = "nameKey";
    public static final String loginTypeKey = "loginTypeKey";
    private static int SPLASH_TIME_OUT = 3000;
    SharedPreferences sharedpreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                sharedpreferences = getSharedPreferences(LoginPREFERENCES, Context.MODE_PRIVATE);

                String id = sharedpreferences.getString(idKey, "0");
                String email = sharedpreferences.getString(emailKey, "0");
                String url = sharedpreferences.getString(urlKey, "0");
                String name = sharedpreferences.getString(nameKey, "0");
                String logintype = sharedpreferences.getString(loginTypeKey, "0");

                if (logintype.equals("SimpleLogin")) {
                    Intent in = new Intent(SplashScreen.this, WelcomeActivity.class);
                    in.putExtra("id", id);
                    startActivity(in);

                } else if (logintype.equals("Twitter")) {

                    Intent in = new Intent(SplashScreen.this, TwitterHomePage.class);
                    in.putExtra("name", name);
                    in.putExtra("email", email);
                    in.putExtra("url", url);
                    startActivity(in);
                    finish();
                } else if (logintype.equals("Facebook")) {

                    Intent in = new Intent(SplashScreen.this, FacebookHomepage.class);
                    in.putExtra("name", name);
                    in.putExtra("email", email);
                    in.putExtra("url", url);
                    startActivity(in);
                    finish();
                } else if (logintype.equals("Google")) {

                    Intent in = new Intent(SplashScreen.this, GooglePlusHomepage.class);
                    in.putExtra("name", name);
                    in.putExtra("email", email);
                    in.putExtra("url", url);
                    startActivity(in);
                    finish();
                } else {
                    Intent i = new Intent(SplashScreen.this, SignupActivity.class);
                    startActivity(i);
                    finish();
                }


            }
        }, SPLASH_TIME_OUT);
    }

}