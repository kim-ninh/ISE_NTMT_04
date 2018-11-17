package com.hcmus.dreamers.foodmap.fragment;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.hcmus.dreamers.foodmap.AsyncTask.TaskCompleteCallBack;
import com.hcmus.dreamers.foodmap.EditRestaurantActivity;
import com.hcmus.dreamers.foodmap.Model.Offer;
import com.hcmus.dreamers.foodmap.Model.Restaurant;
import com.hcmus.dreamers.foodmap.OrderListActivity;
import com.hcmus.dreamers.foodmap.R;
import com.hcmus.dreamers.foodmap.adapter.OrderListAdapter;
import com.hcmus.dreamers.foodmap.common.FoodMapApiManager;
import com.hcmus.dreamers.foodmap.common.ResponseJSON;
import com.hcmus.dreamers.foodmap.define.ConstantCODE;
import com.hcmus.dreamers.foodmap.jsonapi.ParseJSON;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class OrderListFragment extends Fragment {

    Restaurant restaurant;

    private ListView listOffer;
    private OrderListAdapter adapter;
    private List<Offer> offers;
    private int id_rest;

    Context context = null;
    EditRestaurantActivity editRestaurantActivity;
    LinearLayout rootLayout;

    public OrderListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        // Get host activity "EditRestaurant"
        context = getActivity();
        editRestaurantActivity = (EditRestaurantActivity) getActivity();

        if (editRestaurantActivity != null)
        {
            restaurant = editRestaurantActivity.restaurant;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootLayout =(LinearLayout) inflater.inflate(R.layout.fragment_order_list, container, false);

        refferences();
        getItentFromActivity();
        //must not remove
        //refreshData();



        offers = new ArrayList<>();
        for (int i = 0; i < 20; i++)
            offers.add(new Offer("Phở " + i, 10, "chauhoangphuc@gmail.com", i));


        adapter = new OrderListAdapter(context, R.layout.order_item_list, offers);
        listOffer.setAdapter(adapter);
        return rootLayout;
    }

    private void refferences(){
        listOffer = rootLayout.findViewById(R.id.list_order);
    }


    private void getItentFromActivity(){
        //Intent intent = getIntent();
        //id_rest = intent.getIntExtra("id_rest", -1);
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
                        adapter = new OrderListAdapter(context, R.layout.order_item_list, offers);
                        listOffer.setAdapter(adapter);
                    }else if(responseJSON.getCode() == ConstantCODE.NOTFOUND){
                        Toast.makeText(context, "NOT FOUND!", Toast.LENGTH_SHORT).show();
                    }else {
                        Toast.makeText(context, "NOT INTERNET!", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    Toast.makeText(context, "NOT INTERNET!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
