package com.hcmus.dreamers.foodmap;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.hcmus.dreamers.foodmap.AsyncTask.TaskCompleteCallBack;
import com.hcmus.dreamers.foodmap.Model.Offer;
import com.hcmus.dreamers.foodmap.Model.Restaurant;
import com.hcmus.dreamers.foodmap.adapter.DiscountListAdapter;
import com.hcmus.dreamers.foodmap.common.FoodMapApiManager;
import com.hcmus.dreamers.foodmap.common.ResponseJSON;
import com.hcmus.dreamers.foodmap.define.ConstantCODE;
import com.hcmus.dreamers.foodmap.jsonapi.ParseJSON;

import org.json.JSONException;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AddOrderActivity extends AppCompatActivity {

    private Toolbar tlbAddOrder;
    private EditText edtEmail;
    private EditText edtTotal;
    private Button btnSubmit;

    private int id_discount;


    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_order);

        Intent data = getIntent();
        id_discount = (int) data.getIntExtra("id_discount", -1);

        if (id_discount == -1) {
            finish();
        }

        references();
    }

    private void references(){
        // set header toolbar in the layout
        tlbAddOrder = (Toolbar) findViewById(R.id.tlbAddOrder);
        setSupportActionBar(tlbAddOrder);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        edtEmail = (EditText) findViewById(R.id.txtEmail);
        edtTotal = (EditText) findViewById(R.id.txtTotal);

        btnSubmit = (Button) findViewById(R.id.btnSubmit);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edtEmail.getText().length() == 0 || edtTotal.getText().length() == 0) {
                    Toast.makeText(AddOrderActivity.this, "Vui lòng nhập đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!isEmailValid(edtEmail.getText().toString())) {
                    Toast.makeText(AddOrderActivity.this, "Email không hợp lệ!", Toast.LENGTH_SHORT).show();
                    return;
                }

                Offer offer = new Offer();
                offer.setGuestEmail(edtEmail.getText().toString());
                offer.setTotal(Integer.parseInt(edtTotal.getText().toString()));

                final ProgressDialog progressDialog = new ProgressDialog(AddOrderActivity.this);
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.setMessage("Creating Order");
                progressDialog.show();

                // tạo Discount
                FoodMapApiManager.addOrder(offer, id_discount, new TaskCompleteCallBack() {
                    @Override
                    public void OnTaskComplete(Object response) {
                        int code = (int) response;
                        progressDialog.dismiss();
                        if (code == ConstantCODE.SUCCESS) {
                            AddOrderActivity.this.finish();
                            Toast.makeText(getBaseContext(), "Đặt món thành công", Toast.LENGTH_LONG).show();
                            return;
                        } else if (code == FoodMapApiManager.FAIL_INFO) {
                            Toast.makeText(AddOrderActivity.this, "Kiểm tra lại thông tin vừa nhập", Toast.LENGTH_LONG).show();
                        } else if (code == ConstantCODE.NOTINTERNET) {
                            Toast.makeText(AddOrderActivity.this, "Kiểm tra kết nối mạng", Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });
    }

    //set click event for toolbar
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public static boolean isEmailValid(String email) {
        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
}
