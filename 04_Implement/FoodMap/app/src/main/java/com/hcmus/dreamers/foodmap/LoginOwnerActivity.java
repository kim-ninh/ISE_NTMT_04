package com.hcmus.dreamers.foodmap;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.hcmus.dreamers.foodmap.AsyncTask.TaskCompleteCallBack;
import com.hcmus.dreamers.foodmap.common.FoodMapApiManager;


public class LoginOwnerActivity extends AppCompatActivity {

    EditText edtUsername;
    EditText edtPassword;
    Button btnForgotPass;
    Button btnLogin;
    Button btnRegister;
    ProgressDialog progressDialog;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_owner);

        progressDialog = new ProgressDialog(LoginOwnerActivity.this);
        progressDialog.setTitle("Check login...");

        edtUsername = (EditText)findViewById(R.id.edtUsername);
        edtPassword = (EditText)findViewById(R.id.edtPassword);
        btnForgotPass = (Button)findViewById(R.id.btnForgotPassword);
        btnLogin = (Button)findViewById(R.id.btnLogin);
        btnRegister = (Button)findViewById(R.id.btnRegister);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = edtUsername.getText().toString();
                String password = edtPassword.getText().toString();

                if (username.equals("") || password.equals("")){
                    Toast.makeText(LoginOwnerActivity.this, "Không được để trống tên đăng nhập và mật khẩu", Toast.LENGTH_LONG).show();
                }else{
                    progressDialog.show();
                    FoodMapApiManager.Login(username, password, new TaskCompleteCallBack() {
                        @Override
                        public void OnTaskComplete(Object response) {
                            progressDialog.dismiss();
                            if ((int)response == FoodMapApiManager.SUCCESS){
                                Intent returnIntent = new Intent();
                                returnIntent.putExtra("isLogin", true);
                                LoginOwnerActivity.this.setResult(Activity.RESULT_OK, returnIntent);
                                LoginOwnerActivity.this.finish();
                            }
                            else if ((int)response == FoodMapApiManager.FAIL_INFO){
                                Toast.makeText(LoginOwnerActivity.this, "Vui lòng kiểm tra lại thông tin đăng nhập", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }
            }
        });


        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        btnForgotPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }



}
