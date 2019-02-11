package com.example.baseproject.di.component;

import com.example.baseproject.MainActivity;
import com.example.baseproject.di.module.AppModule;
import com.example.baseproject.di.module.NetModule;
import dagger.Component;

import javax.inject.Singleton;

@Singleton
@Component(modules={AppModule.class, NetModule.class})
public interface NetComponent {
    void inject(MainActivity activity);
    // void inject(MyFragment fragment);
    // void inject(MyService service);
}
