package com.hcmus.dreamers.foodmap.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;


import com.google.gson.Gson;
import com.hcmus.dreamers.foodmap.AddDishActivity;
import com.hcmus.dreamers.foodmap.EditDishActivity;
import com.hcmus.dreamers.foodmap.EditRestaurantActivity;
import com.hcmus.dreamers.foodmap.Model.Catalog;
import com.hcmus.dreamers.foodmap.Model.Dish;
import com.hcmus.dreamers.foodmap.Model.Restaurant;
import com.hcmus.dreamers.foodmap.R;
import com.hcmus.dreamers.foodmap.adapter.DishInfoListAdapter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class DishListFragment extends Fragment {

    List<Dish> dishes;
    Restaurant restaurant;

    Context context = null;
    EditRestaurantActivity editRestaurantActivity;


    ListView dishListView;
    DishInfoListAdapter adapter;
    FloatingActionButton fab;
    CoordinatorLayout rootLayout;

    // arbitrary interprocess communication ID (just a nickname!)
    final int IPC_ID = (int) (10001 * Math.random());
    final int AFA_ID = 1009;

    public DishListFragment(){
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        // Get host activity "EditRestaurant"
        context = getActivity();
        editRestaurantActivity = (EditRestaurantActivity) getActivity();

        if (editRestaurantActivity != null)
        {
            restaurant = editRestaurantActivity.restaurant;

            //Null pointer is already check in host activity
            dishes = restaurant.getDishes();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootLayout = (CoordinatorLayout) inflater.inflate(
                R.layout.fragment_dish_list, container, false);

        setup();
        handleClickEvnet();

        return  rootLayout;
    }

    private void setup() {
        dishListView = (ListView) rootLayout.findViewById(R.id.dish_list);
        fab = (FloatingActionButton) rootLayout.findViewById(R.id.fabAddDish);

        adapter = new DishInfoListAdapter(context,
                R.layout.adapter_dish_info_list, dishes
        );

        dishListView.setAdapter(adapter);
    }

    private void handleClickEvnet() {
        // Chọn 1 món ăn từ danh sách
        dishListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Dish dish = dishes.get(position);
                transferData2NextActivity(dish ,position);
            }
        });

        // Nút Thêm món ăn mới
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(context, AddDishActivity.class);
               intent.putExtra("rest", (Serializable)restaurant);
               startActivityForResult(intent, AFA_ID);
            }
        });
    }

    private void transferData2NextActivity(Dish dish, int position) {

        Bundle transferData = new Bundle();
        Gson gson = new Gson();
        Intent manageRest_manageDish = new Intent(
                context,
                EditDishActivity.class);

        transferData.putString("dishJSON",gson.toJson(dish));
        transferData.putInt("restID", restaurant.getId());
        transferData.putInt("dishRow", position);

        manageRest_manageDish.putExtras(transferData);
        startActivityForResult(manageRest_manageDish, IPC_ID);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
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
                        Toast.makeText(context, "Cập nhật thành công!", Toast.LENGTH_LONG).show();
                    }else{
                        dishes.remove(delete);
                        Toast.makeText(context, "Xóa thành công!", Toast.LENGTH_LONG).show();
                    }
                    adapter.notifyDataSetChanged();
                }
            }
            else if (requestCode == AFA_ID && resultCode == Activity.RESULT_OK){

                Gson gson = new Gson();
                Dish dish;

                String dishJSON =  data.getStringExtra("dishJSON");
                dish = gson.fromJson(dishJSON, Dish.class);

                dishes.add(dish);
                adapter.notifyDataSetChanged();
            }
        }catch (Exception e){
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG)
                    .show();
        } //try
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
}
