package com.hcmus.dreamers.foodmap;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import com.hcmus.dreamers.foodmap.Model.Restaurant;
import com.hcmus.dreamers.foodmap.adapter.ViewPagerAdapter;
import com.hcmus.dreamers.foodmap.fragment.DishListFragment;
import com.hcmus.dreamers.foodmap.fragment.OneFragment;
import com.hcmus.dreamers.foodmap.fragment.OrderListFragment;
import com.hcmus.dreamers.foodmap.fragment.RestaurantInfoFragment;

public class EditRestaurantActivity extends AppCompatActivity implements RestaurantInfoFragment.OnFragmentInteractionListener {


    public Restaurant restaurant;

    Toolbar toolbar;
    TabLayout tabLayout;
    ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_restaurant);
        //lấy dữ liệu từ Restaurant manager
        getTransferDataFromActivity();
        takeReferenceFromResource();

        //Enable the Up button
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // set-up view pager (for tab layout)
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());

        adapter.addFragment(new RestaurantInfoFragment(), "CHUNG");
        adapter.addFragment(new DishListFragment(), "MÓN ĂN");
        OrderListFragment orderListFragment = new OrderListFragment();
        orderListFragment.setId_rest(restaurant.getId());
        orderListFragment.setContext(EditRestaurantActivity.this);
        adapter.addFragment(orderListFragment, "ĐƠN ĐẶT HÀNG");

        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);

    }


    private void getTransferDataFromActivity() {
        Intent data = getIntent();
        restaurant = (Restaurant) data.getSerializableExtra("rest");

        //restaurant should not be null
        if (restaurant == null)
        {
            Toast.makeText(EditRestaurantActivity.this,
                    "Restaurant is null",
                    Toast.LENGTH_LONG).show();
            finish();
            return;
        }

    }


    private void takeReferenceFromResource() {
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        toolbar = (Toolbar) findViewById(R.id.edit_restaurant_toolbar);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
