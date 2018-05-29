package com.ids.idsuserapp.db.repository;

import android.app.Application;
import android.os.AsyncTask;

import com.ids.idsuserapp.db.AppRoomDatabase;
import com.ids.idsuserapp.db.dao.ArcoDao;
import com.ids.idsuserapp.db.entity.Arco;
import com.ids.idsuserapp.db.entity.Tronco;

public class ArcoRepository {
    private ArcoDao mArcoDao;

    public ArcoRepository(Application application) {
        AppRoomDatabase db = AppRoomDatabase.getDatabase(application);
        mArcoDao = db.arcoDao();
    }

    /* public void deleteByMappa(int mappa_id) {
         new ArcoRepository.DeleteByMappaAsyncTask(mArcoDao).execute(mappa_id);
     }
 */
    private static class insertAsyncTask extends AsyncTask<Arco, Void, Void> {

        private ArcoDao mAsyncTaskDao;

        insertAsyncTask(ArcoDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Arco... arcos) {
            mAsyncTaskDao.insert(arcos[0]);
            return null;
        }
    }

    public void deleteAll() {
        new ArcoRepository.deleteAsyncTask(mArcoDao).execute();
    }
    public void update(Arco arco){
        new ArcoRepository.updateAsyncTask(mArcoDao).execute(arco);
    }



    private static class deleteAsyncTask extends AsyncTask<Arco, Void, Void> {

        private ArcoDao mAsyncTaskDao;

        deleteAsyncTask(ArcoDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Arco... params) {
            mAsyncTaskDao.delete(params[0]);
            return null;
        }
    }

    //map update AsyncTask
    private static class updateAsyncTask extends AsyncTask<Arco, Void, Void> {

        private ArcoDao mAsyncTaskDao;

        updateAsyncTask(ArcoDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Arco... params) {
            mAsyncTaskDao.update(params[0]);
            return null;
        }
    }

    public Arco find(int arco_id) {
        return mArcoDao.find(arco_id);
    }

    public Tronco findTronco(int arco_id){
        return mArcoDao.findTronco(arco_id);
    }

    public void insert (Arco arco) {
        new ArcoRepository.insertAsyncTask(mArcoDao).execute(arco);
    }

    public void delete (Arco arco){
        new ArcoRepository.deleteAsyncTask(mArcoDao).execute(arco);
    }



}