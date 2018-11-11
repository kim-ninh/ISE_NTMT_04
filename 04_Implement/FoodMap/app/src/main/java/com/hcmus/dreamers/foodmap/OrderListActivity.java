package com.hcmus.dreamers.foodmap;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.ListView;
import android.widget.Toast;

import com.hcmus.dreamers.foodmap.AsyncTask.DoingTask;
import com.hcmus.dreamers.foodmap.AsyncTask.TaskCompleteCallBack;
import com.hcmus.dreamers.foodmap.Model.Offer;
import com.hcmus.dreamers.foodmap.adapter.OrderListAdapter;
import com.hcmus.dreamers.foodmap.common.FoodMapApiManager;
import com.hcmus.dreamers.foodmap.common.ResponseJSON;
import com.hcmus.dreamers.foodmap.define.ConstantCODE;
import com.hcmus.dreamers.foodmap.jsonapi.ParseJSON;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class OrderListActivity extends AppCompatActivity {

    private ListView listOffer;
    private OrderListAdapter adapter;
    private List<Offer> offers;
    private int id_rest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_list);
        refferences();
        getItentFromActivity();
        refreshData();

//        offers = new ArrayList<>();
//        for (int i = 0; i < 20; i++)
//            offers.add(new Offer("Phở " + i, 10, "chauhoangphuc@gmail.com", i));



    }

    private void refferences(){
        listOffer = (ListView) findViewById(R.id.list_order);
    }


    private void getItentFromActivity(){
        Intent intent = getIntent();
        id_rest = intent.getIntExtra("id_rest", -1);
    }

    private void refreshData(){
        FoodMapApiManager.getOffer(id_rest, new TaskCompleteCallBack() {
            @Override
            public void OnTaskComplete(Object response) {
                String resp = response.toString();
                try {
                    ResponseJSON responseJSON = ParseJSON.parseFromAllResponse(resp);
                    if(responseJSON.getCode() == ConstantCODE.SUCCESS){
                        offers = ParseJSON.parseOffer(resp);
                        adapter = new OrderListAdapter(OrderListActivity.this, R.layout.order_item_list, offers);
                        listOffer.setAdapter(adapter);
                    }else if(responseJSON.getCode() == ConstantCODE.NOTFOUND){
                        Toast.makeText(OrderListActivity.this, "NOT FOUND!", Toast.LENGTH_SHORT).show();
                    }else {
                        Toast.makeText(OrderListActivity.this, "NOT INTERNET!", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    Toast.makeText(OrderListActivity.this, "NOT INTERNET!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}
