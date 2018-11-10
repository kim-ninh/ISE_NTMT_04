package com.hcmus.dreamers.foodmap.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;

import com.hcmus.dreamers.foodmap.AsyncTask.DownloadImageTask;
import com.hcmus.dreamers.foodmap.R;

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
            dishImage.setPadding(5,5,5,5);
        }
        else
        {
            dishImage = (ImageView) convertView;
        }

        String imageUri = imagesUri.get(position).toString();

        // Đường dẫn luôn là public
        if (imageUri.matches("^(http|https)://.*"))
        {
            DownloadImageTask taskDownload = new DownloadImageTask(dishImage, mContext);
            taskDownload.loadImageFromUrl(imageUri);
        }

        return dishImage;
    }
}
