package com.hcmus.dreamers.foodmap;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
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
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.hcmus.dreamers.foodmap.AsyncTask.DownloadImageTask;
import com.hcmus.dreamers.foodmap.Model.Comment;
import com.hcmus.dreamers.foodmap.Model.Guest;
import com.hcmus.dreamers.foodmap.Model.Restaurant;
import com.hcmus.dreamers.foodmap.common.FoodMapApiManager;
import com.hcmus.dreamers.foodmap.common.FoodMapManager;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.Inflater;

public class RestaurantInfoActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "RestAcitvity";
    TextView txtRestName;

    TextView txtNCheckIn;
    TextView txtNComment;
    TextView txtNFavorite;
    TextView txtNShare;
    TextView txtNRate;

    TextView txtStatus;
    TextView txtOpenTime;

    TextView txtLocation;
    TextView txtDescription;

    ImageView imgDescription;
    ListView lstDish;
    LinearLayout lnrFavorite;
    LinearLayout lnrRate;
    LinearLayout lnrContact;
    Restaurant restaurant = new Restaurant();
    Guest guest = Guest.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_info);

        //set header toolbar in the layout
        Toolbar toolbar = (Toolbar) findViewById(R.id.tlbRestInfo);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        txtRestName = (TextView) findViewById(R.id.txtRestName);

        txtNCheckIn = (TextView) findViewById(R.id.txtNCheckIn);
        txtNComment = (TextView) findViewById(R.id.txtNComment);
        txtNFavorite = (TextView) findViewById(R.id.txtNFavorite);
        txtNShare = (TextView) findViewById(R.id.txtNShare);
        txtNRate = (TextView) findViewById(R.id.txtNRate);

        txtStatus = (TextView) findViewById(R.id.txtStatus);
        txtOpenTime = (TextView) findViewById(R.id.txtOpenTime);
        txtLocation = (TextView) findViewById(R.id.txtLocation);
        txtDescription = (TextView) findViewById(R.id.txtDescription);
        imgDescription = (ImageView) findViewById(R.id.imgDescription);
        lstDish = (ListView) findViewById(R.id.lstDish);

        lnrFavorite = (LinearLayout)findViewById(R.id.lnrFavourite);
        lnrRate = (LinearLayout)findViewById(R.id.lnrRate);
        lnrContact = (LinearLayout) findViewById(R.id.lnrContact);

        //get Restaurant
        Intent intent = this.getIntent();
        int RestID = intent.getIntExtra("RestID", -1);
        if(RestID == -1)
        {
            Log.i(TAG,"can't get restaurant data");
            Toast.makeText(this,"can't get restaurant data", Toast.LENGTH_LONG).show();
        }
        else
        {
            //get data
            //restaurant = FoodMapManager.findRestaurant(RestID);

            //debug
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
            try {
                restaurant.setTimeOpen(simpleDateFormat.parse("08:00"));
                restaurant.setTimeClose(simpleDateFormat.parse("22:00"));
            } catch (ParseException e) {
                Log.d("Time",e.toString());
                e.printStackTrace();
            }
            restaurant.setnFavorites(10);
            restaurant.setDescription("do an vat abc");
            restaurant.setUrlImage("https://i.pinimg.com/236x/82/fa/8a/82fa8a8d0abac9e28614df1f5c45efeb.jpg");
            restaurant.setName("sao bang lanh gia");
            restaurant.setPhoneNumber("0377389063");
            restaurant.setAddress("227 Nguyen Van Cu");
            //endbug

            setLayoutInfo();

            //set phone call event
            PhoneStateListener phoneStateListener = new PhoneStateListener();
            TelephonyManager telephonyManager = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
            telephonyManager.listen(phoneStateListener, PhoneStateListener.LISTEN_CALL_STATE);


            lnrContact.setOnClickListener(this);
            lnrRate.setOnClickListener(this);
            lnrFavorite.setOnClickListener(this);
        }

    }

    //set click event for toolbar
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

    @SuppressLint({"SetTextI18n", "DefaultLocale"})
    private void setLayoutInfo() {
        //set Description Image
        DownloadImageTask taskDownload = new DownloadImageTask(imgDescription, getApplicationContext());
        taskDownload.loadImageFromUrl(restaurant.getUrlImage());

        //set Restaurant name
        txtRestName.setText(restaurant.getName());

        //Set a number of  check-in


        // Set a number of comments
        try {
            txtNComment.setText(Integer.toString(restaurant.getComments().size()));
        }catch (Exception e)
        {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
        }

        // Set a number of favorites
        txtNFavorite.setText(Integer.toString(restaurant.getnFavorites()));

        //Set a number of shares


        //Set a number of rates
        try {
            double average = 0;

            for(Map.Entry<String, Integer> kvp : restaurant.getRanks().entrySet()) {
                average += kvp.getValue();
            }

            if(average != 0)
            {
                average /= restaurant.getRanks().size();
            }

            txtNRate.setText(String.format("%.1f",average));
        }catch (Exception e)
        {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
        }


        //set Time and Status
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
        txtOpenTime.setText(simpleDateFormat.format(restaurant.getTimeOpen()) + " - " + simpleDateFormat.format(restaurant.getTimeClose()));

        Date date = Calendar.getInstance().getTime();
        Date start = Calendar.getInstance().getTime();
        Date end = Calendar.getInstance().getTime();
        start.setHours(restaurant.getTimeOpen().getHours());
        start.setMinutes(restaurant.getTimeOpen().getMinutes());
        end.setHours(restaurant.getTimeClose().getHours());
        end.setMinutes(restaurant.getTimeClose().getMinutes());

        if (!start.after(date) && !end.before(date))
        {
            txtStatus.setText("OPENING");
            txtStatus.setTextColor(Color.GREEN);
        } else {
            txtStatus.setText("CLOSING");
            txtStatus.setTextColor(Color.RED);
        }


        //set Restaurant address
        txtLocation.setText(restaurant.getAddress());

        //Set Restaurant description
        txtDescription.setText((restaurant.getDescription()));

        //Set Price range of restaurant


        //set Menu
        /*DishInfoListAdapter dishInfoList = new DishInfoListAdapter(this, R.layout.row_dish_info, restaurant.getDishes());
        lstDish.setAdapter(dishInfoList);*/
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.lnrCheckIn:
                break;
            case R.id.lnrComment:
                break;
            case R.id.lnrFavourite:
                //check login
                if(true)
                {
                    //kiem tra da ton tai trong ds yeu thich chua
                    if(!guest.getFavRestaurant().contains(restaurant))
                    {
                        guest.getFavRestaurant().add(restaurant);
                        restaurant.setnFavorites(restaurant.getnFavorites() + 1);
                    }
                    Toast.makeText(this, "This restaurant has been your favorites", Toast.LENGTH_LONG).show();
                }
                else
                {
                    Toast.makeText(this, "You must login first", Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.lnrRate:
                //check login
                if(true) {
                    View ratingLayout = getLayoutInflater().inflate(R.layout.dialog_rating, null);
                    final Dialog dialog = new Dialog(RestaurantInfoActivity.this);
                    dialog.setContentView(R.layout.dialog_rating);
                    dialog.setCancelable(true);
                    dialog.show();

                    final RatingBar rtbRate = (RatingBar) ratingLayout.findViewById(R.id.rtbRate);
                    Button btnRateSubmit = (Button) ratingLayout.findViewById(R.id.btnRateSubmit);
                    btnRateSubmit.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            restaurant.getRanks().put(guest.getEmail(), rtbRate.getNumStars());
                            dialog.dismiss();
                        }
                    });
                }
                else {
                    Toast.makeText(this, "You must login first", Toast.LENGTH_LONG).show();
                }

                break;
            case R.id.lnrShare:
                break;
            case R.id.lnrContact:
                Toast.makeText(this, restaurant.getPhoneNumber(), Toast.LENGTH_LONG).show();
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







