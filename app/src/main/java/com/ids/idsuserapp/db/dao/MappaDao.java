package com.ids.idsuserapp.db.dao;


import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;


import com.ids.idsuserapp.db.entity.Mappa;

import java.util.List;

/**
 * Created by zek on 22/03/18.
 */
@Dao
public interface MappaDao {
    @Insert
    public void insert(Mappa mappa);
    @Delete
    public void delete(Mappa mappa);
    @Update
    public void update(Mappa mappa);
    @Query("SELECT * FROM mappa ORDER BY name ASC")
    public LiveData<List<Mappa>> getAll();
    @Query("SELECT * FROM mappa WHERE id=:mappa_id")
    public Mappa find(int mappa_id);

    @Query("SELECT * FROM mappa WHERE name=:mappa_nome")
    public Mappa findByNome(String mappa_nome);


    @Query("DELETE FROM mappa")
    public void deleteAll();

    @Query("SELECT * FROM mappa")
    public List<Mappa> getID();
}
