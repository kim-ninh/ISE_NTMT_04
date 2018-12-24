package com.hcmus.dreamers.foodmap.AsyncTask;

import android.content.Context;
import android.os.AsyncTask;

import com.hcmus.dreamers.foodmap.Model.Catalog;
import com.hcmus.dreamers.foodmap.Model.Restaurant;
import com.hcmus.dreamers.foodmap.common.FoodMapApiManager;
import com.hcmus.dreamers.foodmap.database.DBManager;
import com.hcmus.dreamers.foodmap.define.ConstantCODE;

import java.text.ParseException;
import java.util.List;

public class LoadDataLocalTask extends AsyncTask<Void, Void, Boolean> {
    Context context;
    List<Restaurant> restaurants;
    List<Catalog> catalogs;
    TaskCompleteCallBack taskCompleteCallBack;

    public LoadDataLocalTask(Context context, List<Restaurant> restaurants, List<Catalog> catalogs, TaskCompleteCallBack taskCompleteCallBack) {
        this.context = context;
        this.restaurants = restaurants;
        this.catalogs = catalogs;
        this.taskCompleteCallBack = taskCompleteCallBack;
    }

    @Override
    protected Boolean doInBackground(Void... voids) {
        DBManager dbManager = new DBManager(context);

        try {
            restaurants.addAll(dbManager.getAllRestaurant());
            catalogs.addAll(dbManager.getAllCatalog());
        } catch (ParseException e) {
            restaurants = null;
            catalogs = null;
        }

        dbManager.close();
        if (restaurants == null)
            return false;
        return true;
    }

    @Override
    protected void onPostExecute(Boolean result) {
        if (result){
            taskCompleteCallBack.OnTaskComplete(ConstantCODE.SUCCESS);
        }
        else {
            taskCompleteCallBack.OnTaskComplete(ConstantCODE.NOTFOUND);
        }
    }
}
