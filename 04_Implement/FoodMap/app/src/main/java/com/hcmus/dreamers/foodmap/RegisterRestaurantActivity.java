package com.hcmus.dreamers.foodmap;

import android.app.Activity;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.hcmus.dreamers.foodmap.AsyncTask.TaskCompleteCallBack;
import com.hcmus.dreamers.foodmap.Model.Owner;
import com.hcmus.dreamers.foodmap.Model.Restaurant;
import com.hcmus.dreamers.foodmap.common.FoodMapApiManager;
import com.hcmus.dreamers.foodmap.database.FoodMapManager;
import com.hcmus.dreamers.foodmap.define.ConstantCODE;
import com.theartofdev.edmodo.cropper.CropImage;

import org.osmdroid.util.GeoPoint;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class RegisterRestaurantActivity extends AppCompatActivity implements View.OnClickListener {

    private final int IPC_ID = 1009;
    private final int CLA_ID = 1008;
    private static final int REQUEST_OPEN_GALERY = 12345;
    private static final int REQUEST_TAKE_PHOTO = 11111;
    private final String LAT_LONG_FORMAT = "Lat %f - Long %f";

    private RelativeLayout selectLocationSection;
    private EditText edtName;
    private EditText edtAddress;
    private EditText edtDesciption;
    private EditText edtPhoneNumber;

    private Button btnRegister;

    private ImageView igvUpload;

    private TextView txvOpen;
    private TextView txvClose;
    private TextView txvLatLongInfo;

    private Toolbar toolbar;

    private Restaurant restaurant;
    String imageURI; // lình hình ảnh

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_register);

        restaurant = new Restaurant();
        restaurant.setOwnerUsername(Owner.getInstance().getUsername());
        Date timeOpen = new Date();
        timeOpen.setHours(7);
        timeOpen.setMinutes(0);
        restaurant.setTimeOpen(timeOpen);
        Date timeClose = new Date();
        timeClose.setHours(18);
        timeClose.setMinutes(0);
        restaurant.setTimeClose(timeClose);

        imageURI = "";

        edtName = (EditText) findViewById(R.id.edt_name_restaurant);
        edtAddress = (EditText) findViewById(R.id.edt_address_restaurant);
        edtDesciption = (EditText) findViewById(R.id.edt_description_restaurant);
        edtPhoneNumber = (EditText) findViewById(R.id.edt_phone_number);

        selectLocationSection = (RelativeLayout) findViewById(R.id.selectLocationSection);
        selectLocationSection.setOnClickListener(this);

        txvLatLongInfo = (TextView) findViewById(R.id.latLongInfo);

        btnRegister = (Button) findViewById(R.id.btnRegister);
        btnRegister.setOnClickListener(this);

        igvUpload = (ImageView) findViewById(R.id.igv_upload);
        igvUpload.setOnClickListener(this);

        txvOpen = (TextView) findViewById(R.id.txvOpenTime);
        txvOpen.setOnClickListener(this);

        txvClose = (TextView) findViewById(R.id.txvCloseTime);
        txvClose.setOnClickListener(this);

        toolbar = (Toolbar) findViewById(R.id.register_restaurant_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            RegisterRestaurantActivity.this.finish();
        }
        return true;
    }

    @Override
    public void onClick(View v) {
        final int id = v.getId();
        if (id == R.id.txvOpenTime || id == R.id.txvCloseTime) {
            Calendar mcurrentTime = Calendar.getInstance();
            int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
            int minute = mcurrentTime.get(Calendar.MINUTE);
            TimePickerDialog mTimePicker;
            mTimePicker = new TimePickerDialog(RegisterRestaurantActivity.this, new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                    String time = String.valueOf(selectedHour) + " : " + String.valueOf(selectedMinute);
                    Date date = new Date();
                    date.setHours(selectedHour);
                    date.setMinutes(selectedMinute);
                    if (id == R.id.txvOpenTime) {
                        txvOpen.setText(time);
                        restaurant.setTimeOpen(date);
                    } else {
                        txvClose.setText(time);
                        restaurant.setTimeClose(date);
                    }
                }
            }, hour, minute, true);//Yes 24 hour time

            if (id == R.id.txvOpenTime) {
                mTimePicker.setTitle("Giờ mở cửa");
            } else {
                mTimePicker.setTitle("Giờ đóng cửa");
            }
            mTimePicker.show();
        } else if (id == R.id.igv_upload) {

            // Chọn hình từ máy ảnh hoặc thư viện hình ảnh
            PopupMenu popupMenu = new PopupMenu(RegisterRestaurantActivity.this, v);
            // Inflating the popup using xml file
            popupMenu.getMenuInflater().inflate(R.menu.pick_image_menu, popupMenu.getMenu());
            // registering popup with OnMenuItemClickListener

            popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {

                    int itemID = item.getItemId();
                    switch (itemID) {
                        case R.id.open_camera:
                            dispatchTakePictureIntent();
                            break;


                        case R.id.open_galery:

                            Intent intent = new Intent(Intent.ACTION_PICK,
                                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                            startActivityForResult(intent, REQUEST_OPEN_GALERY);

                            break;
                    }
                    return true;
                }
            });
            popupMenu.show();
        } else if (id == R.id.btnRegister) {
            String name = edtName.getText().toString();
            String address = edtAddress.getText().toString();
            String desciption = edtDesciption.getText().toString();
            String phoneNumber = edtPhoneNumber.getText().toString();

            if (restaurant.getLocation() == null || name.equals("") || address.equals("")
                    || desciption.equals("") || imageURI.equals("") || phoneNumber.equals("")) {
                Toast.makeText(RegisterRestaurantActivity.this, "Vui lòng điền đầy đủ thông tin", Toast.LENGTH_LONG).show();
            } else if (phoneNumber.length() < 10 || phoneNumber.length() > 11) {
                Toast.makeText(RegisterRestaurantActivity.this, "Số điện thoại không hợp lệ", Toast.LENGTH_LONG).show();
            } else {
                restaurant.setName(name);
                restaurant.setAddress(address);
                restaurant.setDescription(desciption);
                restaurant.setPhoneNumber(phoneNumber);

                final ProgressDialog progressDialog = new ProgressDialog(RegisterRestaurantActivity.this);
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.setMessage("Add restaurant");
                progressDialog.show();

                // tạo quán ăn
                FoodMapApiManager.createRestaurant(restaurant, new TaskCompleteCallBack() {
                    @Override
                    public void OnTaskComplete(Object response) {
                        final int code = (int) response;
                        if (code > 0) {
                            restaurant.setId(code);
                            // upload ảnh, tránh trùng tên
                            Date date = Calendar.getInstance().getTime();
                            String imageName = String.format("%tF_%tT_%s",date.getTime(), date.getTime(), "avatarRes");

                            FoodMapApiManager.uploadImage(RegisterRestaurantActivity.this, code, imageName, imageURI, new TaskCompleteCallBack() {
                                @Override
                                public void OnTaskComplete(Object response) {
                                    String url = (String) response;
                                    if (url != null) {
                                        restaurant.setUrlImage(url);

                                        // cập nhật lại thông tin ảnh
                                        FoodMapApiManager.updateRestaurant(restaurant, new TaskCompleteCallBack() {
                                            @Override
                                            public void OnTaskComplete(Object response) {
                                                progressDialog.dismiss();

                                                if ((int) response == FoodMapApiManager.SUCCESS) {
                                                    // thêm quán ăn vào danh sách quán ăn
                                                    FoodMapManager.addRestaurant(RegisterRestaurantActivity.this, restaurant);
                                                    Owner.getInstance().addRestaurant(restaurant);

                                                    // thoát
                                                    Intent intent = new Intent();
                                                    intent.putExtra("isAdd", true);
                                                    setResult(Activity.RESULT_OK, intent);
                                                    RegisterRestaurantActivity.this.finish();
                                                } else if ((int) response == ConstantCODE.NOTINTERNET) {
                                                    Toast.makeText(RegisterRestaurantActivity.this, "Kiểm tra kết nối internet", Toast.LENGTH_LONG).show();
                                                } else {
                                                    Toast.makeText(RegisterRestaurantActivity.this, "Error", Toast.LENGTH_LONG).show();
                                                }
                                            }
                                        });
                                    } else {
                                        progressDialog.dismiss();
                                        Toast.makeText(RegisterRestaurantActivity.this, "Kiểm tra kết nối internet", Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                        } else if (code == ConstantCODE.NOTINTERNET) {
                            progressDialog.dismiss();
                            Toast.makeText(RegisterRestaurantActivity.this, "Kiểm tra kết nối internet", Toast.LENGTH_LONG).show();
                        } else {
                            progressDialog.dismiss();
                            Toast.makeText(RegisterRestaurantActivity.this, "Error", Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        } else if (id == R.id.selectLocationSection) {

            String address = edtAddress.getText().toString();

            if (address.equals("")) {
                Toast.makeText(RegisterRestaurantActivity.this, "Vui lòng nhập địa chỉ", Toast.LENGTH_LONG).show();
            } else {
                Intent intent = new Intent(RegisterRestaurantActivity.this, ChooseLocationActivity.class);
                intent.putExtra("address", address);
                startActivityForResult(intent, CLA_ID);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == CLA_ID) {
            if (resultCode == Activity.RESULT_OK) {
                double lat = data.getDoubleExtra("lat", -1000.0);
                double lon = data.getDoubleExtra("lon", -1000.0);
                String address = data.getStringExtra("address");
                if (lat != -1000.0 && lon != -1000.0) {
                    restaurant.setLocation(new GeoPoint(lat, lon));
                    txvLatLongInfo.setText(String.format(LAT_LONG_FORMAT, lat,lon));
                }
                if (address != null) {
                    //edtAddress.setText(address);
                }
            }
        }

        if (requestCode == REQUEST_TAKE_PHOTO && resultCode == RESULT_OK) {

            startCropImageActivity(photoURI);
        }

        if (requestCode == REQUEST_OPEN_GALERY && resultCode == RESULT_OK) {
            // Chuỗi URI trả về có dạng content://<path>
            Uri imageUri = Uri.parse(data.getDataString());
            startCropImageActivity(imageUri);
        }

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();

                igvUpload.setImageURI(resultUri);
                Log.w("EncodedPath", resultUri.getEncodedPath());
                Log.w("Path",resultUri.getPath());
                imageURI = resultUri.getPath();
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
                Log.e("CropImageResult: ", error.getMessage());
                Toast.makeText(RegisterRestaurantActivity.this,
                        "Không thể cắt hình. Hãy thử lại với hình khác", Toast.LENGTH_LONG).show();
            }
        }
    }

    String mCurrentPhotoPath;

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    Uri photoURI;

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
                Log.e("CreateImageFile", ex.getMessage());
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                photoURI = FileProvider.getUriForFile(this,
                        "com.hcmus.dreamers.foodmap",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }

    private void startCropImageActivity(Uri imageUri) {
        CropImage.activity(imageUri)
                .start(this);
    }
}
