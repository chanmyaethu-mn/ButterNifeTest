package com.test.cmt.butternifetest.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.test.cmt.butternifetest.entity.LocationEntity;
import com.test.cmt.butternifetest.repository.LocationRepository;

import java.util.List;

public class LocationViewModel extends AndroidViewModel {

    private LocationRepository mLocationRepository;

    private LiveData<List<LocationEntity>> mAllLocations;

    public LocationViewModel(@NonNull Application application) {
        super(application);

        mLocationRepository = new LocationRepository(application);
        mAllLocations = mLocationRepository.getmAllLocation();
    }

    public LiveData<List<LocationEntity>> getmAllLocations() {
        return mAllLocations;
    }

    public LocationEntity getLocationById(long id) {
        return mLocationRepository.getLocationById(id);
    }

    public void insert(LocationEntity locationEntity) {
        mLocationRepository.insert(locationEntity);
    }

    public void update(LocationEntity locationEntity) {
        mLocationRepository.update(locationEntity);
    }

    public void deleteLocationById(long locId) {
        mLocationRepository.deleteLocationById(locId);
    }
}
