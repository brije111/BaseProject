package com.quicklinkconsultancy.baseproject.db;

import com.quicklinkconsultancy.baseproject.NetManager;
import com.quicklinkconsultancy.baseproject.db.model.Country;
import io.reactivex.Single;

import java.util.List;

public class Repository {
    private NetManager netManager;
    private LocalRepository localRepository;
    private RemoteRepository remoteRepository;

    public Repository(NetManager netManager, LocalRepository localRepository, RemoteRepository remoteRepository){
        this.netManager = netManager;
        this.localRepository = localRepository;
        this.remoteRepository = remoteRepository;
    }

    public Single<List<Country>> getCountries(){
        if ( netManager.isConnectedToInternet())
            return remoteRepository.getCountries();
        else return localRepository.getCountries();
    }

    /*
        methods saving data in local
     */
    public void saveCountryLocal(List<Country> list){
        localRepository.saveCountryLocal(list);
    }
}
