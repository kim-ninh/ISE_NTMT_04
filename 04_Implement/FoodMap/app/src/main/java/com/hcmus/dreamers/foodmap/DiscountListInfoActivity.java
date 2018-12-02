// http://www.technotalkative.com/contextual-action-bar-cab-android/

package com.hcmus.dreamers.foodmap;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.hcmus.dreamers.foodmap.AsyncTask.TaskCompleteCallBack;
import com.hcmus.dreamers.foodmap.Model.Discount;
import com.hcmus.dreamers.foodmap.Model.Restaurant;
import com.hcmus.dreamers.foodmap.adapter.DiscountListAdapter;
import com.hcmus.dreamers.foodmap.common.FoodMapApiManager;
import com.hcmus.dreamers.foodmap.common.ResponseJSON;
import com.hcmus.dreamers.foodmap.define.ConstantCODE;
import com.hcmus.dreamers.foodmap.jsonapi.ParseJSON;

import org.json.JSONException;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

public class DiscountListInfoActivity extends AppCompatActivity implements AdapterView.OnItemClickListener{

    private ListView lvDiscount;
    private Toolbar tlbDiscountListInfo;
    private DiscountListAdapter discountListAdapter;
    private List<Discount> discounts;

    private Restaurant restaurant = null;
    Context context = null;


    public void setContext(Context context) {
        this.context = context;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_discount_list_info);

        references();

        // set header toolbar in the layout
        setSupportActionBar(tlbDiscountListInfo);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent data = getIntent();
        restaurant = (Restaurant) data.getSerializableExtra("rest");

        if (restaurant == null) {
            finish();
        }

        FoodMapApiManager.getDiscount(restaurant.getId(), new TaskCompleteCallBack() {
            @Override
            public void OnTaskComplete(Object response) {
                String resp = response.toString();
                try {
                    ResponseJSON responseJSON = ParseJSON.parseFromAllResponse(resp);
                    if(responseJSON.getCode() == ConstantCODE.SUCCESS){

                        discounts = ParseJSON.parseDiscount(resp);
                        discountListAdapter = new DiscountListAdapter(DiscountListInfoActivity.this, R.layout.item_discount_list, discounts, restaurant);
                        lvDiscount.setAdapter(discountListAdapter);
                        discountListAdapter.notifyDataSetChanged();

                    }else if(responseJSON.getCode() == ConstantCODE.NOTFOUND){
                        Toast.makeText(DiscountListInfoActivity.this, "NOT FOUND!", Toast.LENGTH_SHORT).show();
                    }else {
                        Toast.makeText(DiscountListInfoActivity.this, "NOT INTERNET!", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    Toast.makeText(DiscountListInfoActivity.this, "NOT INTERNET!", Toast.LENGTH_SHORT).show();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void references(){
        tlbDiscountListInfo = (Toolbar) findViewById(R.id.tlbDiscountListInfo);
        lvDiscount = findViewById(R.id.lvDiscount);
        lvDiscount.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(DiscountListInfoActivity.this, AddOrderActivity.class);
        intent.putExtra("id_discount", discounts.get(position).getId());
        startActivity(intent);
    }

    //set click event for toolbar
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
