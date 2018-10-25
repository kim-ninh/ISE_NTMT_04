package com.hcmus.dreamers.foodmap;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.support.v7.widget.Toolbar;

import com.hcmus.dreamers.foodmap.event.LocationChange;
import com.hcmus.dreamers.foodmap.event.MarkerClick;

import android.widget.Toast;

import com.hcmus.dreamers.foodmap.common.GenerateRequest;
import com.hcmus.dreamers.foodmap.common.ResponseJSON;
import com.hcmus.dreamers.foodmap.common.SendRequest;
import com.hcmus.dreamers.foodmap.event.LocationChange;
import com.hcmus.dreamers.foodmap.event.MarkerClick;
import com.hcmus.dreamers.foodmap.jsonapi.ParseJSON;
import com.hcmus.dreamers.foodmap.model.Owner;

import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.ItemizedOverlayWithFocus;
import org.osmdroid.views.overlay.OverlayItem;
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private NavigationView navigationMenu;

    private MapView mMap;
    private MyLocationNewOverlay mLocationOverlay;
    private LocationManager mLocMgr;
    private  IMapController mapController;
    private boolean isPermissionOK;
    private ArrayList<OverlayItem> markers;

    private static final int PERMISSION_CODEREQUEST = 9001;

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
        setContentView(R.layout.activity_main);

        mMap = (MapView) findViewById(R.id.map);
        isPermissionOK = false;
        // setup map
        mapInit();
        navmenuToolbarInit();

        // debug
        Owner owner = Owner.getInstance();
        owner.setUsername("sdddd");
        owner.setPassword("aeaersa");
        owner.setPhoneNumber("029839843");
        owner.setEmail("dhsfh@gmail.com");
        owner.setName("Châu Hoàng Phúc");

        (new Test()).execute(owner);

        //end debug

        mMap = (MapView) findViewById(R.id.map);
        isPermissionOK = false;

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

        // cài đặt marker vị trí
        this.mLocationOverlay = new MyLocationNewOverlay(new GpsMyLocationProvider(MainActivity.this),mMap);
        Bitmap iconMyLocation = BitmapFactory.decodeResource(getResources(),R.drawable.ic_mylocation);
        mLocationOverlay.setPersonIcon(iconMyLocation);
        mapController.setCenter(this.mLocationOverlay.getMyLocation());
        // thêm marker vào
        mMap.getOverlays().add(this.mLocationOverlay);

        // check permission
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkPermission();
        }

        // cài đặt event location change
        if (!isPermissionOK)
            return;
        Context ctx = getApplicationContext();
        Configuration.getInstance().load(ctx, PreferenceManager.getDefaultSharedPreferences(ctx));
        //
        mLocMgr = (LocationManager) getSystemService(LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mLocMgr.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 100,
                new LocationChange(mMap, mLocationOverlay, mapController));
    }

    // thêm một marker vào map
    private ItemizedOverlayWithFocus<OverlayItem> addMarker(String title, String description, GeoPoint point){
        mapController.setZoom(17.0);
        markers.add(new OverlayItem(title, description, point)); // Lat/Lon decimal degrees
        // thêm sự kiện marker click
        ItemizedOverlayWithFocus<OverlayItem> mOverlay = new ItemizedOverlayWithFocus<OverlayItem>(MainActivity.this, markers, new MarkerClick());
        mOverlay.setFocusItemsOnTap(true);
        // thêm marker vào map
        mMap.getOverlays().add(mOverlay);
        mMap.invalidate();
        return mOverlay;
    }

    // xóa marker ra khỏi map
    private void deleteMarker(ItemizedOverlayWithFocus<OverlayItem> marker)
    {
        mMap.getOverlays().add(marker);
        mMap.invalidate();
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

        // cài đặt marker vị trí
        this.mLocationOverlay = new MyLocationNewOverlay(new GpsMyLocationProvider(MainActivity.this),mMap);
        Bitmap iconMyLocation = BitmapFactory.decodeResource(getResources(),R.drawable.ic_mylocation);
        mLocationOverlay.setPersonIcon(iconMyLocation);
        mapController.setCenter(this.mLocationOverlay.getMyLocation());
        // thêm marker vào
        mMap.getOverlays().add(this.mLocationOverlay);
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


    // navigation menu and toolbar init
    void navmenuToolbarInit(){
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);

        drawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,drawerLayout, toolbar, R.string.nav_open,R.string.nav_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        // change header
        navigationMenu = (NavigationView)findViewById(R.id.nav_view);
        navigationMenu.removeHeaderView(navigationMenu.getHeaderView(0));
        navigationMenu.inflateHeaderView(R.layout.nav_header);
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);

        }
        else {
            super.onBackPressed();
        }
    }

    class Test extends AsyncTask<Owner, Void , String> {

        @Override
        protected void onPostExecute(String s) {
            ResponseJSON responseJSON = ParseJSON.fromStringToResponeJSON(s);
            Toast.makeText(MainActivity.this, responseJSON.getMessage(), Toast.LENGTH_LONG).show();
        }

        @Override
        protected String doInBackground(Owner...owners) {

            Request request = null;
            try {
                request = GenerateRequest.createAccount(owners[0]);
                String response = SendRequest.send(request);
                return response;
            } catch (IOException e) {
                e.printStackTrace();
            }
            return "";
        }
    }
}
