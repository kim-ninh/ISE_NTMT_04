package com.hcmus.dreamers.foodmap;

import android.app.PictureInPictureParams;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;


import com.hcmus.dreamers.foodmap.AsyncTask.DoingTask;
import com.hcmus.dreamers.foodmap.AsyncTask.TaskCompleteCallBack;
import com.hcmus.dreamers.foodmap.AsyncTask.TaskRequest;
import com.hcmus.dreamers.foodmap.Model.Catalog;
import com.hcmus.dreamers.foodmap.Model.Dish;
import com.hcmus.dreamers.foodmap.Model.Owner;
import com.hcmus.dreamers.foodmap.Model.Restaurant;
import com.hcmus.dreamers.foodmap.common.GenerateRequest;
import com.hcmus.dreamers.foodmap.common.ResponseJSON;
import com.hcmus.dreamers.foodmap.define.ConstantCODE;
import com.hcmus.dreamers.foodmap.jsonapi.ParseJSON;

public class ManageDishActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_dish);
        Toolbar toolbar = (Toolbar) findViewById(R.id.edit_dish_toolbar);
        setSupportActionBar(toolbar);

        //Enable the Up button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.action_delete:
                Toast.makeText(ManageDishActivity.this, "action delete selected",
                        Toast.LENGTH_LONG).show();
                return true;

            case R.id.action_done:
                Toast.makeText(ManageDishActivity.this, "action done selected",
                        Toast.LENGTH_LONG).show();
                return true;

            default:
                    return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.edit_item_menu,menu);
        return true;
    }

    public void updateInfomation(String name,
                                    int price,
                                    String urlImage,
                                    Catalog catalog,
                                    Restaurant restaurant){

        // Set the new Dish info
        final Dish newDish = new Dish(name,price,urlImage,catalog);

        TaskRequest updateDishInfoTask = new TaskRequest();

        // Implement call back
        updateDishInfoTask.setOnCompleteCallBack(new TaskCompleteCallBack() {
            @Override
            public void OnTaskComplete(Object response) {
                try
                {
                    ResponseJSON responseJSON =  ParseJSON
                            .parseFromAllResponse(response.toString());

                    // Pop-up the result message through Toast
                    if (ConstantCODE.SUCCESS == responseJSON.getCode()){
                        Toast.makeText(ManageDishActivity.this,
                                "Update successful!",
                                Toast.LENGTH_LONG).show();
                    }
                    else{
                        Toast.makeText(ManageDishActivity.this,
                                responseJSON.getMessage(),
                                Toast.LENGTH_LONG).show();
                    }

                }catch (Exception e){
                        Toast.makeText(ManageDishActivity.this,
                                e.getMessage(),
                                Toast.LENGTH_LONG).show();
                }
            }
        });

        // Invoke task
        updateDishInfoTask.execute(new DoingTask(
                GenerateRequest
                        .updateDish(
                                restaurant.getId(),
                                newDish,
                                Owner.getInstance().getToken())));

        // Send the new Dish back to previous activity?
        // ...
    }
}
