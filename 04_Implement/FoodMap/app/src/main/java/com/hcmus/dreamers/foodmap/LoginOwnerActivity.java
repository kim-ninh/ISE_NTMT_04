package com.hcmus.dreamers.foodmap;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.hcmus.dreamers.foodmap.AsyncTask.DoingTask;
import com.hcmus.dreamers.foodmap.AsyncTask.TaskCompleteCallBack;
import com.hcmus.dreamers.foodmap.AsyncTask.TaskRequest;
import com.hcmus.dreamers.foodmap.Model.Owner;
import com.hcmus.dreamers.foodmap.common.FoodMapApiManager;
import com.hcmus.dreamers.foodmap.common.GenerateRequest;


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
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setTitle("Check login");

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
                            else {
                                Toast.makeText(LoginOwnerActivity.this, "Lỗi đăng nhập", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }
            }
        });


        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginOwnerActivity.this, RegisterOwnerActivity.class);
                startActivity(intent);
            }
        });

        btnForgotPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(LoginOwnerActivity.this);
                View forgotPassLayout = getLayoutInflater().inflate(R.layout.dialog_forgot_password, null);

                final AlertDialog dialog = builder.create();

                final EditText edtEmail = (EditText)forgotPassLayout.findViewById(R.id.edtEmail);
                Button btnSend = (Button)forgotPassLayout.findViewById(R.id.btnSend);
                btnSend.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final String email = edtEmail.getText().toString();
                        if (email.equals("") || !RegisterOwnerActivity.checkEmail(email)){
                            Toast.makeText(LoginOwnerActivity.this, "Email không hợp lệ", Toast.LENGTH_LONG).show();
                        }
                        else{
                            FoodMapApiManager.forgotPassword(email, new TaskCompleteCallBack() {
                                @Override
                                public void OnTaskComplete(Object response) {
                                    if ((int)response == FoodMapApiManager.SUCCESS){
                                        dialog.dismiss();
                                        showTypeCodeDialog(email);
                                    }
                                    else if ((int)response == FoodMapApiManager.FAIL_INFO){
                                        Toast.makeText(LoginOwnerActivity.this, "Email không tồn tại", Toast.LENGTH_LONG).show();
                                    }
                                    else{
                                        Toast.makeText(LoginOwnerActivity.this, "Xảy ra lỗi trong quá trình gửi email", Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                        }
                    }
                });

                dialog.setView(forgotPassLayout);
                dialog.show();
            }
        });
    }

    void showTypeCodeDialog(final String email){
        AlertDialog.Builder builder = new AlertDialog.Builder(LoginOwnerActivity.this);
        View typeCodeLayout = getLayoutInflater().inflate(R.layout.dialog_type_code, null);

        final AlertDialog dialog = builder.create();

        final EditText edtCode = (EditText)typeCodeLayout.findViewById(R.id.edtCode);
        Button btnSend = (Button)typeCodeLayout.findViewById(R.id.btnSend);
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String code = edtCode.getText().toString();
                if (code.equals("")){
                    Toast.makeText(LoginOwnerActivity.this, "Vui lòng nhập mã reset", Toast.LENGTH_LONG).show();
                }
                else {
                    FoodMapApiManager.checkCode(email, code, new TaskCompleteCallBack() {
                        @Override
                        public void OnTaskComplete(Object response) {
                            if ((int)response == FoodMapApiManager.SUCCESS){
                                dialog.dismiss();
                                showResetPassworddialog();
                            }
                            else if((int)response == FoodMapApiManager.FAIL_INFO){
                                Toast.makeText(LoginOwnerActivity.this, "Mã xác nhận không tồn tại", Toast.LENGTH_LONG).show();
                            }
                            else {
                                Toast.makeText(LoginOwnerActivity.this, "Xảy ra lỗi trong quá trình check code", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }

            }
        });


        // show dialog
        dialog.setView(typeCodeLayout);
        dialog.show();
    }

    void showResetPassworddialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(LoginOwnerActivity.this);
        View resetPassLayout = getLayoutInflater().inflate(R.layout.dialog_reset_password, null);

        final AlertDialog dialog = builder.create();


        // show dialog
        dialog.setView(resetPassLayout);
        dialog.show();
    }

}
