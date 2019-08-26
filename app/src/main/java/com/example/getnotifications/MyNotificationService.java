package com.example.getnotifications;


import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.util.Log;

import java.util.concurrent.Semaphore;

public class MyNotificationService extends NotificationListenerService {
    private static final String TAG = "MyNotificationService";
    static MyNotificationService _this;
    static Semaphore sem = new Semaphore(0);

    public static MyNotificationService get() {
        sem.acquireUninterruptibly();
        MyNotificationService ret = _this;
        sem.release();
        return ret;
    }

    @Override
    public void onListenerConnected() {
        Log.i(TAG, "Connected");
        _this = this;
        sem.release();
    }

    @Override
    public void onListenerDisconnected() {
        Log.i(TAG, "Disconnected");
        sem.acquireUninterruptibly();
        _this = null;
    }

    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {

    }

    @Override
    public void onNotificationRemoved(StatusBarNotification sbn) {
        super.onNotificationRemoved(sbn);
    }
}
