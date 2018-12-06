package com.hcmus.dreamers.foodmap;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.method.DateTimeKeyListener;

import android.util.Log;

import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import android.widget.PopupMenu;

import android.widget.Spinner;
import android.widget.Toast;


import com.google.gson.Gson;
import com.hcmus.dreamers.foodmap.AsyncTask.TaskCompleteCallBack;
import com.hcmus.dreamers.foodmap.Model.Catalog;
import com.hcmus.dreamers.foodmap.Model.Dish;
import com.hcmus.dreamers.foodmap.Model.Restaurant;
import com.hcmus.dreamers.foodmap.common.FoodMapApiManager;
import com.hcmus.dreamers.foodmap.database.FoodMapManager;
import com.hcmus.dreamers.foodmap.define.ConstantCODE;

import com.theartofdev.edmodo.cropper.CropImage;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class AddDishActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    private EditText edtName;
    private EditText edtPrice;
    private Spinner spnCatalog;
    private ImageView imgImage;
    private Button btnSubmit;
    private String urlImage;
    private Catalog catalog;
    private Toolbar toolbar;
    private List<String> listCatalog;
    private Restaurant restaurant;

    private final int IPC_ID = 1231;

    private static final int REQUEST_OPEN_GALERY = 12345;
    private static final int REQUEST_TAKE_PHOTO = 11111;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_dish);
        references();

        urlImage = "";

        Intent data = getIntent();
        restaurant = (Restaurant) data.getSerializableExtra("rest");

        if (restaurant == null) {
            finish();
        }

        btnSubmit.setOnClickListener(this);
    }

    private void references() {
        toolbar = (Toolbar) findViewById(R.id.add_dish_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        edtName = (EditText) findViewById(R.id.edtName);
        edtPrice = (EditText) findViewById(R.id.edtPrice);

        spnCatalog = (Spinner) findViewById(R.id.spnCatalog);
        // Create an ArrayAdapter using the string array and a default spinner layout
        listCatalog = FoodMapManager.getCatalogsString();
        ArrayAdapter<CharSequence> adapter = new ArrayAdapter<CharSequence>(this, android.R.layout.simple_spinner_item);

        adapter.addAll(listCatalog);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnCatalog.setAdapter(adapter);
        spnCatalog.setOnItemSelectedListener(this);

        imgImage = (ImageView) findViewById(R.id.imgImage);
        imgImage.setOnClickListener(this);

        btnSubmit = (Button) findViewById(R.id.btnSubmit);
        btnSubmit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btnSubmit) {
            final String name = edtName.getText().toString();
            final String price = edtPrice.getText().toString();
            if (name.equals("") || price.equals("") || urlImage.equals("") || catalog == null) {
                Toast.makeText(AddDishActivity.this, "Vui lòng điền đầy đủ thông tin", Toast.LENGTH_LONG).show();
            } else {
                final ProgressDialog progressDialog = new ProgressDialog(AddDishActivity.this);
                progressDialog.setMessage("Add Dish");
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.show();

                Date date = Calendar.getInstance().getTime();

                String nameImage = "dish_" + restaurant.getDishes().size() + "_" + String.valueOf(date.getTime());
                FoodMapApiManager.uploadImage(AddDishActivity.this, restaurant.getId(), nameImage, urlImage, new TaskCompleteCallBack() {
                    @Override
                    public void OnTaskComplete(Object response) {
                        String url = (String) response;
                        if (url != null) {
                            final Dish dish = new Dish();
                            dish.setName(name);
                            dish.setCatalog(catalog);
                            dish.setPrice(Integer.valueOf(price));
                            dish.setUrlImage(url);

                            FoodMapApiManager.addDish(restaurant.getId(), dish, new TaskCompleteCallBack() {
                                @Override
                                public void OnTaskComplete(Object response) {
                                    int code = (int) response;
                                    progressDialog.dismiss();
                                    if (code == FoodMapApiManager.SUCCESS) {
                                        restaurant.getDishes().add(dish);

                                        // Send new dish back to previous activity/fragment
                                        // to update the list view
                                        Intent data = new Intent();
                                        Gson gson = new Gson();
                                        String dishJSON = gson.toJson(dish);
                                        data.putExtra("dishJSON", dishJSON);

                                        setResult(Activity.RESULT_OK, data);
                                        AddDishActivity.this.finish();
                                    } else if (code == FoodMapApiManager.FAIL_INFO) {
                                        Toast.makeText(AddDishActivity.this, "Kiểm tra lại thông tin vừa nhập", Toast.LENGTH_LONG).show();
                                    } else if (code == ConstantCODE.NOTINTERNET) {
                                        Toast.makeText(AddDishActivity.this, "Kiểm tra kết nối mạng", Toast.LENGTH_LONG).show();
                                    }
                                }
                            });

                        } else {
                            progressDialog.dismiss();
                            Toast.makeText(AddDishActivity.this, "Tải ảnh lỗi", Toast.LENGTH_LONG).show();
                        }
                    }
                });

            }
        } else if (id == R.id.imgImage) {
            PopupMenu popupMenu = new PopupMenu(AddDishActivity.this, v);
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
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return true;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
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

                imgImage.setImageURI(resultUri);
                urlImage = resultUri.getPath();

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
                Toast.makeText(AddDishActivity.this,
                        error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        catalog = FoodMapManager.findCatalog(listCatalog.get(position));
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        catalog = null;
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
