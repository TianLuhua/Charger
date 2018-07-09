package com.example.administrator.changer.reciver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.example.administrator.changer.utils.ActivityUtils;

import java.io.IOException;

import static com.example.administrator.changer.Config.CHARGER_PACKAGENAME;

/**
 * Created by Tianluhua on 2018\7\9 0009.
 */
public class ChargerReciver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.e("tlh", "ChargerReciver---onReceive:" + intent.getAction());
        if (intent == null)
            return;
        switch (intent.getAction()) {
            case Intent.ACTION_POWER_CONNECTED:
                ActivityUtils.startApplicationWithPackageName(context, CHARGER_PACKAGENAME);
                break;
        }

    }
}
