package com.hcmus.dreamers.foodmap;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.LocationManager;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;

import com.hcmus.dreamers.foodmap.event.LocationChange;

import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.OverlayItem;
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;

import java.util.ArrayList;

public class ChooseLocationActivity extends AppCompatActivity implements View.OnClickListener {

    String address;
    GeoPoint geoPoint;
    private static final int PERMISSION_CODEREQUEST = 9001;

    AutoCompleteTextView atclPlaceSearch;
    ImageView igvDone;
    ImageView igvMyLocation;

    Toolbar toolbar;

    private MapView mMap;
    private MyLocationNewOverlay mLocationOverlay;
    private LocationManager mLocMgr;
    private IMapController mapController;
    private boolean isPermissionOK;
    private ArrayList<OverlayItem> markers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_location);

        mMap = (MapView)findViewById(R.id.map);
        isPermissionOK = false;
        mapInit();

        igvDone = (ImageView) findViewById(R.id.igv_done);
        igvDone.setOnClickListener(this);

        igvMyLocation = (ImageView)findViewById(R.id.igv_mylocation);
        igvMyLocation.setOnClickListener(this);

        atclPlaceSearch = (AutoCompleteTextView)findViewById(R.id.atclSearch);

        toolbar = (Toolbar)findViewById(R.id.choose_loaction_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getData();
    }

    void getData(){
        Intent intent = getIntent();
        address = intent.getStringExtra("address");
        if (address != null){
            atclPlaceSearch.setText(address);
        }
    }

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
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home){
            ChooseLocationActivity.this.finish();
        }
        return true;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        if (id == R.id.igv_done){
            Intent intent = new Intent();
            intent.putExtra("lat", geoPoint.getLatitude());
            intent.putExtra("lon", geoPoint.getLongitude());
            intent.putExtra("address", address);
            setResult(Activity.RESULT_OK, intent);

            ChooseLocationActivity.this.finish();
        }
        else if (id == R.id.igv_mylocation) {
            mapController.setZoom(17.0);
            moveCamera(mLocationOverlay.getMyLocation());
        }
    }

    // kiểm tra permission
    @TargetApi(Build.VERSION_CODES.M)
    @RequiresApi(api = Build.VERSION_CODES.M)
    void checkPermission(){
        isPermissionOK = true;
        String[] permissions = { Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(permissions,PERMISSION_CODEREQUEST);
            isPermissionOK = false;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        isPermissionOK = true;
        for (int i = 0; i <grantResults.length;i++)
        {
            if (grantResults[i] != PackageManager.PERMISSION_GRANTED)
            {
                isPermissionOK = false;
                break;
            }
        }
    }


    private void mapInit()
    {
        // cài đặt map
        mMap.setBuiltInZoomControls(true);
        mMap.setMultiTouchControls(true);
        if (Build.VERSION.SDK_INT >= 16)
            mMap.setHasTransientState(true);

        mapController = mMap.getController();
        mapController.setZoom(17.0);
        mMap = (MapView) findViewById(R.id.map);
        mMap.setTileSource(TileSourceFactory.MAPNIK);

        //list marker
        markers = new ArrayList<OverlayItem>();

        // check permission
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkPermission();
        }

        // cài đặt event location change
        if (!isPermissionOK)
            return;

        // cài đặt marker vị trí
        this.mLocationOverlay = new MyLocationNewOverlay(new GpsMyLocationProvider(ChooseLocationActivity.this),mMap);
        Bitmap iconMyLocation = BitmapFactory.decodeResource(getResources(),R.drawable.ic_mylocation);
        mLocationOverlay.setPersonIcon(iconMyLocation);
        mapController.setCenter(this.mLocationOverlay.getMyLocation());
        // thêm marker vào
        mMap.getOverlays().add(this.mLocationOverlay);


        Context ctx = getApplicationContext();
        Configuration.getInstance().load(ctx, PreferenceManager.getDefaultSharedPreferences(ctx));

        mLocMgr = (LocationManager) getSystemService(LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mLocMgr.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 100,
                new LocationChange(mMap, mLocationOverlay, mapController));
    }

    private void moveCamera(GeoPoint point){
        mapController.setCenter(point);
    }
}
