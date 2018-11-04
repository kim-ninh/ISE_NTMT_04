package com.hcmus.dreamers.foodmap.event;


import android.content.Context;
import android.content.Intent;


import com.hcmus.dreamers.foodmap.RestaurantInfoActivity;

import org.osmdroid.views.overlay.ItemizedIconOverlay;
import org.osmdroid.views.overlay.OverlayItem;

public class MarkerClick implements ItemizedIconOverlay.OnItemGestureListener<OverlayItem> {

    private Context context;

    public MarkerClick(Context context)
    {
        this.context = context;
    }

    @Override
    public boolean onItemSingleTapUp(int index, OverlayItem overlayItem) {

        context.startActivity(new Intent(context, RestaurantInfoActivity.class));
        return true;
    }

    @Override
    public boolean onItemLongPress(int index, OverlayItem overlayItem) {

        return false;
    }
}
