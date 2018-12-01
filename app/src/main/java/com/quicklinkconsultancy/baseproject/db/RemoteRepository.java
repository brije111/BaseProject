package com.quicklinkconsultancy.baseproject.db;

import android.content.SharedPreferences;
import com.quicklinkconsultancy.baseproject.db.model.Country;
import io.reactivex.Single;

import java.util.List;

public class RemoteRepository {

    private static final String TAG = "RemoteRepository";
    private ApiEndpointInterface apiEndpointInterface;
    private SharedPreferences preferences;

    public RemoteRepository(ApiEndpointInterface apiEndpointInterface, SharedPreferences preferences){
        this.apiEndpointInterface = apiEndpointInterface;
        this.preferences = preferences;
    }

    Single<List<Country>> getCountries(){
        return apiEndpointInterface.getCountries(4);
    }
}
