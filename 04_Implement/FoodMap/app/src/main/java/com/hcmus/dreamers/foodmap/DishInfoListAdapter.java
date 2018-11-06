package com.hcmus.dreamers.foodmap;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.hcmus.dreamers.foodmap.Model.Dish;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class DishInfoListAdapter extends ArrayAdapter<Dish>
{
    private  Context context;
    private List<Dish> dishes;

    public DishInfoListAdapter(@NonNull Context context, int resource, @NonNull List<Dish> dishes) {
        super(context, resource, dishes);

        this.context = context;
        this.dishes = dishes;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        LayoutInflater inflater = ((Activity) context).getLayoutInflater();
        View row = inflater.inflate(R.layout.row_dish_info, null);

        TextView lblDishName = (TextView) row.findViewById(R.id.lblDishName);
        TextView lblDisgPrice = (TextView) row.findViewById(R.id.lblDishPrice);
        ImageView icon = (ImageView) row.findViewById(R.id.dish_thumb);

        Dish dish = dishes.get(position);

        lblDishName.setText(dish.getName());
        lblDisgPrice.setText(Integer.toString(dish.getPrice()));
        

        return row;
    }

    @Override
    public int getCount() {
        return dishes.size();
    }

    @Nullable
    @Override
    public Dish getItem(int position) {
        return dishes.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
}
