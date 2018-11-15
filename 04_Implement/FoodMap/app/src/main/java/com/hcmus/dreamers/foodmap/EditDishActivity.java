package com.hcmus.dreamers.foodmap;


import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.PopupMenu;
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

import java.io.File;
import java.util.ArrayList;
import java.util.List;


public class EditDishActivity extends AppCompatActivity {


    TextView txtDishName;
    EditText txtDishCost;
    Spinner spnrDishType;
    GridView gridView;
    FloatingActionButton fab;
    Toolbar toolbar;
    ImageAdapter adapter;


    Intent manageRest_manageDish;
    Bundle transferData = new Bundle();

    int rest_id;

    int gridRow = -1;
    Dish dish;
    List<Uri> imagesUri = new ArrayList<>();

    // arbitrary interprocess communication ID (just a nickname!)
    private final int IPC_PICK_IMAGE_ID = (int) (10001 * Math.random());

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

                // Open Galery to pick image
                Intent editDish_pickImage = new Intent(Intent.ACTION_PICK,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                gridRow = -1;
                startActivityForResult(editDish_pickImage, IPC_PICK_IMAGE_ID);
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

                // Chọn hình khác để đổi
                Intent editDish_pickImage = new Intent(Intent.ACTION_PICK,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                gridRow = position;
                startActivityForResult(editDish_pickImage, IPC_PICK_IMAGE_ID);

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
                            String imagePath = imagesUri.get(position).toString();
                            FoodMapApiManager.deleteImage(imagePath, new TaskCompleteCallBack() {
                                @Override
                                public void OnTaskComplete(Object response) {
                                    if((int)response == FoodMapApiManager.SUCCESS){

                                        // Xóa thành công trên server, cập nhật lại grid view
                                        imagesUri.remove(position);
                                        adapter.notifyDataSetChanged();
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

    private void putDataToViews() {
        txtDishName.setText(dish.getName());
        txtDishCost.setText(Integer.toString(dish.getPrice()));

        spnrDishType.setSelection(dish.getCatalog().getId() - 1);

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
                    FoodMapApiManager.updateDish(rest_id, dish, new TaskCompleteCallBack() {
                        @Override
                        public void OnTaskComplete(Object response) {
                            if((int)response == FoodMapApiManager.SUCCESS) {
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
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try{

           if (requestCode == IPC_PICK_IMAGE_ID){
               if (resultCode == Activity.RESULT_OK){

                   // Chuỗi URI trả về có dạng content://<path>
                   Uri imageUri = Uri.parse(data.getDataString());
                   File imageFile = new File(imageUri.getPath());
                   String encodedData = "";


                   // Mã hóa hình theo Base64
                   try
                   {
                       encodedData = Base64Converter.encodeToBase64(EditDishActivity.this,
                               imageUri);
                   }catch (Exception e)
                   {
                        Log.d("ConvertBase64",e.getMessage());
                   }


                   // Upload hình lên server
                    FoodMapApiManager.uploadImage(rest_id, imageFile.getName(), encodedData, new TaskCompleteCallBack() {
                        @Override
                        public void OnTaskComplete(Object response) {

                            // Kiểm tra chuỗi trả về có phải là đường dẫn URL:
                            String strResponse = (String) response;
                            if (strResponse.matches("^(http|https)://.*"))
                            {

                                // Cập nhật dataset List<Uri> => Cập nhật grid view
                                if (gridRow != -1)
                                {
                                    //Thay đổi 1 hình có sẵn
                                    imagesUri.remove(gridRow);
                                    imagesUri.add(gridRow, Uri.parse(strResponse));
                                }else {
                                    // Thêm mới 1 hình
                                    imagesUri.add(Uri.parse(strResponse));
                                }

                                adapter.notifyDataSetChanged();
                            }else{

                                // Đã có lỗi trong quá trình upload, in thông báo
                                Toast.makeText(EditDishActivity.this,
                                        strResponse,
                                        Toast.LENGTH_LONG)
                                        .show();
                            }
                        }// OnTaskComplete
                    });
               }// Activity.RESULT_OK
           }// IPC_PICK_IMAGE_ID

        }catch (Exception e){
            Toast.makeText(EditDishActivity.this,
                    e.getMessage(),
                    Toast.LENGTH_LONG).show();
        }
    }
}