package com.hcmus.dreamers.foodmap;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.hcmus.dreamers.foodmap.AsyncTask.TaskCompleteCallBack;
import com.hcmus.dreamers.foodmap.common.FoodMapApiManager;
import com.hcmus.dreamers.foodmap.define.ConstantCODE;

public class RegisterOwnerActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText edtUsername;
    private EditText edtPassword;
    private EditText edtRePassword;
    private EditText edtName;
    private EditText edtEmail;
    private EditText edtPhoneNumber;
    private Button btnRegister;
    private Toolbar toolbar;

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_owner);

        // setup toolbar
        toolbar = (Toolbar)findViewById(R.id.register_owner_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        edtUsername = (EditText) findViewById(R.id.edtUsername);
        edtName = (EditText) findViewById(R.id.edtName);
        edtPassword = (EditText) findViewById(R.id.edtPassword);
        edtRePassword = (EditText) findViewById(R.id.edtRePassword);
        edtEmail = (EditText) findViewById(R.id.edtEmail);
        edtPhoneNumber = (EditText) findViewById(R.id.edtPhoneNumber);

        btnRegister = (Button)findViewById(R.id.btnRegister);
        btnRegister.setOnClickListener(this);

        progressDialog = new ProgressDialog(RegisterOwnerActivity.this);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setMessage("Register");
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnRegister){
            String username = edtUsername.getText().toString();
            String name = edtName.getText().toString();
            String password = edtPassword.getText().toString();
            final String rePassword = edtRePassword.getText().toString();
            String email = edtEmail.getText().toString();
            String phoneNumber = edtPhoneNumber.getText().toString();

            if (username.equals("") || name.equals("") || password.equals("") || rePassword.equals("") || email.equals("") || phoneNumber.equals("")){
                Toast.makeText(RegisterOwnerActivity.this, "Vui lòng điền đầy đủ thông tin", Toast.LENGTH_LONG).show();
            }
            else if (!password.equals(rePassword)){
                Toast.makeText(RegisterOwnerActivity.this, "Xác nhận mật khẩu chưa đúng", Toast.LENGTH_LONG).show();
                edtPassword.setText("");
                edtRePassword.setFocusable(true);
                edtRePassword.setText("");
            }
            else if (!RegisterOwnerActivity.checkEmail(email)){
                Toast.makeText(RegisterOwnerActivity.this, "Email không hợp lệ", Toast.LENGTH_LONG).show();
                edtEmail.setText("");
                edtEmail.setFocusable(true);
            }
            else if (phoneNumber.length() < 10 || phoneNumber.length() > 11){
                Toast.makeText(RegisterOwnerActivity.this, "Số điện thoại không hợp lệ", Toast.LENGTH_LONG).show();
                edtPhoneNumber.setText("");
                edtPhoneNumber.setFocusable(true);
            }
            else{
                progressDialog.show();;
                FoodMapApiManager.createAccount(username, password, name, phoneNumber, email, new TaskCompleteCallBack() {
                    @Override
                    public void OnTaskComplete(Object response) {
                        progressDialog.dismiss();
                        if ((int)response == FoodMapApiManager.SUCCESS){
                            Toast.makeText(RegisterOwnerActivity.this, "Đăng ký thành công", Toast.LENGTH_LONG).show();

                            Intent intent = new Intent();
                            intent.putExtra("isRegister", true);
                            RegisterOwnerActivity.this.setResult(Activity.RESULT_OK, intent);
                            RegisterOwnerActivity.this.finish();
                        }
                        else if ((int)response == FoodMapApiManager.FAIL_INFO) {
                            Toast.makeText(RegisterOwnerActivity.this, "Tên đăng nhập hoặc email đã tồn tại",Toast.LENGTH_LONG).show();
                        }
                        else if ((int)response == ConstantCODE.NOTINTERNET){
                            Toast.makeText(RegisterOwnerActivity.this, "Kiểm tra lại kết nối internet", Toast.LENGTH_LONG).show();
                        }
                        else{
                            Toast.makeText(RegisterOwnerActivity.this, "Đăng ký lỗi",Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        }
    }


    public static boolean checkEmail(String email) {
        String ePattern = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";
        java.util.regex.Pattern p = java.util.regex.Pattern.compile(ePattern);
        java.util.regex.Matcher m = p.matcher(email);
        return m.matches();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        switch (itemId) {
            case android.R.id.home:
                RegisterOwnerActivity.this.finish();
                break;
        }
        return true;
    }
}
