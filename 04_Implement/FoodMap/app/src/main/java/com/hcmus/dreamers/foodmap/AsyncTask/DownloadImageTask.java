package com.hcmus.dreamers.foodmap.AsyncTask;

import android.content.Context;
import android.widget.ImageView;

import com.hcmus.dreamers.foodmap.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

public class DownloadImageTask {
    private ImageView imageView;
    private Context context;

    public DownloadImageTask(ImageView imageView, Context context)
    {
        this.imageView = imageView;
        this.context = context;
    }

    public void loadImageFromUrl(String url)
    {
        Picasso.with(context).load(url)
                .placeholder(context.getResources().getDrawable(R.mipmap.ic_launcher))
                .error(context.getResources().getDrawable(R.mipmap.ic_launcher))
                .into(imageView, new Callback() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onError() {

                    }
                });
    }

}
