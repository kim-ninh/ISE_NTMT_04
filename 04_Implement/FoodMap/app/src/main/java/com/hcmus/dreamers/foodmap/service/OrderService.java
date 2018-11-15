package com.hcmus.dreamers.foodmap.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.github.nkzawa.socketio.client.Socket;
import com.hcmus.dreamers.foodmap.websocket.OrderSocket;

public class OrderService extends Service {
    private Socket socket;
    @Override
    public void onCreate() {
        super.onCreate();
        socket = OrderSocket.getInstance();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String email = intent.getStringExtra("email");
        socket.emit("register", email);
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
