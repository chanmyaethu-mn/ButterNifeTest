package com.test.cmt.butternifetest.repository;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import com.test.cmt.butternifetest.dao.LocationDao;
import com.test.cmt.butternifetest.database.AppRoomDatabase;
import com.test.cmt.butternifetest.entity.LocationEntity;

import java.util.List;

public class LocationRepository {

    private LocationDao mLocationDao;

    private LiveData<List<LocationEntity>> mAllLocation;

    public LocationRepository(Application application) {
        AppRoomDatabase db = AppRoomDatabase.getDatabase(application);
        mLocationDao = db.locationDao();
        //mAllLocation = mLocationDao.getAllLocation();
    }

    public LiveData<List<LocationEntity>> getmAllLocation() {
        mAllLocation = mLocationDao.getAllLocation();
        return mAllLocation;
    }

    public LocationEntity getLocationById(long id) {
        return mLocationDao.getLocationById(id);
    }

    public void insert(LocationEntity locationEntity) {
        new insertAsyncTask(mLocationDao).execute(locationEntity);
    }

    public void update(LocationEntity locationEntity) {
        new updateAsyncTask(mLocationDao).execute(locationEntity);
    }

    public void deleteLocationById(long id) {
        new deleteByIdAsyncTask(mLocationDao).execute(id);
    }

    private static class deleteByIdAsyncTask extends AsyncTask<Long, Void, Void> {

        private  LocationDao mAsyncTaskDao;

        deleteByIdAsyncTask(LocationDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(Long... longs) {
            long locId = longs[0];
            mAsyncTaskDao.deleteLocationById(locId);
            return null;
        }
    }

    private static class updateAsyncTask extends AsyncTask<LocationEntity, Void, Void> {

        private LocationDao mLocationDao;

        updateAsyncTask(LocationDao dao) {
            mLocationDao = dao;
        }

        @Override
        protected Void doInBackground(LocationEntity... locationEntities) {
            mLocationDao.update(locationEntities[0]);
            return null;
        }
    }

    private static class  insertAsyncTask extends AsyncTask<LocationEntity, Void, Void> {

        private  LocationDao mAsyncTaskDao;

        insertAsyncTask(LocationDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final LocationEntity... locationEntities) {
            mAsyncTaskDao.insert(locationEntities[0]);
            return null;
        }
    }
}
