package com.example.baseproject.di.module;

import android.app.Application;
import android.arch.persistence.room.Room;
import android.content.SharedPreferences;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.example.baseproject.Constants;
import com.example.baseproject.NetManager;
import com.example.baseproject.db.*;
import dagger.Module;
import dagger.Provides;
import okhttp3.Cache;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

import javax.inject.Singleton;
import java.text.SimpleDateFormat;

import static android.content.Context.MODE_PRIVATE;

@Module
public class NetModule {

    String mBaseUrl; //initialized in Application class
    private static final String TAG = "NetModule";

    // Constructor needs one parameter to instantiate.
    public NetModule(String baseUrl) {
        this.mBaseUrl = baseUrl;
    }

    // Dagger will only look for methods annotated with @Provides
    @Provides
    @Singleton
    // Application reference must come from AppModule.class
    SharedPreferences providesSharedPreferences(Application application) {
        return application.getSharedPreferences(Constants.SHARED_PREF.NAME, MODE_PRIVATE);
    }

    @Provides
    @Singleton
    Cache provideOkHttpCache(Application application) {
        int cacheSize = 10 * 1024 * 1024; // 10 MiB
        return new Cache(application.getCacheDir(), cacheSize);
    }

    @Provides
    @Singleton
    Gson provideGson() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES);
        return gsonBuilder.create();
    }

    @Provides
    @Singleton
    OkHttpClient provideOkHttpClient(Cache cache) {
        OkHttpClient.Builder client = new OkHttpClient.Builder();
        client.cache(cache);
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        // Can be Level.BASIC, Level.HEADERS, or Level.BODY
        // See http://square.github.io/okhttp/3.x/logging-interceptor/ to see the options.
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        client.networkInterceptors().add(httpLoggingInterceptor);
        return client.build();
    }

    @Provides
    @Singleton
    Retrofit provideRetrofit(Gson gson, OkHttpClient okHttpClient) {
        return new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl(mBaseUrl)
                .client(okHttpClient)
                .build();
        //return retrofit;
    }

    @Provides
    @Singleton
    ApiEndpointInterface provideApiEndpointInterface(Retrofit retrofit) {
        return retrofit.create(ApiEndpointInterface.class);
    }

    @Provides
    @Singleton
    SimpleDateFormat provideSimpleDateFormat() {
        return new SimpleDateFormat("dd MM yyyy HH:mm:ss");
    }

    @Provides
    @Singleton
    ObjectMapper provideObjectMapper() {
        ObjectMapper objectMapper=new ObjectMapper();
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        //objectMapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
        return objectMapper;
    }

    @Provides
    @Singleton
    AppDatabase provideAppDatabase(Application application){
        return Room.databaseBuilder(application, AppDatabase.class, Constants.DATABASE_NAME).build();
    }

    @Provides
    @Singleton
    LocalRepository provideLocalRepository(AppDatabase appDatabase){
        return new LocalRepository(appDatabase);
    }

    @Provides
    @Singleton
    RemoteRepository provideRemoteRepository(ApiEndpointInterface apiEndpointInterface, SharedPreferences preferences){
        return new RemoteRepository(apiEndpointInterface, preferences);
    }

    @Provides
    @Singleton
    NetManager provideNetManager(Application application){
        return new NetManager(application);
    }

    @Provides
    @Singleton
    Repository provideRepository(NetManager netManager, LocalRepository localRepository, RemoteRepository remoteRepository){
        return new Repository(netManager, localRepository, remoteRepository);
    }
}