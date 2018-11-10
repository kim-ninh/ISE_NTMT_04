package com.hcmus.dreamers.foodmap;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;

import android.support.v7.widget.Toolbar;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.MenuItem;
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

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookAuthorizationException;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.share.Sharer;
import com.facebook.share.model.ShareHashtag;
import com.facebook.share.model.SharePhoto;
import com.facebook.share.model.SharePhotoContent;
import com.facebook.share.widget.ShareDialog;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.hcmus.dreamers.foodmap.AsyncTask.DownloadImageTask;
import com.hcmus.dreamers.foodmap.AsyncTask.TaskCompleteCallBack;
import com.hcmus.dreamers.foodmap.Model.Guest;
import com.hcmus.dreamers.foodmap.Model.Restaurant;
import com.hcmus.dreamers.foodmap.adapter.DishInfoListAdapter;
import com.hcmus.dreamers.foodmap.common.FoodMapApiManager;
import com.hcmus.dreamers.foodmap.common.FoodMapManager;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;


public class LoginGuestActivity extends AppCompatActivity {

    private static final String TAG = "LoginGuestActivity";
    private static final int LOGIN_OWNER_REQUEST = 1222;
    private static final int REGISTER_OWNER_REQUEST = 1333;
    CallbackManager callbackManager;
    FirebaseAuth mAuth;

    Button btnLoginFb;
    Button btnLoginOwner;
    Button btnRegisterOwner;
    Toolbar toolbar;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_guest);

        btnLoginFb = (Button)findViewById(R.id.btnFacebookLogin);
        btnLoginOwner = (Button)findViewById(R.id.btnOwnerLogin);
        btnLoginOwner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginGuestActivity.this, LoginOwnerActivity.class);
                startActivityForResult(intent, LOGIN_OWNER_REQUEST);
            }
        });

        btnRegisterOwner = (Button)findViewById(R.id.btnOwnerRegister);
        btnRegisterOwner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginGuestActivity.this, RegisterOwnerActivity.class);
                startActivityForResult(intent, REGISTER_OWNER_REQUEST);
            }
        });

        // setup toolbar
        toolbar = (Toolbar)findViewById(R.id.login_guest_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        // init
        mAuth = FirebaseAuth.getInstance();
        // btnFaceBook Login
        callbackManager = CallbackManager.Factory.create();
        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d(TAG, "onSuccess: Login OK");
                handleFacebookAccessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                Log.d(TAG, "facebook:onCancel");
            }

            @Override
            public void onError(FacebookException error) {
                Log.d(TAG, "facebook: onError"+ error.getMessage());
                if (error instanceof FacebookAuthorizationException) {
                    if (AccessToken.getCurrentAccessToken() != null) {
                        LoginManager.getInstance().logOut();
                    }
                }
            }
        });

        LoginManager.getInstance().logOut();

        btnLoginFb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginManager.getInstance().logInWithReadPermissions(LoginGuestActivity.this, Arrays.asList("email", "public_profile"));
            }
        });
    }


    private void handleFacebookAccessToken(AccessToken token) {
        Log.d(TAG, "handleFacebookAccessToken:" + token);

        final ProgressDialog progressDialog = new ProgressDialog(LoginGuestActivity.this);
        progressDialog.setTitle("Checking...");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");

                            FirebaseUser user = mAuth.getCurrentUser();
                            ///Todo
                            Guest.getInstance().setName(user.getDisplayName());
                            Guest.getInstance().setEmail(user.getEmail());
                            Guest.getInstance().setUrlAvatar(user.getPhotoUrl());
                            FoodMapApiManager.addGuest(Guest.getInstance(), new TaskCompleteCallBack() {
                                @Override
                                public void OnTaskComplete(Object response) {
                                    if ((int)response == FoodMapApiManager.SUCCESS){
                                        progressDialog.dismiss();
                                        // đóng activity login guest
                                        LoginGuestActivity.this.finish();
                                    }
                                }
                            });
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());

                            Toast.makeText(LoginGuestActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK){
            if (requestCode == LOGIN_OWNER_REQUEST){
                if (data.getBooleanExtra("isLogin", false) == true)
                    LoginGuestActivity.this.finish();
            }
            else if (requestCode == REGISTER_OWNER_REQUEST){
                if (data.getBooleanExtra("isRegister", false) == true){
                    Intent intent = new Intent(LoginGuestActivity.this, LoginOwnerActivity.class);
                    startActivityForResult(intent, LOGIN_OWNER_REQUEST);
                }
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        switch (itemId) {
            case android.R.id.home:
                LoginGuestActivity.this.finish();
                break;
        }
        return true;
    }

    public static class RestaurantInfoActivity extends AppCompatActivity implements View.OnClickListener {

        private static final String TAG = "RestAcitvity";
        private static final int CM_ID = 1009;

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
        LinearLayout lnrCheckIn;
        LinearLayout lnrComment;
        LinearLayout lnrFavorite;
        LinearLayout lnrRate;
        LinearLayout lnrShare;
        Button btnContact;
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
            lnrCheckIn = (LinearLayout) findViewById(R.id.lnrCheckIn);
            lnrComment = (LinearLayout) findViewById(R.id.lnrComment);
            lnrFavorite = (LinearLayout)findViewById(R.id.lnrFavourite);
            lnrRate = (LinearLayout)findViewById(R.id.lnrRate);
            lnrShare = (LinearLayout)findViewById(R.id.lnrShare);
            btnContact = (Button) findViewById(R.id.btnContact);


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
                setLayoutInfo();

                //set phone call event
                PhoneStateListener phoneStateListener = new PhoneStateListener();
                TelephonyManager telephonyManager = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
                telephonyManager.listen(phoneStateListener, PhoneStateListener.LISTEN_CALL_STATE);


                btnContact.setOnClickListener(this);
                lnrRate.setOnClickListener(this);
                lnrShare.setOnClickListener(this);
                lnrFavorite.setOnClickListener(this);
                lnrComment.setOnClickListener(this);
                lnrCheckIn.setOnClickListener(this);
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
                DishInfoListAdapter dishInfoList = new DishInfoListAdapter(this, R.layout.adapter_dish_info_list, restaurant.getDishes());
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
                    clickOnCheckInEvent();
                    break;
                case R.id.lnrComment:
                    clickOnCommentEvent();
                    break;
                case R.id.lnrFavourite:
                    clickOnFavoriteEvent();
                    break;
                case R.id.lnrRate:
                    clickOnRateEvent();
                    break;
                case R.id.lnrShare:
                    clickOnShareEvent();
                    break;
                case R.id.btnContact:
                    clickOnContactEvent();
                    break;
                case R.id.lnrMenu:
                    break;
            }
        }

        private void clickOnCheckInEvent() {
            startActivity(new Intent(RestaurantInfoActivity.this, CheckInActivity.class));
        }

        private void clickOnCommentEvent(){
            Intent intent = new Intent(RestaurantInfoActivity.this, CommentActivity.class);
            intent.putExtra("rest", restaurant);
            startActivityForResult(intent, CM_ID);
        }

        private void clickOnFavoriteEvent(){
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
        }

        private void clickOnRateEvent() {
            //check login
            if(FoodMapApiManager.isGuestLogin()) {
                View ratingLayout = getLayoutInflater().inflate(R.layout.dialog_rating, null);
                //final AlertDialog dialog = new AlertDialog(RestaurantInfoActivity.this);
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
        }

        private void clickOnShareEvent(){
            if(FoodMapApiManager.isGuestLogin()) {

                //Init Facebook
                callbackManager = CallbackManager.Factory.create();
                shareDialog = new ShareDialog(this);

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

                //Make the target
                Target target = new Target() {
                    @Override
                    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                        SharePhoto sharePhoto = new SharePhoto.Builder()
                                .setBitmap(bitmap)
                                .build();

                        ShareHashtag shareHashtag = new ShareHashtag.Builder()
                                .setHashtag("#whatever")
                                .build();

                        SharePhotoContent shareContent = new SharePhotoContent.Builder()
                                .addPhoto(sharePhoto)
                                .setShareHashtag(shareHashtag)
                                .build();


                        if (ShareDialog.canShow(SharePhotoContent.class)) {
                            shareDialog.show(RestaurantInfoActivity.this, shareContent);

                        }
                    }

                    @Override
                    public void onBitmapFailed(Drawable errorDrawable) {

                    }

                    @Override
                    public void onPrepareLoad(Drawable placeHolderDrawable) {

                    }
                };



                //load image on facebook
                Picasso.with(getApplicationContext())
                        .load(restaurant.getUrlImage())
                        .into(target);
            }
            else
            {
                Toast.makeText(this, "You must login first", Toast.LENGTH_LONG).show();
            }
        }

        private void clickOnContactEvent(){
            Toast.makeText(this, restaurant.getPhoneNumber(), Toast.LENGTH_LONG).show();
            Intent callIntent = new Intent(Intent.ACTION_CALL);
            callIntent.setData(Uri.parse("tel:" + restaurant.getPhoneNumber()));
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            startActivity(callIntent);
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

        @Override
        protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
            if (requestCode == CM_ID){
                if (resultCode == Activity.RESULT_OK){
                    boolean isRefesh = data.getBooleanExtra("isRefesh", false);
                    if (isRefesh){
                        int id_rest = restaurant.getId();
                        restaurant = FoodMapManager.findRestaurant(id_rest);
                        if (restaurant == null)
                            RestaurantInfoActivity.this.finish();
                    }
                }
            }
            try {
                callbackManager.onActivityResult(requestCode, resultCode, data);
            }catch(Exception e){
                e.printStackTrace();
            }
        }
    }
}
