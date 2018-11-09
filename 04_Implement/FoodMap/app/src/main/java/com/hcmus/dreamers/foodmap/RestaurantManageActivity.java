package com.hcmus.dreamers.foodmap;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

import com.hcmus.dreamers.foodmap.Model.Owner;
import com.hcmus.dreamers.foodmap.Model.Restaurant;
import com.hcmus.dreamers.foodmap.adapter.RestaurantListAdapter;
import com.hcmus.dreamers.foodmap.event.ClickListener;

import java.util.List;

public class RestaurantManageActivity extends AppCompatActivity implements View.OnClickListener, ClickListener {

    private Toolbar toolbar;
    private ImageView igvAdd;
    private RecyclerView rcvRestaurant;

    private RestaurantListAdapter restaurantListAdapter;
    private List<Restaurant> restaurantList;

    private final int RRA_ID = 1234;
    private final int RMA_ID = 1111;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_manage);

        toolbar = (Toolbar)findViewById(R.id.manage_restaurant_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        igvAdd = (ImageView)findViewById(R.id.igv_add);
        igvAdd.setOnClickListener(this);

        rcvRestaurant = (RecyclerView) findViewById(R.id.rcvRestaurant);
        rcvRestaurant.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
            @Override
            public boolean onInterceptTouchEvent(@NonNull RecyclerView recyclerView, @NonNull MotionEvent motionEvent) {
                return false;
            }

            @Override
            public void onTouchEvent(@NonNull RecyclerView recyclerView, @NonNull MotionEvent motionEvent) {

            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean b) {

            }
        });
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        rcvRestaurant.setLayoutManager(mLayoutManager);

        restaurantList = Owner.getInstance().getListRestaurant();
        restaurantListAdapter = new RestaurantListAdapter(RestaurantManageActivity.this,R.layout.item_restaurant_list, restaurantList);
        restaurantListAdapter.setOnClickListener(this);
        rcvRestaurant.setAdapter(restaurantListAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home){
            RestaurantManageActivity.this.finish();
        }
        return true;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.igv_add){
            Intent intent = new Intent(RestaurantManageActivity.this, RegisterRestaurantActivity.class);
            startActivityForResult(intent, RRA_ID);
        }
    }

    @Override
    public void onItemClick(int position, View v) {
        Intent intent = new Intent(RestaurantManageActivity.this, EditRestaurantActivity.class);
        intent.putExtra("rest", restaurantList.get(position));
        startActivityForResult(intent, RMA_ID);
    }

    @Override
    public void onItemLongClick(int position, View v) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == RRA_ID && resultCode == Activity.RESULT_OK){
            boolean isAdd = data.getBooleanExtra("isAdd", false);
            if (isAdd){
                restaurantList.clear();
                restaurantList.addAll(Owner.getInstance().getListRestaurant());
                restaurantListAdapter.notifyDataSetChanged();
            }
        }
        else if (requestCode == RMA_ID && resultCode == Activity.RESULT_OK){
            restaurantList.clear();
            restaurantList.addAll(Owner.getInstance().getListRestaurant());
            restaurantListAdapter.notifyDataSetChanged();
        }
    }
}
