package com.hcmus.dreamers.foodmap;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;

public class ImageAdapter extends BaseAdapter {
    private Context mContext;
    public ImageAdapter(Context context) {
        mContext = context;
    }

    @Override
    public int getCount() {
        return mDishThumbIds.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
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

        dishImage.setImageResource(mDishThumbIds[position]);
        return dishImage;
    }

    private Integer[] mDishThumbIds ={
            R.drawable.thumbnail_banh_trang_tron,
            R.drawable.thumbnail_banh_trang_tron,
            R.drawable.thumbnail_banh_trang_tron,
            R.drawable.thumbnail_banh_trang_tron,
            R.drawable.thumbnail_banh_trang_tron,
            R.drawable.thumbnail_banh_trang_tron,
            R.drawable.thumbnail_banh_trang_tron
    };
}
