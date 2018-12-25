package com.hcmus.dreamers.foodmap.AsyncTask;

import android.os.AsyncTask;

import com.hcmus.dreamers.foodmap.AsyncTask.TaskCompleteCallBack;

import java.util.HashMap;

public class TaskRequest extends AsyncTask<DoingTask, Void, Object> {
    private TaskCompleteCallBack onCompleteCallBack;

    public TaskRequest() {
    }

    @Override
    protected Object doInBackground(DoingTask... doingTasks) {
        return doingTasks[0].doInBackground();
    }

    @Override
    protected void onPostExecute(Object s) {
        super.onPostExecute(s);
        if (onCompleteCallBack != null)
            onCompleteCallBack.OnTaskComplete(s);
    }

    public void setOnCompleteCallBack(TaskCompleteCallBack onCompleteCallBack)
    {
        this.onCompleteCallBack = onCompleteCallBack;
    }
}
