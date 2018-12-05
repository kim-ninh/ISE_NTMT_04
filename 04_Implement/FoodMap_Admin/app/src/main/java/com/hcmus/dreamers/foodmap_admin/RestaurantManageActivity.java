package com.hcmus.dreamers.foodmap_admin;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.hcmus.dreamers.foodmap_admin.asynctask.TaskCompleteCallBack;
import com.hcmus.dreamers.foodmap_admin.common.FoodMapApi;
import com.hcmus.dreamers.foodmap_admin.model.Admin;
import com.hcmus.dreamers.foodmap_admin.model.Restaurant;

import java.util.List;

public class RestaurantManageActivity extends AppCompatActivity {

    private ListView lstRestaurant;
    private Toolbar toolbar;
    private ImageView imvReload;

    private ListRestaurantAdapter listRestaurantAdapter;
    private List<Restaurant> restaurants;


    private final String WIRTE_PERMISSION = Manifest.permission.WRITE_EXTERNAL_STORAGE;
    private final String CALL_PERMISSION = Manifest.permission.CALL_PHONE;
    private final int CODE_REQUEST = 12213;
    private final int RI_AC = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_manage);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        imvReload = findViewById(R.id.imvReload);
        imvReload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoadData();
            }
        });

        restaurants = Admin.getInstance().getRestaurantList();

        lstRestaurant = (ListView)findViewById(R.id.lstRestaurant);
        lstRestaurant.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(RestaurantManageActivity.this, RestaurantInfo.class);
                intent.putExtra("id_rest", restaurants.get(position).getId());
                startActivityForResult(intent, RI_AC);
            }
        });
        listRestaurantAdapter = new ListRestaurantAdapter(RestaurantManageActivity.this, R.layout.restaurant_item_list, restaurants);
        lstRestaurant.setAdapter(listRestaurantAdapter);


        LoadData();

        // check permisson
        if (Build.VERSION.SDK_INT >= 23){
            if (ContextCompat.checkSelfPermission(RestaurantManageActivity.this, WIRTE_PERMISSION) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(RestaurantManageActivity.this, CALL_PERMISSION) != PackageManager.PERMISSION_GRANTED){
                requestPermissions(new String[]{WIRTE_PERMISSION, CALL_PERMISSION}, CODE_REQUEST);
            }
        }

    }


    void LoadData(){
        final ProgressDialog progressDialog = new ProgressDialog(RestaurantManageActivity.this);
        progressDialog.setMessage("Checking...");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
        FoodMapApi.getPreRestaurant(Admin.getInstance().getUsername(), Admin.getInstance().getPassword(), new TaskCompleteCallBack() {
            @Override
            public void OnTaskComplete(Object response) {
                progressDialog.dismiss();
                int code = (int)response;
                if (code == FoodMapApi.SUCCESS){
                    progressDialog.dismiss();
                    listRestaurantAdapter.notifyDataSetChanged();
                }
                else if (code == FoodMapApi.FAIL_INFO){
                    Toast.makeText(RestaurantManageActivity.this, "Sai thông tin đăng nhập", Toast.LENGTH_LONG).show();
                }
                else{
                    Toast.makeText(RestaurantManageActivity.this, "Kiểm tra kết nối internet", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home){
            finish();
        }
        return false;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == CODE_REQUEST){
            if (grantResults.length > 0)
            {
                for (int i =0 ;i <grantResults.length;i++){
                    if (grantResults[i] != PackageManager.PERMISSION_GRANTED)
                        finish();
                }
            }
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == Activity.RESULT_OK && requestCode == RI_AC){
            boolean isChange = data.getBooleanExtra("ischange", false);
            if (isChange)
                LoadData();
        }
    }
}
