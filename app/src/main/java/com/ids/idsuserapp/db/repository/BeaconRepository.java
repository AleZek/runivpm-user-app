package com.ids.idsuserapp.db.repository;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import com.ids.idsuserapp.db.AppRoomDatabase;
import com.ids.idsuserapp.db.dao.BeaconDao;
import com.ids.idsuserapp.db.entity.Beacon;

import java.util.List;


public class BeaconRepository {
    private BeaconDao mBeaconDao;

    public BeaconRepository(Application application) {
        AppRoomDatabase db = AppRoomDatabase.getDatabase(application);
        mBeaconDao = db.beaconDao();
    }

    public void deleteByMappa(int mappa_id) {
        new DeleteByMappaAsyncTask(mBeaconDao).execute(mappa_id);
    }

    private static class insertAsyncTask extends AsyncTask<Beacon, Void, Void> {

        private BeaconDao mAsyncTaskDao;

        insertAsyncTask(BeaconDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Beacon... beacons) {
            mAsyncTaskDao.insert(beacons[0]);
            return null;
        }
    }

    public void deleteAll() {
        new BeaconRepository.deleteAllAsyncTask(mBeaconDao).execute();
    }
    public void update(Beacon beacon){
        new BeaconRepository.updateAsyncTask(mBeaconDao).execute(beacon);
    }


    private static class deleteAllAsyncTask extends AsyncTask<Beacon, Void, Void> {

        private BeaconDao mAsyncTaskDao;

        deleteAllAsyncTask(BeaconDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Beacon... params) {
            mAsyncTaskDao.delete(params[0]);
            return null;
        }
    }

    private static class DeleteByMappaAsyncTask extends AsyncTask<Integer, Void, Void> {

        private BeaconDao mAsyncTaskDao;

        DeleteByMappaAsyncTask(BeaconDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Integer... params) {
            mAsyncTaskDao.deleteByMap(params[0]);
            return null;
        }
    }

    private static class deleteAsyncTask extends AsyncTask<Beacon, Void, Void> {

        private BeaconDao mAsyncTaskDao;

        deleteAsyncTask(BeaconDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Beacon... params) {
            mAsyncTaskDao.delete(params[0]);
            return null;
        }
    }

    //map update AsyncTask
    private static class updateAsyncTask extends AsyncTask<Beacon, Void, Void> {

        private BeaconDao mAsyncTaskDao;

        updateAsyncTask(BeaconDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Beacon... params) {
            mAsyncTaskDao.update(params[0]);
            return null;
        }
    }

    public LiveData<Beacon> find(int beacon_id) {
        return mBeaconDao.find(beacon_id);
    }

    public Beacon trova(int beacon_id){
        return mBeaconDao.trova(beacon_id);
    }

    public void insert (Beacon beacon) {
        new BeaconRepository.insertAsyncTask(mBeaconDao).execute(beacon);
    }

    public LiveData<List<BeaconDao.BeaconWithMap>> getAll() {return mBeaconDao.getAll();}
    public LiveData<List<Beacon>> getAllBeacons() {return mBeaconDao.getAllBeacons();}
    public LiveData<List<Beacon>> getBeaconByIdMappa(int mappa)
    {return mBeaconDao.getBeaconByIdMappa(mappa);}

    public void delete (Beacon beacon){
        new BeaconRepository.deleteAsyncTask(mBeaconDao).execute(beacon);
    }
}