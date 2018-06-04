package com.ids.idsuserapp.db.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Relation;
import android.arch.persistence.room.Update;

import com.ids.idsuserapp.db.entity.Beacon;
import com.ids.idsuserapp.db.entity.Mappa;

import java.util.List;

/**
 * Created by zek on 22/03/18.
 */

@Dao
public interface BeaconDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void insert(Beacon beacon);
    @Delete
    public void delete(Beacon beacon);
    @Update
    public void update(Beacon beacon);
    @Query("SELECT * FROM beacon B JOIN mappa M on B.mappa=m.id")
    public LiveData<List<BeaconWithMap>> getAll();
    @Query("SELECT Beacon.*, Mappa.* FROM beacon Beacon JOIN  mappa Mappa on Beacon.mappa=Mappa.id WHERE mappa=:mappa")
    public LiveData<List<BeaconWithMap>> getMapBeacons(int mappa);
    @Query("DELETE FROM beacon WHERE mappa=:mappa")
    public void deleteByMap(int mappa);
    @Query("SELECT * FROM beacon WHERE id=:beacon_id")
    public LiveData<Beacon> find(int beacon_id);
    @Query("DELETE FROM beacon")
    public void deleteAll();

    @Query("SELECT * FROM beacon WHERE id=:beacon_id")
    public Beacon trova(int beacon_id);



    @Query("SELECT * FROM beacon ORDER BY nome ASC")
    public LiveData<List<Beacon>> getAllBeacons();
    @Query("SELECT * FROM beacon WHERE mappa=:mappa ORDER BY nome ASC")
    public LiveData<List<Beacon>> getBeaconByIdMappa(int mappa);



    public class BeaconWithMap {
        @Embedded
        Beacon beacon;
        @Relation(parentColumn = "mappa", entityColumn = "id", entity = Mappa.class)
        List<Mappa> mappa;

        public Beacon getBeacon() {
            return beacon;
        }

        public List<Mappa> getMappa() {
            return mappa;
        }

        public void setBeacon(Beacon beacon) {
            this.beacon = beacon;
        }

        public void setMappa(List<Mappa> mappa) {
            this.mappa = mappa;
        }


    }
}
