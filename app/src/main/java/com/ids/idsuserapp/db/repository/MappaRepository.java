package com.ids.idsuserapp.db.repository;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import com.ids.idsuserapp.db.AppRoomDatabase;
import com.ids.idsuserapp.db.dao.MappaDao;
import com.ids.idsuserapp.db.entity.Beacon;
import com.ids.idsuserapp.db.entity.Mappa;

import java.util.ArrayList;
import java.util.List;

public class MappaRepository {
    private MappaDao mMappaDao;
    private LiveData<ArrayList<Beacon>> mBeaconMappa;

    public MappaRepository(Application application) {
        AppRoomDatabase db = AppRoomDatabase.getDatabase(application);
        mMappaDao = db.mappaDao();

    }

    public void deleteAll() {
        new MappaRepository.deleteAllAsyncTask(mMappaDao).execute();
    }

    private static class insertAsyncTask extends AsyncTask<Mappa, Void, Void> {

        private MappaDao mAsyncTaskDao;

        insertAsyncTask(MappaDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Mappa... params) {
            mAsyncTaskDao.insert(params[0]);
            return null;
        }
    }

    private static class deleteAllAsyncTask extends AsyncTask<Mappa, Void, Void> {

        private MappaDao mAsyncTaskDao;

        deleteAllAsyncTask(MappaDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Mappa... params) {
            mAsyncTaskDao.deleteAll();
            return null;
        }
    }




   //chiedere se per cancellare una mappa va bene così

    private static class deleteAsyncTask extends AsyncTask<Mappa, Void, Void> {

        private MappaDao mAsyncTaskDao;

        deleteAsyncTask(MappaDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Mappa... params) {
            mAsyncTaskDao.delete(params[0]);
            return null;
        }
    }

    //map update AsyncTask
    private static class updateAsyncTask extends AsyncTask<Mappa, Void, Void> {

        private MappaDao mAsyncTaskDao;

        updateAsyncTask(MappaDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Mappa... params) {
            mAsyncTaskDao.update(params[0]);
            return null;
        }
    }




    public Mappa find(int mappa_id) {
        return mMappaDao.find(mappa_id);
    }

    public void insert (Mappa mappa) {
        new MappaRepository.insertAsyncTask(mMappaDao).execute(mappa);
    }


    public void delete (Mappa mappa){
        new MappaRepository.deleteAsyncTask(mMappaDao).execute(mappa);
    }

    public void update(Mappa mappa){
        new MappaRepository.updateAsyncTask(mMappaDao).execute(mappa);
    }


    public LiveData<List<Mappa>> getAll() {
        return mMappaDao.getAll();
    }

    public LiveData<ArrayList<Beacon>> getmBeaconMappa() {
        return mBeaconMappa;
    }
}
