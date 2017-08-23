package com.example.android1.loginmedia;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class WelcomeActivity extends AppCompatActivity {

    /////for shared preferences
    public static final String LoginPREFERENCES = "LoginPrefs";
    public static final String idKey = "nameKey";
    public static final String emailKey = "emailKey";
    public static final String urlKey = "urlKey";
    public static final String nameKey = "nameKey";
    public static final String loginTypeKey = "loginTypeKey";
    SharedPreferences sharedpreferences;
    SharedPreferences.Editor editor;


    String TAG = "Welcome";
    String name, email, phone, id;
    TextView textViewname, textViewemail, textViewphone, textViewid;
    DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        sharedpreferences = getSharedPreferences(LoginPREFERENCES, Context.MODE_PRIVATE);
        editor = sharedpreferences.edit();
        editor.clear().apply();
        Cursor cursor = null;
        try {
            databaseHelper = new DatabaseHelper(WelcomeActivity.this);

            id = getIntent().getStringExtra("id");
            Log.d(TAG, id);


            cursor = databaseHelper.getData(Integer.parseInt(id));
            cursor.moveToNext();
        } catch (Exception e) {
            e.printStackTrace();
        }


        textViewemail = (TextView) findViewById(R.id.email);
        textViewname = (TextView) findViewById(R.id.name);
        textViewphone = (TextView) findViewById(R.id.phone);
        textViewid = (TextView) findViewById(R.id.id);


        try {
            name = cursor.getString(1);
            phone = cursor.getString(2);
            email = cursor.getString(3);


            editor.putString(nameKey, name);
            editor.putString(emailKey, email);
            editor.putString(idKey, id);
            editor.putString(loginTypeKey, "SimpleLogin");
            editor.apply();


        } catch (Exception e) {
            e.printStackTrace();
        }


        textViewid.setText(id + "");
        textViewname.setText(name);
        textViewphone.setText(phone);
        textViewemail.setText(email);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);//Menu Resource, Menu
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.logout:
                editor.clear().apply();
                Intent intent = new Intent(WelcomeActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }


}



