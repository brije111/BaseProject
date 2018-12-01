package com.quicklinkconsultancy.baseproject.di.component;

import com.quicklinkconsultancy.baseproject.MainActivity;
import com.quicklinkconsultancy.baseproject.di.module.AppModule;
import com.quicklinkconsultancy.baseproject.di.module.NetModule;
import dagger.Component;

import javax.inject.Singleton;

@Singleton
@Component(modules={AppModule.class, NetModule.class})
public interface NetComponent {
    void inject(MainActivity activity);
    // void inject(MyFragment fragment);
    // void inject(MyService service);
}
