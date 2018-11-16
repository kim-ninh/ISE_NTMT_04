package com.hcmus.dreamers.foodmap.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.text.style.ParagraphStyle;

import com.github.nkzawa.socketio.client.Socket;
import com.hcmus.dreamers.foodmap.AsyncTask.TaskCompleteCallBack;
import com.hcmus.dreamers.foodmap.MainActivity;
import com.hcmus.dreamers.foodmap.Model.Offer;
import com.hcmus.dreamers.foodmap.View.NotificationBuilder;
import com.hcmus.dreamers.foodmap.common.FoodMapApiManager;
import com.hcmus.dreamers.foodmap.define.ConstantCODE;
import com.hcmus.dreamers.foodmap.jsonapi.ParseJSON;
import com.hcmus.dreamers.foodmap.websocket.OrderEmitterListener;
import com.hcmus.dreamers.foodmap.websocket.OrderSocket;

import org.json.JSONException;
import org.json.JSONObject;

public class OrderService extends Service {
    private Socket socket;
    private Context context;
    private OrderEmitterListener receiceOrder = new OrderEmitterListener(new TaskCompleteCallBack() {
        @Override
        public void OnTaskComplete(Object response) {
            try {
                final Offer offer = ParseJSON.parseOfferObject(response.toString());
                JSONObject resp = new JSONObject(response.toString());
                FoodMapApiManager.addOrder(offer, resp.getInt("id_discount"), new TaskCompleteCallBack() {
                    @Override
                    public void OnTaskComplete(Object response) {
                        if((int)response == ConstantCODE.SUCCESS){
                            NotificationBuilder.ShowNotification(context, "Thông báo", offer.getGuestEmail() + " vừa mới đặt hàng.");
                            String content = "{\"email_guest\":" + offer.getGuestEmail() + ", \"status\":200, \"message\":\"Đặt hàng thành công!\"";
                            socket.emit("send_result", content);
                        }else{
                            String content = "{\"email_guest\":" + offer.getGuestEmail() + ", \"status\":500, \"message\":\"Đặt hàng thất bại!\"";
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
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String email = intent.getStringExtra("email");
        context = (Context) intent.getSerializableExtra("context");
        socket.emit("register", email);
        socket.on("receive_order", receiceOrder);
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        socket.disconnect();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
