package com.hcmus.dreamers.foodmap.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
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

        View cell = LayoutInflater.from(context).inflate(R.layout.adapter_image_check_in_list, null);

        ImageView imgCheckIn = (ImageView) cell.findViewById(R.id.imgCheckIn);

        imgCheckIn.setLayoutParams(new FrameLayout.LayoutParams(350,350));

        BitmapDrawable bitmapDrawable = new BitmapDrawable(context.getResources(), bitmapList.get(position));
        imgCheckIn.setBackgroundDrawable(bitmapDrawable);

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
