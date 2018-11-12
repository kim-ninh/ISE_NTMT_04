package com.study.android.testorderserver;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

import org.json.JSONObject;

import java.net.URISyntaxException;

public class MainActivity extends AppCompatActivity {
    private Socket msocket;
    private Button accept, order, rgGuest, rgOwner;

    {
        try {
            msocket = IO.socket("http://192.168.1.100:3000");
        } catch (URISyntaxException e) {}
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        msocket.connect();
        accept = (Button) findViewById(R.id.accept);
        order = (Button) findViewById(R.id.order);
        rgGuest = (Button) findViewById(R.id.rgGuest);
        rgOwner = (Button) findViewById(R.id.rgOwner);

        msocket.on("receive_order", onReceice);
        msocket.on("receive_result", onReceice);

        rgGuest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                msocket.emit("register", "chauhoangphuc@gmail.com");
            }
        });

        rgOwner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                msocket.emit("register", "nguyenvanphuoc@gmail.com");
            }
        });

        order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Order order = new Order("nguyenvanphuoc@gmail.com", "chauhoangphuc@gmail.com");
                msocket.emit("send_order", order.toString());
            }
        });


        accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Order order = new Order("nguyenvanphuoc@gmail.com", "chauhoangphuc@gmail.com");
                msocket.emit("send_result", order.toString());
            }
        });

    }

    private Emitter.Listener onReceice = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    String data = (String) args[0];
                    Toast.makeText(MainActivity.this, data, Toast.LENGTH_LONG).show();
                }
            });
        }
    };


}
