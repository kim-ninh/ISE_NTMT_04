package com.hcmus.dreamers.foodmap.map;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.DisplayMetrics;

import org.osmdroid.tileprovider.MapTileProviderBase;
import org.osmdroid.util.BoundingBox;
import org.osmdroid.util.TileSystem;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.ScaleBarOverlay;


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

    public void initZoomLimit() {
        this.setBuiltInZoomControls(true);
        this.setMultiTouchControls(true);
        this.setMinZoomLevel(3.0);
        this.setMaxZoomLevel(21.0);
        this.setVerticalMapRepetitionEnabled(false);                //Không lặp bản đồ theo chiều dọc
        this.setScrollableAreaLimitDouble(new BoundingBox(
                TileSystem.MaxLatitude, TileSystem.MaxLongitude,
                TileSystem.MinLatitude, TileSystem.MinLongitude
        ));                                                         //Giới hạn không gian vuốt
        this.setBuiltInZoomControls(false);
        this.setTilesScaledToDpi(true);
    }

    public void initScaleBar() {
        final Context context = this.getContext();
        final DisplayMetrics dm = context.getResources().getDisplayMetrics();

        ScaleBarOverlay scaleBarOverlay = new ScaleBarOverlay(this);
        scaleBarOverlay.setTextSize(30.0F);
        scaleBarOverlay.setMaxLength(1.0F);
        scaleBarOverlay.setAlignBottom(true);
        scaleBarOverlay.setUnitsOfMeasure(ScaleBarOverlay.UnitsOfMeasure.metric);
        this.getOverlayManager().add(scaleBarOverlay);
    }
}