package com.test.cmt.butternifetest.entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import java.io.Serializable;
import java.util.Date;

@Entity(tableName = "location_table")
public class LocationEntity implements Serializable {

    public LocationEntity(){}

    public LocationEntity(String placeName, String latLon) {
        this.placeName = placeName;
        this.latLon = latLon;
    }

    public LocationEntity(long id, String placeName, String latLon) {
        this._id = id;
        this.placeName = placeName;
        this.latLon = latLon;
    }



    @PrimaryKey(autoGenerate = true)
    private long _id;

    @ColumnInfo
    @NonNull
    private String placeName;

    @ColumnInfo
    @NonNull
    private String latLon;

    @ColumnInfo
    private Date createdDate;

    public long get_id() {
        return _id;
    }

    public void set_id(long _id) {
        this._id = _id;
    }

    @NonNull
    public String getPlaceName() {
        return placeName;
    }

    public void setPlaceName(@NonNull String placeName) {
        this.placeName = placeName;
    }

    @NonNull
    public String getLatLon() {
        return latLon;
    }

    public void setLatLon(@NonNull String latLon) {
        this.latLon = latLon;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }
}
