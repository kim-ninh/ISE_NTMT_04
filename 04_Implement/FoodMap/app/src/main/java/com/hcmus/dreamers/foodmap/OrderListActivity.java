package com.hcmus.dreamers.foodmap;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.ListView;

import com.hcmus.dreamers.foodmap.Model.Offer;
import com.hcmus.dreamers.foodmap.adapter.OrderListAdapter;

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

        offers = new ArrayList<>();
        for (int i = 0; i < 5; i++)
            offers.add(new Offer("Phở " + i, 10, "chauhoangphuc@gmail.com", i));

        adapter = new OrderListAdapter(OrderListActivity.this, R.layout.order_item_list, offers);
        listOffer.setAdapter(adapter);
    }

    private void refferences(){
        listOffer = (ListView) findViewById(R.id.list_order);
    }


    private void getItentFromActivity(){
        Intent intent = getIntent();
        id_rest = intent.getIntExtra("id_rest", -1);
    }

}
