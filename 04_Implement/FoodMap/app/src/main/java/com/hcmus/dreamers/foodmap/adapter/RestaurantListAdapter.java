package com.hcmus.dreamers.foodmap.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.hcmus.dreamers.foodmap.AsyncTask.DownloadImageTask;
import com.hcmus.dreamers.foodmap.Model.Restaurant;
import com.hcmus.dreamers.foodmap.R;
import com.hcmus.dreamers.foodmap.event.ClickListener;

import java.util.List;


public class RestaurantListAdapter extends RecyclerView.Adapter<RestaurantListAdapter.ViewHolderRestaurant> {
    private Context context;
    private int resource;
    List<Restaurant> restaurantList;
    private static ClickListener onClickListener;

    public RestaurantListAdapter(Context context, int resource, List<Restaurant> restaurantList) {
        super();
        this.context = context;
        this.resource = resource;
        this.restaurantList = restaurantList;
    }

    @NonNull
    @Override
    public ViewHolderRestaurant onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        ViewHolderRestaurant viewHolderRestaurant = new ViewHolderRestaurant(LayoutInflater.from(context).inflate(resource,viewGroup, false));

        return viewHolderRestaurant;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderRestaurant viewHolderRestaurant, int i) {
        Restaurant rest = restaurantList.get(i);
        viewHolderRestaurant.txtName.setText(rest.getName());
        DownloadImageTask task = new DownloadImageTask(viewHolderRestaurant.igvImage, context);
        task.loadImageFromUrl(rest.getUrlImage());
        if (!rest.isCheck())
            viewHolderRestaurant.txtName.setTextColor(context.getResources().getColor(R.color.redColor));
    }

    @Override
    public int getItemCount() {
        return restaurantList.size();
    }

    class ViewHolderRestaurant extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener{
        public ImageView igvImage;
        public TextView txtName;

        public ViewHolderRestaurant(@NonNull View itemView) {
            super(itemView);

            igvImage =(ImageView) itemView.findViewById(R.id.igv_image_rest);
            txtName = (TextView) itemView.findViewById(R.id.txtName);

            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (onClickListener != null)
                onClickListener.onItemClick(getAdapterPosition(),v);
        }

        @Override
        public boolean onLongClick(View v) {
            if (onClickListener != null)
                onClickListener.onItemLongClick(getAdapterPosition(),v);
            return false;
        }
    }

    public void setOnClickListener(ClickListener onClickListener){
        this.onClickListener = onClickListener;
    }
}
