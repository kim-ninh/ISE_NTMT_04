package com.hcmus.dreamers.foodmap_admin.asynctask;

import android.os.AsyncTask;
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
