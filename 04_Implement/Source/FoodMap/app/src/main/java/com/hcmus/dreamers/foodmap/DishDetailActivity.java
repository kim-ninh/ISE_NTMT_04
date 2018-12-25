package com.hcmus.dreamers.foodmap;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.hcmus.dreamers.foodmap.AsyncTask.DownloadImageTask;
import com.hcmus.dreamers.foodmap.Model.Dish;

public class DishDetailActivity extends AppCompatActivity {

    private Dish dish;
    private ImageView img_dish_avatar;
    private TextView txt_dish_name;
    private TextView txt_dish_price;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dish_detail);

        //set header toolbar in the layout
        Toolbar toolbar = (Toolbar) findViewById(R.id.tlbDishDetail);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //get content of dish
        Intent intent = getIntent();
        dish = (Dish) intent.getSerializableExtra("dish");

        img_dish_avatar = (ImageView) findViewById(R.id.img_dish_avatar);
        txt_dish_name = (TextView) findViewById(R.id.txt_dish_name);
        txt_dish_price = (TextView) findViewById(R.id.txt_dish_price);

        DownloadImageTask downloadImageTask = new DownloadImageTask(img_dish_avatar, DishDetailActivity.this);
        downloadImageTask.loadImageFromUrl(dish.getUrlImage());

        txt_dish_name.setText(dish.getName());
        txt_dish_price.setText(Integer.toString(dish.getPrice()) + " VND");

    }

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


}
