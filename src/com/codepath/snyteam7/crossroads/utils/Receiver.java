package com.codepath.snyteam7.crossroads.utils;

import android.content.Context;
import android.content.Intent;

import com.codepath.snyteam7.crossroads.activities.LoginActivity;
import com.parse.ParsePushBroadcastReceiver;

public class Receiver extends ParsePushBroadcastReceiver {

    @Override
    public void onPushOpen(Context context, Intent intent) {
        Intent i = new Intent(context, LoginActivity.class);
        i.putExtras(intent.getExtras());
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(i);
    }

}
