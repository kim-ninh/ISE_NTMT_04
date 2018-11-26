package com.hcmus.dreamers.foodmap;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.hcmus.dreamers.foodmap.AsyncTask.DownloadImageTask;
import com.hcmus.dreamers.foodmap.Model.Owner;
import com.theartofdev.edmodo.cropper.CropImage;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

//Crop Image Source
//https://github.com/ArthurHub/Android-Image-Cropper

public class ManageAccountActivity extends AppCompatActivity {

    private static final int REQUEST_OPEN_GALERY = 12345;
    private static final int REQUEST_TAKE_PHOTO = 11111;
    ImageView ic_avatar;
    EditText txtUserName;
    EditText txtRealName;
    EditText txtPhoneNumber1;
    EditText txtEmail;
    Button buttonChangePass;
    EditText txtCurrentPass;
    EditText txtNewPass;
    LinearLayout passwordSection;
    LinearLayout avatarSection;

    Owner owner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_account);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        takeReferenceFromResource();
        owner = Owner.getInstance();
        putDataToViews();

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        handleClickEvent();
    }

    private void handleClickEvent() {
        buttonChangePass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int isVisible = passwordSection.getVisibility();
                if (isVisible == View.VISIBLE)
                    passwordSection.setVisibility(View.INVISIBLE);
                else
                    passwordSection.setVisibility(View.VISIBLE);
            }
        });

        avatarSection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Đổi hình đại diện (Từ máy ảnh hoặc thư viện)

                PopupMenu popupMenu = new PopupMenu(ManageAccountActivity.this, view);
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
        });
    }

    private void putDataToViews() {
        DownloadImageTask task = new DownloadImageTask(ic_avatar, ManageAccountActivity.this);
        task.loadImageFromUrl(owner.getUrlImage());
        txtUserName.setText(owner.getUsername());
        txtRealName.setText(owner.getName());
        txtPhoneNumber1.setText(owner.getPhoneNumber());
        txtEmail.setText(owner.getEmail());
    }

    private void takeReferenceFromResource() {
        ic_avatar = (ImageView) findViewById(R.id.ic_avatar);
        txtUserName = (EditText) findViewById(R.id.txtUserName);
        txtRealName = (EditText) findViewById(R.id.txtRealName);
        txtPhoneNumber1 = (EditText) findViewById(R.id.txtPhoneNumber1);
        txtEmail = (EditText) findViewById(R.id.txtEmail);
        buttonChangePass = (Button) findViewById(R.id.buttonChangePass);
        txtCurrentPass = (EditText) findViewById(R.id.txtCurrentPass);
        txtNewPass = (EditText) findViewById(R.id.txtNewPass);
        passwordSection = (LinearLayout) findViewById(R.id.passwordSection);
        avatarSection = (LinearLayout) findViewById(R.id.avatarSection);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_delete:
                Toast.makeText(ManageAccountActivity.this, "action delete selected",
                        Toast.LENGTH_LONG).show();
                return true;

            case R.id.action_done:
                Toast.makeText(ManageAccountActivity.this, "action done selected",
                        Toast.LENGTH_LONG).show();

                if (checkInputValid() == false) {
                    Toast.makeText(ManageAccountActivity.this, "There's something wrong",
                            Toast.LENGTH_LONG).show();
                    return true;
                }

                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.edit_item_menu, menu);
        return true;
    }

    private boolean checkInputValid() {
        return true;
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

    @Override
    @SuppressLint("NewApi")
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == REQUEST_TAKE_PHOTO && resultCode == RESULT_OK) {

            startCropImageActivity(photoURI);
        }

        if (requestCode == REQUEST_OPEN_GALERY && resultCode == RESULT_OK) {
            // Chuỗi URI trả về có dạng content://<path>
            Uri imageUri = Uri.parse(data.getDataString());
            String str = imageUri.getPath();
            startCropImageActivity(imageUri);
        }

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();

                ic_avatar.setImageURI(resultUri);
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
                Toast.makeText(ManageAccountActivity.this,
                        error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }
    }

    private void startCropImageActivity(Uri imageUri) {
        CropImage.activity(imageUri)
                .start(this);
    }
}
