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
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.Toast;

import com.hcmus.dreamers.foodmap.AsyncTask.DownloadImageTask;
import com.hcmus.dreamers.foodmap.Model.Guest;
import com.hcmus.dreamers.foodmap.Model.Restaurant;
import com.hcmus.dreamers.foodmap.View.GridViewItem;
import com.hcmus.dreamers.foodmap.adapter.FavorRestListAdapter;
import com.hcmus.dreamers.foodmap.adapter.FavorRestNameAutocompleteAdapter;
;
import java.util.List;


public class FavoriteRestaurantsActivity extends AppCompatActivity implements TextWatcher {

    private ImageView imgBackFavorRest;
    private AutoCompleteTextView txtAutoComplete;
    private GridViewItem grdFavorRest;

    //storing guest's list of favorite restaurants
    private List<Restaurant> favorRestaurant;

    private  FavorRestListAdapter adapter;
    private FavorRestNameAutocompleteAdapter favorRestListAdapter;

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
        grdFavorRest = (GridViewItem) findViewById(R.id.grdFavorRest);

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
        //end debug

        initAdapter();


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
        txtAutoComplete.setText(txtAutoComplete.getText());
    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    private void initAdapter(){
        favorRestaurant = Guest.getInstance().getFavRestaurant();

        //auto complete
        favorRestListAdapter = new FavorRestNameAutocompleteAdapter(FavoriteRestaurantsActivity.this,
                R.layout.adapter_autocomplete_favor_rest_name,
                favorRestaurant);

        txtAutoComplete.setAdapter(favorRestListAdapter);

        txtAutoComplete.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(FavoriteRestaurantsActivity.this, RestaurantInfoActivity.class);
                Restaurant info = (Restaurant)parent.getItemAtPosition(position);


                for(int i = 0; i < favorRestaurant.size(); i++) {
                    if(favorRestaurant.get(i).getId() == info.getId())
                    {
                        intent.putExtra("rest", favorRestaurant.get(i));
                        break;
                    }
                }
                startActivity(intent);
            }
        });


        adapter = new FavorRestListAdapter(FavoriteRestaurantsActivity.this, R.layout.adapter_favor_rest_list, favorRestaurant);

        grdFavorRest.setAdapter(adapter);

        grdFavorRest.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent = new Intent(FavoriteRestaurantsActivity.this, RestaurantInfoActivity.class);
                intent.putExtra("rest",favorRestaurant.get(position));
                startActivity(intent);
            }
        });
    }
    @Override
    protected void onStart() {
        super.onStart();

        adapter.notifyDataSetChanged();
        favorRestListAdapter.notifyDataSetChanged();
    }
}
