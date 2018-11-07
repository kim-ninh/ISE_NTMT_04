package com.hcmus.dreamers.foodmap;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import com.hcmus.dreamers.foodmap.AsyncTask.DownloadImageTask;
import com.hcmus.dreamers.foodmap.Model.Restaurant;
import com.hcmus.dreamers.foodmap.adapter.FavorRestListAdapter;

import java.util.List;

public class FavoriteRestaurantsActivity extends AppCompatActivity implements TextWatcher {

    ImageView imgBackFavorRest;
    AutoCompleteTextView txtAutoComplete;
    GridView grdFavorRest;

    String[] items;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite_restaurants);

        //set header toolbar in the layout
        Toolbar toolbar = (Toolbar) findViewById(R.id.tlbRestInfo);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        imgBackFavorRest = (ImageView) findViewById(R.id.imgBackFavorLayout);
        txtAutoComplete = (AutoCompleteTextView) findViewById(R.id.txtAutoComplete);
        grdFavorRest = (GridView)findViewById(R.id.grdFavorRest);


        //debug
        //set Image background of favorite restaurant layout
        try {
            String url = "https://images.foody.vn/res/g67/669257/prof/s576x330/foody-mobile-16473970_58588712494-449-636338403355842447.jpg";
            DownloadImageTask downloadImageTask = new DownloadImageTask(imgBackFavorRest, getApplicationContext());
            downloadImageTask.loadImageFromUrl(url);
        }catch (Exception e)
        {
            Toast.makeText(this, e.getMessage().toString(), Toast.LENGTH_LONG).show();
        }

        //get list of user's favorite restaurant
        final Restaurant[] restaurants = new Restaurant[16];
        items = new String[16];
        for (int i=0;i<16; i++)
        {
            restaurants[i] = new Restaurant();
            restaurants[i].setUrlImage("https://cafebiz.cafebizcdn.vn/thumb_w/600/2014/ava-1414462559483.jpg");
            restaurants[i].setName("rest " + i);
            items[i] = restaurants[i].getName();

        }

        //end bug

        //auto complete
        txtAutoComplete.setAdapter(new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_single_choice,
                items));

        txtAutoComplete.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(FavoriteRestaurantsActivity.this, RestaurantInfoActivity.class);
                intent.putExtra("RestID", restaurants[position].getId());
                startActivity(intent);
            }
        });

        FavorRestListAdapter adapter = new FavorRestListAdapter(FavoriteRestaurantsActivity.this, R.layout.favor_rest_list, restaurants);
        grdFavorRest.setAdapter(adapter);

        grdFavorRest.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(FavoriteRestaurantsActivity.this, RestaurantInfoActivity.class);
                intent.putExtra("RestID", restaurants[position].getId());
                startActivity(intent);
            }
        });

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

    //text watcher
    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {

    }
}
