package com.hcmus.dreamers.foodmap;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;

import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;

import android.support.design.widget.TextInputEditText;

import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatCheckBox;
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

import com.hcmus.dreamers.foodmap.AsyncTask.TaskCompleteCallBack;
import com.hcmus.dreamers.foodmap.Model.Owner;
import com.hcmus.dreamers.foodmap.common.FoodMapApiManager;
import com.hcmus.dreamers.foodmap.define.ConstantCODE;
import com.hcmus.dreamers.foodmap.define.ConstantURL;
import com.hcmus.dreamers.foodmap.define.ConstantValue;
import com.theartofdev.edmodo.cropper.CropImage;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import bolts.Task;

//Crop Image Source
//https://github.com/ArthurHub/Android-Image-Cropper

public class ManageAccountActivity extends AppCompatActivity {

    private static final int REQUEST_OPEN_GALERY = 12345;
    private static final int REQUEST_TAKE_PHOTO = 11111;
    ImageView ic_avatar;
    TextView txtUserName;
    EditText txtRealName;
    EditText txtPhoneNumber1;
    EditText txtEmail;
    TextInputEditText txtCurrentPass;
    TextInputEditText txtNewPass;
    LinearLayout passwordSection;
    LinearLayout avatarSection;

    AppCompatCheckBox checkBoxChangePassword;

    Owner owner;
    Uri resultUri;

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

        checkBoxChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkBoxChangePassword.isChecked())
                    passwordSection.setVisibility(View.VISIBLE);
                else
                    passwordSection.setVisibility(View.INVISIBLE);
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
        txtUserName = (TextView) findViewById(R.id.txtUserName);
        txtRealName = (EditText) findViewById(R.id.txtRealName);
        txtPhoneNumber1 = (EditText) findViewById(R.id.txtPhoneNumber1);
        txtEmail = (EditText) findViewById(R.id.txtEmail);
        txtCurrentPass = (TextInputEditText) findViewById(R.id.txtCurrentPass);
        txtNewPass = (TextInputEditText) findViewById(R.id.txtNewPass);
        passwordSection = (LinearLayout) findViewById(R.id.passwordSection);
        avatarSection = (LinearLayout) findViewById(R.id.avatarSection);
        checkBoxChangePassword = findViewById(R.id.checkBoxChangePassword);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_delete:

                AlertDialog alertDeleteAccountDialog = createAlertDialog();
                alertDeleteAccountDialog.show();
                return true;

            case R.id.action_done:
                final ProgressDialog progressDialog = new ProgressDialog(ManageAccountActivity.this);
                progressDialog.setMessage("Đang cập nhật...");
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.show();

                if (checkInputValid() == false) {
                    progressDialog.dismiss();
                    return true;
                }
                setDataFromView();
                updateAccount(progressDialog);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void setDataFromView() {
        owner.setName(txtRealName.getText().toString());
        owner.setEmail(txtEmail.getText().toString());
        owner.setPhoneNumber(txtPhoneNumber1.getText().toString());
        if (checkBoxChangePassword.isChecked())
        {
            owner.setPassword(txtNewPass.getText().toString());
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.edit_item_menu, menu);
        return true;
    }

    private boolean checkInputValid() {
        boolean isValid = true;

        if (txtRealName.length() == 0 && isValid) {
            Toast.makeText(ManageAccountActivity.this, "Tên thật còn trống", Toast.LENGTH_SHORT).show();
            isValid = false;
        }

        if (txtPhoneNumber1.length() != ConstantValue.PHONE_NUMBER_LENGTH && isValid) {
            Toast.makeText(ManageAccountActivity.this, "Chiều dài số điện thoại chưa đúng", Toast.LENGTH_SHORT).show();
            isValid = false;
        }

        String emailPattern = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";
        String ownerMail = txtEmail.getText().toString();
        if (!ownerMail.matches(emailPattern) && isValid) {
            Toast.makeText(ManageAccountActivity.this, "Email chưa chính xác", Toast.LENGTH_SHORT).show();
            isValid = false;
        }

        if (checkBoxChangePassword.isChecked())
        {
            if (! owner.getPassword().equals(txtCurrentPass.getText().toString()) && isValid)
            {
                Toast.makeText(ManageAccountActivity.this,
                        "Mật khẩu hiện tại không trùng khớp, hãy thử lại",Toast.LENGTH_LONG).show();
                isValid = false;
            }

            if (txtNewPass.getText().toString().isEmpty() && isValid)
            {
                Toast.makeText(ManageAccountActivity.this,
                        "Mật khẩu mới đang trống. Hãy điền mật khẩu mới vào.", Toast.LENGTH_LONG).show();
                isValid = false;
            }
        }
        return isValid;
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
                resultUri = result.getUri();

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

    private AlertDialog createAlertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(ManageAccountActivity.this);

        builder.setMessage(R.string.confirmDeleteAccount).setTitle(R.string.title_confirmDeleteAccount);

        // Add the buttons
        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked OK button
                final ProgressDialog progressDialog = new ProgressDialog(ManageAccountActivity.this);
                progressDialog.setMessage("Đang cập nhật...");
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.show();

                deleteAccount(progressDialog);
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User cancelled the alertDeleteAccountDialog
                Toast.makeText(ManageAccountActivity.this, "Đã hủy thao tác xóa tài khoản", Toast.LENGTH_SHORT).show();
            }
        });

        return builder.create();
    }

    private void deleteAccount(final ProgressDialog progressDialog) {
        TaskCompleteCallBack deleteImageTaskCallBack;
        final TaskCompleteCallBack deleteAccountTaskCallBack;

        deleteAccountTaskCallBack = new TaskCompleteCallBack() {
            @Override
            public void OnTaskComplete(Object response) {
                progressDialog.dismiss();
                if ((int) response == FoodMapApiManager.SUCCESS) {
                    Toast.makeText(ManageAccountActivity.this, "Xóa tài khoản thành công", Toast.LENGTH_LONG).show();
                    Owner.setInstance(null);
                    finish();
                } else if ((int) response == ConstantCODE.NOTINTERNET) {
                    Toast.makeText(ManageAccountActivity.this, "Không có kết nối INTERNET!", Toast.LENGTH_LONG).show();
                } else {
                    Log.e("DeleteAccountCallBack", "Can't delete account");
                }
            }
        };

        deleteImageTaskCallBack = new TaskCompleteCallBack() {
            @Override
            public void OnTaskComplete(Object response) {
                if ((int) response == FoodMapApiManager.SUCCESS) {
                    FoodMapApiManager.deleteAcount(deleteAccountTaskCallBack);
                } else if ((int) response == ConstantCODE.NOTINTERNET) {
                    progressDialog.dismiss();
                    Toast.makeText(ManageAccountActivity.this, "Không có kết nối INTERNET!", Toast.LENGTH_LONG).show();
                } else {
                    progressDialog.dismiss();
                    Log.e("DeleteImageCallBack", "Can't delete image");
                }
            }
        };

        // Nếu hình đang lưu thuộc foodmapserver thì xóa hình trước, xóa tài khoản sau
        if (owner.getUrlImage() != null && owner.getUrlImage().matches("^(http|https)://foodmapserver.000webhostapp.com/.*")) {

            String absolutePath = owner.getUrlImage();
            String imageName = new File(absolutePath).getName();
            String relativePath = String.format(ConstantURL.IMAGE_RELATIVE_PATH, 0, imageName);

            FoodMapApiManager.deleteImage(relativePath, deleteImageTaskCallBack);
        } else {
            FoodMapApiManager.deleteAcount(deleteAccountTaskCallBack);
        }
    }

    private void updateAccount(final ProgressDialog progressDialog) {
        final TaskCompleteCallBack updateAccountCallBack;
        TaskCompleteCallBack deleteImageCallBack;
        final TaskCompleteCallBack uploadImageCallBack;


        updateAccountCallBack = new TaskCompleteCallBack() {
            @Override
            public void OnTaskComplete(Object response) {
                progressDialog.dismiss();

                if ((int) response == FoodMapApiManager.SUCCESS) {
                    Toast.makeText(ManageAccountActivity.this, "Cập nhật thành công", Toast.LENGTH_LONG).show();
                } else if ((int) response == ConstantCODE.NOTINTERNET) {
                    Toast.makeText(ManageAccountActivity.this, "Không có kết nối INTERNET!", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(ManageAccountActivity.this, "Cập nhật thông tin tài khoản thất bại!", Toast.LENGTH_LONG).show();
                }
            }
        };

        uploadImageCallBack = new TaskCompleteCallBack() {
            @Override
            public void OnTaskComplete(Object response) {
                if (((String)response).matches("^(http|https)://.*"))
                {
                    owner.setUrlImage((String) response);
                    FoodMapApiManager.updateAccount(owner, updateAccountCallBack);
                }
                else
                {
                    progressDialog.dismiss();
                    Toast.makeText(ManageAccountActivity.this, (String)response,Toast.LENGTH_SHORT).show();
                }
            }
        };

        deleteImageCallBack = new TaskCompleteCallBack() {
            @Override
            public void OnTaskComplete(Object response) {
                if ((int) response == FoodMapApiManager.SUCCESS) {
                    owner.setUrlImage("");
                    FoodMapApiManager.uploadImage(ManageAccountActivity.this, 0, resultUri, uploadImageCallBack);
                } else if ((int) response == ConstantCODE.NOTINTERNET) {
                    progressDialog.dismiss();
                    Toast.makeText(ManageAccountActivity.this, "Không có kết nối INTERNET!", Toast.LENGTH_SHORT).show();
                } else {
                    progressDialog.dismiss();
                    Toast.makeText(ManageAccountActivity.this, "Cập nhật thông tin tài khoản thất bại!", Toast.LENGTH_SHORT).show();
                }
            }
        };


        // Nếu chưa chọn hình thì cập nhật trực tiếp tài khoản
        if (resultUri == null) {
            FoodMapApiManager.updateAccount(owner, updateAccountCallBack);
        } else {
            // Nếu tài khoản chưa có hình hoặc hình ko thuộc foodmapserver thì upload trực tiếp
            // Ngược lại thì xóa hình cũ -> up hình mới lên
            if (owner.getUrlImage() == null || !owner.getUrlImage().matches("^(http|https)://foodmapserver.000webhostapp.com/.*")) {
                FoodMapApiManager.uploadImage(ManageAccountActivity.this, 0, resultUri, uploadImageCallBack);
            } else {
                // Tài khoản đã có hình thuộc foodmapserver
                String absolutePath = owner.getUrlImage();
                String imageName = new File(absolutePath).getName();
                String relativePath = String.format(ConstantURL.IMAGE_RELATIVE_PATH, 0, imageName);
                FoodMapApiManager.deleteImage(relativePath, deleteImageCallBack);
            }
        }

    }
}
