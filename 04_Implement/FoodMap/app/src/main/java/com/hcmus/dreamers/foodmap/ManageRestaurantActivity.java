package com.hcmus.dreamers.foodmap;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.zxing.client.result.EmailDoCoMoResultParser;
import com.hcmus.dreamers.foodmap.AsyncTask.DoingTask;
import com.hcmus.dreamers.foodmap.AsyncTask.TaskCompleteCallBack;
import com.hcmus.dreamers.foodmap.AsyncTask.TaskRequest;
import com.hcmus.dreamers.foodmap.Model.Comment;
import com.hcmus.dreamers.foodmap.Model.Dish;
import com.hcmus.dreamers.foodmap.Model.Owner;
import com.hcmus.dreamers.foodmap.Model.Restaurant;
import com.hcmus.dreamers.foodmap.common.GenerateRequest;
import com.hcmus.dreamers.foodmap.common.ResponseJSON;
import com.hcmus.dreamers.foodmap.define.ConstantCODE;
import com.hcmus.dreamers.foodmap.jsonapi.ParseJSON;

import org.osmdroid.util.GeoPoint;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ManageRestaurantActivity extends AppCompatActivity {

    List<Dish> dishes = new ArrayList<>();
    EditText txtResName;
    EditText txtAddress;
    EditText txtPhoneNumber;
    TextView lblOpenHour;
    TextView lblCloseHour;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //region DEFAULT INIT
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_restaurant);
        Toolbar toolbar = (Toolbar) findViewById(R.id.edit_restaurant_toolbar);
        ListView dishListView = (ListView) findViewById(R.id.dish_list);
        txtAddress = (EditText) findViewById(R.id.txtAddress);
        txtResName = (EditText) findViewById(R.id.txtResName);
        txtPhoneNumber = (EditText) findViewById(R.id.txtPhoneNumber);
        lblCloseHour = (TextView) findViewById(R.id.closeHour);
        lblOpenHour = (TextView) findViewById(R.id.openHour);
        //endregion

        //Enable the Up button
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Add_Floating_Button_Click event here
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fabAddDish);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        //region dish info -> dist_list_view
        //TODO Remove the comment below when the Owner has only ONE RESTAURANT!
//        dishes = Owner.getInstance()
//                .getRestaurant()
//                .getDishes();

        generateFakeDishList();   //TODO Remove this line when the data is ready!

        DishInfoListAdapter adapter = new DishInfoListAdapter(
                this,
                R.layout.row_dish_info,
                dishes
        );
        dishListView.setAdapter(adapter);
        //endregion

        //region List_View_item click event here
        //transfer dish object to the next activity
        dishListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Dish dish = dishes.get(position);
                //Restaurant restaurant = Owner.getInstance().getRestaurant();

                Intent manageRest_manageDish = new Intent(
                        ManageRestaurantActivity.this,
                        ManageDishActivity.class);

                Gson gson = new Gson();
                manageRest_manageDish.putExtra("dishJSON", gson.toJson(dish));
                //manageRest_manageDish.putExtra("restID", restaurant.getId());

                startActivity(manageRest_manageDish);
            }
        });
        //endregion


        //TODO Remove the comment below when the Owner has only ONE RESTAURANT!
        // Restaurant Obj -> Text View
//        Restaurant restaurant = Owner.getInstance().getRestaurant(0);
//
//        txtPhoneNumber.setText(restaurant.getPhoneNumber());
//        txtResName.setText(restaurant.getName());
//        txtAddress.setText(restaurant.getAddress());
//        lblOpenHour.setText(restaurant.getTimeOpen().toString());
//        lblCloseHour.setText(restaurant.getTimeClose().toString());
    }

    private void generateFakeDishList() {
        dishes.add(new Dish("Bánh tráng trộn",100000,"",null));
        dishes.add(new Dish("Bánh tráng trộn",5000,"",null));
        dishes.add(new Dish("Bánh tráng trộn",80000,"",null));
        dishes.add(new Dish("Bánh tráng trộn",90000,"",null));
        dishes.add(new Dish("Bánh tráng trộn",0,"",null));
        dishes.add(new Dish("Bánh tráng trộn",70000,"",null));
        dishes.add(new Dish("Bánh tráng trộn",10000,"",null));
        dishes.add(new Dish("Bánh tráng trộn",20000,"",null));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.action_delete:
                Toast.makeText(ManageRestaurantActivity.this, "action delete selected",
                        Toast.LENGTH_LONG).show();
                return true;

            case R.id.action_done:
                Toast.makeText(ManageRestaurantActivity.this, "action done selected",
                        Toast.LENGTH_LONG).show();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
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
                        Toast.makeText(ManageRestaurantActivity.this,
                                "Update successful!",
                                Toast.LENGTH_LONG).show();
                    }
                    else{
                        Toast.makeText(ManageRestaurantActivity.this,
                                responseJSON.getMessage(),
                                Toast.LENGTH_LONG).show();
                    }

                }catch (Exception e){
                    Toast.makeText(ManageRestaurantActivity.this,
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
                        Toast.makeText(ManageRestaurantActivity.this,
                                "Update successful!",
                                Toast.LENGTH_LONG).show();
                    }
                    else{
                        Toast.makeText(ManageRestaurantActivity.this,
                                responseJSON.getMessage(),
                                Toast.LENGTH_LONG).show();
                    }

                }catch (Exception e){
                    Toast.makeText(ManageRestaurantActivity.this,
                            e.getMessage(),
                            Toast.LENGTH_LONG).show();
                }
            }
        });

        // Invoke task
        addDishTask.execute(new DoingTask(GenerateRequest
                .createDish(id_rest, dish, Owner.getInstance().getToken())));
    }

    public void DeleteDish(int id_rest, String dishName) {
        TaskRequest deleteDish = new TaskRequest();

        // Implement call back
        deleteDish.setOnCompleteCallBack(new TaskCompleteCallBack() {
            @Override
            public void OnTaskComplete(Object response) {
                try
                {
                    ResponseJSON responseJSON =  ParseJSON.parseFromAllResponse(response.toString());

                    // Pop-up the result message through Toast
                    if (ConstantCODE.SUCCESS == responseJSON.getCode()){
                        Toast.makeText(ManageRestaurantActivity.this,
                                "Update successful!",
                                Toast.LENGTH_LONG).show();
                    }
                    else{
                        Toast.makeText(ManageRestaurantActivity.this,
                                responseJSON.getMessage(),
                                Toast.LENGTH_LONG).show();
                    }

                }catch (Exception e){
                    Toast.makeText(ManageRestaurantActivity.this,
                            e.getMessage(),
                            Toast.LENGTH_LONG).show();
                }
            }
        });

        // Invoke task
        deleteDish.execute(new DoingTask(GenerateRequest
                .deleteDish(id_rest, dishName, Owner.getInstance().getToken())));
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
                        Toast.makeText(ManageRestaurantActivity.this,
                                "Update successful!",
                                Toast.LENGTH_LONG).show();
                    }
                    else{
                        Toast.makeText(ManageRestaurantActivity.this,
                                responseJSON.getMessage(),
                                Toast.LENGTH_LONG).show();
                    }

                }catch (Exception e){
                    Toast.makeText(ManageRestaurantActivity.this,
                            e.getMessage(),
                            Toast.LENGTH_LONG).show();
                }
            }
        });

        // Invoke task
        addComment.execute(new DoingTask(GenerateRequest
                .comment(id_rest, comment, Owner.getInstance().getToken())));
    }
}
