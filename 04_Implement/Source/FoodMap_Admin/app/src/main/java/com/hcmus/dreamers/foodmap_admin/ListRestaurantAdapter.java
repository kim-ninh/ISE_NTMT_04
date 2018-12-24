package com.hcmus.dreamers.foodmap_admin;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.hcmus.dreamers.foodmap_admin.model.Restaurant;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ListRestaurantAdapter extends ArrayAdapter<Restaurant> {

    private Context context;
    private int resource;
    private List<Restaurant> restaurants;

    private ImageView imvAvatar;
    private TextView txvName;
    private TextView txvOwner;
    private TextView txvAddress;

    public ListRestaurantAdapter(Context context, int resource,List<Restaurant> restaurants) {
        super(context, resource, restaurants);

        this.context = context;
        this.resource = resource;
        this.restaurants = restaurants;
    }


    @Override
    public int getCount() {
        return restaurants.size();
    }

    @Override
    public Restaurant getItem(int position) {
        return restaurants.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(resource, null);
        imvAvatar = view.findViewById(R.id.imvAvatar);
        txvName = view.findViewById(R.id.txvName);
        txvAddress = view.findViewById(R.id.txvAddress);
        txvOwner = view.findViewById(R.id.txvOwner);

        Restaurant restaurant = restaurants.get(position);

        Picasso.get().load(restaurant.getUrlImage()).into(imvAvatar);
        txvName.setText(restaurant.getName());
        txvAddress.setText(restaurant.getAddress());
        txvOwner.setText(restaurant.getOwnerUsername());

        return view;
    }
}
