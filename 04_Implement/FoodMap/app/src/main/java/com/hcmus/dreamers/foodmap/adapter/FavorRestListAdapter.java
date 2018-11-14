package com.hcmus.dreamers.foodmap.adapter;

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
import com.hcmus.dreamers.foodmap.Model.Restaurant;
import com.hcmus.dreamers.foodmap.R;
import com.hcmus.dreamers.foodmap.View.GridViewItem;

import java.util.List;

public class FavorRestListAdapter extends ArrayAdapter<Restaurant> {
    private Context context;
    private List<Restaurant> restes;
    private int resource;

    public FavorRestListAdapter(@NonNull Context context, int resource, @NonNull List<Restaurant> objects) {
        super(context, resource, objects);
        this.context = context;
        this.restes = objects;
        this.resource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View cell = LayoutInflater.from(context).inflate(this.resource, null);

        ImageView imgBackFavorRest = (ImageView) cell.findViewById(R.id.imgBackFavorRest);
        TextView txtFavorRestName = (TextView) cell.findViewById(R.id.txtFavorRestName);
        if (convertView == null) {
            // new image in GridView formatted to:
            // 100x75 pixels (its actual size)
            // center-cropped, and 5dp padding all around
            imgBackFavorRest.setLayoutParams(new GridViewItem.LayoutParams(100, 75));
            imgBackFavorRest.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imgBackFavorRest.setPadding(5, 5, 5, 5);
        } else {
            imgBackFavorRest = (ImageView) convertView;
        }

        DownloadImageTask downloadImageTask = new DownloadImageTask( imgBackFavorRest, context);
        downloadImageTask.loadImageFromUrl(restes.get(position).getUrlImage());

        txtFavorRestName.setText(restes.get(position).getName());

        return cell;
    }

    @Override
    public int getCount() {
        return restes.size();
    }

    @Nullable
    @Override
    public Restaurant getItem(int position) {
        return restes.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
}
