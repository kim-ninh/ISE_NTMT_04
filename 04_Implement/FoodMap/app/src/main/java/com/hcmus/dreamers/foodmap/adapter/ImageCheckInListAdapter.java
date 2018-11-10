package com.hcmus.dreamers.foodmap.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import java.util.List;
import com.hcmus.dreamers.foodmap.R;


public class ImageCheckInListAdapter extends ArrayAdapter<Bitmap> {
    private Context context;
    private List<Bitmap> bitmapList;
    private int resource;

    public ImageCheckInListAdapter(@NonNull Context context, int resource, @NonNull List<Bitmap> objects) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
        this.bitmapList = objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View cell = LayoutInflater.from(context).inflate(resource, null);

        ImageView imgCheckIn = (ImageView) cell.findViewById(R.id.imgCheckIn);

        imgCheckIn.setImageBitmap(bitmapList.get(position));

        return cell;
    }

    @Override
    public int getCount() {
        return bitmapList.size();
    }

    @Nullable
    @Override
    public Bitmap getItem(int position) {
        return bitmapList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
}
