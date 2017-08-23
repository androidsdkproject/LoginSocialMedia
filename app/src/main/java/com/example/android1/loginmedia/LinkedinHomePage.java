package com.example.android1.loginmedia;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.linkedin.platform.APIHelper;
import com.linkedin.platform.errors.LIApiError;
import com.linkedin.platform.listeners.ApiListener;
import com.linkedin.platform.listeners.ApiResponse;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

public class LinkedinHomePage extends AppCompatActivity {
    private static final String host = "api.linkedin.com";
    private static final String url = "https://" + host + "/v1/people/~:" +
            "(email-address,formatted-name,phone-numbers,picture-urls::(original))";
    private static final String TAG = "LinkedinHomepage";
    private ProgressDialog progress;
    private TextView user_name, user_email;
    private ImageView profile_picture;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_linkedin_home_page);
        // Initialize the progressbar
        progress = new ProgressDialog(this);
        progress.setMessage("Retrieve data...");
        progress.setCanceledOnTouchOutside(false);
        progress.show();

        user_email = (TextView) findViewById(R.id.email);
        user_name = (TextView) findViewById(R.id.name);
        profile_picture = (ImageView) findViewById(R.id.profile_picture);

        linkededinApiHelper();

    }

    public void linkededinApiHelper() {
        APIHelper apiHelper = APIHelper.getInstance(getApplicationContext());
        apiHelper.getRequest(LinkedinHomePage.this, url, new ApiListener() {
            @Override
            public void onApiSuccess(ApiResponse result) {
                try {
                    showResult(result.getResponseDataAsJson());
                    progress.dismiss();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onApiError(LIApiError error) {

            }
        });
    }

    public void showResult(JSONObject response) {

        try {
            Log.d(TAG, response.toString());
            user_email.setText(response.get("emailAddress").toString());
            user_name.setText(response.get("formattedName").toString());

            Picasso.with(this).load(response.getString("pictureUrl"))
                    .into(profile_picture);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}