package com.npsdk.demo;

import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.multidex.MultiDexApplication;

import com.npsdk.LibListener;
import com.npsdk.demo.event.UserInfoEvent;
import com.npsdk.module.NPayLibrary;
import com.npsdk.module.model.SdkConfig;
import com.npsdk.module.model.UserInfo;
import com.npsdk.module.utils.Flavor;

import org.greenrobot.eventbus.EventBus;

public class MainApplication extends MultiDexApplication {

    public static final String TAG = MainApplication.class.getSimpleName();

    @Override
    public void onCreate() {
        super.onCreate();

        Log.d(TAG, "onCreate");



    }



}