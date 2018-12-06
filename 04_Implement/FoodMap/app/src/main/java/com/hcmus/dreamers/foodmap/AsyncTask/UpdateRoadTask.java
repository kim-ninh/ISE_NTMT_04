package com.hcmus.dreamers.foodmap.AsyncTask;

import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;

import com.hcmus.dreamers.foodmap.common.GenerateRequest;
import com.hcmus.dreamers.foodmap.common.ResponseJSON;
import com.hcmus.dreamers.foodmap.define.ConstantCODE;
import com.hcmus.dreamers.foodmap.jsonapi.ParseJSON;
import com.hcmus.dreamers.foodmap.map.LocationDirection;

import org.json.JSONException;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Polyline;

import java.util.ArrayList;
import java.util.List;

public class UpdateRoadTask extends AsyncTask<ArrayList<GeoPoint>, Void, LocationDirection> {

    private Context context;
    private MapView mapView;
    private Polyline roadOverlay;

    TaskCompleteCallBack onTaskCompleteCallBack;


    public UpdateRoadTask(Context context, MapView mapView, TaskCompleteCallBack onTaskCompleteCallBack)
    {
        this.context = context;
        this.mapView = mapView;
        roadOverlay = new Polyline();
        this.onTaskCompleteCallBack = onTaskCompleteCallBack;
    }

    @Override
    protected LocationDirection doInBackground(ArrayList<GeoPoint>... objects) {
        DoingTask doingTask = new DoingTask(GenerateRequest.directionMap(objects[0].get(0), objects[0].get(1)));
        String response = doingTask.doInBackground().toString();
        ResponseJSON responseJSON = ParseJSON.fromStringToResponeJSON(response);
        if (responseJSON.getCode() != ConstantCODE.NOTINTERNET){
            try {
                return ParseJSON.parseLocationDirection(response);
            } catch (Exception e) {
                return null;
            }
        }
        return null;
    }

    @Override
    protected void onPostExecute(LocationDirection response) {
        if (response != null)
        {
            roadOverlay.getPoints().addAll(response.toPoint());
            roadOverlay.setWidth(10);
            roadOverlay.setColor(Color.BLUE);
            roadOverlay.setGeodesic(true);
            mapView.getOverlays().add(roadOverlay);
            mapView.invalidate();
            onTaskCompleteCallBack.OnTaskComplete(ConstantCODE.SUCCESS);
        }
        else
        {
            onTaskCompleteCallBack.OnTaskComplete(ConstantCODE.NOTINTERNET);
        }
    }


    public void removePolyline() {
       mapView.getOverlays().clear();
    }
}
