package com.hcmus.dreamers.foodmap.AsyncTask;

import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;

import org.osmdroid.bonuspack.routing.OSRMRoadManager;
import org.osmdroid.bonuspack.routing.Road;
import org.osmdroid.bonuspack.routing.RoadManager;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Polyline;

import java.util.ArrayList;

public class UpdateRoadTask extends AsyncTask<ArrayList<GeoPoint>, Void, Road> {

    private Context context;
    private MapView mapView;
    private Polyline roadOverlay;

    public UpdateRoadTask(Context context, MapView mapView)
    {
        this.context = context;
        this.mapView = mapView;
    }

    @Override
    protected Road doInBackground(ArrayList<GeoPoint>... objects) {
        ArrayList<GeoPoint> waypoints = objects[0];
        RoadManager roadManager = new OSRMRoadManager(context);

        return roadManager.getRoad(waypoints);
    }
    @Override
    protected void onPostExecute(Road road) {
        super.onPostExecute(road);

        roadOverlay = RoadManager.buildRoadOverlay(road);
        roadOverlay.setWidth(10);
        roadOverlay.setColor(Color.BLUE);
        roadOverlay.setGeodesic(true);
        mapView.getOverlays().add(roadOverlay);
        mapView.invalidate();
    }

    public void removePolyline() {
        roadOverlay.onDestroy();

    }
}
