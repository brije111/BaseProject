package com.example.baseproject;

import android.app.Application;
import com.example.baseproject.di.component.NetComponent;
import com.example.baseproject.di.module.AppModule;
import com.example.baseproject.di.module.NetModule;

public class MyApplication extends Application {

    private NetComponent netComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        netComponent = DaggerNetComponent.builder()
                .appModule(new AppModule(this))
                .netModule(new NetModule("122.160.45.128:3030/api/"))
                .build();

    }

    public NetComponent getNetComponent() {
        return netComponent;
    }
}
