package com.hcmus.dreamers.foodmap.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import com.hcmus.dreamers.foodmap.Model.FavorRestInfo;
import com.hcmus.dreamers.foodmap.R;

import java.util.ArrayList;
import java.util.List;

public class FavorRestNameAutocompleteAdapter extends ArrayAdapter<FavorRestInfo> {
    private Context context;
    private int resource;
    private List<FavorRestInfo> favorRestInfoList;
    private List<FavorRestInfo> favorRestInfoListAll;

    public FavorRestNameAutocompleteAdapter(@NonNull Context context, int resource, @NonNull List<FavorRestInfo> objects) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
        //it will be modified when inputting text (it will contain the suggestion texts, it depends on objects)
        this.favorRestInfoList = objects;
        this.favorRestInfoListAll = new ArrayList<>(objects);
    }

    Filter nameFilter = new Filter() {
        @Override
        public CharSequence convertResultToString(Object resultValue) {
            return ((FavorRestInfo)resultValue).getName();
        }

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<FavorRestInfo> suggestions = new ArrayList<>();
            FilterResults filterResults = new FilterResults();
            if(constraint != null){
                suggestions.clear();
                for(FavorRestInfo item : favorRestInfoListAll){
                    if(item.getName().toLowerCase().startsWith(constraint.toString().toLowerCase())){
                        suggestions.add(item);
                    }
                }

                filterResults.values = suggestions;
                filterResults.count = suggestions.size();
            }
            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            clear();
            if(results != null && results.count > 0){
                addAll((ArrayList<FavorRestInfo>)results.values);
            }
            else{
                addAll(favorRestInfoList);
            }
            notifyDataSetChanged();
        }
    };

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View row = LayoutInflater.from(context).inflate(resource, null);

        TextView txtFavorName = row.findViewById(R.id.txtFavorRestName);

        txtFavorName.setText(favorRestInfoList.get(position).getName());

        return row;
    }

    @Override
    public int getCount() {
        return favorRestInfoList.size();
    }

    @Nullable
    @Override
    public FavorRestInfo getItem(int position) {
        return favorRestInfoList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @NonNull
    @Override
    public Filter getFilter() {
        return nameFilter;
    }


}
