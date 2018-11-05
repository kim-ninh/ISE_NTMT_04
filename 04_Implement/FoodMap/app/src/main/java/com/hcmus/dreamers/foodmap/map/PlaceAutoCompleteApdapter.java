package com.hcmus.dreamers.foodmap.map;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hcmus.dreamers.foodmap.Model.DetailAddress;
import com.hcmus.dreamers.foodmap.R;

import java.util.List;

public class PlaceAutoCompleteApdapter extends ArrayAdapter<DetailAddress> {
    Context context;
    List<DetailAddress> detailAddresses;
    int resource;

    public PlaceAutoCompleteApdapter(Context context ,int resource, List<DetailAddress> detailAddresses) {
        super(context, resource, detailAddresses);

        this.context = context;
        this.resource = resource;
        this.detailAddresses = detailAddresses;
    }

    @Override
    public int getCount() {
        return detailAddresses.size();
    }

    @Override
    public DetailAddress getItem(int position) {
        return detailAddresses.get(position);
    }

    @Override
    public View getView(int position, View convertView,  ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(resource, null);
        TextView txtName;
        TextView txtAddress;

        txtName = (TextView)view.findViewById(R.id.txtName);
        txtAddress = (TextView)view.findViewById(R.id.txtAddress);

        txtName.setText(detailAddresses.get(position).getName());
        txtAddress.setText(detailAddresses.get(position).toString());

        return  view;
    }
}
