package com.hcmus.dreamers.foodmap;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import android.location.LocationManager;
import android.net.Uri;
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

import com.facebook.AccessToken;
import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.gson.Gson;
import com.hcmus.dreamers.foodmap.AsyncTask.DoingTask;
import com.hcmus.dreamers.foodmap.AsyncTask.DownloadImageTask;
import com.hcmus.dreamers.foodmap.AsyncTask.TaskCompleteCallBack;
import com.hcmus.dreamers.foodmap.AsyncTask.TaskRequest;
import com.hcmus.dreamers.foodmap.Model.Catalog;
import com.hcmus.dreamers.foodmap.Model.Comment;
import com.hcmus.dreamers.foodmap.Model.Guest;
import com.hcmus.dreamers.foodmap.Model.Restaurant;
import com.hcmus.dreamers.foodmap.define.ConstantURL;
import com.hcmus.dreamers.foodmap.event.LocationChange;
import com.hcmus.dreamers.foodmap.event.MarkerClick;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.hcmus.dreamers.foodmap.common.GenerateRequest;
import com.hcmus.dreamers.foodmap.common.ResponseJSON;
import com.hcmus.dreamers.foodmap.common.SendRequest;
import com.hcmus.dreamers.foodmap.jsonapi.ParseJSON;
import com.hcmus.dreamers.foodmap.Model.Owner;

import org.json.JSONException;
import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.ItemizedOverlayWithFocus;
import org.osmdroid.views.overlay.OverlayItem;
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Request;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    
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
        navmenuToolbarInit();
        mMap.onResume();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        TaskRequest task = new TaskRequest();
//        task.setOnCompleteCallBack(new TaskCompleteCallBack() {
//            @Override
//            public void OnTaskComplete(Object response) {
//                Toast.makeText(MainActivity.this, response.toString(), Toast.LENGTH_LONG);
//            }
//        });
//
//        task.execute(new DoingTask(GenerateRequest.directionMap(new GeoPoint(10.0900, 11.0098), new GeoPoint(10.8347, 11.8374))));

        mMap = (MapView) findViewById(R.id.map);
        isPermissionOK = false;
        // setup map
        mapInit();
        navmenuToolbarInit();

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

        //debug
       /*ImageView img = (ImageView)findViewById(R.id.imgSearch);
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent intent = new Intent(MainActivity.this, RestaurantInfoActivity.class);
                    //Bundle bundle = intent.getExtras();
                    //bundle.putInt("RestID", 123);
                    intent.putExtra("RestID",123);

                    startActivity(intent);
                }catch (Exception e)
                {
                    Toast.makeText(getApplicationContext(),"error: Cannot open this restaurant",Toast.LENGTH_LONG).show();
                }
            }
        });*/

       //end debug
    }

    // thêm một marker vào map
    private ItemizedOverlayWithFocus<OverlayItem> addMarker(String title, String description, GeoPoint point){
        mapController.setZoom(17.0);
        markers.add(new OverlayItem(title, description, point)); // Lat/Lon decimal degrees
        // thêm sự kiện marker click
        ItemizedOverlayWithFocus<OverlayItem> mOverlay = new ItemizedOverlayWithFocus<OverlayItem>(MainActivity.this, markers, new MarkerClick(getApplicationContext()));
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

        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        boolean isLoggedIn = accessToken != null && !accessToken.isExpired();
        Owner owner = Owner.getInstance();
        if (isLoggedIn){
            initMenuLoginGuest();
        }
        else if (owner.getToken() != null){
            initMenuLoginOwner();
        }
        else{
            initMenuNotLogin();
        }
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

    void initMenuLoginGuest(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        // change header
        navigationMenu = (NavigationView)findViewById(R.id.nav_view);
        navigationMenu.removeHeaderView(navigationMenu.getHeaderView(0));
        navigationMenu.inflateHeaderView(R.layout.nav_header);

        Menu menu = navigationMenu.getMenu();
        menu.clear();
        getMenuInflater().inflate(R.menu.drawer_menu_guest, menu);

        View head = navigationMenu.getHeaderView(0);
        TextView txtName = (TextView)head.findViewById(R.id.txtName);
        TextView txtEmail = (TextView)head.findViewById(R.id.txtEmail);
        ImageView imgAvatar = (ImageView)head.findViewById(R.id.igvAvatar);

        Guest.getInstance().setName(user.getDisplayName());
        txtName.setText(user.getDisplayName());

        Guest.getInstance().setEmail(user.getEmail());
        txtEmail.setText(user.getEmail());

        Guest.getInstance().setUrlAvatar(user.getPhotoUrl());

        //DownloadImageTask taskDownload = new DownloadImageTask(imgAvatar);
        //taskDownload.execute(user.getPhotoUrl().getPath());
        DownloadImageTask taskDownload = new DownloadImageTask(imgAvatar, getApplicationContext());
        taskDownload.loadImageFromUrl(user.getPhotoUrl().getPath());

        navigationMenu.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                int id = menuItem.getItemId();
                switch (id){
                    case R.id.btnFavorite:
                        Log.d(TAG, "onClick: btnFavorite");
                        Toast.makeText(MainActivity.this, "onClick: btnFavorite", Toast.LENGTH_SHORT).show();
                        break;
                    case  R.id.btnFeedBack:
                        Log.d(TAG, "onClick: btnFeedBack");
                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(ConstantURL.LINKFORM));
                        startActivity(browserIntent);
                        break;
                    case  R.id.btnUpdate:
                        Log.d(TAG, "onClick: btnUpdate");
                        Toast.makeText(MainActivity.this, "onClick: btnUpdate", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.btnLogout:
                        Log.d(TAG, "onClick: btnLogout");
                        Toast.makeText(MainActivity.this, "onClick: btnLogout", Toast.LENGTH_SHORT).show();
                        LoginManager.getInstance().logOut();
                        initMenuNotLogin();
                        break;
                    case R.id.btnAbout:
                        Log.d(TAG, "onClick: btnAbout");
                        Toast.makeText(MainActivity.this, "onClick: btnAbout", Toast.LENGTH_SHORT).show();
                        break;
                }
                return true;
            }
        });
    }

    void initMenuNotLogin(){
        // change header
        navigationMenu = (NavigationView)findViewById(R.id.nav_view);
        navigationMenu.removeHeaderView(navigationMenu.getHeaderView(0));
        navigationMenu.inflateHeaderView(R.layout.nav_header_notlogin);

        View head = navigationMenu.getHeaderView(0);
        Button btnLogin = (Button)head.findViewById(R.id.btnNavDangNhap);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, LoginGuestActivity.class);
                startActivity(intent);
            }
        });


        Menu menu = navigationMenu.getMenu();
        menu.clear();
        getMenuInflater().inflate(R.menu.drawer_menu_notlogin, menu);

        navigationMenu.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                int id = menuItem.getItemId();
                switch (id){
                    case  R.id.btnFeedBack:
                        Log.d(TAG, "onClick: btnFeedBack");
                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(ConstantURL.LINKFORM));
                        startActivity(browserIntent);
                        break;
                    case  R.id.btnUpdate:
                        Log.d(TAG, "onClick: btnUpdate");
                        Toast.makeText(MainActivity.this, "onClick: btnUpdate", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.btnAbout:
                        Log.d(TAG, "onClick: btnAbout");
                        Toast.makeText(MainActivity.this, "onClick: btnAbout", Toast.LENGTH_SHORT).show();
                        break;
                }
                return true;
            }
        });

    }

    private void Goto_NinhShortcut() {
        Restaurant rest = new Restaurant();
        Gson gson = new Gson();
        Intent main_manageDish = new Intent(MainActivity.this,ManageDishActivity.class);
        main_manageDish.putExtra("restJSON",gson.toJson(rest));
        startActivity(main_manageDish);
    }

    void initMenuLoginOwner(){
// change header
        navigationMenu = (NavigationView)findViewById(R.id.nav_view);
        navigationMenu.removeHeaderView(navigationMenu.getHeaderView(0));
        navigationMenu.inflateHeaderView(R.layout.nav_header);

        Menu menu = navigationMenu.getMenu();
        menu.clear();
        getMenuInflater().inflate(R.menu.drawer_menu_guest, menu);

        View head = navigationMenu.getHeaderView(0);
        TextView txtName = (TextView)head.findViewById(R.id.txtName);
        TextView txtEmail = (TextView)head.findViewById(R.id.txtEmail);
        ImageView imgAvatar = (ImageView)head.findViewById(R.id.igvAvatar);

        Owner owner =  Owner.getInstance();
        txtName.setText(owner.getUsername());

        txtEmail.setText(owner.getEmail());

        if (owner.getRestaurant(0) != null && owner.getRestaurant(0).getUrlImage() != null)
        {
            //DownloadImageTask taskDownload = new DownloadImageTask(imgAvatar);
            //taskDownload.execute(owner.getRestaurant(0).getUrlImage());
            DownloadImageTask taskDownload = new DownloadImageTask(imgAvatar, getApplicationContext());
            taskDownload.loadImageFromUrl(owner.getRestaurant(0).getUrlImage());
        }

        navigationMenu.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                int id = menuItem.getItemId();
                switch (id){
                    case R.id.btnManager:
                        Log.d(TAG, "onClick: btnManager");
                        //Toast.makeText(MainActivity.this, "onClick: btnManager", Toast.LENGTH_SHORT).show();
                        Intent main_manageRest = new Intent(MainActivity.this,
                                ManageRestaurantActivity.class);
                        startActivity(main_manageRest);
                        break;
                    case  R.id.btnFeedBack:
                        Log.d(TAG, "onClick: btnFeedBack");
                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(ConstantURL.LINKFORM));
                        startActivity(browserIntent);
                        break;
                    case  R.id.btnUpdate:
                        Log.d(TAG, "onClick: btnUpdate");
                        Toast.makeText(MainActivity.this, "onClick: btnUpdate", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.btnLogout:
                        Log.d(TAG, "onClick: btnLogout");
                        LoginManager.getInstance().logOut();
                        initMenuNotLogin();
                        break;
                    case R.id.btnAbout:
                        Log.d(TAG, "onClick: btnAbout");
                        Toast.makeText(MainActivity.this, "onClick: btnAbout", Toast.LENGTH_SHORT).show();
                        break;
                }
                return true;
            }
        });
    }
}
