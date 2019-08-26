package com.example.getnotifications;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.provider.Settings;
import android.service.notification.StatusBarNotification;
import android.util.Log;
import android.widget.Toast;
import android.telephony.PhoneNumberUtils;

public class MainActivity extends AppCompatActivity {
    public static final String TAG = "MainActivity";
    private static final String ACTION_NOTIFICATION_LISTENER_SETTINGS = "android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS";


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public void onStart() {
        showNotifications();
        super.onStart();
    }

    @Override
    public void onResume() {
        showNotifications();
        super.onResume();
    }

    public void showNotifications() {
        if (isNotificationServiceEnabled()) {
            Log.i(TAG, "Notification enabled -- trying to fetch it");
            getNotifications();
        } else {
            Log.i(TAG, "Notification disabled -- Opening settings");
            startActivity(new Intent(ACTION_NOTIFICATION_LISTENER_SETTINGS));
        }
    }

    public void getNotifications() {
        Log.i(TAG, "Waiting for MyNotificationService");
        MyNotificationService myNotificationService = MyNotificationService.get();
        Log.i(TAG, "Active Notifications: [");
        for (StatusBarNotification notification : myNotificationService.getActiveNotifications()) {
            Log.i(TAG, "    " + notification.getPackageName() + " / " + notification.getTag() + " / " + notification.getNotification().extras.getString("android.title") + " / " + notification.getNotification().extras.getString("android.text"));

        }
        Log.i(TAG, "]");
    }

    public static void whatsapp(Activity activity, String phone) {
        String formattedNumber = PhoneNumberUtils.formatNumber(phone);
        try{
            Intent sendIntent =new Intent("android.intent.action.MAIN");
            sendIntent.setComponent(new ComponentName("com.whatsapp", "com.whatsapp.Conversation"));
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.setType("text/plain");
            sendIntent.putExtra(Intent.EXTRA_TEXT,"");
            sendIntent.putExtra("jid", formattedNumber +"@s.whatsapp.net");
            sendIntent.setPackage("com.whatsapp");
            activity.startActivity(sendIntent);
        }
        catch(Exception e)
        {
            Toast.makeText(activity,"Error/n"+ e.toString(),Toast.LENGTH_SHORT).show();
        }
    }

    private boolean isNotificationServiceEnabled(){
        String pkgName = getPackageName();
        final String allNames = Settings.Secure.getString(getContentResolver(), "enabled_notification_listeners");
        if (allNames != null && !allNames.isEmpty()) {
            for (String name : allNames.split(":")) {
                if (getPackageName().equals(ComponentName.unflattenFromString(name).getPackageName())) {
                    return true;
                }
            }
        }
        return false;
    }
}
