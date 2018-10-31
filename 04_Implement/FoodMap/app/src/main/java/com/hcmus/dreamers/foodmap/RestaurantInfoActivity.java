package com.hcmus.dreamers.foodmap;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.hcmus.dreamers.foodmap.Model.Restaurant;
import com.hcmus.dreamers.foodmap.AsyncTask.DownloadImageTask;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class RestaurantInfoActivity extends AppCompatActivity implements View.OnClickListener {

    TextView txtRestName;
    TextView txtStatus;
    TextView txtOpenTime;
    TextView txtLocation;
    TextView txtDescription;
    ImageView imgDescription;
    ListView lstDish;
    Restaurant restaurant = new Restaurant();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_info);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolBar1);
        setSupportActionBar(toolbar);

        txtRestName = (TextView) findViewById(R.id.txtResName);
        txtStatus = (TextView) findViewById(R.id.txtStatus);
        txtOpenTime = (TextView) findViewById(R.id.txtOpenTime);
        txtLocation = (TextView) findViewById(R.id.txtLocation);
        txtDescription = (TextView) findViewById(R.id.txtDescription);
        imgDescription = (ImageView) findViewById(R.id.imgDescription);
        lstDish = (ListView) findViewById(R.id.lstDish);


        restaurant.setPhoneNumber("0377389063");
        PhoneStateListener phoneStateListener = new PhoneStateListener();
        TelephonyManager telephonyManager = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
        telephonyManager.listen(phoneStateListener, PhoneStateListener.LISTEN_CALL_STATE);

        LinearLayout lnrContact = (LinearLayout) findViewById(R.id.lnrContact);

        lnrContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:" + restaurant.getPhoneNumber()));
                if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {

                    return;
                }
                startActivity(callIntent);
            }
        });

    }

    public void setRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
    }

    @SuppressLint("SetTextI18n")
    private void setLayoutInfo() {
        //set Description Image
        {
            new DownloadImageTask(imgDescription).execute(restaurant.getUrlImage());
        }

        //set Restaurant name
        txtRestName.setText(restaurant.getName());

        //Set a quality of comments, check in, saves, shares, rate


        //set Time and Status
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
        txtOpenTime.setText(simpleDateFormat.format(restaurant.getTimeOpen()) + " - " + simpleDateFormat.format(restaurant.getTimeClose()));

        Date date = Calendar.getInstance().getTime();
        try {
            date = simpleDateFormat.parse(date.toString());
            if (!restaurant.getTimeOpen().after(date) && !restaurant.getTimeClose().before(date)) {
                txtStatus.setText("OPENING");
                txtStatus.setTextColor(Color.GREEN);
            } else {
                txtStatus.setText("CLOSING");
                txtStatus.setTextColor(Color.RED);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        //set Restaurant address
        txtLocation.setText(restaurant.getAddress());

        //Set Restaurant description
        txtDescription.setText((restaurant.getDescription()));

        //Set Price range of restaurant


        //set Menu
        DishInfoList dishInfoList = new DishInfoList(this, R.layout.row_dish_info, restaurant.getDishes());
        lstDish.setAdapter(dishInfoList);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.lnrCheckIn:
                break;
            case R.id.lnrComment:
                break;
            case R.id.lnrFavourite:
                break;
            case R.id.lnrSave:
                break;
            case R.id.lnrShare:
                break;
            case R.id.lnrRate:
                break;
            case R.id.lnrContact:
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:" + restaurant.getPhoneNumber()));
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                startActivity(callIntent);
                break;
            case R.id.lnrMenu:
                break;
        }
    }
}







