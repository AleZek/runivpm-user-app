package com.ids.idsuserapp.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;

import com.ids.idsuserapp.db.dao.BeaconDao;
import com.ids.idsuserapp.db.entity.Beacon;
import com.ids.idsuserapp.db.repository.BeaconRepository;

import java.util.List;


public class BeaconViewModel extends AndroidViewModel {

    private BeaconRepository mBeaconRepository;
    public static String[] CAMPI = {"id","name","x","y","floor","width","mappa","device", "type", "meterx","metery"};

    public BeaconViewModel(Application application) {
        super(application);
        mBeaconRepository = new BeaconRepository(application);
    }

    public void deleteAll(){ mBeaconRepository.deleteAll();}

    public void insert(Beacon beacon) { mBeaconRepository.insert(beacon); }

    public LiveData<Beacon> find(int beacon_id) {
        return mBeaconRepository.find(beacon_id);
    }

    public Beacon trova(int beacon_id){
        return mBeaconRepository.trova(beacon_id);
    }


    public LiveData<List<Beacon>> getAllBeacons() { return mBeaconRepository.getAllBeacons(); }
    public LiveData<List<Beacon>> getBeaconByIdMappa(int mappa)
    {return mBeaconRepository.getBeaconByIdMappa(mappa);}
    public void delete(Beacon beacon){ mBeaconRepository.delete(beacon); }
    public void update(Beacon beacon) {mBeaconRepository.update(beacon);}

    public List<Beacon> getBeaconsByFloor(int floor, int x0, int y0, int radius) {
        return mBeaconRepository.getBeaconsByFloor(floor, x0, y0, radius);
    }

    public void deleteByMappa(int mappa_id) {
        mBeaconRepository.deleteByMappa(mappa_id);
    }
}