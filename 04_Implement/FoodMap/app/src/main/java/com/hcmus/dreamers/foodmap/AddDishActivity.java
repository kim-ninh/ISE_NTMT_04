package com.hcmus.dreamers.foodmap;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.method.DateTimeKeyListener;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;


import com.google.gson.Gson;
import com.hcmus.dreamers.foodmap.AsyncTask.TaskCompleteCallBack;
import com.hcmus.dreamers.foodmap.Model.Catalog;
import com.hcmus.dreamers.foodmap.Model.Dish;
import com.hcmus.dreamers.foodmap.Model.Restaurant;
import com.hcmus.dreamers.foodmap.common.FoodMapApiManager;
import com.hcmus.dreamers.foodmap.database.FoodMapManager;
import com.hcmus.dreamers.foodmap.define.ConstantCODE;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class AddDishActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    private EditText edtName;
    private EditText edtPrice;
    private Spinner spnCatalog;
    private ImageView imgImage;
    private Button btnSubmit;
    private String urlImage;
    private Catalog catalog;
    private Toolbar toolbar;
    private List<String> listCatalog;
    private Restaurant restaurant;

    private final int IPC_ID = 1231;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_dish);
        references();

        urlImage = "";

        Intent data = getIntent();
        restaurant = (Restaurant) data.getSerializableExtra("rest");

        if (restaurant == null){
            finish();
        }

        btnSubmit.setOnClickListener(this);
    }

    private void references(){
        toolbar = (Toolbar)findViewById(R.id.add_dish_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        edtName = (EditText) findViewById(R.id.edtName);
        edtPrice = (EditText) findViewById(R.id.edtPrice);

        spnCatalog  = (Spinner)findViewById(R.id.spnCatalog);
        // Create an ArrayAdapter using the string array and a default spinner layout
        listCatalog = FoodMapManager.getCatalogsString();
        ArrayAdapter<CharSequence> adapter = new  ArrayAdapter<CharSequence>(this, android.R.layout.simple_spinner_item);
        adapter.addAll(listCatalog);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnCatalog.setAdapter(adapter);
        spnCatalog.setOnItemSelectedListener(this);

        imgImage = (ImageView) findViewById(R.id.imgImage);
        imgImage.setOnClickListener(this);

        btnSubmit = (Button) findViewById(R.id.btnSubmit);
        btnSubmit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btnSubmit){
            final String name = edtName.getText().toString();
            final String price = edtPrice.getText().toString();
            if (name.equals("") || price.equals("") || urlImage.equals("") || catalog == null){
                Toast.makeText(AddDishActivity.this, "Vui lòng điền đầy đủ thông tin", Toast.LENGTH_LONG).show();
            }
            else {
                final ProgressDialog progressDialog = new ProgressDialog(AddDishActivity.this);
                progressDialog.setMessage("Add Dish");
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.show();

                Date date = Calendar.getInstance().getTime();

                String nameImage = "dish_" + restaurant.getDishes().size() +"_" + date.getTime();
                FoodMapApiManager.uploadImage(AddDishActivity.this, restaurant.getId(), nameImage, urlImage, new TaskCompleteCallBack() {
                    @Override
                    public void OnTaskComplete(Object response) {
                        String url = (String) response;
                        if (url != null){
                            final Dish dish = new Dish();
                            dish.setName(name);
                            dish.setCatalog(catalog);
                            dish.setPrice(Integer.valueOf(price));
                            dish.setUrlImage(url);

                            FoodMapApiManager.addDish(restaurant.getId(), dish, new TaskCompleteCallBack() {
                                @Override
                                public void OnTaskComplete(Object response) {
                                    int code = (int)response;
                                    progressDialog.dismiss();
                                    if (code == FoodMapApiManager.SUCCESS){
                                        restaurant.getDishes().add(dish);

                                        // Send new dish back to previous activity/fragment
                                        // to update the list view
                                        Intent data = new Intent();
                                        Gson gson = new Gson();
                                        String dishJSON = gson.toJson(dish);
                                        data.putExtra("dishJSON", dishJSON);

                                        setResult(Activity.RESULT_OK, data);
                                        AddDishActivity.this.finish();
                                    }
                                    else if (code == FoodMapApiManager.FAIL_INFO)
                                    {
                                        Toast.makeText(AddDishActivity.this, "Kiểm tra lại thông tin vừa nhập", Toast.LENGTH_LONG).show();
                                    }
                                    else if (code == ConstantCODE.NOTINTERNET){
                                        Toast.makeText(AddDishActivity.this, "Kiểm tra kết nối mạng", Toast.LENGTH_LONG).show();
                                    }
                                }
                            });

                        }
                        else {
                            progressDialog.dismiss();
                            Toast.makeText(AddDishActivity.this,"Tải ảnh lỗi", Toast.LENGTH_LONG).show();
                        }
                    }
                });

            }
        }
        else if (id == R.id.imgImage){
            Intent editDish_pickImage = new Intent(Intent.ACTION_PICK,
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(editDish_pickImage, IPC_ID);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home){
            finish();
        }
        return true;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == IPC_ID){
            if (resultCode == Activity.RESULT_OK){
                // Chuỗi URI trả về có dạng content://<path>
                urlImage = data.getDataString();
                Uri uri = Uri.parse(urlImage);
                imgImage.setImageURI(uri);
            }
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        catalog = FoodMapManager.findCatalog(listCatalog.get(position));
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        catalog = null;
    }
}
