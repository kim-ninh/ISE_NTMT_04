package com.hcmus.dreamers.foodmap;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.hcmus.dreamers.foodmap.AsyncTask.TaskCompleteCallBack;
import com.hcmus.dreamers.foodmap.Model.Discount;
import com.hcmus.dreamers.foodmap.Model.Dish;
import com.hcmus.dreamers.foodmap.Model.Restaurant;
import com.hcmus.dreamers.foodmap.common.FoodMapApiManager;
import com.hcmus.dreamers.foodmap.define.ConstantCODE;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class AddDiscountActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    private Toolbar tbAddDiscount;
    private Spinner spnDishName;
    private EditText edtDiscountPercent;
    private TextView tvDateBegin;
    private TextView tvTimeBegin;
    private TextView tvDateEnd;
    private TextView tvTimeEnd;
    private Button btnSubmit;

    private Restaurant restaurant;
    private List<String> listDishName;
    private Discount discount;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_discount);

        Intent data = getIntent();
        restaurant = (Restaurant) data.getSerializableExtra("rest");

        if (restaurant == null) {
            finish();
        }
        List<Dish> dishes = restaurant.getDishes();
        listDishName = new ArrayList<String>();
        //listDishName = dishes.stream().map(Dish::getName).collect(Collectors.toList());
        for (Dish dish : dishes) {
            listDishName.add(dish.getName());
        }

        discount = new Discount();

        references();
    }

    public void references() {
        tbAddDiscount = (Toolbar) findViewById(R.id.add_discount_toolbar);
        setSupportActionBar(tbAddDiscount);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        spnDishName = (Spinner) findViewById(R.id.spnDishName);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item);
        adapter.addAll(listDishName);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnDishName.setAdapter(adapter);
        spnDishName.setOnItemSelectedListener(this);

        edtDiscountPercent = (EditText)findViewById(R.id.txtDiscountPercent);
        edtDiscountPercent.setText("");
        edtDiscountPercent.setOnClickListener(this);

        tvDateBegin = (TextView) findViewById(R.id.txtDateBegin);
        tvDateBegin.setText(new SimpleDateFormat("dd - MM - yyyy").format(Calendar.getInstance().getTime()));
        tvDateBegin.setOnClickListener(this);

        tvTimeBegin = (TextView) findViewById(R.id.txtTimeBegin);
        tvTimeBegin.setOnClickListener(this);

        tvDateEnd = (TextView) findViewById(R.id.txtDateEnd);
        tvDateEnd.setText(new SimpleDateFormat("dd - MM - yyyy").format(Calendar.getInstance().getTime()));
        tvDateEnd.setOnClickListener(this);

        tvTimeEnd = (TextView) findViewById(R.id.txtTimeEnd);
        tvTimeEnd.setOnClickListener(this);

        btnSubmit = (Button) findViewById(R.id.btnSubmit);
        btnSubmit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        final int id = v.getId();
        if (v.getId() == spnDishName.getId()) {

        } else if (id == tvDateBegin.getId() || id == tvDateEnd.getId()) {
            Calendar currentDate = Calendar.getInstance();
            int year = currentDate.get(Calendar.YEAR);
            int month = currentDate.get(Calendar.MONTH);
            int day = currentDate.get(Calendar.DAY_OF_MONTH);
            DatePickerDialog datePicker;
            datePicker = new DatePickerDialog(AddDiscountActivity.this, 0,  new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker datePicker, int selectedYear, int selectedMonth, int selectedDay) {
                    String date = String.format("%02d - %02d - %d", selectedDay, selectedMonth + 1, selectedYear);
                    if (id == tvDateBegin.getId()) {
                        tvDateBegin.setText(date);
                        Calendar.getInstance().set(Calendar.DAY_OF_MONTH, selectedDay);
                        Calendar.getInstance().set(Calendar.MONTH, selectedMonth + 1);
                        Calendar.getInstance().set(Calendar.YEAR, selectedYear);
                        discount.setTimeStart(Calendar.getInstance().getTime());
                    } else {
                        tvDateEnd.setText(date);
                        Calendar.getInstance().set(Calendar.DAY_OF_MONTH, selectedDay);
                        Calendar.getInstance().set(Calendar.MONTH, selectedMonth + 1);
                        Calendar.getInstance().set(Calendar.YEAR, selectedYear);
                        discount.setTimeEnd(Calendar.getInstance().getTime());
                    }
                }
            }, year, month, day);//Yes 24 hour time

            if (id == tvDateBegin.getId()) {
                datePicker.setTitle("Ngày bắt đầu");
            } else {
                datePicker.setTitle("Ngày kết thúc");
            }
            datePicker.show();

        } else if (v.getId() == tvTimeBegin.getId() || v.getId() == tvTimeEnd.getId()) {
            Calendar currentTime = Calendar.getInstance();
            int hour = currentTime.get(Calendar.HOUR_OF_DAY);
            int minute = currentTime.get(Calendar.MINUTE);
            TimePickerDialog mTimePicker;
            mTimePicker = new TimePickerDialog(AddDiscountActivity.this, new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                    String time = String.format("%02d:%02d", selectedHour, selectedMinute);
                    if (id == tvTimeBegin.getId()) {
                        tvTimeBegin.setText(time);
                        Calendar.getInstance().set(Calendar.HOUR_OF_DAY, selectedHour);
                        Calendar.getInstance().set(Calendar.MINUTE, selectedMinute);
                        discount.setTimeStart(Calendar.getInstance().getTime());
                    } else {
                        tvTimeEnd.setText(time);
                        Calendar.getInstance().set(Calendar.HOUR_OF_DAY, selectedHour);
                        Calendar.getInstance().set(Calendar.MINUTE, selectedMinute);
                        discount.setTimeEnd(Calendar.getInstance().getTime());
                    }
                }
            }, hour, minute, true);//Yes 24 hour time

            if (id == tvTimeBegin.getId()) {
                mTimePicker.setTitle("Giờ bắt đầu");
            } else {
                mTimePicker.setTitle("Giờ kết thúc");
            }
            mTimePicker.show();

        }  else if (v.getId() == btnSubmit.getId()) {

            if (spnDishName.getSelectedItem() == null) {
                Toast.makeText(AddDiscountActivity.this, "Lỗi: chưa có món ăn nào, không thể tạo Discount!", Toast.LENGTH_LONG).show();
                finish();
                return;
            }

            if (edtDiscountPercent.getText().toString() == null) {
                Toast.makeText(AddDiscountActivity.this, "Vui lòng điền đầy đủ thông tin", Toast.LENGTH_LONG).show();
                return;
            }

            discount.setId_rest(restaurant.getId());
            discount.setNameDish(spnDishName.getSelectedItem().toString());
            discount.setDiscountPercent(Integer.parseInt(edtDiscountPercent.getText().toString()));

            try {
                discount.setTimeStart(new SimpleDateFormat("dd - MM - yyyy HH:mm").parse(tvDateBegin.getText().toString() + " " + tvTimeBegin.getText().toString()));
            } catch (ParseException e) {
                Toast.makeText(AddDiscountActivity.this, "Tạo thất bại", Toast.LENGTH_LONG).show();
                AddDiscountActivity.this.finish();
            }

            try {
                discount.setTimeEnd(new SimpleDateFormat("dd - MM - yyyy HH:mm").parse(tvDateEnd.getText().toString() + " " + tvTimeEnd.getText().toString()));
            } catch (ParseException e) {
                Toast.makeText(AddDiscountActivity.this, "Tạo thất bại", Toast.LENGTH_LONG).show();
                AddDiscountActivity.this.finish();
            }

            if (discount.getTimeStart().compareTo(discount.getTimeEnd()) >= 0) {
                Toast.makeText(AddDiscountActivity.this, "Lỗi: Thời gian không hợp lí!", Toast.LENGTH_LONG).show();
                return;
            }

            final ProgressDialog progressDialog = new ProgressDialog(AddDiscountActivity.this);
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.setMessage("Creating Discount");
            progressDialog.show();

            // tạo Discount
            FoodMapApiManager.createDiscount(discount, new TaskCompleteCallBack() {
                @Override
                public void OnTaskComplete(Object response) {
                    int code = (int) response;
                    progressDialog.dismiss();
                    if (code == ConstantCODE.SUCCESS) {
                        AddDiscountActivity.this.finish();
                        Toast.makeText(getBaseContext(), "Tạo thành công", Toast.LENGTH_LONG).show();
                        return;
                    } else if (code == FoodMapApiManager.FAIL_INFO) {
                        Toast.makeText(AddDiscountActivity.this, "Kiểm tra lại thông tin vừa nhập", Toast.LENGTH_LONG).show();
                    } else if (code == ConstantCODE.NOTINTERNET) {
                        Toast.makeText(AddDiscountActivity.this, "Kiểm tra kết nối mạng", Toast.LENGTH_LONG).show();
                    }
                }
            });
        } else {


        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return true;
    }
}
