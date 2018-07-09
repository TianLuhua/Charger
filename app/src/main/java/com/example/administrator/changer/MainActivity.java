package com.example.administrator.changer;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.os.storage.StorageManager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.administrator.changer.utils.FastAnimationUtils;
import com.example.administrator.changer.utils.HideSystemUIUtils;
import com.example.administrator.charger.R;

public class MainActivity extends Activity {

    private ImageView btnMount;
    private ImageView changerImage;

    private ImageView baretty;

    private LinearLayout.LayoutParams linearParams;
    private TextView baretty_level;

    private Handler mAsyncStorageHandler;
    private StorageManager mStorageManager;
    private boolean mountFlag;
    private int mBatteryLevel;


    private final int CONNECTED = 0X000;
    private final int CONNECTING = 0X001;
    private final int DISCONNECTED = 0X002;
    private final int DISCONNECTING = 0X003;


    private boolean delayFlag = false;
    private boolean mounted = false;

    private FastAnimationUtils fastAnimationUtils;
    private final int ANIMATION_INTERVAL = 100;


    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

            switch (msg.what) {
                //已经连接
                case CONNECTED:
                    btnMount.setImageDrawable(MainActivity.this.getResources().getDrawable(R.mipmap.quxiao_btn_dis));
                    mounted = true;
                    delayFlag = false;
                    break;
                //正在连接
                case CONNECTING:
                    switchUsbMassStorage(true);
                    break;

                //已经断开
                case DISCONNECTED:
                    btnMount.setImageDrawable(MainActivity.this.getResources().getDrawable(R.mipmap.quxiao_btn));
                    mounted = false;
                    delayFlag = false;
                    break;
                //正在断开
                case DISCONNECTING:
                    switchUsbMassStorage(false);
                    break;
            }

        }
    };

    private final int[] IMAGE_RESOURCES = {
            R.mipmap.changer_01,
            R.mipmap.changer_02,
            R.mipmap.changer_03,
            R.mipmap.changer_04,
            R.mipmap.changer_05,
            R.mipmap.changer_06,
            R.mipmap.changer_07,
            R.mipmap.changer_08,
            R.mipmap.changer_09,
            R.mipmap.changer_10,
            R.mipmap.changer_11,
            R.mipmap.changer_12,
            R.mipmap.changer_13,
            R.mipmap.changer_14,
            R.mipmap.changer_15,
            R.mipmap.changer_16,
            R.mipmap.changer_17,
            R.mipmap.changer_18,
            R.mipmap.changer_19,
            R.mipmap.changer_20,
            R.mipmap.changer_21,
            R.mipmap.changer_22,
            R.mipmap.changer_23,
            R.mipmap.changer_24,
            R.mipmap.changer_25,
            R.mipmap.changer_26,
            R.mipmap.changer_27,
            R.mipmap.changer_28,
            R.mipmap.changer_29,
            R.mipmap.changer_30,
            R.mipmap.changer_31,
            R.mipmap.changer_32,
            R.mipmap.changer_33,
            R.mipmap.changer_34,
            R.mipmap.changer_35,
            R.mipmap.changer_36,
            R.mipmap.changer_37,
            R.mipmap.changer_38
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        HideSystemUIUtils.hideSystemUI(this);
        setContentView(R.layout.activity);
        Log.e("tlh", "onCreate");
        init();
    }

    private void init() {
        HandlerThread handlerThread = new HandlerThread("charger");
        handlerThread.start();
        mAsyncStorageHandler = new Handler(handlerThread.getLooper());
        if (mStorageManager == null) {
            mStorageManager = ((StorageManager) getSystemService(Context.STORAGE_SERVICE));
            if (mStorageManager == null) {
                Log.e("tlh", "Failed to get StorageManager");
            }
        }
        changerImage = findViewById(R.id.charageing_bg);
        baretty_level = findViewById(R.id.baretty_level);
        btnMount = findViewById(R.id.btnMount);
        btnMount.setOnClickListener(new MountOnClicListener(mHandler));
        baretty = findViewById(R.id.electric);
        baretty.setBackgroundResource(R.mipmap.battery);
        linearParams = (LinearLayout.LayoutParams) baretty.getLayoutParams();
        fastAnimationUtils = FastAnimationUtils.getInstance(changerImage);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e("tlh", "onResume");
        installIntentFilter();

        fastAnimationUtils.addAllFrames(IMAGE_RESOURCES, ANIMATION_INTERVAL);
        fastAnimationUtils.startAnimation();

    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.e("tlh", "onPause");
        unregisterReceiver(mReceiver);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e("tlh", "onDestroy");
    }


    private void installIntentFilter() {
        IntentFilter intentFilter = new IntentFilter("android.intent.action.ACTION_SHUTDOWN");
        intentFilter.addAction("android.intent.action.ACTION_POWER_DISCONNECTED");
        intentFilter.addAction("android.intent.action.BATTERY_CHANGED");
        intentFilter.addAction("android.intent.action.BATTERY_OKAY");
        registerReceiver(mReceiver, intentFilter);
    }

    private void switchUsbMassStorage(final boolean paramBoolean) {
        mAsyncStorageHandler.post(new Runnable() {
            public void run() {
                if (paramBoolean) {
//                    mStorageManager.enableUsbMassStorage();
                    Log.e("tlh", "switchUsbMassStorage,enableUsbMassStorage:" + paramBoolean);
                } else {
//                    mStorageManager.disableUsbMassStorage();
                    Log.e("tlh", "switchUsbMassStorage,disableUsbMassStorage:" + paramBoolean);
                }
            }
        });
    }


    private void setBarettylevel(int Level) {
        Log.e("tlh", "setBarettylevel:" + Level);
        if (Level < 10) {
            linearParams.height = (Level * 29);
            baretty.setLayoutParams(linearParams);
        }
        startAnimation(Level >= 10);
    }

    private void startAnimation(boolean chargerComplete) {
        if (chargerComplete) {
            Log.e("tlh", "充电完成");
            fastAnimationUtils.stopAnimation();
            changerImage.setImageDrawable(getResources().getDrawable(R.mipmap.changer_complete));
        } else {
            Log.e("tlh", "正在充电");
            fastAnimationUtils.startAnimation();
        }
    }


    //测试代码
//    int i;
//
//    public void test(View view) {
//        setBarettylevel(i++);
//        if (i > 10)
//            i = 0;
//
//    }


    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {

        public void onReceive(Context mContext, Intent intent) {
            String action = intent.getAction();

            Log.e("tlh", "action:" + action);

            if (("android.intent.action.BATTERY_CHANGED".equals(action)) || ("android.intent.action.BATTERY_OKAY".equals(action))) {
                mBatteryLevel = intent.getIntExtra("level", 10);
                int i = intent.getIntExtra("status", -1);
                if (i != -1) {
                    Log.e("tlh", "mBatteryLevel==" + mBatteryLevel);
                    setBarettylevel(mBatteryLevel / 10);
                    baretty_level.setText(mBatteryLevel + "%");
                }
            }

            if ("android.intent.action.ACTION_POWER_DISCONNECTED".equals(action)) {
              System.exit(0);
            }
            if (("android.intent.action.ACTION_SHUTDOWN".equals(action)) || (mountFlag)) {
                mountFlag = false;
                switchUsbMassStorage(false);
                Log.i("tlh", "shutdown System...");
            }
        }
    };

    private class MountOnClicListener implements View.OnClickListener {

        private Handler mHandler;

        public MountOnClicListener(Handler mHandler) {
            this.mHandler = mHandler;
        }

        public void onClick(View paramView) {
            if (delayFlag)
                return;
            Log.i("tlh", "onClick");
            delayFlag = true;
            if (!mounted) {
                btnMount.setImageDrawable(getResources().getDrawable(R.mipmap.quxiao_btninv));
                mHandler.sendEmptyMessage(CONNECTING);
                mHandler.sendEmptyMessageDelayed(CONNECTED, 3000);
            } else {
                btnMount.setImageDrawable(MainActivity.this.getResources().getDrawable(R.mipmap.quxiao_btn_disinv));
                mHandler.sendEmptyMessage(DISCONNECTING);
                mHandler.sendEmptyMessageDelayed(DISCONNECTED, 3000);
            }

        }
    }
}
