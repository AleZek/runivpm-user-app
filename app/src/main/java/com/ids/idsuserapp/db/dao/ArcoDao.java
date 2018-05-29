package com.ids.idsuserapp.db.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;


import com.ids.idsuserapp.db.entity.Arco;
import com.ids.idsuserapp.db.entity.Tronco;

@Dao
public interface ArcoDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void insert(Arco arco);
    @Delete
    public void delete(Arco arco);
    @Update
    public void update(Arco arco);

    //TODO implementare query a seconda dell'esigenza


    @Query("SELECT * FROM arco ")
    public Arco getAll();
    @Query("SELECT Arco.*, Beacon.* FROM arco Arco JOIN  beacon Beacon on Arco.`begin`=Beacon.id JOIN beacon B2 ON Arco.`end`=B2.id WHERE arco_id=:id")
    public Tronco findTronco(int id);
    @Query("DELETE FROM beacon WHERE mappa=:mappa")
    public void deleteByMap(int mappa);

    @Query("SELECT * FROM arco WHERE arco_id=:arco_id")
    public Arco find(int arco_id);

    @Query("SELECT * FROM arco WHERE arco_id=:arco_id")
    public Arco trova(int arco_id);



}