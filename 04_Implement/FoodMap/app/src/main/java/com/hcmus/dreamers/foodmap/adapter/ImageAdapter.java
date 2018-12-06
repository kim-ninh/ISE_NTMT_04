package com.hcmus.dreamers.foodmap.adapter;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.hcmus.dreamers.foodmap.AsyncTask.DownloadImageTask;
import com.hcmus.dreamers.foodmap.R;
import com.squareup.picasso.Callback;

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
        LayoutInflater inflater = ((Activity)mContext).getLayoutInflater();
        RelativeLayout cell =(RelativeLayout) inflater.inflate(R.layout.loading_image_cell, null);

        ImageView dishImage = cell.findViewById(R.id.imageView);
        final ProgressBar progressBar = cell.findViewById(R.id.progressBar);
        String imageUri = imagesUri.get(position).toString();

        // Đường dẫn luôn là public
        if (imageUri.matches("^(http|https)://.*"))
        {
            DownloadImageTask taskDownload = new DownloadImageTask(dishImage, mContext);

            progressBar.setVisibility(View.VISIBLE);
            progressBar.setIndeterminate(true);

            taskDownload.loadImageFromUrl(imageUri, new Callback() {
                @Override
                public void onSuccess() {
                    progressBar.setVisibility(View.INVISIBLE);
                }

                @Override
                public void onError(Exception e) {
                }
            });
        }
        return cell;
    }
}
