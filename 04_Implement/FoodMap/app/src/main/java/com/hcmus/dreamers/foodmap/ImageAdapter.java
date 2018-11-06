package com.hcmus.dreamers.foodmap;

import android.content.Context;
import android.net.Uri;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;

import java.util.List;

public class ImageAdapter extends BaseAdapter {
    private Context mContext;
    private List<Uri> imagesUri;
    public ImageAdapter(Context context, List<Uri> imagesUri) {
        mContext = context;
        this.imagesUri = imagesUri;
    }

    @Override
    public int getCount() {
        return imagesUri.size();
    }

    @Override
    public Object getItem(int position) {
        return imagesUri.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView dishImage;
        if (convertView == null){
            dishImage = new ImageView(mContext);
            int grid_size = mContext.getResources().getDimensionPixelSize(R.dimen.gridview_size);
            dishImage.setLayoutParams(new ViewGroup.LayoutParams(grid_size,grid_size));
            dishImage.setScaleType(ImageView.ScaleType.CENTER_CROP);

        }
        else
        {
            dishImage = (ImageView) convertView;
        }

        dishImage.setImageURI(imagesUri.get(position));
        return dishImage;
    }
}
