package com.hcmus.dreamers.foodmap;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.hcmus.dreamers.foodmap.AsyncTask.DownloadImageTask;
import com.hcmus.dreamers.foodmap.Model.Dish;
import com.hcmus.dreamers.foodmap.Model.Restaurant;

import java.util.List;

public class FavorRestListAdapter extends ArrayAdapter<Restaurant> {
    private Context context;
    private Restaurant[] restes;

    public FavorRestListAdapter(@NonNull Context context, int resource, @NonNull Restaurant[] objects) {
        super(context, resource, objects);
        this.context = context;
        this.restes = objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        LayoutInflater inflater = ((Activity) context).getLayoutInflater();
        View cell = inflater.inflate(R.layout.favor_rest_list, null);

        ImageView imgBackFavorRest = (ImageView) cell.findViewById(R.id.imgBackFavorRest);
        TextView txtFavorRestName = (TextView) cell.findViewById(R.id.txtFavorRestName);

        DownloadImageTask downloadImageTask = new DownloadImageTask( imgBackFavorRest, context);
        downloadImageTask.loadImageFromUrl(restes[position].getUrlImage());

        txtFavorRestName.setText(restes[position].getName());


        return cell;
    }

    @Override
    public int getCount() {
        return restes.length;
    }

    @Nullable
    @Override
    public Restaurant getItem(int position) {
        return restes[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
}
