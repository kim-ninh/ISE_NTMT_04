package com.hcmus.dreamers.foodmap_admin;

import android.app.ProgressDialog;
import android.content.Intent;
import android.icu.text.UnicodeSetSpanner;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.hcmus.dreamers.foodmap_admin.asynctask.TaskCompleteCallBack;
import com.hcmus.dreamers.foodmap_admin.common.FoodMapApi;
import com.hcmus.dreamers.foodmap_admin.common.ResponseJSON;
import com.hcmus.dreamers.foodmap_admin.model.Admin;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btnLogin;
    private EditText edtUsername;
    private EditText edtPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnLogin = findViewById(R.id.btnLogin);
        edtUsername = findViewById(R.id.edtUsername);
        edtPassword = findViewById(R.id.edtPassword);

        btnLogin.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnLogin){
            final String username = edtUsername.getText().toString();
            final String password = edtPassword.getText().toString();

            if (username.equals("") || password.equals("")){
                Toast.makeText(MainActivity.this, "Vui lòng điền đầy đủ thông tin", Toast.LENGTH_LONG).show();
                edtPassword.setText("");
            }
            else{
                final ProgressDialog progressDialog = new ProgressDialog(MainActivity.this);
                progressDialog.setMessage("Checking...");
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.show();

                FoodMapApi.login(username, password, new TaskCompleteCallBack() {
                    @Override
                    public void OnTaskComplete(Object response) {
                        progressDialog.dismiss();
                        int code = (int)response;
                        if (code == FoodMapApi.SUCCESS){
                            Admin.getInstance().setUsername(username);
                            Admin.getInstance().setPassword(password);

                            Intent intent = new Intent(MainActivity.this, RestaurantManageActivity.class);
                            startActivity(intent);
                        }
                        else if (code == FoodMapApi.FAIL_INFO){
                            Toast.makeText(MainActivity.this, "Sai thông tin đăng nhập", Toast.LENGTH_LONG).show();
                            edtPassword.setText("");
                        }
                        else{
                            Toast.makeText(MainActivity.this, "Kiểm tra kết nối internet", Toast.LENGTH_LONG).show();
                        }
                    }
                });

            }
        }
    }
}
