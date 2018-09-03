package com.test.cmt.butternifetest.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.test.cmt.butternifetest.entity.LocationEntity;

import java.util.List;

@Dao
public interface LocationDao {

    @Insert
    void insert(LocationEntity locationEntity);

    @Delete
    void delete(LocationEntity locationEntity);

    @Update
    void update(LocationEntity locationEntity);

    @Query("SELECT * FROM location_table ORDER BY _id ASC")
    LiveData<List<LocationEntity>> getAllLocation();

    @Query("SELECT * FROM location_table WHERE _id = :id")
    LocationEntity getLocationById(long id);

    @Query("DELETE FROM location_table WHERE _id = :id")
    void deleteLocationById(long id);
}
