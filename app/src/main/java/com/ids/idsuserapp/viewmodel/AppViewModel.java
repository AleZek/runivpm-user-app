package com.ids.idsuserapp.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import java.util.List;

/**
 * Created by zek on 13/04/18.
 */

public abstract class AppViewModel<T> extends AndroidViewModel {
    public AppViewModel(@NonNull Application application) {
        super(application);
    }
    public abstract String[] getCAMPI();
    public abstract void insert(T t);
//    public abstract void update(T t);
    public abstract T find(int id);
    public abstract LiveData<List<T>> getAll();

}
