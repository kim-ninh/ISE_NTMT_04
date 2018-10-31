package com.hcmus.dreamers.foodmap;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookAuthorizationException;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.hcmus.dreamers.foodmap.AsyncTask.TaskCompleteCallBack;
import com.hcmus.dreamers.foodmap.Model.Guest;
import com.hcmus.dreamers.foodmap.common.FoodMapApiManager;

import java.util.Arrays;


public class LoginGuestActivity extends AppCompatActivity {

    private static final String TAG = "LoginGuestActivity";
    private static final int LOGIN_OWNER_REQUEST = 1222;

    CallbackManager callbackManager;
    FirebaseAuth mAuth;

    Button btnLoginFb;
    Button btnLoginOwner;


    ProgressDialog progressDialog;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_guest);
        btnLoginFb = (Button)findViewById(R.id.btnFacebookLogin);
        btnLoginOwner = (Button)findViewById(R.id.btnOwnerLogin);
        btnLoginOwner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginGuestActivity.this, LoginOwnerActivity.class);
                startActivityForResult(intent, LOGIN_OWNER_REQUEST);
            }
        });

        progressDialog = new ProgressDialog(LoginGuestActivity.this);
        progressDialog.setTitle("Check login...");
        // init
        mAuth = FirebaseAuth.getInstance();
        // btnFaceBook Login
        callbackManager = CallbackManager.Factory.create();
        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d(TAG, "onSuccess: Login OK");
                handleFacebookAccessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                Log.d(TAG, "facebook:onCancel");
            }

            @Override
            public void onError(FacebookException error) {
                Log.d(TAG, "facebook: onError"+ error.getMessage());
                if (error instanceof FacebookAuthorizationException) {
                    if (AccessToken.getCurrentAccessToken() != null) {
                        LoginManager.getInstance().logOut();
                    }
                }
            }
        });

        LoginManager.getInstance().logOut();

        btnLoginFb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginManager.getInstance().logInWithReadPermissions(LoginGuestActivity.this, Arrays.asList("email", "public_profile"));
            }
        });
    }


    private void handleFacebookAccessToken(AccessToken token) {
        Log.d(TAG, "handleFacebookAccessToken:" + token);

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            progressDialog.show();

                            FirebaseUser user = mAuth.getCurrentUser();
                            ///Todo
                            Guest.getInstance().setName(user.getDisplayName());
                            Guest.getInstance().setEmail(user.getEmail());
                            Guest.getInstance().setUrlAvatar(user.getPhotoUrl());

                            FoodMapApiManager.addGuest(Guest.getInstance(), new TaskCompleteCallBack() {
                                @Override
                                public void OnTaskComplete(Object response) {
                                    if ((int)response == FoodMapApiManager.SUCCESS){
                                        progressDialog.dismiss();
                                        // đóng activity login guest
                                        LoginGuestActivity.this.finish();
                                    }
                                }
                            });
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());

                            Toast.makeText(LoginGuestActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == LOGIN_OWNER_REQUEST){
            if (resultCode == Activity.RESULT_OK){
                if (data.getBooleanExtra("isLogin", false) == true)
                    LoginGuestActivity.this.finish();
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
