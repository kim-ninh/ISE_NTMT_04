package com.hcmus.dreamers.foodmap.websocket;

import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;
import com.hcmus.dreamers.foodmap.define.ConstantURL;

import java.net.URISyntaxException;

public class OrderSocket {
    private static Socket mSocket = null;

    private OrderSocket() {
    }

    public static Socket getInstance(){
            if(mSocket == null){
                try {
                    mSocket = IO.socket(ConstantURL.URLWEBSOCKET);
                }
                catch (URISyntaxException e)
                {
                    System.out.println("Can't create socket to websocket!");
                    return null;
                }
            }
        return mSocket;
    }

    public static boolean isNULL(){
        return (mSocket == null);
    }

    public static void setSocket(Socket socket){
        mSocket = socket;
    }

}
