package com.hcmus.dreamers.foodmap;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.hcmus.dreamers.foodmap.AsyncTask.DoingTask;
import com.hcmus.dreamers.foodmap.AsyncTask.TaskCompleteCallBack;
import com.hcmus.dreamers.foodmap.AsyncTask.TaskRequest;
import com.hcmus.dreamers.foodmap.Model.Catalog;
import com.hcmus.dreamers.foodmap.Model.Comment;
import com.hcmus.dreamers.foodmap.Model.Dish;
import com.hcmus.dreamers.foodmap.Model.Owner;
import com.hcmus.dreamers.foodmap.Model.Restaurant;
import com.hcmus.dreamers.foodmap.common.FoodMapApiManager;
import com.hcmus.dreamers.foodmap.common.GenerateRequest;
import com.hcmus.dreamers.foodmap.common.ResponseJSON;
import com.hcmus.dreamers.foodmap.define.ConstantCODE;
import com.hcmus.dreamers.foodmap.jsonapi.ParseJSON;

import org.osmdroid.util.GeoPoint;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class EditRestaurantActivity extends AppCompatActivity {


    Bundle transferData = new Bundle();
    List<Dish> dishes;
    Restaurant restaurant;
    DishInfoListAdapter adapter;
    int row;

    EditText txtResName;
    EditText txtAddress;
    EditText txtPhoneNumber;
    TextView lblOpenHour;
    TextView lblCloseHour;
    Toolbar toolbar;
    ListView dishListView;

    // arbitrary interprocess communication ID (just a nickname!)
    private final int IPC_ID = (int) (10001 * Math.random());


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_restaurant);
        //must not remove
        //getTransferDataFromActivity();

        takeReferenceFromResource();
        //restaurant = Owner.getInstance().getRestaurant(0);    //TODO Bỏ dòng comment khi đã xong phần SQLite
        //dishes = restaurant.getDishes();                      //TODO Như trên


        //region test without data f*ck my life
        try
        {
            restaurant = new Restaurant(4,
                    "",
                    "TEST",
                    "Bình Tân, HCM",
                    "09484783434",
                    "sea food restaurant",
                    "",
                    new SimpleDateFormat("hh:mm").parse("07:00"),
                    new SimpleDateFormat("hh:mm").parse("22:00"),
                    new GeoPoint(0.0,0.0));

            dishes = new ArrayList<>();   //Empty dish is passed
            dishes.add(new Dish("Banh trang tron",
                    5000,
                    "",
                    new Catalog(1, "Com")));

        }catch (Exception e){
            //..
        }
        //endregion debug



        //restaurant should not be null
        if (restaurant == null)
        {
            Toast.makeText(EditRestaurantActivity.this,
                    "Restaurant is null",
                    Toast.LENGTH_LONG).show();
            finish();
        }



        generateFakeDishList();
        putDataToViews();
        adapter = new DishInfoListAdapter(
                this,
                R.layout.row_dish_info,
                dishes
        );
        dishListView.setAdapter(adapter);
        justifyListViewHeightBasedOnChildren(dishListView);

        //Enable the Up button
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        // HANDLE Click event
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fabAddDish);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();

                //justifyListViewHeightBasedOnChildren(dishListView);       //TODO nhớ gọi hàm này sau khi đã thêm 1 món ăn
            }
        });


        dishListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Dish dish = dishes.get(position);
                transferData2NextActivity(dish ,position);
            }
        });
    }

    private void transferData2NextActivity(Dish dish, int position) {

        Gson gson = new Gson();
        Intent manageRest_manageDish = new Intent(
                EditRestaurantActivity.this,
                EditDishActivity.class);

        transferData.putString("dishJSON",gson.toJson(dish));
        transferData.putInt("restID", restaurant.getId());
        transferData.putInt("dishRow", position);

        manageRest_manageDish.putExtras(transferData);
        startActivityForResult(manageRest_manageDish, IPC_ID);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        try{
            if (IPC_ID == requestCode) {

                if (resultCode == Activity.RESULT_OK)
                {
                    Gson gson = new Gson();
                    Dish dish;
                    int dishRowID;

                    int delete = data.getIntExtra("delete", -1);
                    if(delete == -1){
                        String dishJSON =  data.getStringExtra("dishJSON");
                        dishRowID = data.getIntExtra("dishRow", -1);
                        dish = gson.fromJson(dishJSON, Dish.class);
                        dishes.set(dishRowID, dish);
                        Toast.makeText(EditRestaurantActivity.this, "Cập nhật thành công!", Toast.LENGTH_LONG).show();
                    }else{
                        dishes.remove(delete);
                        Toast.makeText(EditRestaurantActivity.this, "Xóa thành công!", Toast.LENGTH_LONG).show();
                    }
                    adapter.notifyDataSetChanged();
                }
            }
        }catch (Exception e){
            Toast.makeText(getBaseContext(),e.getMessage(), Toast.LENGTH_LONG)
                    .show();
        } //try
    }// onActivityResult



    //TODO must not remove
//    private void getTransferDataFromActivity() {
//        // Get the restaurant ID and dish Obj
//        Gson gson = new Gson();
//        Intent manageRest = getIntent();
//        transferData = manageRest.getExtras();
//        String dishJSON = transferData.getString("restJSON");
//        restaurant = gson.fromJson(dishJSON, Restaurant.class);
//        row = transferData.getInt("restRow", -1);
//    }

    private void putDataToViews() {

        DateFormat hourFormat = new SimpleDateFormat("hh:mm");
        String openingHour = hourFormat.format(restaurant.getTimeOpen());
        String closingHour = hourFormat.format(restaurant.getTimeClose());

        txtPhoneNumber.setText(restaurant.getPhoneNumber());
        txtResName.setText(restaurant.getName());
        txtAddress.setText(restaurant.getAddress());
        lblOpenHour.setText(openingHour);
        lblCloseHour.setText(closingHour);
    }

    private void takeReferenceFromResource() {
        toolbar = (Toolbar) findViewById(R.id.edit_restaurant_toolbar);
        dishListView = (ListView) findViewById(R.id.dish_list);
        txtAddress = (EditText) findViewById(R.id.txtAddress);
        txtResName = (EditText) findViewById(R.id.txtResName);
        txtPhoneNumber = (EditText) findViewById(R.id.txtPhoneNumber);
        lblCloseHour = (TextView) findViewById(R.id.closeHour);
        lblOpenHour = (TextView) findViewById(R.id.openHour);
    }

    private void generateFakeDishList() {
        dishes = new ArrayList<>();
        dishes.add(new Dish("Bánh tráng trộn",100000,"",new Catalog(1, "Cơm")));
        dishes.add(new Dish("Bánh tráng trộn",5000,"",new Catalog(1, "Cơm")));
        dishes.add(new Dish("Bánh tráng trộn",80000,"",new Catalog(1, "Cơm")));
        dishes.add(new Dish("Bánh tráng trộn",90000,"",new Catalog(1, "Cơm")));
        dishes.add(new Dish("Bánh tráng trộn",0,"",new Catalog(1, "Cơm")));
        dishes.add(new Dish("Bánh tráng trộn",70000,"",new Catalog(1, "Cơm")));
        dishes.add(new Dish("Bánh tráng trộn",10000,"",new Catalog(1, "Cơm")));
        dishes.add(new Dish("Bánh tráng trộn",20000,"",new Catalog(1, "Cơm")));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.action_delete:
//                FoodMapApiManager.deleteRestaurant(restaurant.getId(), new TaskCompleteCallBack() {
//                    @Override
//                    public void OnTaskComplete(Object response) {
//                        if((int)response == FoodMapApiManager.SUCCESS){
//                            Intent intent = new Intent();
//                            intent.putExtra("delete", row);
//                            setResult(RESULT_OK, intent);
//                            finish();
//                        }else if((int)response == ConstantCODE.NOTINTERNET){
//                            Toast.makeText(EditRestaurantActivity.this, "Không có kết nối INTERNET!", Toast.LENGTH_LONG).show();
//                        } else {
//                            Toast.makeText(EditRestaurantActivity.this, "Xóa món ăn thất bại!", Toast.LENGTH_LONG).show();
//                        }
//                    }
//                });
                return true;

            case R.id.action_done:
//                if(checkValid()){
//                    FoodMapApiManager.updateRestaurant(restaurant, new TaskCompleteCallBack() {
//                        @Override
//                        public void OnTaskComplete(Object response) {
//                            if((int)response == FoodMapApiManager.SUCCESS) {
//                                Gson gson = new Gson();
//                                Intent intent = new Intent();
//                                intent.putExtra("restJSON", gson.toJson(restaurant));
//                                intent.putExtra("restRow", row);
//                                setResult(RESULT_OK, intent);
//                                finish();
//                            }else if((int)response == ConstantCODE.NOTINTERNET){
//                                Toast.makeText(EditRestaurantActivity.this, "Không có kết nối INTERNET!", Toast.LENGTH_LONG).show();
//                            } else {
//                                Toast.makeText(EditRestaurantActivity.this, "Xóa nhà hàng thất bại!", Toast.LENGTH_LONG).show();
//                            }
//                        }
//                    });
//                }else{
//                    Toast.makeText(EditRestaurantActivity.this, "Hãy nhập đầy đủ thông tin!", Toast.LENGTH_LONG).show();
//                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private boolean checkValid(){
        if(txtResName.length() > 0 && txtAddress.length() > 0 && txtPhoneNumber.length() > 0){
            restaurant.setName(txtResName.getText().toString());
            restaurant.setPhoneNumber(txtPhoneNumber.getText().toString());
            restaurant.setAddress(txtAddress.getText().toString());
            return true;
        }
        return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.edit_item_menu,menu);
        return true;
    }
	
	public void UpdateInformation(int id_rest,
                                  String name,
                                  String address,
                                  String phoneNumber,
                                  String description,
                                  Date timeOpen,
                                  Date timeClose,
                                  GeoPoint location) {

        // Set the new Restaurant info
        final Restaurant newRestaurant = new Restaurant(id_rest, null, address, name,
                phoneNumber, description, null, timeOpen, timeClose, location);


        TaskRequest updateRestaurantInfoTask = new TaskRequest();

        // Implement call back
        updateRestaurantInfoTask.setOnCompleteCallBack(new TaskCompleteCallBack() {
            @Override
            public void OnTaskComplete(Object response) {
                try
                {
                    ResponseJSON responseJSON =  ParseJSON.parseFromAllResponse(response.toString());

                    // Pop-up the result message through Toast
                    if (ConstantCODE.SUCCESS == responseJSON.getCode()){
                        Toast.makeText(EditRestaurantActivity.this,
                                "Update successful!",
                                Toast.LENGTH_LONG).show();
                    }
                    else{
                        Toast.makeText(EditRestaurantActivity.this,
                                responseJSON.getMessage(),
                                Toast.LENGTH_LONG).show();
                    }

                }catch (Exception e){
                    Toast.makeText(EditRestaurantActivity.this,
                            e.getMessage(),
                            Toast.LENGTH_LONG).show();
                }
            }
        });

        // Invoke task
        updateRestaurantInfoTask.execute(new DoingTask(GenerateRequest
                .updateRestaurant(newRestaurant, Owner.getInstance().getToken())));
    }

    public void AddDish(int id_rest, Dish dish) {
        TaskRequest addDishTask = new TaskRequest();

        // Implement call back
        addDishTask.setOnCompleteCallBack(new TaskCompleteCallBack() {
            @Override
            public void OnTaskComplete(Object response) {
                try
                {
                    ResponseJSON responseJSON =  ParseJSON.parseFromAllResponse(response.toString());

                    // Pop-up the result message through Toast
                    if (ConstantCODE.SUCCESS == responseJSON.getCode()){
                        Toast.makeText(EditRestaurantActivity.this,
                                "Update successful!",
                                Toast.LENGTH_LONG).show();
                    }
                    else{
                        Toast.makeText(EditRestaurantActivity.this,
                                responseJSON.getMessage(),
                                Toast.LENGTH_LONG).show();
                    }

                }catch (Exception e){
                    Toast.makeText(EditRestaurantActivity.this,
                            e.getMessage(),
                            Toast.LENGTH_LONG).show();
                }
            }
        });

        // Invoke task
        addDishTask.execute(new DoingTask(GenerateRequest
                .createDish(id_rest, dish, Owner.getInstance().getToken())));
    }

    public void AddComment(int id_rest, Comment comment) {
        TaskRequest addComment = new TaskRequest();

        // Implement call back
        addComment.setOnCompleteCallBack(new TaskCompleteCallBack() {
            @Override
            public void OnTaskComplete(Object response) {
                try
                {
                    ResponseJSON responseJSON =  ParseJSON.parseFromAllResponse(response.toString());

                    // Pop-up the result message through Toast
                    if (ConstantCODE.SUCCESS == responseJSON.getCode()){
                        Toast.makeText(EditRestaurantActivity.this,
                                "Update successful!",
                                Toast.LENGTH_LONG).show();
                    }
                    else{
                        Toast.makeText(EditRestaurantActivity.this,
                                responseJSON.getMessage(),
                                Toast.LENGTH_LONG).show();
                    }

                }catch (Exception e){
                    Toast.makeText(EditRestaurantActivity.this,
                            e.getMessage(),
                            Toast.LENGTH_LONG).show();
                }
            }
        });

        // Invoke task
        addComment.execute(new DoingTask(GenerateRequest
                .comment(id_rest, comment, Owner.getInstance().getToken())));
    }

    //region
    //
    // This method is get the adapter of the listview and calculate it size when all items are show
    // Then we will have some thing like *ListView without Scrolling*
    //
    // Source: https://stackoverflow.com/questions/4338185/how-to-get-a-non-scrollable-listview
    //endregion
    // TODO REMEMBER to call this method every time the ListView adapter has changed (updated/deleted)
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
