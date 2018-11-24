package com.hcmus.dreamers.foodmap.service;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

import com.github.nkzawa.socketio.client.Socket;
import com.hcmus.dreamers.foodmap.AsyncTask.TaskCompleteCallBack;
import com.hcmus.dreamers.foodmap.Model.Offer;
import com.hcmus.dreamers.foodmap.Activity.OrderListActivity;
import com.hcmus.dreamers.foodmap.R;
import com.hcmus.dreamers.foodmap.common.FoodMapApiManager;
import com.hcmus.dreamers.foodmap.define.ConstantCODE;
import com.hcmus.dreamers.foodmap.jsonapi.ParseJSON;
import com.hcmus.dreamers.foodmap.websocket.OrderEmitterListener;
import com.hcmus.dreamers.foodmap.websocket.OrderSocket;

import org.json.JSONException;
import org.json.JSONObject;

public class OrderService extends Service {
    private Socket socket;
    private String name_rest;
    private int id_rest;
    private OrderEmitterListener receiceOrder = new OrderEmitterListener(new TaskCompleteCallBack() {
        @Override
        public void OnTaskComplete(Object response) {
            try {
                final Offer offer = ParseJSON.parseOfferObject(response.toString());
                JSONObject resp = new JSONObject(response.toString());
                name_rest = resp.getString("name_rest");
                id_rest = resp.getInt("id_rest");
                FoodMapApiManager.addOrder(offer, resp.getInt("id_discount"), new TaskCompleteCallBack() {
                    @Override
                    public void OnTaskComplete(Object response) {
                        if((int)response == ConstantCODE.SUCCESS){
                            //NotificationBuilder.ShowNotification(OrderService.this, "Thông báo", offer.getGuestEmail() + " vừa mới đặt hàng.");
                            notification("Quán ăn \"" + name_rest + "\"", offer.getGuestEmail() + " vừa mới đặt hàng.");
                            String content = "{\"email_guest\":\"" + offer.getGuestEmail() + "\", \"status\":200, \"message\":\"Đặt hàng thành công!\"}";
                            socket.emit("send_result", content);
                        }else{
                            String content = "{\"email_guest\":\"" + offer.getGuestEmail() + "\", \"status\":500, \"message\":\"Đặt hàng thất bại!\"}";
                            socket.emit("send_result", content);
                        }
                    }
                });
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    });
    @Override
    public void onCreate() {
        super.onCreate();
        socket = OrderSocket.getInstance();
        socket.connect();
        Toast.makeText(this, "connected", Toast.LENGTH_LONG).show();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String email = intent.getStringExtra("email");
        socket.emit("register", email);
        Toast.makeText(this, email + " registed", Toast.LENGTH_SHORT).show();
        socket.on("receive_order", receiceOrder);
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        socket.disconnect();
        OrderSocket.setSocket(null);
        Toast.makeText(this, "disconnected", Toast.LENGTH_SHORT).show();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    private void notification(String title, String content){
        Uri soundNotify = Uri.parse("android.resource://com.hcmus.dreamers.foodmap/" + R.raw.messenger_sound);;
        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.ic_notifications)
                        .setContentTitle(title)
                        .setContentText(content)
                        .setSound(soundNotify)
                        .setAutoCancel(true);;
        int NOTIFICATION_ID = 12345;

        Intent targetIntent = new Intent(this, OrderListActivity.class);
        targetIntent.putExtra("id_rest", id_rest);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, targetIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(contentIntent);
        NotificationManager nManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        nManager.notify(NOTIFICATION_ID, builder.build());
    }

}
