package com.example.android1.loginmedia;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class WelcomeScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_screen);


        String str = getIntent().getStringExtra("type");
        TextView textView = (TextView) findViewById(R.id.textviewnotificationtype);
        textView.setText("Notification type :" + str);
    }
}
