package com.ids.idsuserapp.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;

import com.ids.idsuserapp.db.entity.Mappa;
import com.ids.idsuserapp.db.repository.ArcoRepository;
import com.ids.idsuserapp.db.repository.BeaconRepository;
import com.ids.idsuserapp.db.repository.MappaRepository;

import java.util.ArrayList;

public class ArcoViewModel extends AndroidViewModel {
    private MappaRepository mMappaRepository;
    private BeaconRepository mBeaconRepository;
    private ArcoRepository mArcoRepository;
    private LiveData<ArrayList<Mappa>> mMapBeacons;
    public static String[] CAMPI = {"id", "begin", "end", "length", "width", "stairs", "v", "i", "c", "los"};

    public ArcoViewModel(Application application) {
        super(application);
        mArcoRepository = new ArcoRepository(application);
    }

}
