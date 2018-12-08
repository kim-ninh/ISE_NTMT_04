package com.hcmus.dreamers.foodmap.map;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;

import org.osmdroid.tileprovider.MapTileProviderBase;
import org.osmdroid.views.MapView;

import microsoft.mappoint.TileSystem;

public class ZoomLimitMapView extends MapView
{
    public ZoomLimitMapView(Context context, MapTileProviderBase tileProvider, Handler tileRequestCompleteHandler, AttributeSet attrs) {
        super(context, tileProvider, tileRequestCompleteHandler, attrs);
    }

    public ZoomLimitMapView(Context context, MapTileProviderBase tileProvider, Handler tileRequestCompleteHandler, AttributeSet attrs, boolean hardwareAccelerated) {
        super(context, tileProvider, tileRequestCompleteHandler, attrs, hardwareAccelerated);
    }

    public ZoomLimitMapView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ZoomLimitMapView(Context context) {
        super(context);
    }

    public ZoomLimitMapView(Context context, MapTileProviderBase aTileProvider) {
        super(context, aTileProvider);
    }

    public ZoomLimitMapView(Context context, MapTileProviderBase aTileProvider, Handler tileRequestCompleteHandler) {
        super(context, aTileProvider, tileRequestCompleteHandler);
    }

    @Override
    public double getMinZoomLevel() {
        return 4.0;
    }

    @Override
    public void scrollTo(int x, int y) {

        final int worldSize = TileSystem.MapSize(this.getZoomLevel());
        if(y < -worldSize/2) { // when over north pole
            y = 0; // scroll to north pole
        }else if(y + getHeight() >= worldSize) { // when over south pole
            y = worldSize - getHeight() - 1; // scroll to south pole
        }

        super.scrollTo(x, y);
    }

}