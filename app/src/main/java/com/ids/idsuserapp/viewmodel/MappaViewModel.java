package com.ids.idsuserapp.viewmodel;

import android.app.Application;
import android.arch.lifecycle.LiveData;

import com.ids.idsuserapp.db.entity.Mappa;
import com.ids.idsuserapp.db.repository.BeaconRepository;
import com.ids.idsuserapp.db.repository.MappaRepository;

import java.util.ArrayList;
import java.util.List;


public class MappaViewModel extends AppViewModel<Mappa> {
    private MappaRepository mMappaRepository;
    private BeaconRepository mBeaconRepository;
    private LiveData<ArrayList<Mappa>> mMapBeacons;
    public static String[] CAMPI = {"name","id","image"};

    public MappaViewModel(Application application) {
        super(application);
        mMappaRepository = new MappaRepository(application);
        mBeaconRepository = new BeaconRepository(application);
    }

    public LiveData<List<Mappa>> getAllBeacons() { return mMappaRepository.getAll(); }

    public void insert(Mappa mappa) { mMappaRepository.insert(mappa); }

    public void update(Mappa mappa) {mMappaRepository.update(mappa);}

    public void deleteAll(){ mMappaRepository.deleteAll();}

    public Mappa find(int mappa_id) {
        return mMappaRepository.find(mappa_id);
    }

    public Mappa findByNome(String mappa_nome) {
        return mMappaRepository.findByNome(mappa_nome);
    }



    public LiveData<List<Mappa>> getAll() { return mMappaRepository.getAll(); }

    public String[] getCAMPI() {
        return CAMPI;
    }

    public void delete(Mappa mappa){ mMappaRepository.delete(mappa); }
}
