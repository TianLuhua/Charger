package com.example.administrator.changer;

import com.example.administrator.changer.callback.ImageCallBack;
import com.example.administrator.changer.utils.ThreadPoolManager;
import com.example.administrator.charger.R;

/**
 * Created by Tianluhua on 2018\7\5 0005.
 */
public class MainActivityPresenter {

    private ImageCallBack callBack;

    public MainActivityPresenter(ImageCallBack callBack) {
        this.callBack = callBack;
    }

    public void getImageData() {
        ThreadPoolManager.newInstance().addExecuteTask(new Runnable() {
            @Override
            public void run() {
                final int[] IMAGE_RESOURCES = {
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
                callBack.setImageDatas(IMAGE_RESOURCES);

            }
        });
    }
}
