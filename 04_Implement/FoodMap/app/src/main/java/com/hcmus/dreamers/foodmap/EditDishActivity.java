package com.hcmus.dreamers.foodmap;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;


import com.google.gson.Gson;
import com.hcmus.dreamers.foodmap.AsyncTask.DoingTask;
import com.hcmus.dreamers.foodmap.AsyncTask.TaskCompleteCallBack;
import com.hcmus.dreamers.foodmap.AsyncTask.TaskRequest;
import com.hcmus.dreamers.foodmap.Model.Catalog;
import com.hcmus.dreamers.foodmap.Model.Dish;
import com.hcmus.dreamers.foodmap.Model.Owner;
import com.hcmus.dreamers.foodmap.common.FoodMapApiManager;
import com.hcmus.dreamers.foodmap.common.GenerateRequest;
import com.hcmus.dreamers.foodmap.common.ResponseJSON;
import com.hcmus.dreamers.foodmap.define.ConstantCODE;
import com.hcmus.dreamers.foodmap.jsonapi.ParseJSON;

import java.io.File;

public class EditDishActivity extends AppCompatActivity {


    EditText txtDishName;
    EditText txtDishCost;
    Spinner spnrDishType;
    GridView gridView;
    FloatingActionButton fab;
    Toolbar toolbar;

    Intent manageRest_manageDish;
    Bundle transferData = new Bundle();

    int rest_id;
    int row;
    Dish dish;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_dish);

        takeReferenceFromResource();
        getTransferDataFromActivity();
        putDataToViews();

        gridView.setAdapter(new ImageAdapter(this));
        spnrDishType.setAdapter(new ArrayAdapter<String>(
                this,
                R.layout.support_simple_spinner_dropdown_item,
                Catalog.getDishTypes()
        ));

        //Enable the Up button (Icon look like this: <- )
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        // HANDLE ALL CLICK EVENT
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
       spnrDishType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
           @Override
           public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
               dish.setCatalog(new Catalog(position + 1,Catalog.getDishTypes()[position]));
           }

           @Override
           public void onNothingSelected(AdapterView<?> parent) {

           }
       });
    }

    private void putDataToViews() {
        txtDishName.setText(dish.getName());
        txtDishCost.setText(Integer.toString(dish.getPrice()));

        // Mặc định sẽ lấy hình đầu tiên làm hình đại diện của món
        ImageView dishImage =(ImageView) gridView.getChildAt(0);

        // Nếu có đường dẫn hình của món ăn thì đặt thì gán vào ImageView
        if (!dish.getUrlImage().isEmpty())
            dishImage.setImageURI(Uri.fromFile(new File(dish.getUrlImage())));

        spnrDishType.setSelection(dish.getCatalog().getId() - 1);
    }

    private void getTransferDataFromActivity() {
        // Get the restaurant ID and dish Obj
        Gson gson = new Gson();
        manageRest_manageDish = getIntent();
        transferData = manageRest_manageDish.getExtras();

        rest_id = transferData.getInt("restID");
        String dishJSON = transferData.getString("dishJSON");
        dish = gson.fromJson(dishJSON, Dish.class);
        row = transferData.getInt("dishRow");
    }

    private void takeReferenceFromResource() {
        txtDishName = (EditText) findViewById(R.id.txtDishName);
        txtDishCost = (EditText) findViewById(R.id.txtDishCost);
        spnrDishType = (Spinner) findViewById(R.id.dishType);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        toolbar = (Toolbar) findViewById(R.id.edit_dish_toolbar);
        gridView  = (GridView) findViewById(R.id.gridview);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.action_delete:
                FoodMapApiManager.deleteDish(rest_id, dish.getName(), new TaskCompleteCallBack() {
                    @Override
                    public void OnTaskComplete(Object response) {
                        if((int)response == FoodMapApiManager.SUCCESS){
                            Intent intent = new Intent();
                            intent.putExtra("delete", row);
                            setResult(RESULT_OK, intent);
                            finish();
                        }else if((int)response == ConstantCODE.NOTINTERNET){
                            Toast.makeText(EditDishActivity.this, "Không có kết nối INTERNET!", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(EditDishActivity.this, "Xóa món ăn thất bại!", Toast.LENGTH_LONG).show();
                        }
                    }
                });
                return true;

            case R.id.action_done:
                if(checkInputValid()){
                    FoodMapApiManager.updateDish(rest_id, dish, new TaskCompleteCallBack() {
                        @Override
                        public void OnTaskComplete(Object response) {
                            if((int)response == FoodMapApiManager.SUCCESS) {
                                Gson gson = new Gson();
                                Intent intent = new Intent();
                                intent.putExtra("dishJSON", gson.toJson(dish));
                                intent.putExtra("dishRow", row);
                                setResult(RESULT_OK, intent);
                                finish();
                            }else if((int)response == ConstantCODE.NOTINTERNET){
                                Toast.makeText(EditDishActivity.this, "Không có kết nối INTERNET!", Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(EditDishActivity.this, "Xóa món ăn thất bại!", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }else{
                    Toast.makeText(EditDishActivity.this, "Hãy nhập đầy đủ thông tin!", Toast.LENGTH_LONG).show();
                }


                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private boolean checkInputValid() {
        if(txtDishName.length() > 0 && txtDishCost.length() > 0){
            dish.setName(txtDishName.getText().toString());
            dish.setPrice(Integer.parseInt(txtDishCost.getText().toString()));
            return true;
        }
        return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.edit_item_menu,menu);
        return true;
    }
}