package com.hcmus.dreamers.foodmap;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Toast;

import com.hcmus.dreamers.foodmap.AsyncTask.UpdateRoadTask;
import com.hcmus.dreamers.foodmap.event.LocationChange;

import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.ItemizedIconOverlay;
import org.osmdroid.views.overlay.ItemizedOverlayWithFocus;
import org.osmdroid.views.overlay.OverlayItem;
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;

import java.io.File;
import java.util.ArrayList;

public class MapActivity extends AppCompatActivity {

    MapView mMap;
    private MyLocationNewOverlay mLocationOverlay;
    private LocationManager mLocMgr;
    private IMapController mapController;
    private GeoPoint startPoint;
    private GeoPoint endPoint;
    private ArrayList<OverlayItem> markers;

    @Override
    protected void onPause() {
        super.onPause();
        mMap.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mMap.onResume();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        //set header toolbar in the layout
        Toolbar toolbar = (Toolbar) findViewById(R.id.tlbFindWayMap);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //list marker
        markers = new ArrayList<OverlayItem>();

        //Initialize map
        mapInit();

        //Get data (endPoint) from Intent
        Intent intent = this.getIntent();
        endPoint = (GeoPoint) intent.getSerializableExtra("endPoint");
        if(endPoint == null)
        {
            Toast.makeText(this,"Can't get data", Toast.LENGTH_SHORT).show();
        }
        else {
            //get start point
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            Location location = mLocMgr.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            double lat = location.getLatitude();
            double lon = location.getLongitude();

            startPoint = new GeoPoint(lat, lon);

            //add marker of end point
            addMarker("title","description",endPoint);

            ArrayList<GeoPoint> pointsList = new ArrayList<GeoPoint>();
            pointsList.add(startPoint);
            pointsList.add(endPoint);

            //Display the paths between 2 points
            {new UpdateRoadTask(getApplicationContext(), mMap).execute(pointsList);}
        }


    }
    private void mapInit()
    {
        //

        Context ctx = getApplicationContext();
        Configuration.getInstance().load(ctx, PreferenceManager.getDefaultSharedPreferences(ctx));
        Configuration.getInstance().setUserAgentValue(getPackageName());

        Configuration.getInstance().setOsmdroidBasePath(new File(Environment.getExternalStorageDirectory(), "osmdroid"));
        Configuration.getInstance().setOsmdroidTileCache(new File(Environment.getExternalStorageDirectory(), "osmdroid/tiles"));


        // cài đặt map
        mMap = (MapView) findViewById(R.id.FindWayMap);
        mMap.setBuiltInZoomControls(true);
        mMap.setMultiTouchControls(true);
        if (Build.VERSION.SDK_INT >= 16)
            mMap.setHasTransientState(true);

        mapController = mMap.getController();
        mapController.setZoom(17.0);
        mMap.setTileSource(TileSourceFactory.MAPNIK);

        this.mLocationOverlay = new MyLocationNewOverlay(new GpsMyLocationProvider(MapActivity.this),mMap);
        mLocationOverlay.enableMyLocation();

        mapController.setCenter(this.mLocationOverlay.getMyLocation());
        mMap.getOverlays().add(this.mLocationOverlay);


        mLocMgr = (LocationManager) getSystemService(LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mLocMgr.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 100,
                new LocationChange(mMap, mLocationOverlay, mapController));

    }

    private ItemizedOverlayWithFocus<OverlayItem> addMarker(String title, String description, GeoPoint point){

        markers.clear();
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
        mMap.getOverlays().add(mOverlay);
        mMap.invalidate();
        return mOverlay;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
