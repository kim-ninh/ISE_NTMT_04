package com.hcmus.dreamers.foodmap.fragment;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
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
public class OrderListFragment extends Fragment implements AdapterView.OnItemLongClickListener {

    Restaurant restaurant;

    private ListView listOffer;
    private OrderListAdapter adapter;
    private List<Offer> offers;
    private int id_rest;

    Context context = null;
    LinearLayout rootLayout;

    public OrderListFragment() {
        // Required empty public constructor
    }

    public void setId_rest(int id_rest) {
        this.id_rest = id_rest;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    @Override
    public void onResume() {
        super.onResume();
        refreshData();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootLayout =(LinearLayout) inflater.inflate(R.layout.fragment_order_list, container, false);

        refferences();
        return rootLayout;
    }

    private void refferences(){
        listOffer = rootLayout.findViewById(R.id.list_order);
        listOffer.setOnItemLongClickListener(this);
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
                        adapter.notifyDataSetChanged();
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

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
        showConfirmDialog(position);
        return true;
    }


    private void showConfirmDialog(final int position){
        new AlertDialog.Builder(context)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("Xóa Đơn hàng")
                .setMessage("Bạn có muốn xóa đơn hàng này?")
                .setPositiveButton("Có", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        final Offer offer = (Offer) offers.get(position);
                        FoodMapApiManager.deleteOffer(offer.getId(), new TaskCompleteCallBack() {
                            @Override
                            public void OnTaskComplete(Object response) {
                                if((int)response == ConstantCODE.SUCCESS){
                                    offers.remove(position);
                                    adapter.notifyDataSetChanged();
                                    Toast.makeText(context, "Xóa Đơn hàng thành công!", Toast.LENGTH_SHORT).show();
                                }else if((int) response == ConstantCODE.NOTFOUND){
                                    Toast.makeText(context, "Lỗi xóa Đơn hàng không tồn tại, xin kiểm tra lại!", Toast.LENGTH_SHORT).show();
                                }else{
                                    Toast.makeText(context, "Không có kết nối internet, xin kiểm tra lại!", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }

                })
                .setNegativeButton("Không", null)
                .show();
    }
}
