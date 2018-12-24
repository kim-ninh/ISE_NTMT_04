package com.hcmus.dreamers.foodmap.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.hcmus.dreamers.foodmap.AsyncTask.DownloadImageTask;
import com.hcmus.dreamers.foodmap.Model.Dish;
import com.hcmus.dreamers.foodmap.R;

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
        View row = inflater.inflate(R.layout.adapter_dish_info_list, null);

        TextView lblDishName = (TextView) row.findViewById(R.id.lblDishName);
        TextView lblDisgPrice = (TextView) row.findViewById(R.id.lblDishPrice);
        ImageView icon = (ImageView) row.findViewById(R.id.dish_thumb);

        Dish dish = dishes.get(position);

        lblDishName.setText(dish.getName());
        lblDisgPrice.setText(Integer.toString(dish.getPrice()));


        //Kiểm tra xem đã có hình chưa? Nếu chưa thì lấy 1 hình đc chỉ sẵn
        DownloadImageTask taskDownload = new DownloadImageTask(icon, getContext());
        taskDownload.loadImageFromUrl(dish.getUrlImage());

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
