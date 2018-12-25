package com.hcmus.dreamers.foodmap.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hcmus.dreamers.foodmap.AsyncTask.DownloadImageTask;
import com.hcmus.dreamers.foodmap.Model.Discount;
import com.hcmus.dreamers.foodmap.Model.Dish;
import com.hcmus.dreamers.foodmap.Model.Restaurant;
import com.hcmus.dreamers.foodmap.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class DiscountListAdapter extends ArrayAdapter<Discount> {
    private Restaurant restaurant;
    HashMap<String, String> nameDishToUrlImage = null;
    private Context context;
    private List<Discount> discounts;

    public ArrayList<Integer> selectedIndex = null;
    private HashMap<Integer, Boolean> mSelection = new HashMap<Integer, Boolean>();
    final private int COLOR_SELECTED = 0xFF99CCFF;

    public DiscountListAdapter(@NonNull Context context, int resource, @NonNull List<Discount> discounts, Restaurant restaurant) {
        super(context, resource, discounts);
        this.context = context;
        this.discounts = discounts;
        this.restaurant = restaurant;

        selectedIndex = new ArrayList<>();
        List<Dish> dishes = this.restaurant.getDishes();
        nameDishToUrlImage = new HashMap<>();

        for (Dish dish : dishes) {
            nameDishToUrlImage.put(dish.getName(), dish.getUrlImage());
        }
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = ((Activity) context).getLayoutInflater();
        View view = inflater.inflate(R.layout.item_discount_list, null);

        LinearLayout list_row = (LinearLayout) view.findViewById(R.id.list_row);
        ImageView dishThumb = (ImageView) view.findViewById(R.id.iv_image_dish);
        TextView dishName = (TextView) view.findViewById(R.id.txtDishName);
        TextView discountPercent = (TextView) view.findViewById(R.id.txt_discount_percent);
        TextView timeStart = (TextView)view.findViewById(R.id.txt_time_start);
        TextView timeEnd = (TextView)view.findViewById(R.id.txt_time_end);

        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy HH:mm");

        Discount discount = discounts.get(position);
        dishName.setText(discount.getNameDish());
        discountPercent.setText(String.valueOf(discount.getDiscountPercent() + "%"));
        timeStart.setText(df.format(discount.getTimeStart()));
        timeEnd.setText(df.format(discount.getTimeEnd()));


        DownloadImageTask taskDownload = new DownloadImageTask(dishThumb, getContext());
        if (nameDishToUrlImage.get(discount.getNameDish()) != null) {
            taskDownload.loadImageFromUrl(nameDishToUrlImage.get(discount.getNameDish()));
        }

        //if (!selectedIndex.isEmpty()) {
        //    list_row.setBackgroundColor(selectedIndex.contains(position) ? COLOR_SELECTED : 0xFFFAFAFA);
        //}
        view.setBackgroundColor(0xFFFAFAFA);
        if (mSelection.get(position) != null) {
            view.setBackgroundColor(COLOR_SELECTED);// this is a selected position so make it red
        }

        return view;
    }

    @Override
    public int getCount() {
        return discounts.size();
    }

    @Nullable
    @Override
    public Discount getItem(int position) {
        return discounts.get(position);
    }


    @Override
    public long getItemId(int position) {
        return position;
    }

    public void setDiscounts(List<Discount> discounts) {
        this.discounts = discounts;
    }

    public boolean isPositionChecked(int position) {
        Boolean result = mSelection.get(position);
        return result == null ? false : result;
    }

    public void setNewSelection(int position, boolean value) {
        mSelection.put(position, value);
        notifyDataSetChanged();
    }

    public void removeSelection(int position) {
        mSelection.remove(position);
        notifyDataSetChanged();
    }

    public void clearSelection() {
        mSelection = new HashMap<Integer, Boolean>();
        notifyDataSetChanged();
    }

    public List<Integer> getSelectedIndex() {
        List<Integer> ids = new ArrayList<>();
        for (Integer index : mSelection.keySet()) {
            if (mSelection.get(index)) {
                ids.add(index);
            }
        }

        Collections.sort(ids, Collections.<Integer>reverseOrder());

        return ids;
    }
}
