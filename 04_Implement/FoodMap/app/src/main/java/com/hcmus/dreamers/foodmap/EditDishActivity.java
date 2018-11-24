package com.hcmus.dreamers.foodmap;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;


import com.google.gson.Gson;
import com.hcmus.dreamers.foodmap.AsyncTask.TaskCompleteCallBack;
import com.hcmus.dreamers.foodmap.Model.Catalog;
import com.hcmus.dreamers.foodmap.Model.Dish;
import com.hcmus.dreamers.foodmap.adapter.ImageAdapter;
import com.hcmus.dreamers.foodmap.common.Base64Converter;
import com.hcmus.dreamers.foodmap.common.FoodMapApiManager;
import com.hcmus.dreamers.foodmap.database.FoodMapManager;
import com.hcmus.dreamers.foodmap.define.ConstantCODE;
import com.hcmus.dreamers.foodmap.define.ConstantURL;
import com.theartofdev.edmodo.cropper.CropImage;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class EditDishActivity extends AppCompatActivity {


    TextView txtDishName;
    EditText txtDishCost;
    Spinner spnrDishType;
    GridView gridView;
    FloatingActionButton fab;
    Toolbar toolbar;
    ImageAdapter adapter;
    ProgressBar progressBar;

    Intent manageRest_manageDish;
    Bundle transferData = new Bundle();

    int rest_id;

    int gridRow = -1;
    Dish dish;
    List<Uri> imagesUri = new ArrayList<>();

    // arbitrary interprocess communication ID (just a nickname!)
    private final int IPC_PICK_IMAGE_ID = (int) (10001 * Math.random());
    private static final int REQUEST_OPEN_GALERY = 12345;
    private static final int REQUEST_TAKE_PHOTO = 11111;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_dish);

        takeReferenceFromResource();
        getTransferDataFromActivity();


        adapter = new ImageAdapter(this,imagesUri);
        gridView.setAdapter(adapter);

        spnrDishType.setAdapter(new ArrayAdapter<String>(
                this,
                R.layout.support_simple_spinner_dropdown_item,
                FoodMapManager.getCatalogsString()
        ));

        putDataToViews();

        //Enable the Up button (Icon look like this: <- )
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        handleClickEvent();
    }

    private void handleClickEvent() {

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Add new image
                gridRow = imagesUri.size();
                showPopupMenuSelection(view); //Chụp từ camera/chọn từ Galery
            }
        });


        spnrDishType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                List<String> catalogsString = FoodMapManager.getCatalogsString();
                dish.setCatalog(new Catalog(position + 1,catalogsString.get(position)));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                gridRow = position;
                showPopupMenuSelection(view);
            }
        });


        // Source: https://www.javatpoint.com/android-popup-menu-example
        gridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, final long id) {

                // Xóa hình, đặt làm hình đại diện (Dùng popup menu hiển thị)


                // Create the instance of Popup Menu
                PopupMenu popupMenu = new PopupMenu(EditDishActivity.this, view);
                // Inflating the popup using xml file
                popupMenu.getMenuInflater().inflate(R.menu.popup_menu_edit_dish_layout, popupMenu.getMenu());
                // registering popup with OnMenuItemClickListener
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {

                        if (item.getItemId() == R.id.delete_dishImg)
                        {
                            // Xóa hình trên server
                            String absolutePath = imagesUri.get(position).toString();
                            String imageName = new File(absolutePath).getName();
                            String relativePath = String.format(ConstantURL.IMAGE_RELATIVE_PATH, rest_id, imageName);

                            FoodMapApiManager.deleteImage(relativePath, new TaskCompleteCallBack() {
                                @Override
                                public void OnTaskComplete(Object response) {
                                    if((int)response == ConstantCODE.SUCCESS){

                                        // Xóa thành công trên server, cập nhật lại grid view
                                        if (dish.getUrlImage().equals(imagesUri.get(position).toString()))
                                        {
                                            dish.setUrlImage("");
                                        }

                                        imagesUri.remove(position);
                                        adapter.notifyDataSetChanged();
                                        Toast.makeText(EditDishActivity.this,
                                                "Xóa thành công",
                                                Toast.LENGTH_LONG)
                                                .show();

                                    }else if((int)response == ConstantCODE.NOTINTERNET){
                                        Toast.makeText(EditDishActivity.this,
                                                "Không có kết nối INTERNET!",
                                                Toast.LENGTH_LONG)
                                                .show();
                                    } else {
                                        Toast.makeText(EditDishActivity.this,
                                                "Xóa hình ảnh thất bại",
                                                Toast.LENGTH_LONG)
                                                .show();
                                    }
                                }
                            });
                        }
                        else if(item.getItemId() == R.id.set_default_thumbnail)
                        {
                            dish.setUrlImage(imagesUri.get(position).toString());
                        }

                        return true;
                    }
                });
                popupMenu.show();
                return true;
            }
        });


        txtDishName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Snackbar.make(v, "Nếu bạn muốn đổi tên món, hãy xóa và thêm lại món mới", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

    }

    private void showPopupMenuSelection(View view) {
        // Đổi hình đại diện (Từ máy ảnh hoặc thư viện)

        PopupMenu popupMenu = new PopupMenu(EditDishActivity.this, view);
        // Inflating the popup using xml file
        popupMenu.getMenuInflater().inflate(R.menu.pick_image_menu, popupMenu.getMenu());
        // registering popup with OnMenuItemClickListener

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                int itemID = item.getItemId();
                switch (itemID){
                    case R.id.open_camera:
                        if (gridRow == imagesUri.size())
                        {
                            imagesUri.add(Uri.parse(""));
                            adapter.notifyDataSetChanged();
                        }
                        dispatchTakePictureIntent();
                        break;


                    case R.id.open_galery:
                        if (gridRow == imagesUri.size())
                        {
                            imagesUri.add(Uri.parse(""));
                            adapter.notifyDataSetChanged();
                        }
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

    private void putDataToViews() {
        txtDishName.setText(dish.getName());
        txtDishCost.setText(Integer.toString(dish.getPrice()));

        // cần tìm vị trí của catalog trong danh sách, ko phải ID của catalog!
        int catalogPosition = FoodMapManager.getCatalogPosition(dish.getCatalog().getId());
        spnrDishType.setSelection(catalogPosition);

    }

    private void getTransferDataFromActivity() {
        // Get the restaurant ID and dish Obj
        Gson gson = new Gson();
        manageRest_manageDish = getIntent();
        transferData = manageRest_manageDish.getExtras();

        rest_id = transferData.getInt("restID");
        String dishJSON = transferData.getString("dishJSON");
        dish = gson.fromJson(dishJSON, Dish.class);

        // Check null pointer and shut down activity
        if (dish == null)
        {
            Toast.makeText(this,
                    "Dish object is null. Are you forgot to create it?",
                    Toast.LENGTH_LONG).show();
            finish();
        }

        if (!dish.getUrlImage().isEmpty())
            imagesUri.add(Uri.parse(dish.getUrlImage()));
    }

    private void takeReferenceFromResource() {
        txtDishName = (TextView) findViewById(R.id.txtDishName);
        txtDishCost = (EditText) findViewById(R.id.txtDishCost);
        spnrDishType = (Spinner) findViewById(R.id.dishType);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        toolbar = (Toolbar) findViewById(R.id.edit_dish_toolbar);
        gridView  = (GridView) findViewById(R.id.gridview);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.action_delete:
                FoodMapApiManager.deleteDish(rest_id, dish.getName(), new TaskCompleteCallBack() {
                    @Override
                    public void OnTaskComplete(Object response) {
                        if((int)response == FoodMapApiManager.SUCCESS){
                            Intent intent = new Intent();
                            intent.putExtra("isDelete", true);
                            setResult(RESULT_OK, intent);
                            finish();
                        }else if((int)response == ConstantCODE.NOTINTERNET){
                            Toast.makeText(EditDishActivity.this, "Không có kết nối INTERNET!", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(EditDishActivity.this, "Xóa món ăn thất bại!", Toast.LENGTH_LONG).show();
                        }
                    }
                });
                return true;

            case R.id.action_done:
                if(checkInputValid()){
                    final ProgressDialog progressDialog = new ProgressDialog(EditDishActivity.this);
                    progressDialog.setMessage("Updating dish");
                    progressDialog.setCanceledOnTouchOutside(false);
                    progressDialog.show();
                    FoodMapApiManager.updateDish(rest_id, dish, new TaskCompleteCallBack() {
                        @Override
                        public void OnTaskComplete(Object response) {
                            progressDialog.dismiss();
                            if((int)response == FoodMapApiManager.SUCCESS) {
                                Toast.makeText(EditDishActivity.this, "Cập nhật thành công", Toast.LENGTH_LONG).show();
                                Gson gson = new Gson();
                                Intent intent = new Intent();
                                intent.putExtra("dishJSON", gson.toJson(dish));
                                setResult(RESULT_OK, intent);
                                finish();
                            }else if((int)response == ConstantCODE.NOTINTERNET){
                                Toast.makeText(EditDishActivity.this, "Không có kết nối INTERNET!", Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(EditDishActivity.this, "Cập nhật món ăn thất bại!", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }else{
                    Toast.makeText(EditDishActivity.this, "Hãy nhập đầy đủ thông tin!", Toast.LENGTH_LONG).show();
                }


                return true;

            case android.R.id.home:
                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private boolean checkInputValid() {
        if(txtDishName.length() > 0 && txtDishCost.length() > 0){
            dish.setName(txtDishName.getText().toString());
            dish.setPrice(Integer.parseInt(txtDishCost.getText().toString()));

            String catalogString =(String) spnrDishType.getSelectedItem();
            Catalog catalog = FoodMapManager.findCatalog(catalogString);
            dish.setCatalog(catalog);

            if (imagesUri.isEmpty())
                dish.setUrlImage("");

            return true;
        }
        return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.edit_item_menu,menu);
        return true;
    }

    @Override
    @SuppressLint("NewApi")
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode == REQUEST_TAKE_PHOTO && resultCode == RESULT_OK) {

            startCropImageActivity(photoURI);
        }

        if (requestCode == REQUEST_OPEN_GALERY && resultCode == RESULT_OK)
        {
            // Chuỗi URI trả về có dạng content://<path>
            Uri imageUri = Uri.parse(data.getDataString());
            startCropImageActivity(imageUri);
        }

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();

                RelativeLayout cell =(RelativeLayout) gridView.getChildAt(gridRow);
                progressBar = cell.findViewById(R.id.progressBar);
                progressBar.setIndeterminate(true);
                progressBar.setVisibility(View.VISIBLE);

                // Upload hình lên server
                FoodMapApiManager.uploadImage(EditDishActivity.this ,
                        rest_id, resultUri,  new TaskCompleteCallBack() {
                            @Override
                            public void OnTaskComplete(Object response) {

                                progressBar.setVisibility(View.INVISIBLE);

                                // Kiểm tra chuỗi trả về có phải là đường dẫn URL:
                                String strResponse = (String) response;
                                if (strResponse.matches("^(http|https)://.*"))
                                {
                                    // Cập nhật dataset List<Uri> => Cập nhật grid view
                                    imagesUri.remove(gridRow);
                                    imagesUri.add(gridRow, Uri.parse(strResponse));
                                    adapter.notifyDataSetChanged();

                                    //Nếu danh sách chỉ có 1 hình, hỏi người dùng
                                    // có muốn làm hình mặc định hay không
                                    showConfirmDefaultImageDialog();
                                }else{

                                    // Đã có lỗi trong quá trình upload, in thông báo
                                    Toast.makeText(EditDishActivity.this,
                                            strResponse, Toast.LENGTH_LONG).show();
                                }
                            }// OnTaskComplete
                        });
                //ic_avatar.setImageURI(resultUri);
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
                Toast.makeText(EditDishActivity.this,
                        error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }
    }

    private void showConfirmDefaultImageDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(EditDishActivity.this);

        builder.setMessage(R.string.dialog_message).setTitle(R.string.dialog_title);

        // Add the buttons
        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked OK button
                dish.setUrlImage(imagesUri.get(gridRow).toString());
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User cancelled the dialog
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
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