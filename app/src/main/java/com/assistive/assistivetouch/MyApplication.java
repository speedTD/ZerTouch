package com.assistive.assistivetouch;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;
/*duy28*/
import com.assistive.assistivetouch.datasave.AppPreferences;
import com.znitenda.ZAndroidSDK;

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        AppPreferences.INSTANCE.init(this);
        ZAndroidSDK.initApplication(this, getPackageName());
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }
}
