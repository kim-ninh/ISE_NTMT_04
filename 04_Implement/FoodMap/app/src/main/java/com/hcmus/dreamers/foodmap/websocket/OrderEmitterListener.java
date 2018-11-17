package com.hcmus.dreamers.foodmap.websocket;

import com.github.nkzawa.emitter.Emitter;
import com.hcmus.dreamers.foodmap.AsyncTask.TaskCompleteCallBack;

public class OrderEmitterListener implements Emitter.Listener {
    private TaskCompleteCallBack taskCompleteCallBack;

    public OrderEmitterListener(TaskCompleteCallBack taskCompleteCallBack) {
        this.taskCompleteCallBack = taskCompleteCallBack;
    }

    @Override
    public void call(Object... args) {
        taskCompleteCallBack.OnTaskComplete(args[0].toString());
    }
}
