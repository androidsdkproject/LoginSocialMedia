package com.example.android1.loginmedia;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.linkedin.platform.LISessionManager;
import com.linkedin.platform.errors.LIAuthError;
import com.linkedin.platform.listeners.AuthListener;
import com.linkedin.platform.utils.Scope;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.DefaultLogger;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterConfig;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;

import org.json.JSONArray;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;

public class LoginActivity extends AppCompatActivity implements
        View.OnClickListener,
        GoogleApiClient.OnConnectionFailedListener {

    //    google signin start
    private static final String TAG = LoginActivity.class.getSimpleName();
    private static final int RC_SIGN_IN = 007;
    private static int Twitter_Login_Status = 0;
    private static int Facebook_Login_Status = 0;
    private static int Linkedin_Login_Status = 0;
    private static int GooglePlus_Login_Status = 0;


    ///LoginActivity Variables
    TextView textViewsignup;
    Button buttonlogin;
    EditText editTextemail, editTextpassword;
    DatabaseHelper databaseHelper;
    Cursor allDataCursor;
    ArrayList<TypeMember> typeMemberArrayList;


//    ////Linkedin Variables
//    ImageButton linkedinLoginButton;
//    //


    TwitterLoginButton TwitterloginButton;
    ImageButton facebookSigninButton;//for facebook

    private GoogleApiClient mGoogleApiClient;
    private ProgressDialog mProgressDialog;
    private ImageButton imageButtongooglesignin;
    private CallbackManager mCallbackManager;//for facebook


    // set the permission for linkedin to retrieve basic
    private static Scope buildScope() {
        return Scope.build(Scope.R_BASICPROFILE, Scope.R_EMAILADDRESS);
    }


    /////Linkedinlogin Method
    public void Linkedinlogin() {
        LISessionManager.getInstance(getApplicationContext())
                .init(this, buildScope(), new AuthListener() {
                    @Override
                    public void onAuthSuccess() {
                        Log.d(TAG, "Linkedin Login Success");
                        Toast.makeText(getApplicationContext(), "success" + LISessionManager.getInstance(getApplicationContext())
                                        .getSession().getAccessToken().toString(),
                                Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(LoginActivity.this, LinkedinHomePage.class);
                        startActivity(intent);

                    }

                    @Override
                    public void onAuthError(LIAuthError error) {
                        Log.d(TAG, "Linkedin Login Failed");
                        Toast.makeText(getApplicationContext(), "failed "
                                        + error.toString(),
                                Toast.LENGTH_LONG).show();
                    }
                }, true);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        ////Twitter
        TwitterConfig config = new TwitterConfig.Builder(this)
                .logger(new DefaultLogger(Log.DEBUG))
                .twitterAuthConfig(new TwitterAuthConfig("Nf2zP6O4ptO5XhZc5uyrkBNZX", "HtFw2z9b1TSAkAPQd3f1vION7mhVNgmNQ96e70z4Q1jwh6Tguf"))
                .debug(true)
                .build();

        Twitter.initialize(config);
        setContentView(R.layout.activity_login);


        generateHashkey();

        buttonlogin = (Button) findViewById(R.id.buttonLogin);
        editTextemail = (EditText) findViewById(R.id.edittextemail);
        editTextpassword = (EditText) findViewById(R.id.edittextpassword);
        buttonlogin.setOnClickListener(this);
        textViewsignup = (TextView) findViewById(R.id.signupbutton);
        textViewsignup.setOnClickListener(this);


//        linkedinLoginButton = (ImageButton) findViewById(R.id.linkedinibutton);
//        linkedinLoginButton.setOnClickListener(this);


        try {
            databaseHelper = new DatabaseHelper(LoginActivity.this);
            allDataCursor = databaseHelper.getAllData();
            typeMemberArrayList = new ArrayList<>();
            if (allDataCursor != null) {
                while (allDataCursor.moveToNext()) {
                    Log.d("Data", allDataCursor.getString(3) + " " + allDataCursor.getString(4));
                    typeMemberArrayList.add(new TypeMember(allDataCursor.getString(0), allDataCursor.getString(3), allDataCursor.getString(4)));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        TwitterloginButton = (TwitterLoginButton) findViewById(R.id.Twitterbutton);
        TwitterloginButton.setCallback(new Callback<TwitterSession>() {
            @Override
            public void success(Result<TwitterSession> result) {
                ////Twitter_Login_Status = 1;
                Log.d(TAG, "Twitter Login Success " + " Twitter_Login_Status" + Twitter_Login_Status);
                startActivity(new Intent(LoginActivity.this, TwitterHomePage.class));
            }

            @Override
            public void failure(TwitterException exception) {
                //// Twitter_Login_Status = 1;
                Log.d(TAG, "Twitter Login Failed " + " Twitter_Login_Status" + Twitter_Login_Status);
                Log.d(TAG, "Twitter Login Failed");
            }
        });


        /////facebook login start
        facebookSigninButton = (ImageButton) findViewById(R.id.facebookibutton);
        facebookSigninButton.setOnClickListener(this);
        FacebookSdk.sdkInitialize(this.getApplicationContext());

        mCallbackManager = CallbackManager.Factory.create();

        LoginManager.getInstance().registerCallback(mCallbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {

                        Log.d("Success", "Login");

                        GraphRequest request = GraphRequest.newMeRequest(
                                loginResult.getAccessToken(),
                                new GraphRequest.GraphJSONObjectCallback() {
                                    @Override
                                    public void onCompleted(JSONObject object, GraphResponse response) {
                                        Log.v("LoginActivity", "response : " + response.toString());
                                        try {
                                            // Application code
                                            final String name = response.getJSONObject().getString("name");
                                            final String email = response.getJSONObject().getString("email");
                                            final String profilePicUrl = object.getJSONObject("picture").getJSONObject("data").getString("url");

                                            JSONArray jsonArrayFriends = object.getJSONObject("friendlist").getJSONArray("data");

                                            Log.d(TAG, "Friends " + jsonArrayFriends);

                                            runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {


                                                    Intent in = new Intent(LoginActivity.this, FacebookHomepage.class);
                                                    in.putExtra("name", name);
                                                    in.putExtra("email", email);
                                                    in.putExtra("url", profilePicUrl);
                                                    startActivity(in);

                                                }
                                            });


                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }
                                });
                        Bundle parameters = new Bundle();
                        parameters.putString("fields", "id,name,email,picture,friendlist,members");
                        request.setParameters(parameters);
                        request.executeAsync();

                    }

                    @Override
                    public void onCancel() {
                        Toast.makeText(LoginActivity.this, "Login Cancel", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        Toast.makeText(LoginActivity.this, exception.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });

        //faceboook signin end


        /////start google sign in
        imageButtongooglesignin = (ImageButton) findViewById(R.id.googleplusbutton);
        imageButtongooglesignin.setOnClickListener(this);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        /////start google sign in
    }

    private void GoogleSignIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private void handleGoogleSignInResult(GoogleSignInResult result) {
        Log.d(TAG, "handleSignInResult:" + result.isSuccess());
        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = result.getSignInAccount();

            Log.e(TAG, "display name: " + acct.getDisplayName());

            String personName = acct.getDisplayName();
            String personPhotoUrl = acct.getPhotoUrl().toString();
            String email = acct.getEmail();

            Log.e(TAG, "Name: " + personName + ", email: " + email
                    + ", Image: " + personPhotoUrl);
            Intent in = new Intent(LoginActivity.this, GooglePlusHomepage.class);
            in.putExtra("name", personName);
            in.putExtra("email", email);
            in.putExtra("url", personPhotoUrl);
            startActivity(in);


        } else {

        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        switch (id) {
            case R.id.googleplusbutton:
                GoogleSignIn();
                break;

            case R.id.facebookibutton:
                Facebook_Login_Status = 1;
                LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile", "email", "friendlist", "members"));
                break;

            case R.id.buttonLogin:
                methodSimpleLogin();
                break;

            case R.id.signupbutton:
                Intent in = new Intent(LoginActivity.this, SignupActivity.class);
                startActivity(in);
                finish();

//            case R.id.linkedinibutton:
//                Linkedin_Login_Status = 1;
//                Linkedinlogin();
//                break;

        }
    }

    private void methodSimpleLogin() {
        ScrollView loginlayout = (ScrollView) findViewById(R.id.loginlayout);


        String email = editTextemail.getText().toString();
        String password = editTextpassword.getText().toString();
        int flag = 1;
        int user_exists_status = 0;
        if (email.isEmpty()) {
            flag = 0;
            editTextemail.setError("Email can not be Blank");
        }

        if (password.isEmpty()) {
            flag = 0;
            editTextpassword.setError("Password can not be Blank");
        }
        if (flag == 1) {
            for (int i = 0; i < typeMemberArrayList.size(); i++) {
                if (typeMemberArrayList.get(i).getEmail().equals(email) && typeMemberArrayList.get(i).getPassword().equals(password)) {
                    user_exists_status = 1;
                    Snackbar.make(loginlayout, "Successfully Login ", Snackbar.LENGTH_LONG).show();
                    Intent in = new Intent(LoginActivity.this, WelcomeActivity.class);
                    in.putExtra("id", typeMemberArrayList.get(i).getId());
                    startActivity(in);
                    break;
                }
            }
        }
        if (flag == 1 && user_exists_status == 0) {
            Snackbar.make(loginlayout, "User does not exists ", Snackbar.LENGTH_LONG).show();
        }


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            Log.d(TAG, "Google Sign in onActivityResult");
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleGoogleSignInResult(result);
        } else if (Facebook_Login_Status == 1) {
            Log.d(TAG, "Facebook Sign in onActivityResult");
            Facebook_Login_Status = 0;
            mCallbackManager.onActivityResult(requestCode, resultCode, data);
        }
//        else if (Linkedin_Login_Status == 1) {
//            Log.d(TAG, "Linkedin_Login_Status in onActivityResult " + Linkedin_Login_Status);
//            Linkedin_Login_Status = 0;
//            try {
//                LISessionManager.getInstance(getApplicationContext())
//                        .onActivityResult(this,
//                                requestCode, resultCode, data);
//            } catch (Exception e) {
//                Log.d(TAG, "Exception " + e.getMessage() + "\n" + "requestCode" + requestCode + "\n" + "resultCode " + resultCode);
//                e.printStackTrace();
//            }
//
//        }
        else {
            Log.d(TAG, "Twitter Sign in onActivityResult");
            TwitterloginButton.onActivityResult(requestCode, resultCode, data);
        }


    }


    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

        Log.d(TAG, "onConnectionFailed:" + connectionResult);
    }


    public void generateHashkey() {
        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "com.example.android1.loginmedia",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());

                String str = (Base64.encodeToString(md.digest(), Base64.NO_WRAP));
                Log.d(TAG, " " + str);
            }
        } catch (PackageManager.NameNotFoundException e) {
            Log.d("Name not found", e.getMessage(), e);

        } catch (NoSuchAlgorithmException e) {
            Log.d("Error", e.getMessage(), e);
        }
    }

}