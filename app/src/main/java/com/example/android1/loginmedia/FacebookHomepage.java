package com.example.android1.loginmedia;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;


public class FacebookHomepage extends AppCompatActivity {

    /////for shared preferences
    public static final String LoginPREFERENCES = "LoginPrefs";
    public static final String idKey = "nameKey";
    public static final String emailKey = "emailKey";
    public static final String urlKey = "urlKey";
    public static final String nameKey = "nameKey";
    public static final String loginTypeKey = "loginTypeKey";
    SharedPreferences sharedpreferences;
    SharedPreferences.Editor editor;


    String TAG = "FacebookHomepage";
    String name, email, url, type;
    TextView textViewname, textViewemail;
    ImageView profileimageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_facebook_homepage);

        sharedpreferences = getSharedPreferences(LoginPREFERENCES, Context.MODE_PRIVATE);
        editor = sharedpreferences.edit();
        editor.clear().apply();


        textViewemail = (TextView) findViewById(R.id.email);
        textViewname = (TextView) findViewById(R.id.name);
        profileimageView = (ImageView) findViewById(R.id.profilepic);


        name = getIntent().getStringExtra("name");
        email = getIntent().getStringExtra("email");
        url = getIntent().getStringExtra("url");


        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                textViewname.setText(name);
                if (email != null)
                    textViewemail.setText(email);
                Glide.with(getApplicationContext()).load(url)
                        .thumbnail(0.5f)
                        .crossFade()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(profileimageView);

                editor.putString(nameKey, name);
                editor.putString(emailKey, email);
                editor.putString(idKey, "0");
                editor.putString(urlKey, url);
                editor.putString(loginTypeKey, "Facebook");
                editor.apply();


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
                Intent intent = new Intent(FacebookHomepage.this, LoginActivity.class);
                startActivity(intent);
                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }


}