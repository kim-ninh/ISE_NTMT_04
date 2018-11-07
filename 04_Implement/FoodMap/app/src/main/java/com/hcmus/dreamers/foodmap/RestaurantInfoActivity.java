package com.hcmus.dreamers.foodmap;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.share.Sharer;
import com.facebook.share.model.SharePhoto;
import com.facebook.share.model.SharePhotoContent;
import com.facebook.share.widget.ShareDialog;
import com.hcmus.dreamers.foodmap.AsyncTask.DownloadImageTask;
import com.hcmus.dreamers.foodmap.Model.Comment;
import com.hcmus.dreamers.foodmap.Model.Dish;
import com.hcmus.dreamers.foodmap.Model.Guest;
import com.hcmus.dreamers.foodmap.Model.Restaurant;
import com.hcmus.dreamers.foodmap.adapter.DishInfoListAdapter;
import com.hcmus.dreamers.foodmap.common.FoodMapApiManager;
import com.hcmus.dreamers.foodmap.common.FoodMapManager;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.osmdroid.util.GeoPoint;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.Inflater;

public class RestaurantInfoActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "RestAcitvity";
    CallbackManager callbackManager;
    ShareDialog shareDialog;

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
    LinearLayout lnrComment;
    LinearLayout lnrFavorite;
    LinearLayout lnrRate;
    LinearLayout lnrShare;
    LinearLayout lnrContact;
    Restaurant restaurant;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(this.getApplicationContext());
        setContentView(R.layout.activity_restaurant_info);

        //set header toolbar in the layout
        Toolbar toolbar = (Toolbar) findViewById(R.id.tlbRestInfo);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //init View
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
        lnrComment = (LinearLayout) findViewById(R.id.lnrComment);
        lnrFavorite = (LinearLayout)findViewById(R.id.lnrFavourite);
        lnrRate = (LinearLayout)findViewById(R.id.lnrRate);
        lnrShare = (LinearLayout)findViewById(R.id.lnrShare);
        lnrContact = (LinearLayout) findViewById(R.id.lnrContact);


        //get Restaurant
        Intent intent = this.getIntent();
        restaurant = (Restaurant) intent.getSerializableExtra("rest");
        if(restaurant == null)
        {
            Log.i(TAG,"can't get restaurant data");
            Toast.makeText(this,"can't get restaurant data", Toast.LENGTH_LONG).show();
        }

        else
        {
            //debug
            /*SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
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

            Dish dish;
            for(int i=0; i<10; i++)
            {
                dish = new Dish();
                dish.setName("supper chicken " + (i + 1));
                dish.setPrice(i*100 + 100);
                restaurant.getDishes().add(dish);
            }*/
            //endbug

            setLayoutInfo();

            //set phone call event
            PhoneStateListener phoneStateListener = new PhoneStateListener();
            TelephonyManager telephonyManager = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
            telephonyManager.listen(phoneStateListener, PhoneStateListener.LISTEN_CALL_STATE);


            lnrContact.setOnClickListener(this);
            lnrRate.setOnClickListener(this);
            lnrShare.setOnClickListener(this);
            lnrFavorite.setOnClickListener(this);
            lnrComment.setOnClickListener(this);
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
       try {
            DishInfoListAdapter dishInfoList = new DishInfoListAdapter(this, R.layout.row_dish_info, restaurant.getDishes());
            lstDish.setAdapter(dishInfoList);
            //make the list view don't have scroll
            justifyListViewHeightBasedOnChildren(lstDish);

        }catch (Exception e)
        {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.lnrCheckIn:
                break;
            case R.id.lnrComment:
                Intent intent = new Intent(RestaurantInfoActivity.this, CommentActivity.class);
                intent.putExtra("rest", restaurant);
                startActivity(intent);
                break;
            case R.id.lnrFavourite:
                //check login
                if(FoodMapApiManager.isGuestLogin())
                {
                    //kiem tra da ton tai trong ds yeu thich chua
                    if(!Guest.getInstance().getFavRestaurant().contains(restaurant))
                    {
                        Guest.getInstance().getFavRestaurant().add(restaurant);
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
                if(FoodMapApiManager.isGuestLogin()) {
                    View ratingLayout = getLayoutInflater().inflate(R.layout.dialog_rating, null);
                    final Dialog dialog = new Dialog(RestaurantInfoActivity.this);
                    dialog.setContentView(R.layout.dialog_rating);
                    dialog.setCanceledOnTouchOutside(true);
                    dialog.show();

                    final RatingBar rtbRate = (RatingBar) ratingLayout.findViewById(R.id.rtbRate);
                    Button btnRateSubmit = (Button) ratingLayout.findViewById(R.id.btnRateSubmit);
                    btnRateSubmit.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            restaurant.getRanks().put(Guest.getInstance().getEmail(), rtbRate.getNumStars());
                            dialog.dismiss();
                        }
                    });
                }
                else {
                    Toast.makeText(this, "You must login first", Toast.LENGTH_LONG).show();
                }

                break;
            case R.id.lnrShare:
                if(FoodMapApiManager.isGuestLogin()) {
                    //Init Facebook
                    callbackManager = CallbackManager.Factory.create();
                    shareDialog = new ShareDialog(this);

                    //Make the target
                    Target target = new Target() {
                        @Override
                        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                            SharePhoto sharePhoto = new SharePhoto.Builder()
                                    .setBitmap(bitmap)
                                    .build();

                            if (ShareDialog.canShow(SharePhotoContent.class)) {
                                SharePhotoContent content = new SharePhotoContent.Builder()
                                        .addPhoto(sharePhoto)
                                        .build();
                                shareDialog.show(content);
                            }
                        }

                        @Override
                        public void onBitmapFailed(Drawable errorDrawable) {

                        }

                        @Override
                        public void onPrepareLoad(Drawable placeHolderDrawable) {

                        }
                    };

                    shareDialog.registerCallback(callbackManager, new FacebookCallback<Sharer.Result>() {
                        @Override
                        public void onSuccess(Sharer.Result result) {
                            Toast.makeText(RestaurantInfoActivity.this, "success", Toast.LENGTH_LONG).show();
                        }

                        @Override
                        public void onCancel() {
                            Toast.makeText(RestaurantInfoActivity.this, "cancel", Toast.LENGTH_LONG).show();
                        }

                        @Override
                        public void onError(FacebookException error) {
                            Toast.makeText(RestaurantInfoActivity.this, error.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });

                    //load image on facebook
                    Picasso.with(getApplicationContext())
                            .load(restaurant.getUrlImage())
                            .into(target);
                }
                else
                {
                    Toast.makeText(this, "You must login first", Toast.LENGTH_LONG).show();
                }
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

    public void justifyListViewHeightBasedOnChildren (ListView listView) {

        ListAdapter adapter = listView.getAdapter();

        if (adapter == null) {
            return;
        }
        ViewGroup vg = listView;
        int totalHeight = 0;
        for (int i = 0; i < adapter.getCount(); i++) {
            View listItem = adapter.getView(i, null, vg);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams par = listView.getLayoutParams();
        par.height = totalHeight + (listView.getDividerHeight() * (adapter.getCount() - 1));
        listView.setLayoutParams(par);
        listView.requestLayout();
    }
}







