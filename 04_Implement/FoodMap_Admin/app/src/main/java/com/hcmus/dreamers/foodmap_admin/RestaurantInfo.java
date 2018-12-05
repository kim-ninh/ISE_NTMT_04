package com.hcmus.dreamers.foodmap_admin;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.hcmus.dreamers.foodmap_admin.asynctask.TaskCompleteCallBack;
import com.hcmus.dreamers.foodmap_admin.common.FoodMapApi;
import com.hcmus.dreamers.foodmap_admin.define.ConstantCODE;
import com.hcmus.dreamers.foodmap_admin.model.Admin;
import com.hcmus.dreamers.foodmap_admin.model.Restaurant;
import com.squareup.picasso.Picasso;

public class RestaurantInfo extends AppCompatActivity implements View.OnClickListener {

    private ImageView imvAvartar;
    private TextView txtName;
    private TextView txtNumberPhone;
    private TextView txtAddress;
    private TextView txtDescription;
    private Button btnCall;
    private Button btnMap;

    private ImageView imvCheck;
    private ImageView imvDelete;

    private EditText edtNote;
    private Button btnSend;

    private Toolbar toolbar;

    private Restaurant rest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_info);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        imvAvartar = findViewById(R.id.imvAvatar);
        txtName = findViewById(R.id.txtName);
        txtAddress = findViewById(R.id.txtAddress);
        txtDescription = findViewById(R.id.txtDescription);
        txtNumberPhone = findViewById(R.id.txtNumberPhone);

        btnCall = findViewById(R.id.btnCall);
        btnMap = findViewById(R.id.btnMap);
        btnMap.setOnClickListener(this);
        btnCall.setOnClickListener(this);

        imvCheck = findViewById(R.id.imvCheck);
        imvDelete = findViewById(R.id.imvDelete);
        imvCheck.setOnClickListener(this);
        imvDelete.setOnClickListener(this);

        LoadData();
    }

    void LoadData(){
        Intent data = getIntent();
        int id_rest = data.getIntExtra("id_rest", -1);
        if (id_rest == -1)
            finish();
        else{
            rest = Admin.getInstance().findRestaurant(id_rest);
            if (rest == null)
                finish();
            else{
                Picasso.get().load(rest.getUrlImage())
                        .error(R.mipmap.ic_launcher)
                        .into(imvAvartar);

                txtName.setText(rest.getName());
                txtAddress.setText(rest.getAddress());
                txtNumberPhone.setText(rest.getPhoneNumber());
                txtDescription.setText(rest.getDescription());
            }
        }
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btnMap){
            Intent intent = new Intent(RestaurantInfo.this, MapActivity.class);
            intent.putExtra("lat", rest.getLocation().getLatitude());
            intent.putExtra("lon", rest.getLocation().getLongitude());
            startActivity(intent);
        }
        else if (id == R.id.btnCall){
            Intent phoneIntent = new Intent(Intent.ACTION_CALL);
            phoneIntent.setData(Uri.parse("tel:"+rest.getPhoneNumber()));
            startActivity(phoneIntent);
        }
        else if (id == R.id.imvCheck){
            final ProgressDialog progressDialog = new ProgressDialog(RestaurantInfo.this);
            progressDialog.setMessage("Processing...");
            progressDialog.show();
            progressDialog.setCanceledOnTouchOutside(false);
            FoodMapApi.checkOKRestaurant(Admin.getInstance().getUsername(), Admin.getInstance().getPassword(), rest.getId(), new TaskCompleteCallBack() {
                @Override
                public void OnTaskComplete(Object response) {
                    progressDialog.dismiss();
                    int code = (int)response;
                    if (code == FoodMapApi.SUCCESS){
                        Toast.makeText(RestaurantInfo.this, "Cập nhật thành công", Toast.LENGTH_LONG).show();
                        Intent data = new Intent();
                        data.putExtra("ischange", true);
                        setResult(Activity.RESULT_OK, data);
                        finish();
                    }
                    else if (code == ConstantCODE.NOTINTERNET){
                        Toast.makeText(RestaurantInfo.this, "Kiểm tra lại kết nối internet", Toast.LENGTH_LONG).show();
                    }
                    else{
                        Toast.makeText(RestaurantInfo.this, "Đã xảy ra lỗi", Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
        else if (id == R.id.imvDelete){
            AlertDialog.Builder builder = new AlertDialog.Builder(RestaurantInfo.this);
            View noteView = getLayoutInflater().inflate(R.layout.dialog_note, null);
            final AlertDialog dialog = builder.create();
            btnSend = noteView.findViewById(R.id.btnSend);
            edtNote = noteView.findViewById(R.id.edtNote);

            btnSend.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    final ProgressDialog progressDialog = new ProgressDialog(RestaurantInfo.this);
                    progressDialog.setMessage("Processing...");
                    progressDialog.show();
                    progressDialog.setCanceledOnTouchOutside(false);
                    String note = edtNote.getText().toString();
                    FoodMapApi.checkFailRestaurant(Admin.getInstance().getUsername(), Admin.getInstance().getPassword(), rest.getId(), note, new TaskCompleteCallBack() {
                        @Override
                        public void OnTaskComplete(Object response) {
                            progressDialog.dismiss();
                            int code = (int)response;
                            if (code == FoodMapApi.SUCCESS){
                                Toast.makeText(RestaurantInfo.this, "Cập nhật thành công", Toast.LENGTH_LONG).show();

                                Intent data = new Intent();
                                data.putExtra("ischange", true);
                                setResult(Activity.RESULT_OK, data);
                                finish();
                            }
                            else if (code == ConstantCODE.NOTINTERNET){
                                Toast.makeText(RestaurantInfo.this, "Kiểm tra lại kết nối internet", Toast.LENGTH_LONG).show();
                            }
                            else{
                                Toast.makeText(RestaurantInfo.this, "Đã xảy ra lỗi", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }
            });
            dialog.setView(noteView);
            dialog.show();
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home){
            finish();
        }
        return false;
    }
}
