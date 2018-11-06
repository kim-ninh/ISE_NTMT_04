package com.hcmus.dreamers.foodmap;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.hcmus.dreamers.foodmap.AsyncTask.TaskCompleteCallBack;
import com.hcmus.dreamers.foodmap.common.FoodMapApiManager;
import com.hcmus.dreamers.foodmap.define.ConstantCODE;

public class LoadingActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loading_layout);
        FoodMapApiManager.getRestaurant(new TaskCompleteCallBack() {
            @Override
            public void OnTaskComplete(Object response) {
                int code = (int) response;
                if (code == FoodMapApiManager.SUCCESS){
                    Intent intent = new Intent(LoadingActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
                else if (code == FoodMapApiManager.FAIL_INFO){
                    Toast.makeText(LoadingActivity.this, "Error", Toast.LENGTH_LONG).show();
                }
                else if (code == FoodMapApiManager.PARSE_FAIL){
                    Toast.makeText(LoadingActivity.this, "Error", Toast.LENGTH_LONG).show();
                }
                else if (code == ConstantCODE.NOTINTERNET){
                    Toast.makeText(LoadingActivity.this, "Kiểm tra kết nối mạng", Toast.LENGTH_LONG).show();
                }
            }
        });
    }



}
