package com.mobilelive.etee.mobilelive.network;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Build;


public class TaskHandler {
    @SuppressLint("NewApi")
    public static void executeAsync(AsyncTask asyncTask) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR1) {
            asyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, (Void) null);
        } else {
            asyncTask.execute((Void) null);
        }
    }
}
