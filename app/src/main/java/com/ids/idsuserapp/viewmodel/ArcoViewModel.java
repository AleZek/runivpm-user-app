package com.ids.idsuserapp.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;

import com.ids.idsuserapp.db.entity.Arco;
import com.ids.idsuserapp.db.entity.Tronco;
import com.ids.idsuserapp.db.repository.ArcoRepository;

import java.util.List;

public class ArcoViewModel extends AndroidViewModel {
    private ArcoRepository mArcoRepository;
    public static String[] CAMPI = {"id", "begin", "end", "length", "width", "stairs", "v", "i", "c", "los"};

    public ArcoViewModel(Application application) {
        super(application);
        mArcoRepository = new ArcoRepository(application);
    }

    public void insert(Arco arco) { mArcoRepository.insert(arco); }

    public List<Tronco> getTronchi() { return mArcoRepository.getTronchi(); }

    public void deleteAll() {
        mArcoRepository.deleteAll();
    }
}
