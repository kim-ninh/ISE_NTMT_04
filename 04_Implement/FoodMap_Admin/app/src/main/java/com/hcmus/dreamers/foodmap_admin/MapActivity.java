package com.hcmus.dreamers.foodmap_admin;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.hcmus.dreamers.foodmap_admin.map.ZoomLimitMapView;

import org.osmdroid.api.IMapController;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.overlay.ItemizedIconOverlay;
import org.osmdroid.views.overlay.ItemizedOverlayWithFocus;
import org.osmdroid.views.overlay.OverlayItem;

import java.util.ArrayList;
import java.util.List;

public class MapActivity extends AppCompatActivity {

    private ZoomLimitMapView mapView;
    private IMapController mapController;
    private GeoPoint restPos;
    private Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        mapView = findViewById(R.id.map);
        mapView.setBuiltInZoomControls(true);
        mapView.setMultiTouchControls(true);
        if (Build.VERSION.SDK_INT >= 16)
            mapView.setHasTransientState(true);

        mapController = mapView.getController();
        mapController.setZoom(17.0);
        mapView.setTileSource(TileSourceFactory.MAPNIK);

        LoadData();
    }

    void LoadData(){
        Intent intent = getIntent();
        restPos = new GeoPoint(0f , 0f);
        restPos.setLatitude(intent.getDoubleExtra("lat", 0));
        restPos.setLongitude(intent.getDoubleExtra("lon", 0));

        if (restPos.getLatitude() == 0 && restPos.getLongitude() == 0)
            finish();
        addMarker("","", restPos);
    }

    private void addMarker(String title, String description, GeoPoint point){
        List<OverlayItem> markers = new ArrayList<>();
        markers.add(new OverlayItem(title, description, point)); // Lat/Lon decimal degrees
        // thêm sự kiện marker click
        ItemizedOverlayWithFocus<OverlayItem> mOverlay = new ItemizedOverlayWithFocus<OverlayItem>(MapActivity.this, markers, new ItemizedIconOverlay.OnItemGestureListener<OverlayItem>() {
            @Override
            public boolean onItemSingleTapUp(int i, OverlayItem overlayItem) {
                return false;
            }

            @Override
            public boolean onItemLongPress(int i, OverlayItem overlayItem) {
                return false;
            }
        });
        mOverlay.setFocusItemsOnTap(true);

        // thêm marker vào map
        mapView.getOverlays().add(mOverlay);
        mapView.invalidate();
        mapController.setCenter(point);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home){
            finish();
        }
        return false;
    }


}
