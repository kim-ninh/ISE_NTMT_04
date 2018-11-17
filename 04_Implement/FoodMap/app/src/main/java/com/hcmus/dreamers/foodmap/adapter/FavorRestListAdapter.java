package com.hcmus.dreamers.foodmap.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
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

        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(350, 350);

        imgBackFavorRest.setLayoutParams(layoutParams);
        imgBackFavorRest.setScaleType(ImageView.ScaleType.CENTER_CROP);

        DownloadImageTask downloadImageTask = new DownloadImageTask( imgBackFavorRest, context);
        downloadImageTask.loadImageFromUrl(restes.get(position).getUrlImage());

        txtFavorRestName.setText(restes.get(position).getName());

        Animation animation = AnimationUtils.loadAnimation(context, R.anim.scale_list);
        cell.startAnimation(animation);
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
